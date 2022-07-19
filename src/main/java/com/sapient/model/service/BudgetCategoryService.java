package com.sapient.model.service;

import com.sapient.exception.*;
import com.sapient.model.beans.Budget;
import com.sapient.model.beans.BudgetCategory;
import com.sapient.model.beans.User;
import com.sapient.model.dao.BudgetCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetCategoryService {

    @Autowired
    private BudgetCategoryRepository budgetCategoryDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BudgetService budgetService;

    public BudgetCategory createBudgetCategory(String passwordHash, Float amount, Integer categoryId, Integer budgetId)
            throws NotAuthorizedException, BudgetCategoryTakenException, CategoryNotFoundException,
            RecordNotFoundException {
        User user;
        try{
            user = userService.getUserByPasswordHash(passwordHash);
        }catch (UserNotFoundException e){
            throw new NotAuthorizedException("You are not authorized to create a budgetCategory");
        }

        if(budgetCategoryTaken(categoryId, budgetId, user.getId())){
            throw new BudgetCategoryTakenException("BudgetCategory for budget: "+ budgetId + ", category: " +
                    categoryId + " is already created for user " + user.getUsername() );
        }

        BudgetCategory budgetCategory = new BudgetCategory();

        Budget budget = budgetService.getBudget(passwordHash, budgetId);

        budgetCategory.setCategory(categoryService.getCategory(passwordHash, categoryId));
        budgetCategory.setBudget(budget);
        budgetCategory.setAmount(amount);
        budgetCategory.setUser(user);

        budgetCategoryDao.save(budgetCategory);
        return budgetCategory;
    }

    public BudgetCategory updateBudgetCategory(String passwordHash, Integer id, Float amount) throws NotAuthorizedException {
        User user;
        BudgetCategory budgetCategory;
        try{
            user = userService.getUserByPasswordHash(passwordHash);
            budgetCategory = getBudgetCategory(passwordHash, id);
        }catch (UserNotFoundException | RecordNotFoundException e){
            throw new NotAuthorizedException("You are not authorized to update this budgetCategory");
        }
        budgetCategory.setAmount(amount);

        budgetCategoryDao.save(budgetCategory);
        return budgetCategory;
    }

    public void deleteBudgetCategory(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        if (!budgetCategoryExists(id)) {
            throw new RecordNotFoundException("BudgetCategory not found with id: "+ id);
        }
        BudgetCategory budgetCategory = getBudgetCategory(passwordHash, id);
        budgetCategoryDao.delete(budgetCategory);
    }

    public BudgetCategory getBudgetCategory(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        BudgetCategory budgetCategory = budgetCategoryDao.findById(id).orElse(null);
        if(budgetCategory==null){
            throw new RecordNotFoundException();
        }
        if(!budgetCategory.getUser().getPasswordHash().equals(passwordHash)){
            throw new NotAuthorizedException("You are not authorized to access this budgetCategory");
        }
        return budgetCategory;
    }

    public List<BudgetCategory> getBudgetCategories(String passwordHash) throws NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException("Invalid passwordHash");
        }

        List<BudgetCategory> budgetCategories = new ArrayList<BudgetCategory>();

        for(BudgetCategory budgetCategory: budgetCategoryDao.findAll()){
            if(budgetCategory.getUser().getId() == user.getId()){
                budgetCategories.add(budgetCategory);
            }
        }
        return budgetCategories;
    }

    public Boolean budgetCategoryTaken(Integer categoryId, Integer budgetId, Integer userId){
        for(BudgetCategory budgetCategory: budgetCategoryDao.findAll()){
            if(budgetCategory.getCategory().getId() == categoryId &&
                    budgetCategory.getBudget().getId() == budgetId &&
                    budgetCategory.getUser().getId() == userId ){
                return true;
            }
        }
        return false;
    }

    public Boolean budgetCategoryExists(Integer id){
        BudgetCategory budgetCategory = budgetCategoryDao.findById(id).orElse(null);
        return budgetCategory != null;
    }
}
