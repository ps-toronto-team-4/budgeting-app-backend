package com.sapient.model.beans;

public class MonthYear implements Comparable{
    private MonthType month;
    private Integer year;

    public MonthYear(MonthType month, Integer year) {
        this.month = month;
        this.year = year;
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

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof MonthYear)){
            throw new IllegalArgumentException("You can only compare against a MonthYear");
        }
        MonthYear monthYear = (MonthYear) o;
        if(this.getYear().compareTo(monthYear.getYear()) != 0) {
            return this.getYear().compareTo(monthYear.getYear());
        }
        return this.getMonth().compareTo(monthYear.getMonth());
    }
}
