package com.sapient.model.service;

import com.sapient.model.beans.UsersTest;
import com.sapient.model.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userDao;

    public UsersTest getName() {
        return userDao.findAll().get(0);
    }

    public UsersTest makeUser(String name) {
        UsersTest user = new UsersTest();
        user.setName(name);
        userDao.save(user);
        return user;
    }
}
