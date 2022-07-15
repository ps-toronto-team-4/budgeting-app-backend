package com.sapient.controller;

import com.sapient.controller.record.DeleteSuccess;
import com.sapient.controller.record.FailurePayload;
import com.sapient.model.beans.Category;
import com.sapient.model.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    record CategorySuccess(Category category) {}

    @QueryMapping
    public Record category(@Argument String passwordHash, @Argument Integer id){
        try{
            return new CategorySuccess(categoryService.getCategory(passwordHash, id));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    record CategoriesSuccess(List<Category> categories) {}

    @QueryMapping
    public Record categories(@Argument String passwordHash){
        try{
            return new CategoriesSuccess(categoryService.getCategories(passwordHash));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record createCategory(@Argument String passwordHash, @Argument String name, @Argument String colourHex, @Argument String description){
        try{
            return new CategorySuccess(categoryService.createCategory(passwordHash, name, colourHex, description));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record updateCategory(@Argument String passwordHash, @Argument Integer id, @Argument String name, @Argument String colourHex, @Argument String description){
        try{
            return new CategorySuccess(categoryService.updateCategory(passwordHash, id, name, colourHex, description));
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteCategory(@Argument String passwordHash, @Argument Integer id){
        try{
            categoryService.deleteCategory(passwordHash, id);
            return new DeleteSuccess("Successfully deleted category");
        }catch(Exception e){
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
