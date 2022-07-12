package com.sapient.model.dao;

import com.sapient.model.beans.Merchant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends CrudRepository<Merchant, Integer> {
}
