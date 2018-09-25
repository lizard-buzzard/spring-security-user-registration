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
public class UserChangedListener implements ApplicationListener<AfterUserChangedEvent> {
    @Autowired
    UserService userService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    LocaleResolver localeResolver;

    @Override
    public void onApplicationEvent(AfterUserChangedEvent afterUserChangedEvent) {
        User user = afterUserChangedEvent.getUser();
        String token = UUID.randomUUID().toString();

        userService.createUsersToken(user, token);  // save token in Repository to the user

        SimpleMailMessage emailMessage = composeEmailMessage(user, token, afterUserChangedEvent);

        emailSender.send(emailMessage);
    }

    private SimpleMailMessage composeEmailMessage(User user, String token, AfterUserChangedEvent event) {
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Locale localeSelected = localeResolver.resolveLocale(httpServletRequest);

        String prefix = event.getMessage().getPrefix();

//        String subject = "Reset password";
        String subject = messageSource.getMessage(prefix + "confirmation.subject.text", null, localeSelected);
        String recipientAddress = user.getEmail();
        String confirmationUrl = event.getAppUrl() + String.format("/replacePassword?id=%s&token=%s", String.valueOf(user.getId()), token);
        String message = messageSource.getMessage(prefix + "confirmation.message.text", null, localeSelected);

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(recipientAddress);
        emailMessage.setSubject(subject);
        emailMessage.setText(message + "\r\n\r\n" + confirmationUrl + "\r\n");
        return emailMessage;
    }
}
