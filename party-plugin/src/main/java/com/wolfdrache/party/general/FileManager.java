package com.wolfdrache.party.general;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolfdrache.friends.FriendData;
import com.wolfdrache.party.Party;
import com.wolfdrache.party.friends.PlayerFriends;
import com.wolfdrache.party.general.ConfigurationManager.PlayerConfiguration;
import com.wolfdrache.serverapi.achievements.AchievementCategory;
import com.wolfdrache.serverapi.achievements.AchievementProgress;
import com.wolfdrache.serverapi.achievements.Achievement;
import com.wolfdrache.serverapi.achievements.Achievement.IAchievementType;
import com.wolfdrache.serverapi.configuration.ConfigurationService.FriendRequestMode;
import com.wolfdrache.serverapi.configuration.ConfigurationService.MsgMode;
import com.wolfdrache.serverapi.configuration.ConfigurationService.PartyInviteMode;
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
    private final File configurationFile;
    private final File achievementsFolder;

    public FileManager(Party plugin) {
        this.plugin = plugin;
        this.friendsFile = new File(plugin.getDataFolder(), "friends.yml");
        this.reportsFile = new File(plugin.getDataFolder(), "reports.yml");
        this.configurationFile = new File(plugin.getDataFolder(), "configuration.yml");
        this.achievementsFolder = new File(plugin.getDataFolder(), "achievements");
        if (!this.achievementsFolder.exists()) {
            this.achievementsFolder.mkdirs();
        }
    }

    public PlayerFriends getPlayerFriends(OfflinePlayer player) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        UUID uuid = player.getUniqueId();

        boolean unlimitedPermission = config.getBoolean(uuid.toString() + ".unlimitedPermission", false);
        List<Map<?, ?>> friendsData = config.getMapList(uuid.toString() + ".friends");
        List<Map<?, ?>> requestsData = config.getMapList(uuid.toString() + ".requests");
        List<Map<?, ?>> sentData = config.getMapList(uuid.toString() + ".sent");
        List<String> newFriendRequestsStrings = config.getStringList(uuid.toString() + ".newFriendRequests");

        Set<FriendData> friends = friendsData.stream()
            .map(map -> new FriendData(
                plugin.getServer().getOfflinePlayer(UUID.fromString((String) map.get("uuid"))),
                (String) map.get("timestamp")
            ))
            .collect(Collectors.toSet());
        Set<FriendData> requests = requestsData.stream()
            .map(map -> new FriendData(
                plugin.getServer().getOfflinePlayer(UUID.fromString((String) map.get("uuid"))),
                (String) map.get("timestamp")
            ))
            .collect(Collectors.toSet());
        Set<FriendData> sent = sentData.stream()
            .map(map -> new FriendData(
                plugin.getServer().getOfflinePlayer(UUID.fromString((String) map.get("uuid"))),
                (String) map.get("timestamp")
            ))
            .collect(Collectors.toSet());
        Set<OfflinePlayer> newFriendRequests = newFriendRequestsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());

        return new PlayerFriends(unlimitedPermission, friends, requests, sent, newFriendRequests);
    }

    public void savePlayerFriends(OfflinePlayer player, PlayerFriends playerFriends) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        UUID uuid = player.getUniqueId();

        List<Map<String, String>> friendsStrings = playerFriends.friends.stream().map(fd -> Map.of(
            "uuid", fd.friend.getUniqueId().toString(),
            "timestamp", fd.timeStamp
        )).collect(Collectors.toList());
        List<Map<String, String>> requestsStrings = playerFriends.incomingRequests.stream().map(fd -> Map.of(
            "uuid", fd.friend.getUniqueId().toString(),
            "timestamp", fd.timeStamp
        )).collect(Collectors.toList());
        List<Map<String, String>> sentStrings = playerFriends.sentRequests.stream().map(fd -> Map.of(
            "uuid", fd.friend.getUniqueId().toString(),
            "timestamp", fd.timeStamp
        )).collect(Collectors.toList());
        
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

    public PlayerConfiguration getPlayerConfiguration(OfflinePlayer player) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);

        String uuid = player.getUniqueId().toString();

        String msgModeStrg = config.getString(uuid + ".msgMode", "ALL");
        String partyInviteModeStrg = config.getString(uuid + ".partyInviteMode", "ALL");
        String friendRequestModeStrg = config.getString(uuid + ".friendRequestMode", "ALL");

        MsgMode msgMode = MsgMode.valueOf(msgModeStrg);
        PartyInviteMode partyInviteMode = PartyInviteMode.valueOf(partyInviteModeStrg);
        FriendRequestMode friendRequestMode = FriendRequestMode.valueOf(friendRequestModeStrg);

        return new PlayerConfiguration(msgMode, partyInviteMode, friendRequestMode);
    }

    public void savePlayerConfiguration(OfflinePlayer player, PlayerConfiguration configuration) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configurationFile);

        String uuid = player.getUniqueId().toString();

        config.set(uuid + ".msgMode", configuration.msgMode.name());
        config.set(uuid + ".partyInviteMode", configuration.partyInviteMode.name());
        config.set(uuid + ".friendRequestMode", configuration.friendRequestMode.name());

        try {
            config.save(configurationFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<OfflinePlayer, Map<IAchievementType, AchievementProgress>> getAchievementProgress(AchievementCategory category, Achievement achievement) {
        File achievementFile = new File(achievementsFolder, category.getName() + ".yml");
        if (!achievementFile.exists()) {
            try {
                achievementFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new HashMap<>();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(achievementFile);

        ConfigurationSection achievementSection = config.getConfigurationSection(String.valueOf(achievement.getId()));
        if (achievementSection == null) {
            return new HashMap<>();
        }

        Map<OfflinePlayer, Map<IAchievementType, AchievementProgress>> progressMap = new HashMap<>();
        for (String key : achievementSection.getKeys(false)) {
            try {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(key));
                ConfigurationSection playerSection = achievementSection.getConfigurationSection(key);
                for (String typeKey : playerSection.getKeys(false)) {
                    IAchievementType type = achievement.getAchievementType(typeKey);
                    if (type == null) continue;
                    int progress = playerSection.getInt(typeKey + ".progress", 0);
                    boolean completed = playerSection.getBoolean(typeKey + ".completed", false);
                    progressMap.computeIfAbsent(player, k -> new HashMap<>()).put(type, new AchievementProgress(player, progress, completed));
                }
            } catch (IllegalArgumentException ignored) {
                // skip malformed entries
            }
        }
        return progressMap;
    }

    public void saveAchievementProgress(AchievementCategory category, Achievement achievement) {
        File achievementFile = new File(achievementsFolder, category.getName() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(achievementFile);

        String achievementId = String.valueOf(achievement.getId());
        ConfigurationSection achievementSection = config.getConfigurationSection(achievementId);
        if (achievementSection == null) {
            achievementSection = config.createSection(achievementId);
        }

        for (Map.Entry<OfflinePlayer, Map<IAchievementType, AchievementProgress>> entry : achievement.getProgress().entrySet()) {
            OfflinePlayer player = entry.getKey();
            Map<IAchievementType, AchievementProgress> playerProgress = entry.getValue();
            for (Map.Entry<IAchievementType, AchievementProgress> typeEntry : playerProgress.entrySet()) {
                IAchievementType type = typeEntry.getKey();
                AchievementProgress progress = typeEntry.getValue();
                achievementSection.set(player.getUniqueId().toString() + "." + type.getName() + ".progress", progress.getProgress());
                achievementSection.set(player.getUniqueId().toString() + "." + type.getName() + ".completed", progress.isCompleted());
            }
        }

        try {
            config.save(achievementFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
