package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SignUpControllerTest {

    private SignUpController controller;

    private AccountService accountService;

    @BeforeEach
    void beforeEach() {
        accountService = mock(AccountService.class);
        controller = new SignUpController(accountService);
    }

    @Test
    void shouldSignUp() {
        ResponseEntity<String> response = controller.signUp(new Account(null, "Test", "Test",
                "Test@mail.com", "password"));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Account created", response.getBody());
    }

    @Test
    void shouldReturn400WhenMissingAccount_MissingCompletely() {
        ResponseEntity<String> response = controller.signUp(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Json body must have email and password", response.getBody());
    }

    @Test
    void shouldReturn400WhenMissingAccount_MissingEmail() {
        ResponseEntity<String> response = controller.signUp(new Account(null, "Test", "Test",
                "", "password"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Json body must have email and password", response.getBody());
    }

    @Test
    void shouldReturn400WhenMissingAccount_MissingPassword() {
        ResponseEntity<String> response = controller.signUp(new Account(null, "Test", "Test",
                "Test@mail.com", ""));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Json body must have email and password", response.getBody());
    }

    @Test
    void shouldReturn400WhenAnAccountAlreadyExists() {
        Account account = new Account(null, "Test", "Test", "Test@mail.com",
                "password");
        when(accountService.findAccountByEmail(account.getEmail())).thenReturn(account);

        ResponseEntity<String> response = controller.signUp(account);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("There is already an account with this email", response.getBody());
    }

}
