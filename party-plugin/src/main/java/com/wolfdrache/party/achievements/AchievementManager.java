package com.wolfdrache.party.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.wolfdrache.party.general.CoinManager;
import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.serverapi.achievements.AchievementCategory;
import com.wolfdrache.serverapi.achievements.AchievementProgress;
import com.wolfdrache.serverapi.achievements.Achievement.IAchievementType;
import com.wolfdrache.serverapi.achievements.Achievement;

public class AchievementManager {
    private final FileManager fileManager;
    private final CoinManager coinManager;
    
    private final Map<AchievementCategory, Map<IAchievementType, List<Achievement>>> achievements = new HashMap<>();

    public AchievementManager(FileManager fileManager, CoinManager coinManager) {
        this.fileManager = fileManager;
        this.coinManager = coinManager;
    }

    public boolean registerAchievement(AchievementCategory category, Achievement achievement) {
        if (category.hasAchievement(achievement.getId())) return false;
        Map<IAchievementType, List<Achievement>> categoryMap = achievements.computeIfAbsent(category, k -> new HashMap<>());
        for (IAchievementType type : achievement.getTypes()) {
            List<Achievement> typeList = categoryMap.computeIfAbsent(type, k -> new ArrayList<>());
            typeList.add(achievement);
        }
        category.addAchievement(achievement.getId());
        return true;
    }

    public void updateAchievementProgress(Player player, AchievementCategory category, IAchievementType type, int progress) {
        AchievementProgress playerProgress = category.getProgress(player);
        category.setProgress(player, playerProgress);
        int newProgress = playerProgress.getProgress(type) + progress;
        playerProgress.setProgress(type, newProgress);
        for (Achievement achievement : achievements.get(category).getOrDefault(type, new ArrayList<>())) {
            if (category.isCompleted(player, achievement)) continue;
            boolean completed = true;
            for (IAchievementType acType : achievement.getTypes()) {
                if (playerProgress.getProgress(acType) < achievement.getMaxProgress(acType)) {
                    completed = false;
                    break;
                }
            }
            if (completed) {
                unlockAchievement(player, achievement, category);
            }
        }
        fileManager.saveAchievementProgress(category);
    }

    public void registerCategory(AchievementCategory category) {
        achievements.putIfAbsent(category, new HashMap<>());
        category.setProgress(fileManager.getAchievementProgress(category));
    }

    public void unlockAchievement(Player player, Achievement achievement, AchievementCategory category) {
            category.completeAchievement(player, achievement);
            coinManager.depositCoins(player, achievement.getReward());
            MessageHelper.sendMessage(player, MessageHelper.Type.ACHIEVEMENT, "§7Du hast das Achievement§8: §e" + achievement.getName() + " §7freigeschaltet!");
            MessageHelper.sendMessage(player, MessageHelper.Type.ACHIEVEMENT, "§7Du erhältst §6" + achievement.getReward() + " §7Coins!");
    }

    public List<Achievement> getAchievements(AchievementCategory category) {
        return new ArrayList<>(achievements.getOrDefault(category, new HashMap<>()).values().stream().flatMap(List::stream).collect(Collectors.toSet()));
    }

    public List<AchievementCategory> getCategories() {
        return new ArrayList<>(achievements.keySet());
    }

    public int getAmountDone(Player player, AchievementCategory category) {
        int amountDone = 0;
        List<Achievement> achievementList = getAchievements(category);
        for (Achievement achievement : achievementList) {
            if (category.isCompleted(player, achievement)) {
                amountDone++;
            }
        }
        return amountDone;
    }
}
