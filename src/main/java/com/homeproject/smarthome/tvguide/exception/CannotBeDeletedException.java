package com.homeproject.smarthome.tvguide.exception;

public class CannotBeDeletedException extends RuntimeException {
    public CannotBeDeletedException() {
        super();
    }

    public CannotBeDeletedException(String message) {
        super(message);
    }
}