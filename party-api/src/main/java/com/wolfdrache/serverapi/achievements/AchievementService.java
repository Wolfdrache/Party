package com.wolfdrache.serverapi.achievements;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface AchievementService {
    AchievementCategory registerAchievementCategory(String name, Material material, String displayName);
    boolean registerAchievement(Achievement achievement, AchievementCategory category);
    void unlockAchievement(Player player, Achievement achievement, AchievementCategory category);
    void updateAchievementProgress(Player player, Achievement achievement, int progress, AchievementCategory category);
    List<Achievement> getAchievements(AchievementCategory category);
    List<AchievementCategory> getCategories();
}
