package dev.montyoh.gateway.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtUtils {

    private final SecretKey jwtSecretKey;
    private final SecretKey mcpSecretKey;

    public JwtUtils(
            @Value("${service.jwt.secret-key}") String secretKey,
            @Value("${service.jwt.mcp-secret-key}") String mcpSecretKey
    ) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.mcpSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(mcpSecretKey));
    }

    public Jws<Claims> parsingToken(String token) {
        return parsing(token, jwtSecretKey);
    }

    public Jws<Claims> parsingMcpToken(String token) {
        return parsing(token, mcpSecretKey);
    }

    private Jws<Claims> parsing(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
