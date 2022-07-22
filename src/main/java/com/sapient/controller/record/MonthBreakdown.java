package com.sapient.controller.record;

import com.sapient.model.beans.MonthType;

import java.util.List;

public record MonthBreakdown(MonthType month, Integer year, Double totalSpent, List<MonthBreakdownCategory> byCategory) {
}
