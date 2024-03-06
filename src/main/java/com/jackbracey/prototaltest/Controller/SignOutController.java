package com.jackbracey.prototaltest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sign-out")
public class SignOutController {

    @GetMapping
    public String test() {
        return "Hello World!";
    }

}
