package com.sapient.controller.record;

import com.sapient.model.beans.Category;
import com.sapient.model.beans.Merchant;
import com.sapient.model.beans.MonthType;

import java.util.List;

public record MonthBreakdown(MonthType month, Integer year, Double totalSpent, MonthBreakdownCategory topCategory, MonthBreakdownMerchant topMerchant, List<MonthBreakdownCategory> byCategory, List<MonthBreakdownMerchant> byMerchant) {
}
