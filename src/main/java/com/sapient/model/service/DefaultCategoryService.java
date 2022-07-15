package com.sapient.model.service;

import com.sapient.model.beans.DefaultCategory;
import com.sapient.model.beans.User;
import com.sapient.model.dao.DefaultCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultCategoryService {
    @Autowired
    private DefaultCategoryRepository defaultCategoryDao;

    @Autowired
    private CategoryService categoryService;

    public List<DefaultCategory> getDefaultCategories(){
        Iterable<DefaultCategory> found = defaultCategoryDao.findAll();
        List<DefaultCategory> defaultCategories = new ArrayList<>();
        found.forEach(defaultCategories::add);
        return defaultCategories;
    }

    public void saveDefaultCategories(User user){
        for(DefaultCategory defaultCategory: getDefaultCategories()){
            categoryService.createCategoryFromDefaultCategory(user, defaultCategory);
        }
    }
}