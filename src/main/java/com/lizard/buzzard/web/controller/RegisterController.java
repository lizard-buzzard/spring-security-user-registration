package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.event.AfterUserRegisteredEvent;
import com.lizard.buzzard.persistence.model.TokenStatus;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.service.UserServiceImpl;
import com.lizard.buzzard.web.dto.ViewFormLogin;
import com.lizard.buzzard.web.dto.ViewFormUser;
import com.lizard.buzzard.web.exception.ResponseDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String getLoginPage(ViewFormLogin viewFormLogin, Model model, HttpServletRequest httpServletRequest) {
        // an alternative way is: httpServletRequest.getLocale().toString();
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
    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> checkPersonInfo(@RequestBody @Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser,
                                  BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Map<String, String> map = new HashMap<>();
            bindingResult.getAllErrors().forEach(
                    e -> map.put(
                            ((DefaultMessageSourceResolvable) e.getArguments()[0]).getDefaultMessage(),
                            e.getDefaultMessage()
                    )
            );

            ResponseDetails responseDetailsBody = new ResponseDetails("error", map);
            return new ResponseEntity<Object>(responseDetailsBody, new HttpHeaders(), HttpStatus.CONFLICT);
        }

        User newRegisteredUser = userService.saveUserInRepository(viewFormUser);

        ApplicationEvent event = new AfterUserRegisteredEvent(newRegisteredUser, /*localeSelected,*/ getAppUri(request));
        applicationEventPublisher.publishEvent(event);

        model.addAttribute("waitConfirm", "ok");

        String confirmMsg = messageSource.getMessage("registration.message.before.confirmation", new Object[]{}, localeResolver.resolveLocale(request));
        System.out.println(confirmMsg);

        Map<String, String> map = new HashMap<>();
        map.put("confirmMsg", confirmMsg);

//        return "registration";
        ResponseDetails responseDetailsBody = new ResponseDetails("success", map);
        return new ResponseEntity<Object>(responseDetailsBody, new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/registrationStatus", method = RequestMethod.GET)
    public String checkConfirmationToken(@RequestParam("token") String token, Model model) {
        TokenStatus status = userService.verifyConfirmationToken(token);
        if (TOKEN_VALUD.equals(status)) {
            model.addAttribute("tokenValid", "you are successfully registered");
        } else if (TOKEN_EXPIRED.equals(status)) {
            model.addAttribute("tokenExpired", "you are not registered because of late registration");
        } else if (TOKEN_INVALID.equals(status)) {
            model.addAttribute("tokenInvalid", "token you provided is invalid");
        }
        return "registrationStatus";
    }

    // Auxiliary methods

    private String getAppUri(HttpServletRequest req) {
        return "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

    private ResponseEntity<Object> getResponseEntity(String indicator, HttpStatus httpStatus, BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        if(bindingResult != null) {
            bindingResult.getAllErrors().forEach(
                    e -> map.put(
                            ((DefaultMessageSourceResolvable) e.getArguments()[0]).getDefaultMessage(),
                            e.getDefaultMessage()
                    )
            );
        }
        ResponseDetails responseDetailsBody = new ResponseDetails(indicator, map);
        return new ResponseEntity<Object>(responseDetailsBody, new HttpHeaders(), httpStatus);
    }


}
