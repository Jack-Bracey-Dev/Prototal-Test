package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.AccountService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sign-up")
@SuppressWarnings("unused")
public class SignUpController {

    @Autowired
    private AccountService accountService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> signUp(@RequestBody Account account) throws BadRequestException {
        if (account == null || account.getEmail() == null || account.getPassword() == null)
            return new ResponseEntity<>("Json body must have email and password", HttpStatus.BAD_REQUEST);

        Account existingAccount = accountService.findAccountByEmail(account.getEmail());

        if (existingAccount != null)
            return new ResponseEntity<>("There is already an account with this email", HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(accountService.save(account));
    }

}
