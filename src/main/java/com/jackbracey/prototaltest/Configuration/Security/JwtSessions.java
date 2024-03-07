package com.jackbracey.prototaltest.Configuration.Security;

import com.jackbracey.prototaltest.Utilities.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtSessions {

    @Autowired
    private JwtUtils jwtUtils;

    private final List<String> tokens;

    public JwtSessions() {
        tokens = new ArrayList<>();
    }

    public void addToken(String token) {
        tokens.add(token);
    }

    public boolean validate(String token) {
        if (!tokens.contains(token))
            return false;

        Claims claims = jwtUtils.getInfoFromToken(token);
        return new Date(System.currentTimeMillis()).before(claims.getExpiration());
    }

    public void invalidate(String token) {
        tokens.remove(token);
    }

}
