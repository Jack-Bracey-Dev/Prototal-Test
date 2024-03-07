package com.jackbracey.prototaltest.Controller;

import com.jackbracey.prototaltest.Configuration.Security.JwtSessions;
import com.jackbracey.prototaltest.TestEnums.BearerTokens;
import com.jackbracey.prototaltest.TestHelpers.RequestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class SignOutControllerTest {

    private SignOutController controller;

    @BeforeEach
    void beforeEach() {
        JwtSessions jwtSessions = mock(JwtSessions.class);
        controller = new SignOutController(jwtSessions);
    }

    @Test
    void shouldLogOut() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_BEARER);

        ResponseEntity<String> response = controller.signOut(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Logged out", response.getBody());
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldReturn401ForMissingBearerToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<String> response = controller.signOut(request);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertEquals("Missing bearer token", response.getBody());
    }

}
