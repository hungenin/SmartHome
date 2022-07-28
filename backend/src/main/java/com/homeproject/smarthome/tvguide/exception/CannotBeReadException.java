package com.homeproject.smarthome.tvguide.exception;

public class CannotBeReadException extends RuntimeException{
    public CannotBeReadException() {
        super();
    }

    public CannotBeReadException(String message) {
        super(message);
    }
}
