package com.sapient.model.beans;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "merchants")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @ManyToOne
    private Category defaultCategory;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Expense> expenses;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Category getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(Category defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
