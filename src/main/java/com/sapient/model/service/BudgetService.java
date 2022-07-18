package com.sapient.model.service;

import com.sapient.exception.*;
import com.sapient.model.beans.Budget;
import com.sapient.model.beans.Category;
import com.sapient.model.beans.User;
import com.sapient.model.dao.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetRepository budgetDao;

    public Budget createBudget(String passwordHash, Integer month, Integer year) throws NotAuthorizedException, BudgetTakenException {
        User user;
        try{
            user = userService.getUserByPasswordHash(passwordHash);
        }catch (UserNotFoundException e){
            throw new NotAuthorizedException("You are not authorized to create a budget");
        }

        if(budgetTaken(month, year, user.getId())){
            throw new BudgetTakenException("Budget for "+ year + "-" + month + " is already created for user " + user.getUsername() );
        }

        Budget budget = new Budget();
        budget.setMonth(month);
        budget.setYear(year);
        budget.setUser(user);

        budgetDao.save(budget);
        return budget;
    }

    public void deleteBudget(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        if (!budgetExists(id)) {
            throw new RecordNotFoundException("Budget not found with id: "+ id);
        }
        Budget budget = getBudget(passwordHash, id);
        budgetDao.delete(budget);
    }

    public Budget getBudget(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        Budget budget = budgetDao.findById(id).orElse(null);
        if(budget==null){
            throw new RecordNotFoundException();
        }
        if(!budget.getUser().getPasswordHash().equals(passwordHash)){
            throw new NotAuthorizedException("You are not authorized to access this budget");
        }
        return budget;
    }

    public List<Budget> getBudgets(String passwordHash) throws NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException("Invalid passwordHash");
        }

        List<Budget> budgets = new ArrayList<Budget>();

        for(Budget budget: budgetDao.findAll()){
            if(budget.getUser().getId() == user.getId()){
                budgets.add(budget);
            }
        }
        return budgets;
    }

    public Boolean budgetTaken(Integer month, Integer year, Integer userId){
        for(Budget budget: budgetDao.findAll()){
            if(budget.getMonth() == month && budget.getYear() == year && budget.getUser().getId() == userId){
                return true;
            }
        }
        return false;
    }
    public Boolean budgetExists(Integer id){
        Budget budget = budgetDao.findById(id).orElse(null);
        return budget != null;
    }
}
