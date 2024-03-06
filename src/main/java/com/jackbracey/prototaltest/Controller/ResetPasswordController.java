package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Pojo.PasswordChangeDetails;
import com.jackbracey.prototaltest.Security.JwtSessions;
import com.jackbracey.prototaltest.Services.JwtUtils;
import com.jackbracey.prototaltest.Services.AccountService;
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
public class ResetPasswordController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtSessions jwtSessions;

    @PostMapping
    public ResponseEntity<?> passwordReset(@RequestBody PasswordChangeDetails passwordChangeDetails,
                                           HttpServletRequest request) {
        // TODO replace this with a clearer error message
        if (passwordChangeDetails == null || !passwordChangeDetails.isValid())
            return new ResponseEntity<>("This needs improving to show the issue", HttpStatus.BAD_REQUEST);

        String bearer = RequestUtils.ExtractBearerTokenFromRequest(request);
        if (Strings.isBlank(bearer))
            return new ResponseEntity<>("Missing bearer token in request", HttpStatus.UNAUTHORIZED);

        Claims claims = jwtUtils.getInfoFromToken(bearer);
        String email = String.valueOf(claims.get("email"));

        Account account = accountService.findAccountByEmail(email);

        if (account == null)
            return new ResponseEntity<>("Could not find account with that email", HttpStatus.UNAUTHORIZED);

        if (!account.getPassword().equals(passwordChangeDetails.getCurrentPassword()))
            return new ResponseEntity<>("Current password does not match", HttpStatus.BAD_REQUEST);

        account.setPassword(passwordChangeDetails.getNewPassword());
        accountService.save(account);

        jwtSessions.invalidate(bearer);

        return ResponseEntity.ok("Password changed");
    }

}
