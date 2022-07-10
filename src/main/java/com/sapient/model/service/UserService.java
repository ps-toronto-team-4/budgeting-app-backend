package com.sapient.model.service;

import com.sapient.exception.UserNotFoundException;
import com.sapient.exception.UsernameAlreadyTakenException;
import com.sapient.model.beans.User;
import com.sapient.model.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userDao;

    public User createUser(String username, String password, String email) throws UsernameAlreadyTakenException {
        if (userExists(username)) {
            throw new UsernameAlreadyTakenException();
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password);
        user.setEmail(email);
        userDao.save(user);
        return user;
    }

    public User deleteUser(String username, String password) throws UserNotFoundException {
        if (!userExists(username)) {
            throw new UserNotFoundException();
        }
        User user = getUser(username);
        userDao.delete(user);
        return user;
    }

    public User getUser(String username) throws UserNotFoundException {
        for (User user : userDao.findAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }

    public boolean userExists(String username) {
        try {
            getUser(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
