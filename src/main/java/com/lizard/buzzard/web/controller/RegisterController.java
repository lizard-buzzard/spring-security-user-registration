package com.lizard.buzzard.web.controller;

import com.lizard.buzzard.event.AfterUserChangedEvent;
import com.lizard.buzzard.event.AfterUserRegisteredEvent;
import com.lizard.buzzard.event.USER_CHANGED;
import com.lizard.buzzard.persistence.model.TokenStatus;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.service.AdminServiceImpl;
import com.lizard.buzzard.service.UserServiceImpl;
import com.lizard.buzzard.web.dto.UserWithAuthorityRights;
import com.lizard.buzzard.web.dto.ViewFormChangePassword;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private AdminServiceImpl adminService;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(ViewFormUser viewFormUser, Model model) {
        model.addAttribute("viewFormUser", new ViewFormUser());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> checkPersonInfo(@RequestBody @Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser,
                                                  BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return getResponseEntity("error", HttpStatus.CONFLICT, bindingResultMsgMap(bindingResult));
        }

        User newRegisteredUser = userService.saveUserInRepository(viewFormUser, localeResolver.resolveLocale(request));

        ApplicationEvent event = new AfterUserRegisteredEvent(newRegisteredUser, /*localeSelected,*/ getAppUri(request));
        applicationEventPublisher.publishEvent(event);

        return getResponseEntity("success", HttpStatus.OK,
                singleMsgMap("beforeConfirmMsg", messageSource.getMessage("registration.message.before.confirmation",
                        new Object[]{}, localeResolver.resolveLocale(request))
                )
        );
    }

    @RequestMapping(value = "/registrationStatus", method = RequestMethod.GET)
    public String checkConfirmationToken(@RequestParam("token") String token, Model model) {
        TokenStatus status = userService.verifyConfirmationToken(token);

        // TODO:             authWithoutPassword(user);

        if (TOKEN_VALUD.equals(status)) {
            model.addAttribute("tokenValid", "you are successfully registered");
        } else if (TOKEN_EXPIRED.equals(status)) {
            model.addAttribute("tokenExpired", "you are not registered because of late registration");
        } else if (TOKEN_INVALID.equals(status)) {
            model.addAttribute("tokenInvalid", "token you provided is invalid");
        }
        return "registrationStatus";
    }

    @RequestMapping(value = "/homepage/{role}", method = RequestMethod.GET)
    public String getHomePage(Model model, @PathVariable("role") String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute("loggedUserName", String.format(" %s %s", user.getFirstname(), user.getLastname()));

        if (role.equals("admin")) {
            List<UserWithAuthorityRights> userWithAuthorityRights = adminService.getUsersList();
            model.addAttribute("listOfUsers", userWithAuthorityRights);
        }
        return role.equals("user") ? "userHomePage" : "adminConsolePage";
    }

    @RequestMapping("/accessDenied")
    public String authorizationErrorPage() {
        return "authorizationError";
    }

    // change password block of methods

    @RequestMapping(value = "/userAccount", method = RequestMethod.GET)
    public String getUserAccountPage(ViewFormChangePassword viewFormChangePassword, Model model) {
        model.addAttribute("viewFormChangePassword", new ViewFormChangePassword());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if("anonymousUser".equals(principal)) {
            return "redirect:/accessDenied";
        }

        User user = (User)principal;
        model.addAttribute("loggedUserName", String.format(" %s %s", user.getFirstname(), user.getLastname()));

        model.addAttribute("loggedUserEmail", user.getEmail());

        return "userAccountPage";
    }

    @RequestMapping(value = "/userServices", method = RequestMethod.GET)
    public String getUserServicesPage() {
        return "userServicesPage";
    }

    @RequestMapping("/redirect")
    public String redirectPage() {
        return "redirect:https://www.w3.org/";
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> changePassword(
            @RequestBody @Valid @ModelAttribute("viewFormChangePassword") ViewFormChangePassword viewFormChangePassword,
            BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return getResponseEntity("error", HttpStatus.CONFLICT, bindingResultMsgMap(bindingResult));
        }
        User user = userService.replaceUserPassword(viewFormChangePassword);
        AfterUserChangedEvent event = new AfterUserChangedEvent(user, getAppUri(request), USER_CHANGED.PASSWORD);
        applicationEventPublisher.publishEvent(event);

        return getResponseEntity("success", HttpStatus.OK,
                singleMsgMap("beforeConfirmMsg", messageSource.getMessage("change.password.message.before.confirmation",
                        new Object[]{}, localeResolver.resolveLocale(request))
                )
        );
    }

    @RequestMapping(value = "/replacePassword", method = RequestMethod.GET)
    public String replacePassword(Model model, final Locale locale, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        model = userService.validateResetTokenAndReplacePassword(id, token, locale, model);
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/LizardsInfoPage", method = RequestMethod.GET)
    public String getLizardHomepage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String loggedUserNameValue;
        if("anonymousUser".equals(principal)) {
            loggedUserNameValue = "anonymousUser";
        } else {
            User user = (User)principal;
            loggedUserNameValue = String.format(" %s %s", user.getFirstname(), user.getLastname());
        }
        model.addAttribute("loggedUserName", loggedUserNameValue);

        return "LizardsInfoPage";
    }

    // Auxiliary methods

    private String getAppUri(HttpServletRequest req) {
        return "http://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
    }

    private ResponseEntity<Object> getResponseEntity(String indicator, HttpStatus httpStatus, Map<String, String> msgMap) {
        ResponseDetails responseDetails = new ResponseDetails(indicator, msgMap);
        return new ResponseEntity<Object>(responseDetails, new HttpHeaders(), httpStatus);
    }

    private Map<String, String> bindingResultMsgMap(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        if (bindingResult != null) {
            bindingResult.getAllErrors().forEach(
                    e -> map.put(
                            ((DefaultMessageSourceResolvable) e.getArguments()[0]).getDefaultMessage(),
                            e.getDefaultMessage()
                    )
            );
        }
        return map;
    }

    private Map<String, String> singleMsgMap(String key, String msg) {
        Map<String, String> map = new HashMap<>();
        map.put(key, msg);
        return map;
    }
}
