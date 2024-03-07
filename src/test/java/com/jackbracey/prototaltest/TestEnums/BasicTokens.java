package com.jackbracey.prototaltest.TestEnums;

public enum BasicTokens {

    TEST_PASSWORD("test@hotmail.co.uk",
            "password",
            "Basic dGVzdEBob3RtYWlsLmNvLnVrOnBhc3N3b3Jk"),
    USER_PWD("test@hotmail.co.uk",
            "pwd",
            "Basic VGVzdC5Vc2VyQGhvdG1haWwuY28udWs6cHdk");

    private String email;

    private String password;

    private String token;

    BasicTokens(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
