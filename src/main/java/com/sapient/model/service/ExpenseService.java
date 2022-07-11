package com.sapient.model.service;

import com.sapient.exception.RecordNotFoundException;
import com.sapient.model.beans.Expense;
import com.sapient.model.dao.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseDao;

    public Expense createExpense(String title, String description, Double amount) {
        Expense expense = new Expense(title,description,amount);
        expenseDao.save(expense);
        return expense;
//    	Expense expense = new Expense();
//    	expense.setTitle(title);
//    	expense.setAmount(amount);
//    	expense.setDescription(description);
//    	expenseDao.save(expense);
//    	return expense;
    }

    public Expense deleteExpense(Integer id) throws RecordNotFoundException {
    	Expense found = expenseDao.findById(id).orElse(null);
    	if(found == null) {
    		throw new RecordNotFoundException("Unable to find Expense with id '"+id+"' ");
    	}
    	expenseDao.delete(found);
        return found;
    }

    public Expense getExpense(Integer id) throws RecordNotFoundException {
    	Expense found = expenseDao.findById(id).orElse(null);
    	if(found == null) {
    		throw new RecordNotFoundException("Unable to find Expense with id '"+id+"' ");
    	}
        return found;
    }

    public boolean expenseExists(Integer id) {
        try {
            getExpense(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
