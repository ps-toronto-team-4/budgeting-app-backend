package com.sapient.model.service;

import com.sapient.exception.*;
import com.sapient.model.beans.*;
import com.sapient.model.dao.ExpenseRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

	@Autowired
	private MerchantService merchantService;

    public Expense createExpense(String passwordHash, String description, Double amount, Date date, Integer categoryId, Integer merchantId, Integer recurrenceId) throws NotAuthorizedException, RecordNotFoundException {
		User foundUser;
		Category foundCategory = null;
		Merchant foundMerchant = null;
		try {
			foundUser = userService.getUserByPasswordHash(passwordHash);
			if(categoryId != null) {
				foundCategory = categoryService.getCategory(passwordHash, categoryId);
			}
			if(merchantId != null) {
				foundMerchant = merchantService.getMerchant(passwordHash, merchantId);
			}
		} catch (UserNotFoundException e) {
			throw new NotAuthorizedException("Not authorized: " + e.getMessage());
		} catch (CategoryNotFoundException e) {
			throw new RecordNotFoundException("Can't find category for '"+categoryId+"'");
		} catch (MerchantNotFoundException e) {
			throw new RecordNotFoundException("Can't find Merchant for '"+merchantId+"'");
		}
		Expense expense = new Expense();
		//TODO? do we want to auto add in the date to the current time?
//		expense.setDate( new Date(System.currentTimeMillis()) );
		expense.setDate(date);
        expense.setUser(foundUser);
//    	expense.setTitle(title);
    	expense.setAmount(amount);
    	expense.setDescription(description);
		expense.setCategory(foundCategory);
		expense.setMerchant(foundMerchant);
    	expenseDao.save(expense);
    	return expense;
    }

	public Expense updateExpense(String passwordHash, Integer id, String description, Double amount,
								 Date date, Integer categoryId, Integer merchantId, Integer recurrenceId)
			throws NotAuthorizedException, RecordNotFoundException {

		User user;
		try{
			user = userService.getUserByPasswordHash(passwordHash);
		}catch (UserNotFoundException e){
			throw new NotAuthorizedException("Invalid passwordHash");
		}

		Category foundCategory = null;
		Merchant foundMerchant = null;
		Expense expense = expenseDao.findById(id).orElse(null);
		if(expense == null){
			throw new RecordNotFoundException("Can not find Expense with ID of '"+id+"'");
		}
		if(!expense.getUser().getPasswordHash().equals(passwordHash)){
			throw new NotAuthorizedException("Can not modify expense, doesn't not belong to you or aren't " +
					"authenticated properly");
		}
		try {
			if(categoryId != null){
				foundCategory = categoryService.getCategory(passwordHash, categoryId);
			}
			if(merchantId != null){
				foundMerchant = merchantService.getMerchant(passwordHash, merchantId);
			}
		} catch (CategoryNotFoundException e) {
			throw new RecordNotFoundException("Failed to find linked components or authentication to linked " +
					"resources (category) denied: " + e.getMessage());
		} catch (MerchantNotFoundException e) {
			throw new RecordNotFoundException("Failed to find linked components or authentication to linked " +
					"resources (merchant) denied: " + e.getMessage());
		}

		expense.setDate(date);
//		expense.setTitle(title);
		expense.setAmount(amount);
		expense.setDescription(description);
		expense.setCategory(foundCategory);
		expense.setMerchant(foundMerchant);
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

	public List<Expense> getExpenses(String passwordHash ) throws NotAuthorizedException {
		User user;
		try {
			user = userService.getUserByPasswordHash(passwordHash);
		}catch(Exception e){
			throw new NotAuthorizedException();
		}

		List<Expense> foundAll = new ArrayList<Expense>();
		expenseDao.findAll().forEach(expense -> foundAll.add(expense));
		List<Expense> found = foundAll.stream().filter(
				item -> {
					if( item.getUser() == null) {
						return false;
					}else {
						return item.getUser().getId() == user.getId();
					}
				})
				.collect(Collectors.toList());
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

	public List<Expense> getExpensesInMonth(String passwordHash, MonthType month, Integer year) throws NotAuthorizedException {
		List<Expense> allExpenses = getExpenses(passwordHash);
		List<Expense> expenses = new ArrayList<>();
		for(Expense expense:allExpenses){
			if(expense.inMonth(month, year)){
				expenses.add(expense);
			}
		}
		return expenses;
	}
}
