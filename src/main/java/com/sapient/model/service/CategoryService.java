package com.sapient.model.service;

import com.sapient.exception.CategoryNotFoundException;
import com.sapient.exception.NotAuthorizedException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.model.beans.Category;
import com.sapient.model.beans.User;
import com.sapient.model.dao.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryDao;

    @Autowired
    private UserService userService;

    public Category createCategory(String passwordHash, String name, String colourHex, String description) throws UserNotFoundException {
        User user = userService.getUserByPasswordHash(passwordHash);
        Category category = new Category();
        category.setColourHex(colourHex);
        category.setName(name);
        category.setDescription(description);
        category.setUserId(user.getId());
        categoryDao.save(category);
        return category;
    }

    public void deleteCategory(String passwordHash, Integer id) throws CategoryNotFoundException, NotAuthorizedException {
        if (!categoryExists(id)) {
            throw new CategoryNotFoundException();
        }
        Category category = getCategory(passwordHash, id);
        categoryDao.delete(category);
    }

    public Category getCategory(String passwordHash, Integer id) throws CategoryNotFoundException, NotAuthorizedException {
        Category category = categoryDao.findById(id).orElse(null);
        if(category==null){
            throw new CategoryNotFoundException();
        }
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException();
        }
        if(user.getId()!=category.getUserId()){
            throw new NotAuthorizedException();
        }
        return category;
    }

    public List<Category> getCategories(String passwordHash) throws NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByPasswordHash(passwordHash);
        }catch(Exception e){
            throw new NotAuthorizedException();
        }

        List<Category> categories = new ArrayList<Category>();

        for(Category category: categoryDao.findAll()){
            if(category.getUserId() == user.getId()){
                categories.add(category);
            }
        }
        return categories;
    }

    public Boolean categoryExists(Integer id) {
        Category category = categoryDao.findById(id).orElse(null);
        if(category == null){
            return false;
        }
        return true;
    }
}
