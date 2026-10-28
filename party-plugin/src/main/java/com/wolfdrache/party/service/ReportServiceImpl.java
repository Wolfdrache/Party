package com.wolfdrache.party.service;

import com.wolfdrache.party.reports.ReportManager;
import com.wolfdrache.serverapi.reports.ReportService;

public class ReportServiceImpl implements ReportService {
    private ReportManager reportManager;

    public ReportServiceImpl(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public void createCategory(String name) {
        reportManager.createCategory(name);
    }

    @Override
    public boolean addReason(String category, String reason) {
        return reportManager.addReason(category, reason);
    }

    @Override
    public String getBaseCategoryName() {
        return reportManager.getBaseCategoryName();
    }
}
