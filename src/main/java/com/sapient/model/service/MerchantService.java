package com.sapient.model.service;

import java.util.ArrayList;
import java.util.List;

import com.sapient.exception.CategoryNotFoundException;
import com.sapient.exception.MerchantNotFoundException;
import com.sapient.exception.NotAuthorizedException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.model.beans.Merchant;
import com.sapient.model.beans.User;
import com.sapient.model.dao.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    @Autowired
    private MerchantRepository merchantDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    public Merchant createMerchant(String passwordHash, String name, String description, Integer defaultCategoryId) throws UserNotFoundException, NotAuthorizedException, CategoryNotFoundException {
        User user = userService.getUserByPasswordHash(passwordHash);
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setDescription(description);
        merchant.setDefaultCategory(categoryService.getCategory(passwordHash, defaultCategoryId));
        merchant.setUser(user);
        merchantDao.save(merchant);
        return merchant;
    }

    public void deleteMerchant(String passwordHash, Integer id) throws MerchantNotFoundException, NotAuthorizedException{
        if(!merchantExists(id)){
            throw new MerchantNotFoundException();
        }
        Merchant merchant = getMerchant(passwordHash, id);
        merchantDao.delete(merchant);
    }

    public Merchant getMerchant(String passwordHash, Integer id) throws MerchantNotFoundException, NotAuthorizedException {
        Merchant merchant = merchantDao.findById(id).orElse(null);
        if(merchant == null){
            throw new MerchantNotFoundException();
        }
        if(!merchant.getUser().getPasswordHash().equals(passwordHash)){
            throw new NotAuthorizedException();
        }
        return merchant;
    }

    public List<Merchant> getMerchants(String passwordHash) throws NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException();
        }

        List<Merchant> merchants = new ArrayList<Merchant>();

        for(Merchant merchant: merchantDao.findAll()){
            if(merchant.getUser().getId() == user.getId()){
                merchants.add(merchant);
            }
        }
        return merchants;
    }

    public Boolean merchantExists(Integer id) {
        Merchant merchant = merchantDao.findById(id).orElse(null);
        return merchant!=null;
    }

}