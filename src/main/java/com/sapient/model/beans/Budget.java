package com.sapient.model.beans;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private MonthType month;
    private Integer year;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "budget", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BudgetCategory> budgetCategories;
    public Integer getId() {
        return id;
    }

    public MonthType getMonth() {
        return month;
    }

    public void setMonth(MonthType month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<BudgetCategory> getBudgetCategories() {
        return budgetCategories;
    }

    public void setBudgetCategories(List<BudgetCategory> budgetCategories) {
        this.budgetCategories = budgetCategories;
    }
}
