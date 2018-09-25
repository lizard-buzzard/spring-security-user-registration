package com.lizard.buzzard.event;

public enum USER_CHANGED {
    PASSWORD("change.password."),
    EMAIL("change.email.");

    private final String prefix;

    USER_CHANGED(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
