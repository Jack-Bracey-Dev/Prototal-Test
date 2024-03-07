package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.AccountService;
import com.jackbracey.prototaltest.Utilities.EncodingUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sign-up")
@SuppressWarnings("unused")
public class SignUpController {

    private final AccountService accountService;

    @Autowired
    public SignUpController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<String> signUp(@RequestBody Account account) {
        if (account == null || Strings.isBlank(account.getEmail()) || Strings.isBlank(account.getPassword()))
            return new ResponseEntity<>("Json body must have email and password", HttpStatus.BAD_REQUEST);

        Account existingAccount = accountService.findAccountByEmail(account.getEmail());

        if (existingAccount != null)
            return new ResponseEntity<>("There is already an account with this email", HttpStatus.BAD_REQUEST);

        account.setPassword(new EncodingUtils().encode(account.getPassword()));
        accountService.save(account);
        return ResponseEntity.ok("Account created");
    }

}
