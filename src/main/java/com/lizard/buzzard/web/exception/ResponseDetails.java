package com.lizard.buzzard.web.exception;

import java.util.Map;

/**
 * Messages details to pass to html-form as response body
 */
public class ResponseDetails {
    /**
     * "error" or "success"
     */
    private String indicator;

    /**
     * key - id attribute name in "errors'" tags like <span id="emailError" ...></span> on registration.html page
     * value - message which should be displayed in these tags in case of error or other information event detected
     */
    private Map<String, String> messages;

    public ResponseDetails(String indicator, Map<String, String> messages) {
        super();
        this.indicator = indicator;
        this.messages = messages;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }
}