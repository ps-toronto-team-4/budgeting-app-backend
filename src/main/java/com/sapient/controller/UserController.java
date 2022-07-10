package com.sapient.controller;

import com.sapient.model.beans.User;
import com.sapient.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @QueryMapping
    public User user(String username, String password) {
        return null; // TODO
    }

    // Object names map to graphql types of the same name. Eg: "CreateUserFailed".
    record CreateUserSuccess(User user) {}
    record CreateUserFailed(String exceptionName, String errorMessage) {}

    // Properties of a returned object from a @SchemaMapping method map to graphql fields of the same name.
    // Eg: "exceptionName".
    @MutationMapping
    public Record createUser(@Argument String username, @Argument String password, @Argument String email) {
        try {
            return new CreateUserSuccess(userService.createUser(username, password, email));
        } catch (Exception e) {
            return new CreateUserFailed(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    record DeleteUserSuccess(User user) {}
    record DeleteUserFailed(String exceptionName, String errorMessage) {}

    @MutationMapping
    public Record deleteUser(@Argument String username, @Argument String password) {
        try {
            return new DeleteUserSuccess(userService.deleteUser(username, password));
        } catch (Exception e) {
            return new DeleteUserFailed(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
