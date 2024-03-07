package com.jackbracey.prototaltest.TestEnums;

public enum BearerTokens {

    TEST_BEARER("test@hotmail.co.uk",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAaG90bWFpbC5jby51ayJ9.2RH-2dCZYdkYcEILCFTGNCrTokCy4RLP8zKKH9QXaEo"),
    TEST_USER_BEARER("Test.User@email.com",
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IlRlc3QuVXNlckBlbWFpbC5jb20ifQ.Zi_Od7gg3k66BHKPuAEfmpMuHIG-qukx2KxsWF667Pc");

    private String email;

    private String token;

    BearerTokens(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
