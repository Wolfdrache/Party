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

        List<Player> onlineOperators = new ArrayList<>(Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("reports.manage")).toList());
        for (Player operator : onlineOperators) {
            MessageHelper.sendMessage(operator, MessageHelper.Type.REPORT, "§8Neuer Report von §7" + reporter.getName() + "§8 gegen §c" + reported.getName() + "§8 wurde erstellt.");
            MessageHelper.sendReportMessage(operator, report);
        }
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
                MessageHelper.sendMessage((Player) reporter, MessageHelper.Type.REPORT, "§8Dein Report an §c" + reported.getName() + "§8 wurde bearbeitet.");
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

    public ReportModel getReportById(int reportId) {
        for (ReportModel report : reports) {
            if (report.getId() == reportId) {
                return report;
            }
        }
        return null;
    }

    public void closeReport(ReportModel report, Player player, String message) {
        report.operator = player;
        report.conclusion = message;
        report.handledTimestamp = Instant.now().atZone(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        OfflinePlayer reporter = report.reporter;
        if (reporter.isOnline()) {
            MessageHelper.sendMessage((Player) reporter, MessageHelper.Type.REPORT, "§8Dein Report an §c" + report.reportedPlayer.getName() + "§8 wurde bearbeitet.");
        }
        OfflinePlayer reported = report.reportedPlayer;
        PlayerReports reportsForPlayer = playerReports.get(reported);
        if (reportsForPlayer != null) {
            reportsForPlayer.receivedReportsOpen.remove(report);
            reportsForPlayer.receivedReportsClosed.add(report);
        }
        updateReport(report);
    }
}
