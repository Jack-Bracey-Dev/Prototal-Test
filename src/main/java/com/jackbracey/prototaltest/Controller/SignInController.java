package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Configuration.Security.JwtSessions;
import com.jackbracey.prototaltest.Services.JwtUtils;
import com.jackbracey.prototaltest.Services.AccountService;
import com.jackbracey.prototaltest.Utilities.EncryptionUtils;
import com.jackbracey.prototaltest.Utilities.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String credentials = RequestUtils.ExtractBasicFromRequest(request);

        if (Strings.isBlank(credentials))
            return new ResponseEntity<>("Missing email and password auth", HttpStatus.UNAUTHORIZED);

        int splitPosition = credentials.indexOf(":");
        if (splitPosition != -1) {
            String email = credentials.substring(0, splitPosition).trim();
            String password = credentials.substring(splitPosition + 1).trim();

            Account foundAccount = accountService.findAccountByEmail(email);

            if (foundAccount == null)
                return new ResponseEntity<>("This account does not exist", HttpStatus.UNAUTHORIZED);

            if (!new EncryptionUtils().matches(password, foundAccount.getPassword()))
                return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);

            String token = jwtUtils.generate(email);

            jwtSessions.addToken(token);

            return ResponseEntity.ok(token);
        }

        return new ResponseEntity<>("Missing login details", HttpStatus.BAD_REQUEST);
    }

}
