package com.jackbracey.prototaltest.Utilities;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncryptionUtils {

    PasswordEncoder encoder;

    public EncryptionUtils() {
        encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public String encrypt(String value) {
        return encoder.encode(value);
    }

    public boolean matches(String value, String encoded) {
        return encoder.matches(value, encoded);
    }

}
