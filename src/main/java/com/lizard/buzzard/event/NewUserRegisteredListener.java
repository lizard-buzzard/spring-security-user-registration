package com.lizard.buzzard.event;

import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NewUserRegisteredListener implements ApplicationListener<AfterUserRegisteredEvent> {

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(AfterUserRegisteredEvent afterUserRegisteredEvent) {
        User user = afterUserRegisteredEvent.getUser();
        String token = UUID.randomUUID().toString();

        userService.createUsersToken(user, token);
    }
}
