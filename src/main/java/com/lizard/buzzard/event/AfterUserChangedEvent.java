package com.lizard.buzzard.event;

import com.lizard.buzzard.persistence.model.User;
import org.springframework.context.ApplicationEvent;

// TODO: merge with AfterUserRegisteredEvent; do the same for UserChangedListener and NewUserRegisteredListener
//
public class AfterUserChangedEvent extends ApplicationEvent {

    private final User user;
//    private final Locale locale;
    private final String appUrl;
    private final USER_CHANGED message;

    public AfterUserChangedEvent(final User user, /*final Locale locale,*/ final String appUrl, final USER_CHANGED message) {
        super(user);
        this.user = user;
//        this.locale = locale;
        this.appUrl = appUrl;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

//    public Locale getLocale() {
//        return locale;
//    }

    public String getAppUrl() {
        return appUrl;
    }

    public USER_CHANGED getMessage() {
        return message;
    }
}
