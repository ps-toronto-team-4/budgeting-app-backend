package com.sapient.model.service;

import com.sapient.exception.*;
import com.sapient.model.beans.Budget;
import com.sapient.model.beans.MonthType;
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

    public Budget createBudget(String passwordHash, MonthType month, Integer year) throws NotAuthorizedException, BudgetTakenException {
        User user;
        try{
            user = userService.getUserByPasswordHash(passwordHash);
        }catch (UserNotFoundException e){
            throw new NotAuthorizedException("You are not authorized to create a budget");
        }

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

    public void deleteBudget(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        Budget budget = getBudget(passwordHash, id);
        budgetDao.delete(budget);
    }

    public Budget getBudget(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException("Invalid passwordHash");
        }
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
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException("Invalid passwordHash");
        }
        for(Budget budget:user.getBudgets()){
            if(budget.getMonth().equals(month) && budget.getYear().equals(year)){
                return budget;
            }
        }
        throw new RecordNotFoundException("Could not find the specified budget: " + year + "-" + month);
    }

    public List<Budget> getBudgets(String passwordHash) throws NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException("Invalid passwordHash");
        }

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
}
