package com.homeproject.smarthome.tvguide.exception;

public class CannotBeAddedException extends RuntimeException {
    public CannotBeAddedException() {
        super();
    }

    public CannotBeAddedException(String message) {
        super(message);
    }
}