package com.sapient.exception;

public class BudgetTakenException extends Exception {
    public BudgetTakenException() {
        super();
    }

    public BudgetTakenException(String message) {
        super(message);
    }
}
