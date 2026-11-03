package com.wolfdrache.serverapi.reports;

import org.bukkit.entity.Player;

public interface ReportService {
    void createCategory(String name);
    boolean addReason(String category, String reason);
    String getBaseCategoryName();
    void openReports(Player player);
}
