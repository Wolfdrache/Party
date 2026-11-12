package com.wolfdrache.party.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.CoinManager;
import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.serverapi.achievements.AchievementCategory;
import com.wolfdrache.serverapi.achievements.AchievementProgress;
import com.wolfdrache.serverapi.achievements.IAchievement;

public class AchievementManager {
    private final FileManager fileManager;
    private final CoinManager coinManager;
    
    private final Map<AchievementCategory, List<IAchievement>> achievements = new HashMap<>();

    public AchievementManager(FileManager fileManager, CoinManager coinManager) {
        this.fileManager = fileManager;
        this.coinManager = coinManager;
    }

    public void registerAchievement(AchievementCategory category, IAchievement achievement) {
        int nextId = achievements.computeIfAbsent(category, k -> new ArrayList<>()).size();
        achievements.get(category).add(achievement);
        achievement.setId(nextId);
        achievement.setProgress(fileManager.getAchievementProgress(category, nextId));
    }

    public void updateAchievementProgress(Player player, IAchievement achievement, AchievementCategory category, int progress) {
        if (achievement.isCompleted(player)) {
            return;
        }
        Map<OfflinePlayer, AchievementProgress> progressMap = achievement.getProgress();
        AchievementProgress currentProgress = progressMap.getOrDefault(player, new AchievementProgress(player, 0, false));
        int newProgress = currentProgress.getProgress() + progress;
        progressMap.put(player, new AchievementProgress(player, newProgress, currentProgress.isCompleted()));
        if (newProgress >= achievement.getMaxProgress()) {
            unlockAchievement(player, achievement, category);
        }
        fileManager.saveAchievementProgress(category, achievement);
    }

    public void registerCategory(AchievementCategory category) {
        achievements.putIfAbsent(category, new ArrayList<>());
    }

    public void unlockAchievement(Player player, IAchievement achievement, AchievementCategory category) {
        if (!achievement.isCompleted(player)) {
            AchievementProgress currentProgress = achievement.getProgress().getOrDefault(player, new AchievementProgress(player, 0, false));
            achievement.getProgress().put(player, new AchievementProgress(player, currentProgress.getProgress(), true));
            coinManager.depositCoins(player, achievement.getReward());
            fileManager.saveAchievementProgress(category, achievement);
        }
    }

    public List<IAchievement> getAchievements(AchievementCategory category) {
        return achievements.getOrDefault(category, new ArrayList<>());
    }

    public List<AchievementCategory> getCategories() {
        return new ArrayList<>(achievements.keySet());
    }
}
