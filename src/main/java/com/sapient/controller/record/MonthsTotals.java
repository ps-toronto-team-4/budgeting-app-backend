package com.sapient.controller.record;

import java.util.List;

public record MonthsTotals(Double averageSpent, List<MonthsTotalsItem> byMonth) {
}
