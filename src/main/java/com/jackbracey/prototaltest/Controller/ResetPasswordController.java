package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Configuration.Security.JwtSessions;
import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Pojo.PasswordChangeDetails;
import com.jackbracey.prototaltest.Services.AccountService;
import com.jackbracey.prototaltest.Utilities.EncodingUtils;
import com.jackbracey.prototaltest.Utilities.JwtUtils;
import com.jackbracey.prototaltest.Utilities.RequestUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reset-my-password")
@SuppressWarnings("unused")
public class ResetPasswordController {

    private final AccountService accountService;

    private final JwtUtils jwtUtils;

    private final JwtSessions jwtSessions;

    @Autowired
    public ResetPasswordController(AccountService accountService,
                                   JwtUtils jwtUtils,
                                   JwtSessions jwtSessions) {
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
        this.jwtSessions = jwtSessions;
    }

    @PostMapping
    public ResponseEntity<String> passwordReset(@RequestBody PasswordChangeDetails passwordChangeDetails,
                                           HttpServletRequest request) {
        if (passwordChangeDetails == null)
            return new ResponseEntity<>("Missing request body", HttpStatus.BAD_REQUEST);

        String requestBodyErrors = passwordChangeDetails.checkForErrors();
        if (Strings.isNotBlank(requestBodyErrors))
            return new ResponseEntity<>(requestBodyErrors, HttpStatus.BAD_REQUEST);

        String bearer = RequestUtils.ExtractBearerTokenFromRequest(request);
        if (Strings.isBlank(bearer))
            return new ResponseEntity<>("Missing bearer token in request", HttpStatus.UNAUTHORIZED);

        Claims claims = jwtUtils.getInfoFromToken(bearer);

        if (claims == null)
            return new ResponseEntity<>("Bearer token is invalid", HttpStatus.UNAUTHORIZED);

        String email = String.valueOf(claims.get("email"));

        Account account = accountService.findAccountByEmail(email);

        if (account == null)
            return new ResponseEntity<>("Could not find account with that email", HttpStatus.UNAUTHORIZED);

        EncodingUtils encodingUtils = new EncodingUtils();
        if (!encodingUtils.matches(passwordChangeDetails.getCurrentPassword(), account.getPassword()))
            return new ResponseEntity<>("Current password does not match", HttpStatus.BAD_REQUEST);

        account.setPassword(encodingUtils.encode(passwordChangeDetails.getNewPassword()));
        accountService.save(account);

        jwtSessions.invalidate(bearer);

        return ResponseEntity.ok("Password changed, you must sign in again");
    }

}
