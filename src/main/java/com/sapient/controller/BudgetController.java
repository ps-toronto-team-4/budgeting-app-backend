package com.sapient.controller;

import com.sapient.controller.record.BudgetDetails;
import com.sapient.controller.record.DeleteSuccess;
import com.sapient.controller.record.FailurePayload;
import com.sapient.model.beans.Budget;
import com.sapient.model.beans.Merchant;
import com.sapient.model.beans.MonthType;
import com.sapient.model.service.BudgetService;
import com.sapient.model.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    record BudgetSuccess(Budget budget) {}

    @QueryMapping
    public Record budget(@Argument String passwordHash, @Argument Integer id){
        try{
            return new BudgetSuccess(budgetService.getBudget(passwordHash, id));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @QueryMapping
    public Record budgetByDate(@Argument String passwordHash, @Argument MonthType month, @Argument Integer year){
        try{
            return new BudgetSuccess(budgetService.getBudgetByDate(passwordHash, month, year));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @QueryMapping
    public Record budgetDetails(@Argument String passwordHash, @Argument Integer id){
        try{
            return budgetService.getBudgetDetails(passwordHash, id);
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @QueryMapping
    public Record budgetDetailsByDate(@Argument String passwordHash, @Argument MonthType month, @Argument Integer year){
        try{
            return budgetService.getBudgetDetailsByDate(passwordHash, month, year);
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }


    record BudgetsSuccess(List<Budget> budgets) {}

    @QueryMapping
    public Record budgets(@Argument String passwordHash){
        try{
            return new BudgetsSuccess(budgetService.getBudgets(passwordHash));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record createBudget(@Argument String passwordHash, @Argument MonthType month, @Argument Integer year) {
        try{
            return new BudgetSuccess(budgetService.createBudget(passwordHash, month, year));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record copyBudget(@Argument String passwordHash, @Argument Integer id, @Argument MonthType month, @Argument Integer year) {
        try{
            return new BudgetSuccess(budgetService.copyBudget(passwordHash, id, month, year));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteBudget(@Argument String passwordHash, @Argument Integer id){
        try{
            budgetService.deleteBudget(passwordHash, id);
            return new DeleteSuccess("Successfully deleted budget");
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
