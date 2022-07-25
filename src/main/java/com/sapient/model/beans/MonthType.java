package com.sapient.model.beans;

public enum MonthType {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    public MonthYear previousMonth(Integer year){
        if(this.ordinal() == 0){
            return new MonthYear(this.values()[11], year-1);
        }
        return new MonthYear(this.values()[this.ordinal()-1], year);
    }

    public MonthYear nextMonth(Integer year){
        if(this.ordinal() == 11){
            return new MonthYear(this.values()[0], year+1);
        }
        return new MonthYear(this.values()[this.ordinal()+1], year);
    }
}
