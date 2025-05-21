package dev.andreasgeorgatos.pointofservice.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwe;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.KeyAlgorithm; // Corrected import
import jakarta.annotation.PostConstruct; // Corrected import
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64; // Added import
import java.util.Date;
import java.util.Map;

/**
 * Utility class for handling JSON Web Encryption (JWE) operations, including
 * generating and parsing JWE tokens.
 * This class uses AES-256 GCM for content encryption and key wrapping.
 */
@Component
public class JWTUtil {

    private static final String AES_ALGORITHM = "AES";

    // Fields for key algorithms
    private KeyAlgorithm<SecretKey, SecretKey> keyEncryptionAlgorithm;
    private AeadAlgorithm contentEncryptionAlgorithm;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    @Value("${jwt.secretKeyB64}")
    private String secretKeyBase64;

    private SecretKey persistentSecretKey;

    /**
     * Default constructor.
     * Dependencies are injected via @Value and initialization occurs in {@link #init()}.
     */
    public JWTUtil() {
        // Initialization is handled in the init() method marked with @PostConstruct
    }

    /**
     * Initializes the JWTUtil component after dependency injection.
     * This method decodes the Base64 encoded secret key and initializes
     * the key and content encryption algorithms.
     */
    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        this.persistentSecretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, AES_ALGORITHM);

        this.keyEncryptionAlgorithm = Jwts.KEY.A256GCMKW; // AES-256 GCM Key Wrap
        this.contentEncryptionAlgorithm = Jwts.ENC.A256GCM;   // AES-256 GCM Content Encryption
    }

    /**
     * Generates a JWE token with the given subject and claims.
     * The token is encrypted using the configured persistent key and algorithms.
     *
     * @param subject   The subject of the token (typically username).
     * @param claimsMap A map of additional claims to include in the token payload.
     * @return A compact JWE string.
     */
    public String generateJWE(String subject, Map<String, Object> claimsMap) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        Claims claims = Jwts.claims().subject(subject).add(claimsMap).build();
        
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .encryptWith(this.persistentSecretKey, this.keyEncryptionAlgorithm, this.contentEncryptionAlgorithm)
                .compact();
    }

    /**
     * Parses a JWE string and returns the claims contained within it.
     * The token is decrypted using the configured persistent key.
     *
     * @param jweString The compact JWE string to parse.
     * @return The {@link Jwe<Claims>} object which includes headers and payload.
     * @throws io.jsonwebtoken.JwtException if the JWE string is invalid or cannot be parsed.
     */
    public Jwe<Claims> getClaims(String jweString) {
        return Jwts.parser()
                .decryptWith(this.persistentSecretKey)
                .build()
                .parseEncryptedClaims(jweString);
    }

    /**
     * Parses a JWE string and returns the claims payload.
     * This is a convenience method that calls {@link #getClaims(String)} and then extracts the payload.
     *
     * @param jweString The compact JWE string to parse.
     * @return The {@link Claims} object (payload) extracted from the JWE.
     * @throws io.jsonwebtoken.JwtException if the JWE string is invalid or cannot be parsed.
     */
    public Claims getClaimsPayload(String jweString) {
        Jwe<Claims> jwe = getClaims(jweString); // Use the public getClaims method
        return jwe.getPayload();
    }
}
