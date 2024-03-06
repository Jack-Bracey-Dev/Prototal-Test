package com.jackbracey.prototaltest.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reset-my-password")
public class ResetPasswordController {

    @PostMapping
    public String passwordReset() {
        return "Hello World!";
    }

}
