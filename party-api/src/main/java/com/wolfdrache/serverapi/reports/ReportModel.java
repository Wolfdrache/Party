package com.wolfdrache.serverapi.reports;

import org.bukkit.OfflinePlayer;

public class ReportModel {
    private final int id;

    public final OfflinePlayer reportedPlayer;
    public final OfflinePlayer reporter;
    public OfflinePlayer operator;
    public final String category;
    public final String reason;
    public final String message;
    public final String timestamp;
    public String handledTimestamp;
    public String conclusion;

    public ReportModel(int id, OfflinePlayer reportedPlayer, OfflinePlayer reporter, String category, String reason, String message, String timestamp) {
        this.id = id;
        this.reportedPlayer = reportedPlayer;
        this.reporter = reporter;
        this.category = category;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ReportModel(int id, OfflinePlayer reportedPlayer, OfflinePlayer reporter, String category, String reason, String message, String timestamp, OfflinePlayer operator, String handledTimestamp, String conclusion) {
        this.id = id;
        this.reportedPlayer = reportedPlayer;
        this.reporter = reporter;
        this.operator = operator;
        this.category = category;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
        this.handledTimestamp = handledTimestamp;
        this.conclusion = conclusion;
    }

    public int getId() {
        return id;
    }
}
