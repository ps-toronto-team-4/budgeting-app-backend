package com.sapient.model.dao;

import com.sapient.model.beans.DefaultCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultCategoryRepository extends CrudRepository<DefaultCategory, Integer> {
}
