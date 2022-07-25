package com.sapient.model.service;

import com.sapient.controller.record.BudgetCategoryDetails;
import com.sapient.controller.record.BudgetDetails;
import com.sapient.exception.*;
import com.sapient.model.beans.*;
import com.sapient.model.dao.BudgetCategoryRepository;
import com.sapient.model.dao.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BudgetService {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetRepository budgetDao;

    @Autowired
    private BudgetCategoryRepository budgetCategoryDao;

    public Budget createBudget(String passwordHash, MonthType month, Integer year) throws NotAuthorizedException, BudgetTakenException {
        User user = userService.getUserByPasswordHash(passwordHash);

        if(budgetTaken(month, year, user)){
            throw new BudgetTakenException("Budget for "+ year + "-" + month + " is already created for user " + user.getUsername() );
        }

        Budget budget = new Budget();
        budget.setMonth(month);
        budget.setYear(year);
        budget.setUser(user);

        budgetDao.save(budget);
        return budget;
    }

    public Budget copyBudget(String passwordHash, Integer id, MonthType month, Integer year) throws NotAuthorizedException, BudgetTakenException, RecordNotFoundException {
        Budget budgetToCopy = getBudget(passwordHash, id);
        List<BudgetCategory> budgetCategories = budgetToCopy.getBudgetCategories();
        List<BudgetCategory> newBudgetCategories = new ArrayList<>();
        Budget budget = createBudget(passwordHash, month, year);
        budget = budgetDao.save(budget);
        for(BudgetCategory budgetCategory: budgetCategories){
            newBudgetCategories.add(copyBudgetCategoryToNewBudget(budgetCategory, budget));
        }
        budget.setBudgetCategories(newBudgetCategories);
        return budget;
    }

    public void deleteBudget(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        Budget budget = getBudget(passwordHash, id);
        budgetDao.delete(budget);
    }

    public Budget getBudget(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        User user = userService.getUserByPasswordHash(passwordHash);
        Budget budget = budgetDao.findById(id).orElse(null);
        if(budget==null){
            throw new RecordNotFoundException("Could not find the specified budget: " + id);
        }
        if(!budget.getUser().getPasswordHash().equals(passwordHash)){
            throw new NotAuthorizedException("You are not authorized to access this budget");
        }
        return budget;
    }

    public Budget getBudgetByDate(String passwordHash, MonthType month, Integer year) throws RecordNotFoundException, NotAuthorizedException{
        User user = userService.getUserByPasswordHash(passwordHash);
        for(Budget budget:user.getBudgets()){
            if(budget.getMonth().equals(month) && budget.getYear().equals(year)){
                return budget;
            }
        }
        throw new RecordNotFoundException("Could not find the specified budget: " + year + "-" + month);
    }

    public List<Budget> getBudgets(String passwordHash) throws NotAuthorizedException {
        User user = userService.getUserByPasswordHash(passwordHash);
        return  user.getBudgets();
    }

    public Boolean budgetTaken(MonthType month, Integer year, User user) {
        for(Budget budget: user.getBudgets()){
            if(budget.getMonth().equals(month) && budget.getYear().equals(year)){
                return true;
            }
        }
        return false;
    }
    public Boolean budgetExists(Integer id){
        Budget budget = budgetDao.findById(id).orElse(null);
        return budget != null;
    }

    public BudgetDetails getBudgetDetails(String passwordHash, Integer id) throws NotAuthorizedException, RecordNotFoundException {
        User user = userService.getUserByPasswordHash(passwordHash);

        Double totalBudgeted = 0.0;
        Double totalActual = 0.0;
        Double totalUnplanned = 0.0;

        Budget budget = getBudget(passwordHash, id);

        List<BudgetCategoryDetails> byCategory = new ArrayList<>();
        HashMap<Category, Double> categoryAmounts = new HashMap<>();
        for(BudgetCategory budgetCategory: budget.getBudgetCategories()){
            categoryAmounts.put(budgetCategory.getCategory(), 0.0);
            totalBudgeted += budgetCategory.getAmount();
        }
        for(Expense expense: expenseService.getExpensesInMonth(passwordHash, budget.getMonth(), budget.getYear())){
            Category category = expense.getCategory();
            if(categoryAmounts.containsKey(category)){
                totalActual += expense.getAmount();
                categoryAmounts.put(category, categoryAmounts.get(category) + expense.getAmount());
            }else{
                totalUnplanned += expense.getAmount();
            }
        }
        for(BudgetCategory budgetCategory: budget.getBudgetCategories()){
            byCategory.add(new BudgetCategoryDetails(budgetCategory.getCategory(), budgetCategory.getAmount(), categoryAmounts.get(budgetCategory.getCategory())));
        }
        return new BudgetDetails(budget, totalBudgeted, totalActual, totalUnplanned, byCategory);
    }

    private BudgetCategory copyBudgetCategoryToNewBudget(BudgetCategory budgetCategory, Budget newBudget){
        BudgetCategory newBudgetCategory = new BudgetCategory();
        newBudgetCategory.setBudget(newBudget);
        newBudgetCategory.setAmount(budgetCategory.getAmount());
        newBudgetCategory.setCategory(budgetCategory.getCategory());
        newBudgetCategory.setUser(budgetCategory.getUser());
        budgetCategoryDao.save(newBudgetCategory);
        return newBudgetCategory;
    }
}
