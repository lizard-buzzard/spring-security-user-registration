package com.lizard.buzzard.event;

import com.lizard.buzzard.persistence.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class AfterUserRegisteredEvent extends ApplicationEvent {

    private final User user;
    private final Locale locale;
    private final String appUrl;

    public AfterUserRegisteredEvent(final User user, final Locale locale, final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getAppUrl() {
        return appUrl;
    }
}
