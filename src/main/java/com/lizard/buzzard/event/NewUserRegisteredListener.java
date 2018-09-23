package com.lizard.buzzard.event;

import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

@Component
public class NewUserRegisteredListener implements ApplicationListener<AfterUserRegisteredEvent> {

    @Autowired
    UserService userService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    LocaleResolver localeResolver;

    @Override
    public void onApplicationEvent(AfterUserRegisteredEvent afterUserRegisteredEvent) {
        User user = afterUserRegisteredEvent.getUser();
        String token = UUID.randomUUID().toString();

        userService.createUsersToken(user, token);

        SimpleMailMessage emailMessage = composeEmailMessage(user, token, afterUserRegisteredEvent);

        emailSender.send(emailMessage);
    }

    private SimpleMailMessage composeEmailMessage(User user, String token, AfterUserRegisteredEvent event) {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Locale localeSelected = localeResolver.resolveLocale(httpServletRequest);

        String subject = "Registration Confirmation";
        String recipientAddress = user.getEmail();
        String confirmationUrl = event.getAppUrl() + "/registrationStatus.html?token=" + token;
        String message = messageSource.getMessage("registration.email.message.text", null, /*event.getLocale()*/ localeSelected);

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(recipientAddress);
        emailMessage.setSubject(subject);
        emailMessage.setText(message + "\r\n\r\n" + confirmationUrl + "\r\n");
        return emailMessage;
    }


}
