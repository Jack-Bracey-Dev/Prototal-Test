package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Security.JwtSessions;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringTokenizer;

@RestController
@RequestMapping("sign-out")
public class SignOutController {

    @Autowired
    private JwtSessions jwtSessions;

    @PostMapping
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        SecurityContextHolder.getContext().setAuthentication(null);
        StringTokenizer tokenizer = new StringTokenizer(auth);

        if (tokenizer.hasMoreTokens()) {
            String authType = tokenizer.nextToken();

            if (authType.equalsIgnoreCase("Bearer")) {
                String bearer = auth.replace("Bearer", "").trim();
                jwtSessions.invalidate(bearer);
            }
        }

        return ResponseEntity.ok("Logged out");
    }

}
