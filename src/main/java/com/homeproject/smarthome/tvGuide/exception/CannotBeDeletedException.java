package com.homeproject.smarthome.tvGuide.exception;

public class CannotBeDeletedException extends RuntimeException {
    public CannotBeDeletedException() {
        super();
    }

    public CannotBeDeletedException(String message) {
        super(message);
    }
}