package com.sapient.controller;

import com.sapient.model.beans.MonthType;
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

import java.util.Date;
import java.util.List;

@Controller
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    
    record ExpenseSuccess(Expense expense) {}
    record ExpensesSuccess(List<Expense> expenses) {}

    @QueryMapping
    public Record expense(@Argument String passwordHash, @Argument Integer id) {
    	try {
	    	Expense found = expenseService.getExpense(passwordHash,id);
	    	return new ExpenseSuccess(found);
    	} catch (Exception e) {
    		return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
    	}
    }

    @QueryMapping
    public Record expenses(@Argument String passwordHash) {
        try {
            List<Expense> found = expenseService.getExpenses(passwordHash);
            return new ExpensesSuccess(found);
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @QueryMapping
    public Record expensesInMonth(@Argument String passwordHash, @Argument MonthType month, @Argument Integer year) {
        try {
            List<Expense> found = expenseService.getExpensesInMonth(passwordHash, month, year);
            return new ExpensesSuccess(found);
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    // Properties of a returned object from a @SchemaMapping method map to graphql fields of the same name.
    // Eg: "exceptionName".
    @MutationMapping
    public Record createExpense(@Argument String passwordHash, @Argument String description,
                                @Argument Double amount, @Argument Integer epochDate, @Argument Integer categoryId,
                                @Argument Integer merchantId, @Argument Integer recurrenceId) {
        Long epochMillis = ((Long)(long)(int)epochDate) * 1000;
        Date date = new Date(epochMillis); //Epoch time is in second not milliseconds
        try {
            return new ExpenseSuccess(expenseService.createExpense(passwordHash,description,amount,date,
                    categoryId,merchantId,recurrenceId));
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record updateExpense(@Argument String passwordHash, @Argument Integer id,
                                @Argument String description, @Argument Double amount, @Argument Integer epochDate,
                                @Argument Integer categoryId, @Argument Integer merchantId,
                                @Argument Integer recurrenceId){
        Date date = new Date(epochDate * 1000); //Epoch time is in second not milliseconds
        try {
            return new ExpenseSuccess(expenseService.updateExpense(passwordHash,id,description,amount,date,
                    categoryId,merchantId,recurrenceId));
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteExpense(@Argument String passwordHash, @Argument Integer id) {
        try {
            expenseService.deleteExpense(passwordHash,id);
            return new DeleteSuccess("Successfully deleted expense.");
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
