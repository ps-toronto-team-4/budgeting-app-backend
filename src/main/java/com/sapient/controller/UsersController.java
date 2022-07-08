package com.sapient.controller;

import com.sapient.model.beans.UsersTest;
import com.sapient.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UsersController {
    @Autowired
    UserService userService;

    @QueryMapping
    public String getString() {
        return "Hello world from graphql api.";
    }

    @QueryMapping
    public UsersTest getUser() {
        return userService.getName();
    }

    @SchemaMapping(typeName = "UsersTest")
    public String name(UsersTest parent) {
        return parent.getName();
    }

    @MutationMapping
    public UsersTest makeUser(@Argument String name) {
        return userService.makeUser(name);
    }
}
