package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.event.AfterUserRegisteredEvent;
import com.lizard.buzzard.persistence.model.TokenStatus;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.service.UserServiceImpl;
import com.lizard.buzzard.web.dto.ViewFormLogin;
import com.lizard.buzzard.web.dto.ViewFormUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.lizard.buzzard.persistence.model.TokenStatus.*;

@Controller
public class RegisterController {
    private final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    LocaleResolver localeResolver;

    @Autowired
    UserServiceImpl userService;

    // Spring Event to Create the Token and Send the Verification Email
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String getLoginPage(ViewFormLogin viewFormLogin, Model model, HttpServletRequest httpServletRequest) {
        LOGGER.debug("Locale selected on login.html ==>  " + localeResolver.resolveLocale(httpServletRequest).toString());

        model.addAttribute("viewFormLogin", new ViewFormLogin());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLoginPage(@Valid @ModelAttribute("viewFormLogin") ViewFormLogin viewFormLogin, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        // TODO: replace by real http://... page of APPLICATION START PAGE
        String redirectUrl = "https://www.yandex.ru/";
        return "redirect:" + redirectUrl;
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
    public String checkPersonInfo(@Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser,
                                  BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            // NOTE: The chank of the code below is not used, it's an alternative to Thymeleaf's global-errors in place next form code works:
            // <span id="confirmedPassportError" class="alert alert-danger col-sm-4" th:if="${#fields.hasErrors('global')}" th:errors="*{global}"></span>
            ObjectError confirmedPasswordErrMsg = bindingResult.getGlobalError();
            if(confirmedPasswordErrMsg != null) {
                LOGGER.debug("Global error (@PasswordConfirmationValidator) ==>" + confirmedPasswordErrMsg.getDefaultMessage());
                model.addAttribute("confirmedPasswordError", confirmedPasswordErrMsg.getDefaultMessage());
            }
            return "registration";
        }

        User newRegisteredUser = userService.saveUserInRepository(viewFormUser);

//        Locale localeSelected = localeResolver.resolveLocale(request);
        ApplicationEvent event = new AfterUserRegisteredEvent(newRegisteredUser, /*localeSelected,*/ getAppUri(request));
        applicationEventPublisher.publishEvent(event);

        model.addAttribute("waitConfirm", "ok");

        return "registration";
//        return "redirect:/login";
    }

    private String getAppUri(HttpServletRequest req) {
        return "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

    @RequestMapping(value = "/registrationStatus", method = RequestMethod.GET)
    public String checkConfirmationToken(@RequestParam("token") String token, Model model) {
        TokenStatus status = userService.verifyConfirmationToken(token);
        if(TOKEN_VALUD.equals(status)) {
            model.addAttribute("tokenValid", "you are successfully registered");
        } else if (TOKEN_EXPIRED.equals(status)) {
            model.addAttribute("tokenExpired", "you are not registered because of late registration");
        } else if (TOKEN_INVALID.equals(status)) {
            model.addAttribute("tokenInvalid", "token you provided is invalid");
        }
        return "registrationStatus";
    }
}
