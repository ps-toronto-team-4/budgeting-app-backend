package com.sapient.model.service;

import com.sapient.exception.CategoryAlreadyTakenException;
import com.sapient.exception.CategoryNotFoundException;
import com.sapient.model.beans.Category;
import com.sapient.model.dao.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryDao;

    public Category createCategory(String colourHex, String name, String description, int userId){
        Category category = new Category();
        category.setColourHex(colourHex);
        category.setName(name);
        category.setDescription(description);
        category.setUserId(userId);
        categoryDao.save(category);
        return category;
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        if (!categoryExists(id)) {
            throw new CategoryNotFoundException();
        }
        Category category = getCategory(id);
        categoryDao.delete(category);
    }

    public Category getCategory(Integer id) throws CategoryNotFoundException {
        Category category = categoryDao.findById(id).orElse(null);
        if(category==null){
            throw new CategoryNotFoundException();
        }
        return category;
    }

    public boolean categoryExists(Integer id) {
        try {
            getCategory(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
