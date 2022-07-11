package com.sapient.exception;

public class CategoryAlreadyTakenException extends Exception {
    public CategoryAlreadyTakenException() {
        super();
    }

    public CategoryAlreadyTakenException(String message) {
        super(message);
    }
}
