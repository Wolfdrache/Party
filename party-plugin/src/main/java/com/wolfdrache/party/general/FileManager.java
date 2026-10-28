package com.wolfdrache.party.general;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolfdrache.party.Party;
import com.wolfdrache.party.friends.PlayerFriends;
import com.wolfdrache.serverapi.reports.ReportModel;

public class FileManager {
    public static enum FriendsConfig {
        maxFriendsPerPage(5),
        maxFriends(25);

        public final int defaultValue;
        FriendsConfig(int defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    private final Party plugin;

    private final File friendsFile;
    private final File reportsFile;

    public FileManager(Party plugin) {
        this.plugin = plugin;
        this.friendsFile = new File(plugin.getDataFolder(), "friends.yml");
        this.reportsFile = new File(plugin.getDataFolder(), "reports.yml");
    }

    public PlayerFriends getPlayerFriends(OfflinePlayer player) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        UUID uuid = player.getUniqueId();

        boolean unlimitedPermission = config.getBoolean(uuid.toString() + ".unlimitedPermission", false);
        List<String> friendsStrings = config.getStringList(uuid.toString() + ".friends");
        List<String> requestsStrings = config.getStringList(uuid.toString() + ".requests");
        List<String> sentStrings = config.getStringList(uuid.toString() + ".sent");
        List<String> newFriendRequestsStrings = config.getStringList(uuid.toString() + ".newFriendRequests");

        Set<OfflinePlayer> friends = friendsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());
        Set<OfflinePlayer> requests = requestsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());
        Set<OfflinePlayer> sent = sentStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());
        Set<OfflinePlayer> newFriendRequests = newFriendRequestsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());

        return new PlayerFriends(unlimitedPermission, friends, requests, sent, newFriendRequests);
    }

    public void savePlayerFriends(OfflinePlayer player, PlayerFriends playerFriends) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        UUID uuid = player.getUniqueId();

        List<String> friendsStrings = playerFriends.friends.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());
        List<String> requestsStrings = playerFriends.incomingRequests.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());
        List<String> sentStrings = playerFriends.sentRequests.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());
        List<String> newFriendRequestsStrings = playerFriends.newFriendRequests.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());

        config.set(uuid.toString() + ".name", player.getName());
        config.set(uuid.toString() + ".unlimitedPermission", playerFriends.unlimitedPermission);
        config.set(uuid.toString() + ".friends", friendsStrings);
        config.set(uuid.toString() + ".requests", requestsStrings);
        config.set(uuid.toString() + ".sent", sentStrings);
        config.set(uuid.toString() + ".newFriendRequests", newFriendRequestsStrings);

        try {
            config.save(friendsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getFriendsConfig(FriendsConfig friendsConfig) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        return config.getInt("config." + friendsConfig.name(), friendsConfig.defaultValue);
    }

    public List<ReportModel> getReports() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(reportsFile);
        List<ReportModel> reports = new ArrayList<>();
        ConfigurationSection reportSection = config.getConfigurationSection("reports");
        if (reportSection == null) {
            return reports;
        }

        for (String idString : reportSection.getKeys(false)) {
            ConfigurationSection reportData = reportSection.getConfigurationSection(idString);
            if (reportData == null) {
                continue;
            }

            String reported = reportData.getString("reported");
            String reporter = reportData.getString("reporter");
            String category = reportData.getString("category");
            String reason = reportData.getString("reason");
            String message = reportData.getString("message");
            String time = reportData.getString("time");
            String operator = reportData.getString("operator");
            String handledTime = reportData.getString("handledTime");
            String conclusion = reportData.getString("conclusion");

            if (reported == null || reporter == null || category == null || reason == null || message == null || time == null) {
                continue;
            }

            try {
                int id = Integer.parseInt(idString);
                OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(UUID.fromString(reported));
                OfflinePlayer reporterPlayer = Bukkit.getOfflinePlayer(UUID.fromString(reporter));

                if (operator == null || handledTime == null || conclusion == null) {
                    reports.add(new ReportModel(id, reportedPlayer, reporterPlayer, category, reason, message, time));
                } else {
                    OfflinePlayer operatorPlayer = Bukkit.getOfflinePlayer(UUID.fromString(operator));
                    reports.add(new ReportModel(id, reportedPlayer, reporterPlayer, category, reason, message, time, operatorPlayer, handledTime, conclusion));
                }
            } catch (IllegalArgumentException ignored) {
                // Ignore malformed IDs in the YAML layout.
            }
        }
        return reports;
    }

    public void saveReports(List<ReportModel> reports) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(reportsFile);
        config.set("reports", null);

        for (ReportModel report : reports) {
            ConfigurationSection reportSection = config.createSection("reports." + report.getId());
            reportSection.set("reported", report.reportedPlayer.getUniqueId().toString());
            reportSection.set("reporter", report.reporter.getUniqueId().toString());
            reportSection.set("category", report.category);
            reportSection.set("reason", report.reason);
            reportSection.set("message", report.message);
            reportSection.set("time", report.timestamp);
            reportSection.set("operator", report.operator != null ? report.operator.getUniqueId().toString() : null);
            reportSection.set("handledTime", report.handledTimestamp);
            reportSection.set("conclusion", report.conclusion);
        }

        try {
            config.save(reportsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteReport(ReportModel report) {
        if (report == null) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(reportsFile);
        config.set("reports." + report.getId(), null);

        try {
            config.save(reportsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateReport(ReportModel report) {
        if (report == null) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(reportsFile);
        ConfigurationSection reportSection = config.getConfigurationSection("reports." + report.getId());
        if (reportSection == null) {
            reportSection = config.createSection("reports." + report.getId());
        }

        reportSection.set("reported", report.reportedPlayer.getUniqueId().toString());
        reportSection.set("reporter", report.reporter.getUniqueId().toString());
        reportSection.set("category", report.category);
        reportSection.set("reason", report.reason);
        reportSection.set("message", report.message);
        reportSection.set("time", report.timestamp);
        reportSection.set("operator", report.operator != null ? report.operator.getUniqueId().toString() : null);
        reportSection.set("handledTime", report.handledTimestamp);
        reportSection.set("conclusion", report.conclusion);

        try {
            config.save(reportsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getNextReportId() {
    YamlConfiguration config = YamlConfiguration.loadConfiguration(reportsFile);
    ConfigurationSection reportsSection = config.getConfigurationSection("reports");
    if (reportsSection == null) {
        return 1;
    }

    int nextId = 1;
    for (String key : reportsSection.getKeys(false)) {
        try {
            int id = Integer.parseInt(key);
            if (id >= nextId) {
                nextId = id + 1;
            }
        } catch (IllegalArgumentException ignored) {
            // skip malformed entries
        }
    }
    return nextId;
}
}
