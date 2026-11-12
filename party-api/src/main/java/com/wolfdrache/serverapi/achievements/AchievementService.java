package com.wolfdrache.serverapi.achievements;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface AchievementService {
    AchievementCategory registerAchievementCategory(String name, Material material);
    void registerAchievement(IAchievement achievement, AchievementCategory category);
    boolean unlockAchievement(Player player, IAchievement achievement, AchievementCategory category);
    boolean updateAchievementProgress(Player player, IAchievement achievement, int progress, AchievementCategory category);
    List<IAchievement> getAchievements(AchievementCategory category);
    List<AchievementCategory> getCategories();
}
