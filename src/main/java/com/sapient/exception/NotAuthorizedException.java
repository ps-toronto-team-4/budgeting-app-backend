package com.sapient.exception;

public class NotAuthorizedException extends Exception {
    public NotAuthorizedException() {
        super();
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
