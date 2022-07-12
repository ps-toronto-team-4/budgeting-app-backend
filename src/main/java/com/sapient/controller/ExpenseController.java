package com.sapient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.sapient.controller.record.DeleteSuccess;
import com.sapient.controller.record.FailurePayload;
import com.sapient.exception.RecordNotFoundException;
import com.sapient.model.beans.Expense;
import com.sapient.model.service.ExpenseService;

@Controller
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    
 // Object names map to graphql types of the same name. Eg: "CreateUserFailed".
    record ExpenseSuccess(Expense expense) {}
    record ExpenseFailed(String exceptionName, String errorMessage) {}

    @QueryMapping
    public Record expense(@Argument String passwordHash, @Argument Integer id) {
    	try {
	    	Expense found = expenseService.getExpense(passwordHash,id);
	    	return new ExpenseSuccess(found);
    	} catch (Exception e) {
    		return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
    	}
    }

    

    // Properties of a returned object from a @SchemaMapping method map to graphql fields of the same name.
    // Eg: "exceptionName".
    @MutationMapping
    public Record createExpense(@Argument String passwordHash, @Argument String title, @Argument String description, @Argument Double amount) {
        try {
            return new ExpenseSuccess(expenseService.createExpense(passwordHash,title,description,amount));
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteExpense(@Argument String passwordHash, @Argument Integer id) {
        try {
        	expenseService.deleteExpense(passwordHash,id);
            return new DeleteSuccess("Success");
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
