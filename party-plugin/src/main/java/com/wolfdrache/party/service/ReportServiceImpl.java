package com.wolfdrache.party.service;

import org.bukkit.entity.Player;

import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.party.reports.ReportManager;
import com.wolfdrache.party.reports.ui.ReportsGui;
import com.wolfdrache.serverapi.reports.ReportService;

public class ReportServiceImpl implements ReportService {
    private final ReportManager reportManager;

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

    @Override 
    public void openReports(Player player) {
        if (!player.hasPermission("reports.manage")) {
            MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "§cDu kannst keine Reports verwalten.");
            return;
        }
        ReportsGui.open(player);
    }
}
