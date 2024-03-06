package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Security.JwtSessions;
import com.jackbracey.prototaltest.Utilities.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sign-out")
@SuppressWarnings("unused")
public class SignOutController {

    @Autowired
    private JwtSessions jwtSessions;

    @PostMapping
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String bearer = RequestUtils.ExtractBearerTokenFromRequest(request);

        if (Strings.isBlank(bearer))
            return new ResponseEntity<>("Missing bearer token", HttpStatus.UNAUTHORIZED);

        SecurityContextHolder.getContext().setAuthentication(null);
        jwtSessions.invalidate(bearer);

        return ResponseEntity.ok("Logged out");
    }

}
