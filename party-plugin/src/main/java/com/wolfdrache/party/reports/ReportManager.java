package com.wolfdrache.party.reports;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.serverapi.reports.ReportModel;

public class ReportManager {
    private final String baseReportCategory = "general";

    private final FileManager fileManager;

    private final List<ReportModel> reports;
    private final Map<OfflinePlayer, PlayerReports> playerReports = new HashMap<>();
    private final Map<String, ReportCategory> reportCategories = new HashMap<>();

    public ReportManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.reports = fileManager.getReports();
        initializePlayerReports();
        reportCategories.put(baseReportCategory, new ReportCategory(baseReportCategory));
    }

    private void initializePlayerReports() {
        for (ReportModel report : reports) {
            OfflinePlayer reportedPlayer = report.reportedPlayer;
            OfflinePlayer reporter = report.reporter;
            playerReports.computeIfAbsent(reportedPlayer, k -> new PlayerReports());
            playerReports.computeIfAbsent(reporter, k -> new PlayerReports());
            if (report.operator == null) {
                playerReports.get(reportedPlayer).receivedReportsOpen.add(report);
            } else {
                playerReports.get(reportedPlayer).receivedReportsClosed.add(report);
            }
            playerReports.get(reporter).madeReports.add(report);
        }
    }

    public void saveAllReports() {
        fileManager.saveReports(reports);
    }

    public void deleteReport(ReportModel report) {
        if (report == null) {
            return;
        }
        reports.remove(report);
        fileManager.deleteReport(report);
        OfflinePlayer reportedPlayer = report.reportedPlayer;
        OfflinePlayer reporter = report.reporter;
        if (playerReports.containsKey(reportedPlayer)) {
            playerReports.get(reportedPlayer).receivedReportsOpen.remove(report);
            playerReports.get(reportedPlayer).receivedReportsClosed.remove(report);
        }
        if (playerReports.containsKey(reporter)) {
            playerReports.get(reporter).madeReports.remove(report);
        }
    }

    public void updateReport(ReportModel report) {
        if (report == null) {
            return;
        }
        fileManager.updateReport(report);
    }

    public void createReport(OfflinePlayer reported, OfflinePlayer reporter, String category, String reason, String message) {
        int id = fileManager.getNextReportId();
        String timestamp = Instant.now().atZone(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        ReportModel report = new ReportModel(id, reported, reporter, category, reason, message, timestamp);
        reports.add(report);
        updateReport(report);

        playerReports.computeIfAbsent(reported, k -> new PlayerReports());
        playerReports.computeIfAbsent(reporter, k -> new PlayerReports());
        playerReports.get(reported).receivedReportsOpen.add(report);
        playerReports.get(reporter).madeReports.add(report);

        Bukkit.broadcast(MessageHelper.createComponent("§cNeuer Report:"), "reports.manage");
        Bukkit.broadcast(MessageHelper.createComponent("§8" + reported.getName() + ": " + category + " - " + reason + " - " + message), "reports.manage");
    }

    public void closeReports(OfflinePlayer reported, Player operator, String conclusion) {
        if (!playerReports.containsKey(reported)) {
            return;
        }
        PlayerReports reportsForPlayer = playerReports.get(reported);
        String timestamp = Instant.now().atZone(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        for (ReportModel report : reportsForPlayer.receivedReportsOpen) {
            report.operator = operator;
            report.conclusion = conclusion;
            report.handledTimestamp = timestamp;
            reportsForPlayer.receivedReportsClosed.add(report);
            updateReport(report);
            OfflinePlayer reporter = report.reporter;
            if (reporter.isOnline()) {
                MessageHelper.sendMessage((Player) reporter, MessageHelper.Type.REPORT, "Dein Report an " + reported.getName() + " wurde bearbeitet.");
            }
        }
        reportsForPlayer.receivedReportsOpen.clear();
    }

    public String getBaseCategoryName() {
        return baseReportCategory;
    }

    public boolean addReason(String category, String reason) {
        if (!reportCategories.containsKey(category.toLowerCase())) return false;
        reportCategories.get(category.toLowerCase()).reasons.add(reason.toLowerCase());
        return true;
    }

    public void createCategory(String name) {
        if (reportCategories.containsKey(name.toLowerCase())) return;
        reportCategories.put(name.toLowerCase(), new ReportCategory(name.toLowerCase()));
    }

    public ReportCategory getReportCategory(String category) {
        if (!reportCategories.containsKey(category.toLowerCase())) return null;
        return reportCategories.get(category.toLowerCase());
    }

    public List<String> getReportCategoryNames() {
        return new ArrayList<>(reportCategories.keySet());
    }

    public Map<OfflinePlayer, PlayerReports> getAllReports() {
        return playerReports;
    }
}
