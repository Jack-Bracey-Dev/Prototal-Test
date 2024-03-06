package com.jackbracey.prototaltest.Security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class AuthenticationFilter extends GenericFilterBean {

    private final JwtSessions jwtSessions;

    private final JwtUtils jwtUtils;

    public AuthenticationFilter(JwtSessions jwtSessions,
                                JwtUtils jwtUtils) {
        this.jwtSessions = jwtSessions;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (Arrays.stream(SecurityConfig.OPEN_URLS).anyMatch(url -> url.equalsIgnoreCase(request.getServletPath()))) {
            filterChain.doFilter(req, resp);
            return;
        }

        HttpServletResponse response = (HttpServletResponse) resp;
        String auth = request.getHeader("Authorization");

        if (auth == null) {
            response.sendError(401);
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(auth);
        if (tokenizer.hasMoreTokens()) {
            String authType = tokenizer.nextToken();

            if (authType.equalsIgnoreCase("Bearer")) {
                String bearer = auth.replace("Bearer", "").trim();
                if (!jwtSessions.validate(bearer)) {
                    jwtSessions.invalidate(bearer);
                    response.sendError(401, "Invalid bearer token");
                    return;
                } else {
                    Claims claims = jwtUtils.getInfoFromToken(bearer);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(claims.get("email"), bearer, new ArrayList<>()));
                }
            }
        }

        filterChain.doFilter(req, resp);
    }

}
