package com.lizard.buzzard.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class NewUserRegisteredListener implements ApplicationListener<AfterUserRegisteredEvent> {

    @Override
    public void onApplicationEvent(AfterUserRegisteredEvent afterUserRegisteredEvent) {

    }
}
