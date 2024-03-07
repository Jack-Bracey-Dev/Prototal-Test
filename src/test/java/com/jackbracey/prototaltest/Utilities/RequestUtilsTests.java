package com.jackbracey.prototaltest.Utilities;

import com.jackbracey.prototaltest.TestEnums.BasicTokens;
import com.jackbracey.prototaltest.TestEnums.BearerTokens;
import com.jackbracey.prototaltest.TestHelpers.RequestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

@SpringBootTest
public class RequestUtilsTests {

    @Test
    void shouldReturnEmailAndPasswordString() {
        MockHttpServletRequest request = RequestHelper.mockBasicRequest(BasicTokens.TEST_PASSWORD);
        Assertions.assertEquals(String.format("%s:%s", BasicTokens.TEST_PASSWORD.getEmail(), BasicTokens.TEST_PASSWORD.getPassword()),
                RequestUtils.ExtractBasicFromRequest(request));
    }

    @Test
    void shouldReturnBearerString() {
        MockHttpServletRequest request = RequestHelper.mockBearerRequest(BearerTokens.TEST_BEARER);
        Assertions.assertEquals(BearerTokens.TEST_BEARER.getToken().replace("Bearer ", "").trim(),
                RequestUtils.ExtractBearerTokenFromRequest(request));
    }

}
