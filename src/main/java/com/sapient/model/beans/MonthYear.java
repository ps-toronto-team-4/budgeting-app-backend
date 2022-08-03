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

    public MonthYear nextMonth(){
        if(this.month.ordinal() == 11){
            return new MonthYear(this.month.values()[0], year+1);
        }
        return new MonthYear(this.month.values()[this.month.ordinal()+1], year);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonthYear monthYear = (MonthYear) o;

        if (month != monthYear.month) return false;
        return year != null ? year.equals(monthYear.year) : monthYear.year == null;
    }

    @Override
    public int hashCode() {
        int result = month != null ? month.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }
}
