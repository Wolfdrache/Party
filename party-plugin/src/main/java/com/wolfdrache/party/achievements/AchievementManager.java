package com.wolfdrache.party.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.CoinManager;
import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.serverapi.achievements.AchievementCategory;
import com.wolfdrache.serverapi.achievements.AchievementProgress;
import com.wolfdrache.serverapi.achievements.Achievement.IAchievementTye;
import com.wolfdrache.serverapi.achievements.Achievement;

public class AchievementManager {
    private final FileManager fileManager;
    private final CoinManager coinManager;
    
    private final Map<AchievementCategory, List<Achievement>> achievements = new HashMap<>();

    public AchievementManager(FileManager fileManager, CoinManager coinManager) {
        this.fileManager = fileManager;
        this.coinManager = coinManager;
    }

    public boolean registerAchievement(AchievementCategory category, Achievement achievement) {
        if (category.hasAchievement(achievement.getId())) return false;
        achievements.get(category).add(achievement);
        achievement.setProgress(fileManager.getAchievementProgress(category, achievement.getId()));
        category.addAchievement(achievement.getId());
        return true;
    }

    public void updateAchievementProgress(Player player, Achievement achievement, AchievementCategory category, IAchievementTye type, int progress) {
        if (achievement.isCompleted(player)) {
            return;
        }
        Map<OfflinePlayer, Map<String, AchievementProgress>> progressMap = achievement.getProgress();
        Map<String, AchievementProgress> playerProgress = progressMap.getOrDefault(player, new HashMap<>());
        AchievementProgress currentProgress = playerProgress.getOrDefault(type.getName(), new AchievementProgress(player, 0, false));
        int newProgress = currentProgress.getProgress() + progress;
        playerProgress.put(type.getName(), new AchievementProgress(player, newProgress, currentProgress.isCompleted()));
        progressMap.put(player, playerProgress);
        fileManager.saveAchievementProgress(category, achievement);
        if (newProgress >= achievement.getMaxProgress(type)) {
            unlockAchievementType(player, achievement, category, type);
        }
    }

    public void registerCategory(AchievementCategory category) {
        achievements.putIfAbsent(category, new ArrayList<>());
    }

    public void unlockAchievementType(Player player, Achievement achievement, AchievementCategory category, IAchievementTye type) {
        if (!achievement.isCompleted(player)) {
            Map<String, AchievementProgress> playerProgress = achievement.getProgress().getOrDefault(player, new HashMap<>());
            AchievementProgress currentProgress = playerProgress.getOrDefault(type.getName(), new AchievementProgress(player, 0, false));
            playerProgress.put(type.getName(), new AchievementProgress(player, currentProgress.getProgress(), true));
            achievement.getProgress().put(player, playerProgress);
            fileManager.saveAchievementProgress(category, achievement);
            if (achievement.isCompleted(player)) {
                coinManager.depositCoins(player, achievement.getReward());
                MessageHelper.sendMessage(player, MessageHelper.Type.ACHIEVEMENT, "§7Du hast das Achievement§8: §e" + achievement.getName() + " §7freigeschaltet!");
                MessageHelper.sendMessage(player, MessageHelper.Type.ACHIEVEMENT, "§7Du erhältst §6" + achievement.getReward() + " §7Coins!");
            }
        }
    }

    public List<Achievement> getAchievements(AchievementCategory category) {
        return achievements.getOrDefault(category, new ArrayList<>());
    }

    public List<AchievementCategory> getCategories() {
        return new ArrayList<>(achievements.keySet());
    }

    public int getAmountDone(Player player, AchievementCategory category) {
        int amountDone = 0;
        List<Achievement> achievementList = achievements.getOrDefault(category, new ArrayList<>());
        for (Achievement achievement : achievementList) {
            if (achievement.isCompleted(player)) {
                amountDone++;
            }
        }
        return amountDone;
    }
}
