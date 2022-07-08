package com.sapient.model.dao;

import com.sapient.model.beans.UsersTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<UsersTest, Integer> {
}
