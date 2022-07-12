package com.sapient.exception;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException() {
        super();
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
