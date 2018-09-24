package com.lizard.buzzard.web.exception;

import java.util.Locale;

public class UserAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 5861310537366287163L;
    private Locale locale = null;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(final String message, Locale locale) {
        super(message);
        this.locale = locale;
    }

    public UserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

    public Locale getLocale() {
        return locale;
    }
}
