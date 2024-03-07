package com.jackbracey.prototaltest.Utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EncodingUtilsTests {

    @Test
    void shouldEncryptValue() {
        EncodingUtils encodingUtils = new EncodingUtils();
        String notEncrypted = "ABC123";
        String encrypted = encodingUtils.encode(notEncrypted);
        Assertions.assertNotEquals(notEncrypted, encodingUtils.encode(notEncrypted));
        Assertions.assertTrue(encodingUtils.matches(notEncrypted, encrypted));
    }

}
