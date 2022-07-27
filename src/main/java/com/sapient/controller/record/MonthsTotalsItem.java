package com.sapient.controller.record;

import com.sapient.model.beans.Budget;
import com.sapient.model.beans.MonthType;

public record MonthsTotalsItem(MonthType month, Integer year, Double amountSpent, Double amountBudgeted) {
}
