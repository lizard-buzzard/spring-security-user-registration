package com.lizard.buzzard.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: introduced just for test of internationalization feature. Replace by full-fledged version of the controller
 */
@RestController
public class RegisterController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getInternationalPage() {
        return "login";
    }
}
