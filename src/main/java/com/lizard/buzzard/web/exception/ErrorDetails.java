package com.lizard.buzzard.web.exception;

import java.time.Instant;

/**
 * Error details to to pass as response body
 */
public class ErrorDetails {
    private Instant instant;
    private String message;
    private String error;

    public ErrorDetails(String message) {
        super();
        this.instant = Instant.now();
        this.message = message;
    }

    public ErrorDetails(String message, String error) {
        super();
        this.instant = Instant.now();
        this.message = message;
        this.error = error;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}