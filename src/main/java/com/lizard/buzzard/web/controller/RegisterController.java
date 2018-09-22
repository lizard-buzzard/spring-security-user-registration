package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.web.dto.ViewFormUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class RegisterController {
    private final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getInternationalPage() {
        return "login";
    }

    /**
     * @param viewFormUser
     * @param model
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(ViewFormUser viewFormUser, Model model) {
        model.addAttribute("viewFormUser", new ViewFormUser());
        return "registration";
    }

    /**
     * @param viewFormUser
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String checkPersonInfo(@Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        return "redirect:/login";
    }

}
