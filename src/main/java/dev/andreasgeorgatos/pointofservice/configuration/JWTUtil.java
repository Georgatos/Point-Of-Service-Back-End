package dev.andreasgeorgatos.pointofservice.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwe;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.SecretKeyAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    SecretKeyAlgorithm keyAlg;
    SecretKey key;
    AeadAlgorithm aedAlg;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    public JWTUtil() {
        keyAlg = Jwts.KEY.A256GCMKW;
        key = keyAlg.key().build();
        aedAlg = Jwts.ENC.A256GCM;
    }

    public String generateJWE(String username, Map<String, Object> claims) {
        return Jwts
                .builder()
                .subject(username)
                .claims(claims)
                .encryptWith(key, keyAlg, aedAlg)
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .compact();
    }

    public Jwe<Claims> getClaims(String token) {
        return Jwts
                .parser()
                .decryptWith(key)
                .build()
                .parseEncryptedClaims(token);
    }
}
