package com.sapient.model.service;

import com.sapient.exception.NotAuthorizedException;
import com.sapient.exception.RecordNotFoundException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.model.beans.Expense;
import com.sapient.model.beans.User;
import com.sapient.model.dao.ExpenseRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseDao;
    
    @Autowired
    private UserService userService;

    public Expense createExpense(String passwordHash, String title, String description, Double amount) throws NotAuthorizedException {
    	User foundUser;
		try {
			foundUser = userService.getUserByPasswordHash(passwordHash);
		} catch (UserNotFoundException e) {
			throw new NotAuthorizedException();
		}
		Expense expense = new Expense();
		expense.setDate( new Date(System.currentTimeMillis()) );
        expense.setUser(foundUser);
    	expense.setTitle(title);
    	expense.setAmount(amount);
    	expense.setDescription(description);
    	expenseDao.save(expense);
    	return expense;
    }

    public Expense deleteExpense(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
    	Expense found = expenseDao.findById(id).orElse(null);
    	if(found == null) {
    		throw new RecordNotFoundException("Unable to find Expense with id '"+id+"' ");
    	}
    	if(found.getUser().getPasswordHash() != passwordHash) {
    		throw new NotAuthorizedException();
    	}
    	expenseDao.delete(found);
        return found;
    }

    public Expense getExpense(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
    	Expense found = expenseDao.findById(id).orElse(null);
    	if(found == null) {
    		throw new RecordNotFoundException("Unable to find Expense with id '"+id+"' ");
    	}
    	if(found.getUser().getPasswordHash() != passwordHash) {
    		throw new NotAuthorizedException();
    	}
        return found;
    }

    public boolean expenseExists(String passwordHash, Integer id) {
        try {
            getExpense(passwordHash, id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
