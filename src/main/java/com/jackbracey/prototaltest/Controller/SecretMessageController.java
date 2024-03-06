package com.jackbracey.prototaltest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("secret-message")
public class SecretMessageController {

    @GetMapping
    public String getSecretMessage() {
        return "Hello World!";
    }

}
