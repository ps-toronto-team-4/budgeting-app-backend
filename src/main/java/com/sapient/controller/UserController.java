package com.sapient.controller;

import com.sapient.controller.record.DeleteSuccess;
import com.sapient.model.beans.User;
import com.sapient.controller.record.FailurePayload;
import com.sapient.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @QueryMapping
    public String greeting() {
        return "Hello world from graphql endpoint!";
    }

    @QueryMapping
    public User user(@Argument String username) {
        try {
            return userService.getByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }

    // Object names map to graphql types of the same name. Eg: "CreateUserFailed".
    record CreateUserSuccess(User user) {}

    // Properties of a returned object from a @SchemaMapping method map to graphql fields of the same name.
    // Eg: "exceptionName".
    @MutationMapping
    public Record signUp(
            @Argument String username,
            @Argument String password,
            @Argument String email,
            @Argument String firstName,
            @Argument String lastName,
            @Argument String phoneNumber) {
        try {
            return new CreateUserSuccess(
                userService.createUser(
                    username,
                    password,
                    email,
                    firstName,
                    lastName,
                    Optional.ofNullable(phoneNumber)
                )
            );
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @MutationMapping
    public Record deleteUser(@Argument String passwordHash) {
        try {
            userService.deleteUser(passwordHash);
            return new DeleteSuccess("Successfully deleted user!");
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
