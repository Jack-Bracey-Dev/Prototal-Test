package com.jackbracey.prototaltest.Utilities;

import com.jackbracey.prototaltest.Configuration.Security.JwtSessions;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {

    @SuppressWarnings("unused")
    @Value("${runtime.jwt.secret}")
    private String secret;

    @SuppressWarnings("unused")
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

        JwtParser parser = Jwts.parser()
                .verifyWith(this.key)
                .build();
        Jws<Claims> claimsJws;

        try {
             claimsJws = parser.parseSignedClaims(token);
             return claimsJws
                     .getPayload();
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired");
            return null;
        }
    }

}
