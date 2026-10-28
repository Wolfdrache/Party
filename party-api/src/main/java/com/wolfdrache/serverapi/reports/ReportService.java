package com.wolfdrache.serverapi.reports;

public interface ReportService {
    void createCategory(String name);
    boolean addReason(String category, String reason);
    String getBaseCategoryName();
}
