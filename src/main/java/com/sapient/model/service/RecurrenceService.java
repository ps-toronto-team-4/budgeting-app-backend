package com.sapient.model.service;

import com.sapient.exception.NotAuthorizedException;
import com.sapient.exception.RecordNotFoundException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.model.beans.Expense;
import com.sapient.model.beans.Recurrence;
import com.sapient.model.beans.User;
import com.sapient.model.dao.ExpenseRepository;
import com.sapient.model.dao.RecurrenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RecurrenceService {
    @Autowired
    private RecurrenceRepository recurrenceDao;
    
    @Autowired
    private UserService userService;

    public Recurrence createRecurrence(String passwordHash, String title, String description, Double amount) throws NotAuthorizedException {
		User foundUser;
		try {
			foundUser = userService.getUserByPasswordHash(passwordHash);
		} catch (UserNotFoundException e) {
			throw new NotAuthorizedException();
		}
		Recurrence recurrence = new Recurrence();
		recurrence.setStartDate( new Date(System.currentTimeMillis()) );
		//TODO endDate
		//TODO period
		recurrence.setUser(foundUser);
		recurrence.setTitle(title);
    	recurrence.setAmount(amount);
    	recurrence.setDescription(description);
		recurrenceDao.save(recurrence);
    	return recurrence;
    }

    public Recurrence deleteRecurrence(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
		Recurrence found = recurrenceDao.findById(id).orElse(null);
    	if(found == null) {
    		throw new RecordNotFoundException("Unable to find Expense with id '"+id+"' ");
    	}
    	if(!found.getUser().getPasswordHash().equals(passwordHash)) {
    		throw new NotAuthorizedException();
    	}
		recurrenceDao.delete(found);
        return found;
    }

    public Recurrence getExpense(String passwordHash, Integer id) throws RecordNotFoundException, NotAuthorizedException {
		Recurrence found = recurrenceDao.findById(id).orElse(null);
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
