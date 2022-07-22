package com.sapient.model.service;

import com.sapient.exception.CategoryNotFoundException;
import com.sapient.exception.NotAuthorizedException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.model.beans.Category;
import com.sapient.model.beans.DefaultCategory;
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

    public Category createCategory(String passwordHash, String name, String colourHex, String description) throws NotAuthorizedException {
        User user = userService.getUserByPasswordHash(passwordHash);
        Category category = new Category();
        category.setName(name);
        category.setColourHex(colourHex);
        category.setDescription(description);
        category.setUser(user);
        categoryDao.save(category);
        return category;
    }

    public Category updateCategory(String passwordHash, Integer id, String name, String colourHex, String description) throws NotAuthorizedException {
        User user = userService.getUserByPasswordHash(passwordHash);
        Category category;
        try{
            category = getCategory(passwordHash, id);
        }catch (CategoryNotFoundException e){
            throw new NotAuthorizedException("You are not authorized to update this category");
        }
        category.setName(name);
        category.setColourHex(colourHex);
        category.setDescription(description);
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
        if(!category.getUser().getPasswordHash().equals(passwordHash)){
            throw new NotAuthorizedException();
        }
        return category;
    }

    public List<Category> getCategories(String passwordHash) throws NotAuthorizedException {
        User user = userService.getUserByPasswordHash(passwordHash);

        List<Category> categories = new ArrayList<Category>();

        for(Category category: categoryDao.findAll()){
            if(category.getUser().getId() == user.getId()){
                categories.add(category);
            }
        }
        return categories;
    }

    public Boolean categoryExists(Integer id) {
        Category category = categoryDao.findById(id).orElse(null);
        return category != null;
    }

    public void createCategoryFromDefaultCategory(User user, DefaultCategory defaultCategory){
        Category category = new Category();

        category.setUser(user);
        category.setName(defaultCategory.getName());
        category.setDescription(defaultCategory.getDescription());
        category.setColourHex(defaultCategory.getColourHex());

        categoryDao.save(category);
    }
}
