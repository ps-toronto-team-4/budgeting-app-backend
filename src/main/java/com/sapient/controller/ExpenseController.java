package com.sapient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.sapient.exception.RecordNotFoundException;
import com.sapient.model.beans.Expense;
import com.sapient.model.service.ExpenseService;

@Controller
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    
 // Object names map to graphql types of the same name. Eg: "CreateUserFailed".
    record CreateExpenseSuccess(Expense expense) {}
    record CreateExpenseFailed(String exceptionName, String errorMessage) {}

    @QueryMapping
    public Record expense(@Argument Integer id) {
    	try {
	    	Expense found = expenseService.getExpense(id);
	    	return new CreateExpenseSuccess(found);
    	} catch (Exception e) {
    		return new CreateExpenseFailed(e.getClass().getSimpleName(), e.getMessage());
    	}
    }

    

    // Properties of a returned object from a @SchemaMapping method map to graphql fields of the same name.
    // Eg: "exceptionName".
    @MutationMapping
    public Record createExpense(@Argument String title, @Argument String description, @Argument Double amount) {
        try {
            return new CreateExpenseSuccess(expenseService.createExpense(title,description,amount));
        } catch (Exception e) {
            return new CreateExpenseFailed(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    record DeleteExpenseSuccess(Expense expense) {}
    record DeleteExpenseFailed(String exceptionName, String errorMessage) {}

    @MutationMapping
    public Record deleteExpense(@Argument Integer id) {
        try {
            return new DeleteExpenseSuccess(expenseService.deleteExpense(id));
        } catch (Exception e) {
            return new DeleteExpenseFailed(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
