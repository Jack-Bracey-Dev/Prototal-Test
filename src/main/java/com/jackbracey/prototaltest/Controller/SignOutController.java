package com.jackbracey.prototaltest.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sign-out")
public class SignOutController {

    @PostMapping
    public String test() {
        return "Hello World!";
    }

}
