package com.sapient.model.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double amount;
//    private int categoryID;
//    private int merchantID;
    private Date date;
//    private recuranceID;
    private String title;
    private String description;
//    private int userID;
    
    public Expense() {
    	super();
    }
    
    public Expense(String title, String description, Double amount) {
    	super();
    	this.title = title;
    	this.description = description;
    	Date date = new Date(System.currentTimeMillis());
    	this.date = date;
    	this.amount = amount;
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    		

}
