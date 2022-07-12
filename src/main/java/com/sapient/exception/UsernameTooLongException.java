package com.sapient.exception;

public class UsernameTooLongException extends Exception {
    public UsernameTooLongException() {
        super();
    }

    public UsernameTooLongException(String message) {
        super(message);
    }
}
