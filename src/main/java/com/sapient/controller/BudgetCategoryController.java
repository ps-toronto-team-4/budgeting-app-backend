package com.sapient.controller;

import com.sapient.controller.record.DeleteSuccess;
import com.sapient.controller.record.FailurePayload;
import com.sapient.model.beans.BudgetCategory;
import com.sapient.model.beans.Category;
import com.sapient.model.service.BudgetCategoryService;
import com.sapient.model.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BudgetCategoryController {
    @Autowired
    private BudgetCategoryService budgetCategoryService;

    record BudgetCategorySuccess(BudgetCategory budgetCategory) {}

    @QueryMapping
    public Record budgetCategory(@Argument String passwordHash, @Argument Integer id){
        try{
            return new BudgetCategorySuccess(budgetCategoryService.getBudgetCategory(passwordHash, id));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    record BudgetCategoriesSuccess(List<BudgetCategory> budgetCategories) {}

    @QueryMapping
    public Record budgetCategories(@Argument String passwordHash){
        try{
            return new BudgetCategoriesSuccess(budgetCategoryService.getBudgetCategories(passwordHash));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record createBudgetCategory(@Argument String passwordHash, @Argument Double amount, @Argument Integer categoryId, @Argument Integer budgetId){
        try{
            return new BudgetCategorySuccess(budgetCategoryService.createBudgetCategory(passwordHash, amount, categoryId, budgetId));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record updateBudgetCategory(@Argument String passwordHash, @Argument Integer id, @Argument Double amount){
        try{
            return new BudgetCategorySuccess(budgetCategoryService.updateBudgetCategory(passwordHash, id, amount));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteBudgetCategory(@Argument String passwordHash, @Argument Integer id){
        try{
            budgetCategoryService.deleteBudgetCategory(passwordHash, id);
            return new DeleteSuccess("Successfully deleted budgetCategory");
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
