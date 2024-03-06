package com.jackbracey.prototaltest.Utilities;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.StringTokenizer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestUtils {

    /***
     * @param request The request we are getting the basic auth details from
     * @return email / password separated with :
     */
    public static String ExtractBasicFromRequest(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        StringTokenizer tokenizer = new StringTokenizer(auth);
        if (tokenizer.hasMoreTokens()) {
            String authType = tokenizer.nextToken();

            if (authType.equalsIgnoreCase("Basic"))
                return new String(Base64.decodeBase64(tokenizer.nextToken()), UTF_8);
            else
                return null;
        }
        return null;
    }

    /***
     * @param request The request we are getting the bearer auth details from
     * @return
     */
    public static String ExtractBearerTokenFromRequest(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        StringTokenizer tokenizer = new StringTokenizer(auth);

        if (tokenizer.hasMoreTokens()) {
            String authType = tokenizer.nextToken();

            if (authType.equalsIgnoreCase("Bearer"))
                return auth.replace("Bearer", "").trim();
        }

        return null;
    }

}
