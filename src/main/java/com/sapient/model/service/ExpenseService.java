package com.sapient.model.service;

import com.sapient.controller.record.MonthBreakdownCategory;
import com.sapient.controller.record.MonthBreakdown;
import com.sapient.controller.record.MonthBreakdownMerchant;
import com.sapient.exception.*;
import com.sapient.model.beans.*;
import com.sapient.model.dao.ExpenseRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
		User foundUser = userService.getUserByPasswordHash(passwordHash);;
		Category foundCategory = null;
		Merchant foundMerchant = null;
		try {
			if(categoryId != null) {
				foundCategory = categoryService.getCategory(passwordHash, categoryId);
			}
			if(merchantId != null) {
				foundMerchant = merchantService.getMerchant(passwordHash, merchantId);
			}
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

		User user = userService.getUserByPasswordHash(passwordHash);

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
    		throw new NotAuthorizedException("You are not authorized to delete this expense");
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
    		throw new NotAuthorizedException("You are not authorized to view this expense");
    	}
        return found;
    }

	public List<Expense> getExpenses(String passwordHash ) throws NotAuthorizedException {
		User user = userService.getUserByPasswordHash(passwordHash);

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

	public MonthBreakdown getMonthBreakdown(String passwordHash, MonthType month, Integer year) throws NotAuthorizedException {
		List<Expense> monthlyExpenses = getExpensesInMonth(passwordHash, month, year);
		List<MonthBreakdownCategory> byCategory = new ArrayList<>();
		List<MonthBreakdownMerchant> byMerchant = new ArrayList<>();
		User user = userService.getUserByPasswordHash(passwordHash);
		Double totalSpent = 0.0;

		Category topCategory = null;
		Merchant topMerchant = null;

		HashMap<Category, Double> categoryTotals = new HashMap<>();
		for(Category category: user.getCategories()){
			categoryTotals.put(category, 0.0);
		}
		categoryTotals.put(null, 0.0); // uncategorized

		HashMap<Merchant, Double> merchantTotals = new HashMap<>();
		for(Merchant merchant: user.getMerchants()){
			merchantTotals.put(merchant, 0.0);
		}
		merchantTotals.put(null, 0.0); // uncategorized

		for(Expense expense:monthlyExpenses){
			Double amount = expense.getAmount();
			Category category = expense.getCategory();
			Merchant merchant = expense.getMerchant();
			totalSpent += amount;
			categoryTotals.put(category, categoryTotals.get(category)+amount);
			merchantTotals.put(merchant, merchantTotals.get(merchant)+amount);
		}
		for(Category category: categoryTotals.keySet()){
			Double amount  = categoryTotals.get(category);
			byCategory.add(new MonthBreakdownCategory(category, categoryTotals.get(category)));
			if(category == null){
				continue;
			}
			if(topCategory == null){
				topCategory = category;
			}
			if(amount > categoryTotals.get(topCategory)){
				topCategory = category;
			}
		}

		for(Merchant merchant: merchantTotals.keySet()){
			Double amount  = merchantTotals.get(merchant);
			byMerchant.add(new MonthBreakdownMerchant(merchant, merchantTotals.get(merchant)));
			if(merchant == null){
				continue;
			}
			if(topMerchant == null){
				topMerchant = merchant;
			}
			if(amount > merchantTotals.get(topMerchant)){
				topMerchant = merchant;
			}
		}

		return new MonthBreakdown(month, year, totalSpent, new MonthBreakdownCategory(topCategory, categoryTotals.get(topCategory)), new MonthBreakdownMerchant(topMerchant, merchantTotals.get(topMerchant)), byCategory, byMerchant);
	}

	private MonthYear getFirstMonth(User user){
		List<Expense> expenses = user.getExpenses();
		Date lowestDate = expenses.get(0).getDate();
		for(Expense expense: expenses){
			if(lowestDate.compareTo(expense.getDate()) < 0){
				lowestDate = expense.getDate();
			}
		}
		return new MonthYear(MonthType.values()[lowestDate.getMonth()], lowestDate.getYear()+1900);
	}

	private MonthYear getLastMonth(User user){
		List<Expense> expenses = user.getExpenses();
		Date highestDate = expenses.get(0).getDate();
		for(Expense expense: expenses){
			if(highestDate.compareTo(expense.getDate()) > 0){
				highestDate = expense.getDate();
			}
		}
		return new MonthYear(MonthType.values()[highestDate.getMonth()], highestDate.getYear()+1900);
	}


}
