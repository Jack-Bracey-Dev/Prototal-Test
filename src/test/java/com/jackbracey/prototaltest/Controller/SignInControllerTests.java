package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Configuration.Security.JwtSessions;
import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.AccountService;
import com.jackbracey.prototaltest.TestEnums.BasicTokens;
import com.jackbracey.prototaltest.TestHelpers.RequestHelper;
import com.jackbracey.prototaltest.Utilities.EncodingUtils;
import com.jackbracey.prototaltest.Utilities.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SignInControllerTests {

    private SignInController controller;

    private AccountService accountService;

    private JwtUtils jwtUtils;

    private JwtSessions jwtSessions;

    @BeforeEach
    void beforeEach() {
        accountService = mock(AccountService.class);
        jwtUtils = mock(JwtUtils.class);
        jwtSessions = mock(JwtSessions.class);
        controller = new SignInController(accountService, jwtUtils, jwtSessions);
    }

    @Test
    void shouldSignIn() {
        MockHttpServletRequest request = RequestHelper.mockBasicRequest(BasicTokens.USER_PWD);

        when(accountService.findAccountByEmail(any())).thenReturn(new Account(null, "Test", "Test",
                "Test.User@mail.com", new EncodingUtils().encode(BasicTokens.USER_PWD.getPassword())));
        // Password is "pwd" encoded for the above

        when(jwtUtils.generate(any())).thenReturn("JI12O3IFA8DA0DAWDA");

        ResponseEntity<String> response = controller.signIn(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturn401ForMissingCredentials() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<String> response = controller.signIn(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Missing email and password auth", response.getBody());
    }

    @Test
    void shouldReturn401ForIncorrectEmail() {
        MockHttpServletRequest request = RequestHelper.mockBasicRequest(BasicTokens.TEST_PASSWORD);

        when(accountService.findAccountByEmail(any())).thenReturn(null);

        ResponseEntity<String> response = controller.signIn(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("This account does not exist", response.getBody());
    }

    @Test
    void shouldReturn401ForIncorrectPassword() {
        MockHttpServletRequest request = RequestHelper.mockBasicRequest(BasicTokens.TEST_PASSWORD);

        when(accountService.findAccountByEmail(any())).thenReturn(new Account(null, "Test", "Test",
                "Test", "{bcrypt}$2a$10$OytU/m7KYHs9yCRU9r3TY.ZVsB0/A4SBv4uIxqsEsnxt2BrMXZbLO"));

        ResponseEntity<String> response = controller.signIn(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Incorrect password", response.getBody());
    }

}
