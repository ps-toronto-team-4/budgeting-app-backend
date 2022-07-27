package com.sapient.model.service;

import com.sapient.controller.record.*;
import com.sapient.exception.NotAuthorizedException;
import com.sapient.model.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportsService {
    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetService budgetService;

    public MonthBreakdown getMonthBreakdown(String passwordHash, MonthType month, Integer year) throws NotAuthorizedException {
        List<Expense> monthlyExpenses = expenseService.getExpensesInMonth(passwordHash, month, year);
        List<MonthBreakdownCategory> byCategory = new ArrayList<>();
        List<MonthBreakdownMerchant> byMerchant = new ArrayList<>();
        User user = userService.getUserByPasswordHash(passwordHash);
        Double totalSpent = 0.0;

        Category topCategory = null;
        Merchant topMerchant = null;

        HashMap<Category, Double> categoryTotals = new HashMap<>();
        for(Category category: user.getCategories()){
            categoryTotals.put(category, 0.0);
        }
        categoryTotals.put(null, 0.0); // uncategorized

        HashMap<Merchant, Double> merchantTotals = new HashMap<>();
        for(Merchant merchant: user.getMerchants()){
            merchantTotals.put(merchant, 0.0);
        }
        merchantTotals.put(null, 0.0); // uncategorized

        for(Expense expense:monthlyExpenses){
            Double amount = expense.getAmount();
            Category category = expense.getCategory();
            Merchant merchant = expense.getMerchant();
            totalSpent += amount;
            categoryTotals.put(category, categoryTotals.get(category)+amount);
            merchantTotals.put(merchant, merchantTotals.get(merchant)+amount);
        }
        for(Category category: categoryTotals.keySet()){
            Double amount  = categoryTotals.get(category);
            byCategory.add(new MonthBreakdownCategory(category, categoryTotals.get(category)));
            if(category == null){
                continue;
            }
            if(topCategory == null){
                topCategory = category;
            }
            if(amount > categoryTotals.get(topCategory)){
                topCategory = category;
            }
        }

        for(Merchant merchant: merchantTotals.keySet()){
            Double amount  = merchantTotals.get(merchant);
            byMerchant.add(new MonthBreakdownMerchant(merchant, merchantTotals.get(merchant)));
            if(merchant == null){
                continue;
            }
            if(topMerchant == null){
                topMerchant = merchant;
            }
            if(amount > merchantTotals.get(topMerchant)){
                topMerchant = merchant;
            }
        }

        return new MonthBreakdown(month, year, totalSpent, new MonthBreakdownCategory(topCategory, categoryTotals.get(topCategory)), new MonthBreakdownMerchant(topMerchant, merchantTotals.get(topMerchant)), byCategory, byMerchant);
    }

    public MonthsTotals getMonthsTotals(String passwordHash) throws NotAuthorizedException {
        User user = userService.getUserByPasswordHash(passwordHash);
        Double total = 0.0;
        List<MonthsTotalsItem> byMonth = new ArrayList<>();

        HashMap<MonthYear, Double> totals = new HashMap<>();
        List<MonthYear> months = new ArrayList<>();

        MonthYear first = getFirstMonth(user);
        MonthYear last = getLastMonth(user);
        MonthYear curr = first;

        while(curr.compareTo(last) < 1){
            totals.put(curr, 0.0);
            months.add(curr);
            curr = curr.nextMonth();
        }
        for(Expense expense: user.getExpenses()){
            MonthYear my = new MonthYear(MonthType.values()[expense.getDate().getMonth()], expense.getDate().getYear()+1900);
            totals.put(my, totals.get(my) + expense.getAmount());
            total += expense.getAmount();
        }

        for(MonthYear my: months){
            Double amountBudgeted = 0.0;
            try{
                amountBudgeted = budgetService.getBudgetByDate(passwordHash, my.getMonth(), my.getYear()).getAmountBudgeted();
            }catch (Exception e){}
            byMonth.add(new MonthsTotalsItem(my.getMonth(), my.getYear(), totals.get(my), amountBudgeted));
        }
        return new MonthsTotals(total/months.size(), byMonth);
    }

    private MonthYear getLastMonth(User user){
        List<Expense> expenses = user.getExpenses();
        Date highestDate = expenses.get(0).getDate();
        for(Expense expense: expenses){
            if(highestDate.compareTo(expense.getDate()) < 0){
                highestDate = expense.getDate();
            }
        }
        return new MonthYear(MonthType.values()[highestDate.getMonth()], highestDate.getYear()+1900);
    }

    private MonthYear getFirstMonth(User user){
        List<Expense> expenses = user.getExpenses();
        Date lowestDate = expenses.get(0).getDate();
        for(Expense expense: expenses){
            if(lowestDate.compareTo(expense.getDate()) > 0){
                lowestDate = expense.getDate();
            }
        }
        return new MonthYear(MonthType.values()[lowestDate.getMonth()], lowestDate.getYear()+1900);
    }
}
