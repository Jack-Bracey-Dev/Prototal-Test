package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Security.JwtSessions;
import com.jackbracey.prototaltest.Security.JwtUtils;
import com.jackbracey.prototaltest.Services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringTokenizer;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("sign-in")
@SuppressWarnings("unused")
public class SignInController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtSessions jwtSessions;

    @PostMapping
    public ResponseEntity<?> signIn(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        StringTokenizer tokenizer = new StringTokenizer(auth);
        if (tokenizer.hasMoreTokens()) {
            String authType = tokenizer.nextToken();

            if (authType.equalsIgnoreCase("Basic")) {
                String credentials = new String(Base64.decodeBase64(tokenizer.nextToken()), UTF_8);

                int splitPosition = credentials.indexOf(":");
                if (splitPosition != -1) {
                    String email = credentials.substring(0, splitPosition).trim();
                    String password = credentials.substring(splitPosition + 1).trim();

                    Account foundAccount = accountService.findAccountByEmail(email);

                    if (foundAccount == null)
                        return new ResponseEntity<>("This account does not exist", HttpStatus.UNAUTHORIZED);

                    if (!foundAccount.getPassword().equals(password))
                        return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);

                    String token = jwtUtils.generate(email);

                    jwtSessions.addToken(token);

                    return ResponseEntity.ok(token);
                }
            }
        }

        return new ResponseEntity<>("Missing login details", HttpStatus.BAD_REQUEST);
    }

}
