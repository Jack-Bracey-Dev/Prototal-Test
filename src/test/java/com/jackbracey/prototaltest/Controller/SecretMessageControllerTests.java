package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Services.AccountService;
import com.jackbracey.prototaltest.TestEnums.BearerTokens;
import com.jackbracey.prototaltest.TestHelpers.RequestHelper;
import com.jackbracey.prototaltest.Utilities.JwtUtils;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SecretMessageControllerTests {

    private static AccountService accountService;

    private static JwtUtils jwtUtils;

    private static SecretMessageController controller;

    @BeforeEach
    void beforeEach() {
        accountService = mock(AccountService.class);
        jwtUtils = mock(JwtUtils.class);
        controller = new SecretMessageController(accountService, jwtUtils);
    }

    @Test
    void shouldReturnCorrectMessage() {
        Account account = new Account("123", "Test", "User", "Test.User@email.com", "pwd");
        when(accountService.findAccountByEmail(account.getEmail())).thenReturn(account);

        Map<String, Object> mockJwtResults = new HashMap<>();
        mockJwtResults.put("email", BearerTokens.TEST_USER_BEARER.getEmail());
        when(jwtUtils.getInfoFromToken(any())).thenReturn(new DefaultClaims(mockJwtResults));

        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_USER_BEARER);
        ResponseEntity<String> response = controller.getSecretMessage(request);
        Assertions.assertEquals(
                "Test User\n" +
                "A computer would deserve to be called intelligent if it could deceive a human into believing that it was human.",
                response.getBody());
    }

    @Test
    void shouldReturn401ForMissingBearerToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAuthType("Bearer");

        ResponseEntity<String> response = controller.getSecretMessage(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Missing bearer token", response.getBody());
    }

    @Test
    void shouldReturn401ForInvalidBearer() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_USER_BEARER);

        ResponseEntity<String> response = controller.getSecretMessage(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Bearer token is invalid", response.getBody());
    }

    @Test
    void shouldReturn204WhenNoAccountFound() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_USER_BEARER);

        Map<String, Object> mockJwtResults = new HashMap<>();
        mockJwtResults.put("email", BearerTokens.TEST_USER_BEARER.getEmail());
        when(jwtUtils.getInfoFromToken(any())).thenReturn(new DefaultClaims(mockJwtResults));

        ResponseEntity<String> response = controller.getSecretMessage(request);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertEquals("Could not find account", response.getBody());
    }

}
