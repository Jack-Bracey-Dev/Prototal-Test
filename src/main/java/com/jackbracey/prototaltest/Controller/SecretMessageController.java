package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.AccountService;
import com.jackbracey.prototaltest.Utilities.JwtUtils;
import com.jackbracey.prototaltest.Utilities.RequestUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("secret-message")
@SuppressWarnings("unused")
public class SecretMessageController {

    private final AccountService accountService;

    private final JwtUtils jwtUtils;

    @Autowired
    public SecretMessageController(AccountService accountService, JwtUtils jwtUtils) {
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<String> getSecretMessage(HttpServletRequest request) {
        String bearer = RequestUtils.ExtractBearerTokenFromRequest(request);

        if (Strings.isBlank(bearer))
            return new ResponseEntity<>("Missing bearer token", HttpStatus.UNAUTHORIZED);

        Claims claims = jwtUtils.getInfoFromToken(bearer);

        if (claims == null)
            return new ResponseEntity<>("Bearer token is invalid", HttpStatus.UNAUTHORIZED);

        String email = String.valueOf(claims.get("email"));

        Account account = accountService.findAccountByEmail(email);

        if (account == null)
            return new ResponseEntity<>("Could not find account", HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(String.format(
                "%s %s\n" +
                        "A computer would deserve to be called intelligent if it could deceive a human into believing " +
                        "that it was human.",
                account.getFirstName(),
                account.getSurname()
        ));
    }

}
