package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Configuration.Security.JwtSessions;
import com.jackbracey.prototaltest.Models.Account;
import com.jackbracey.prototaltest.Pojo.PasswordChangeDetails;
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
public class ResetPasswordControllerTests {

    private ResetPasswordController controller;

    private AccountService accountService;

    private JwtUtils jwtUtils;

    @BeforeEach
    void beforeEach() {
        accountService = mock(AccountService.class);
        jwtUtils = mock(JwtUtils.class);
        JwtSessions jwtSessions = mock(JwtSessions.class);
        controller = new ResetPasswordController(accountService, jwtUtils, jwtSessions);
    }

    @Test
    void shouldResetPasswordAndInvalidateToken() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_BEARER);

        Map<String, Object> mockJwtResults = new HashMap<>();
        mockJwtResults.put("email", BearerTokens.TEST_BEARER.getEmail());
        when(jwtUtils.getInfoFromToken(any())).thenReturn(new DefaultClaims(mockJwtResults));

        when(accountService.findAccountByEmail(any()))
                .thenReturn(new Account(null, "Test", "Test", "test@hotmail.co.uk",
                        "{bcrypt}$2a$10$qDWyDMMyf7X/pkf9j6ZsB.isx2bTp1Vxpb1Wf0jZv3qUh/0AfSmVO"));
        // Password is "pwd" encoded for the above

        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "password", "password"),
                request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Password changed, you must sign in again", response.getBody());
    }

    @Test
    void shouldReturn400ForMissingRequestBody() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<String> response = controller.passwordReset(null, request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Missing request body", response.getBody());
    }

    @Test
    void shouldReturn400ForInvalidPasswordChangeDetails_MissingCurrentPassword() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("", "password", "password"),
                request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Missing element 'currentPassword'\n", response.getBody());
    }

    @Test
    void shouldReturn400ForInvalidPasswordChangeDetails_MissingNewPassword() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "", "password"),
                request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Missing element 'newPassword'\n", response.getBody());
    }

    @Test
    void shouldReturn400ForInvalidPasswordChangeDetails_MissingNewPasswordCopy() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "password", ""),
                request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Missing element 'newPasswordCopy'\n", response.getBody());
    }

    @Test
    void shouldReturn400ForInvalidPasswordChangeDetails_NewPasswordsNotMatching() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "password", "pasword"),
                request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("'newPassword' and 'newPasswordCopy' do not match", response.getBody());
    }

    @Test
    void shouldReturn400ForInvalidPasswordChangeDetails_MissingAllElements() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("", "", ""),
                request);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(
                """
                        Missing element 'currentPassword'
                        Missing element 'newPassword'
                        Missing element 'newPasswordCopy'
                        """, response.getBody());
    }

    @Test
    void shouldReturn401ForMissingBearerToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "password", "password"),
                request);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Missing bearer token in request", response.getBody());
    }

    @Test
    void shouldReturn401ForInvalidBearerToken() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_BEARER);

        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "password", "password"),
                request);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Bearer token is invalid", response.getBody());
    }

    @Test
    void shouldReturn401ForNullAccount() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_BEARER);

        Map<String, Object> mockJwtResults = new HashMap<>();
        mockJwtResults.put("email", BearerTokens.TEST_BEARER.getEmail());
        when(jwtUtils.getInfoFromToken(any())).thenReturn(new DefaultClaims(mockJwtResults));

        ResponseEntity<String> response = controller.passwordReset(
                new PasswordChangeDetails("pwd", "password", "password"),
                request);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Could not find account with that email", response.getBody());
    }

}
