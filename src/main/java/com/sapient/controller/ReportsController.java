package com.sapient.controller;

import com.sapient.controller.record.FailurePayload;
import com.sapient.model.beans.MonthType;
import com.sapient.model.service.ReportsService;
import com.sapient.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @QueryMapping
    public Record monthBreakdown(@Argument String passwordHash, @Argument MonthType month, @Argument Integer year) {
        try {
            return reportsService.getMonthBreakdown(passwordHash, month, year);
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    @QueryMapping
    public Record monthsTotals(@Argument String passwordHash) {
        try {
            return reportsService.getMonthsTotals(passwordHash);
        } catch (Exception e) {
            return new FailurePayload(e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
