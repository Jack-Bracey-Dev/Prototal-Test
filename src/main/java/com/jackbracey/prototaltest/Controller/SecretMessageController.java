package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.JwtUtils;
import com.jackbracey.prototaltest.Services.AccountService;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getSecretMessage(HttpServletRequest request) {
        String bearer = RequestUtils.ExtractBearerTokenFromRequest(request);

        if (Strings.isBlank(bearer))
            return new ResponseEntity<>("Missing bearer token", HttpStatus.UNAUTHORIZED);

        Claims claims = jwtUtils.getInfoFromToken(bearer);
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
