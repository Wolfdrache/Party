package com.wolfdrache.serverapi.achievements;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface AchievementService {
    AchievementCategory registerAchievementCategory(String name, Material material, String displayName);
    boolean registerAchievement(Achievement achievement, AchievementCategory category);
    void unlockAchievementType(Player player, Achievement achievement, AchievementCategory category, Achievement.IAchievementType type);
    void updateAchievementProgress(Player player, Achievement achievement, AchievementCategory category, Achievement.IAchievementType type, int progress);
    List<Achievement> getAchievements(AchievementCategory category);
    List<AchievementCategory> getCategories();
    int getAmountDone(Player player, AchievementCategory category);
    Achievement.IAchievementType getAchievementType(Achievement achievement, String typeName);
}
