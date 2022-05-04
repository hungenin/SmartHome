package com.homeproject.smarthome.tvGuide.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException() {
        super();
    }

    public InvalidDataException(String message) {
        super(message);
    }
}