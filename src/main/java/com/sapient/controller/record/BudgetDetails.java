package com.sapient.controller.record;

import com.sapient.model.beans.Budget;

import java.util.List;

public record BudgetDetails(Budget budget, Double totalBudgeted, Double totalActual, Double totalUnplanned, List<BudgetCategoryDetails> byCategory) {
}
