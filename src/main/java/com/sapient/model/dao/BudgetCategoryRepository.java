package com.sapient.model.dao;

import com.sapient.model.beans.BudgetCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetCategoryRepository extends CrudRepository<BudgetCategory, Integer> {
}
