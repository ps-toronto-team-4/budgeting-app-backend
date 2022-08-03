package com.sapient.controller.record;

import com.sapient.model.beans.Category;

public record BudgetCategoryDetails(Category category, Double amountBudgeted, Double amountActual) {
}
