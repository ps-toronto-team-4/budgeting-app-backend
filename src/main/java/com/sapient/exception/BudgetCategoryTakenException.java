package com.sapient.exception;

public class BudgetCategoryTakenException extends Exception {
    public BudgetCategoryTakenException() {
        super();
    }

    public BudgetCategoryTakenException(String message) {
        super(message);
    }
}
