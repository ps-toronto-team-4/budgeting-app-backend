package com.sapient.model.service;

import com.sapient.exception.EmailAlreadyTakenException;
import com.sapient.exception.UserNotFoundException;
import com.sapient.exception.UsernameAlreadyTakenException;
import com.sapient.exception.UsernameTooLongException;
import com.sapient.model.beans.User;
import com.sapient.model.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;

@Service
public class UserService {
    @Autowired
    private UserRepository userDao;

    public User createUser(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String phoneNumber)
            throws UsernameAlreadyTakenException, EmailAlreadyTakenException, UsernameTooLongException {
        if (usernameTaken(username)) {
            throw new UsernameAlreadyTakenException("Username '" + username + "' is already taken.");
        }
        if (emailTaken(email)) {
            throw new EmailAlreadyTakenException("Email '" + email + "' is already taken.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashPassword(username, password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        userDao.save(user);
        return user;
    }

    // Hashes password and salts it with the username.
    // Returned hash is in Modular Crypt Format.
    private String hashPassword(String username, String password) throws UsernameTooLongException {
        String salt = this.genSalt(username);
        byte[] hash = BCrypt.withDefaults().hash(6, salt.getBytes(StandardCharsets.UTF_8),
                password.getBytes(StandardCharsets.UTF_8));
        return new String(hash, StandardCharsets.UTF_8); // correct way to convert byte[] to String.
    }

    public boolean verifyPasswordHash(String username, String password, String passwordHash)
            throws UsernameTooLongException {
        String salt = this.genSalt(username);
        return passwordHash.equals(hashPassword(username, password));
    }

    // Username must be <= 16 characters.
    private String genSalt(String username) throws UsernameTooLongException {
        if (username.length() > 16) {
            throw new UsernameTooLongException("Username must be <= 16 characters.");
        }
        return username + " ".repeat(16-username.length());
    }

    public void deleteUser(String passwordHash) throws UserNotFoundException {
        for(User user: userDao.findAll()){
            if(user.getPasswordHash().equals(passwordHash)){
                userDao.delete(user);
                return;
            }
        }
        throw new UserNotFoundException();
    }

    public User getUser(Integer id) throws UserNotFoundException {
        User user = userDao.findById(id).orElse(null);
        if(user==null){
            throw new UserNotFoundException();
        }
        return user;
    }

    public Boolean userExists(Integer id){
        try{
            userDao.findById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Boolean usernameTaken(String username) {
        for(User user: userDao.findAll()){
            if(username.equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    public User getUserByPasswordHash(String passwordHash) throws UserNotFoundException{
        for(User user:userDao.findAll()){
            if(user.getPasswordHash().equals(passwordHash)){
                return user;
            }
        }
        throw new UserNotFoundException();
    }

    public Boolean emailTaken(String email) {
        for (User user : userDao.findAll()) {
            if (email.equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public User getByUsername(String username) throws UserNotFoundException {
        for (User user : userDao.findAll()) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }
}
