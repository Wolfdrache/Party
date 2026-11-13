package com.wolfdrache.serverapi.achievements;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface AchievementService {
    AchievementCategory registerAchievementCategory(String name, Material material, String displayName, Set<Achievement.IAchievementType> types);
    boolean registerAchievement(Achievement achievement, AchievementCategory category);
    void updateAchievementTypeProgress(Player player, AchievementCategory category, Achievement.IAchievementType type, int progress);
    List<Achievement> getAchievements(AchievementCategory category);
    List<AchievementCategory> getCategories();
    int getAmountDone(Player player, AchievementCategory category);
    Achievement.IAchievementType getAchievementType(AchievementCategory category, String typeName);
}
