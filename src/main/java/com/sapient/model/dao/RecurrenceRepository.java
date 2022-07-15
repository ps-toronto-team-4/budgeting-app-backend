package com.sapient.model.dao;

import com.sapient.model.beans.Expense;
import com.sapient.model.beans.Recurrence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurrenceRepository extends CrudRepository<Recurrence, Integer> {
}
