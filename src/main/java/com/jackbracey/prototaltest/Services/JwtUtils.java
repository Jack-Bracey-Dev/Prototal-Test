package com.jackbracey.prototaltest.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtUtils {

    @Value("${runtime.jwt.secret}")
    private String secret;

    @Value("${runtime.jwt.expires}")
    private Integer expiryTime;

    private SecretKey key;

    public String generate(String email) {
        if (this.key == null)
            this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        HashMap<String, Object> jwtContent = new HashMap<>();
        jwtContent.put("email", email);

        JwtBuilder builder = Jwts.builder();

        builder.claims(jwtContent);
        builder.issuedAt(new Date());
        builder.expiration(new Date(System.currentTimeMillis() + expiryTime));
        builder.signWith(this.key);

        return builder.compact();
    }

    public Claims getInfoFromToken(String token) {
        if (this.key == null)
            this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        // TODO fix error thrown when token has expired
        return Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
