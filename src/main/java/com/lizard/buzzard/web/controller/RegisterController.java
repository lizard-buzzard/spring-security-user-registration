package com.lizard.buzzard.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * TODO: introduced just for test of internationalization feature. Replace by full-fledged version of the controller
 */
@Controller
public class RegisterController {

    @GetMapping("/login")
    public String getInternationalPage() {
        return "login";
    }
}
