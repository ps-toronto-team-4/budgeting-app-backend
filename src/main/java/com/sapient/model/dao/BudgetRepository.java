package com.sapient.model.dao;

import com.sapient.model.beans.Budget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Integer> {
}
