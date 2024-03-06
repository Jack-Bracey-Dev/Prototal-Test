package com.jackbracey.prototaltest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reset-my-password")
public class ResetPasswordController {

    @GetMapping
    public String test() {
        return "Hello World!";
    }

}
