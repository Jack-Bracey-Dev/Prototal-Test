package com.jackbracey.prototaltest.TestHelpers;

import com.jackbracey.prototaltest.TestEnums.BasicTokens;
import com.jackbracey.prototaltest.TestEnums.BearerTokens;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestHelper {

    public static MockHttpServletRequest mockBearerRequest(BearerTokens bearerToken) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAuthType("bearer");
        request.addHeader("Authorization", bearerToken.getToken());
        return request;
    }

    public static MockHttpServletRequest mockBasicRequest(BasicTokens basicToken) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAuthType("basic");
        request.addHeader("Authorization", basicToken.getToken());
        return request;
    }

}
