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

@Component
public class JWTUtil {

    // Fields for key algorithms (can be initialized in init or constructor)
    private KeyAlgorithm<SecretKey, SecretKey> keyAlg; // Corrected type
    private AeadAlgorithm aedAlg;

    // Injected properties
    @Value("${jwt.expirationMs}")
    private long expirationMs;

    @Value("${jwt.secretKeyB64}")
    private String secretKeyBase64;

    // Persistent key, initialized in @PostConstruct
    private SecretKey persistentKey;

    // Constructor: expirationMs is injected directly to the field
    public JWTUtil() {
    }

    @PostConstruct
    public void init() {
        // Decode the Base64 key from properties
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        this.persistentKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // Initialize algorithms (as they were in the original constructor)
        this.keyAlg = Jwts.KEY.A256GCMKW; // AES-256 GCM Key Wrap
        this.aedAlg = Jwts.ENC.A256GCM;   // AES-256 GCM Content Encryption
    }

    public String generateJWE(String subject, Map<String, Object> claimsMap) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        // Create Claims object
        Claims claims = Jwts.claims().subject(subject).add(claimsMap).build();
        
        return Jwts.builder()
                .claims(claims) // Use the Claims object
                .issuedAt(now) // Set issuedAt if desired, standard practice
                .expiration(expiryDate) // Set expiration
                .encryptWith(this.persistentKey, this.keyAlg, this.aedAlg) // Use persistent key
                .compact();
    }

    public Claims getClaims(String jweString) { // Return type often Claims for convenience
        Jwe<Claims> jwe = Jwts.parser()
                .decryptWith(this.persistentKey) // Use persistent key
                .build()
                .parseEncryptedClaims(jweString);
        return jwe.getPayload();
    }
}
