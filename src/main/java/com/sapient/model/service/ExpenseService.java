package com.sapient.model.service;

import com.sapient.exception.CategoryNotFoundException;
import com.sapient.exception.NotAuthorizedException;
import com.sapient.exception.RecordNotFoundException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.model.beans.Category;
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

	@Autowired
	private CategoryService categoryService;

    public Expense createExpense(String passwordHash, String title, String description, Double amount, Date date,
								 Integer categoryId, Integer merchantId, Integer recurrenceId)
			throws NotAuthorizedException, RecordNotFoundException {
		User foundUser;
		Category foundCategory;
		try {
			foundUser = userService.getUserByPasswordHash(passwordHash);
			foundCategory = categoryService.getCategory(passwordHash, categoryId);
		} catch (UserNotFoundException e) {
			throw new NotAuthorizedException("Not authorized: " + e.getMessage());
		} catch (CategoryNotFoundException e) {
			throw new RecordNotFoundException("Can't find category for '"+categoryId+"'");
		}
		Expense expense = new Expense();
		//TODO? do we want to auto add in the date to the current time?
//		expense.setDate( new Date(System.currentTimeMillis()) );
		expense.setDate(date);
        expense.setUser(foundUser);
    	expense.setTitle(title);
    	expense.setAmount(amount);
    	expense.setDescription(description);
		expense.setCategory(foundCategory);
		//TODO merchant
    	expenseDao.save(expense);
    	return expense;
    }

	public Expense updateExpense(String passwordHash, Integer id, String title, String description, Double amount,
								 Date date, Integer categoryId, Integer merchantId, Integer recurrenceId)
			throws NotAuthorizedException, RecordNotFoundException {
		Category foundCategory;
		Expense expense = expenseDao.findById(id).orElse(null);
		if(expense == null){
			throw new RecordNotFoundException("Can not find Expense with ID of '"+id+"'");
		}
		if(!expense.getUser().getPasswordHash().equals(passwordHash)){
			throw new NotAuthorizedException("Can not modify expense, doesn't not belong to you or aren't " +
					"authenticated properly");
		}
		try {
			foundCategory = categoryService.getCategory(passwordHash, categoryId);
		} catch (CategoryNotFoundException e) {
			throw new RecordNotFoundException("Failed to find linked components or authentication to linked " +
					"resources denied: " + e.getMessage());
		}

		expense.setDate(date);
		expense.setTitle(title);
		expense.setAmount(amount);
		expense.setDescription(description);
		expense.setCategory(foundCategory);
		//TODO merchant
		expenseDao.save(expense);
		return expense;
	}

    public Expense deleteExpense(String passwordHash, Integer id)
			throws RecordNotFoundException, NotAuthorizedException {
    	Expense found = expenseDao.findById(id).orElse(null);
    	if(found == null) {
    		throw new RecordNotFoundException("Unable to find Expense with id '"+id+"' ");
    	}
    	if(!found.getUser().getPasswordHash().equals(passwordHash)) {
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
    	if(!found.getUser().getPasswordHash().equals(passwordHash)) {
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
