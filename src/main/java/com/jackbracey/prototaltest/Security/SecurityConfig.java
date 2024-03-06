package com.jackbracey.prototaltest.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtSessions jwtSessions;

    @Autowired
    private JwtUtils jwtUtils;

    public static final String[] OPEN_URLS = {
            "/sign-up",
            "/sign-in",
            "/error"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*.csrf(AbstractHttpConfigurer::disable)*/
                .authorizeHttpRequests(req ->
                        req.anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new AuthenticationFilter(jwtSessions, jwtUtils),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
