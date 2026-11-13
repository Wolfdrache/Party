package com.wolfdrache.party.service;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.wolfdrache.party.achievements.AchievementManager;
import com.wolfdrache.serverapi.achievements.AchievementCategory;
import com.wolfdrache.serverapi.achievements.AchievementService;
import com.wolfdrache.serverapi.achievements.Achievement.IAchievementType;
import com.wolfdrache.serverapi.achievements.Achievement;

public class AchievementServiceImpl implements AchievementService {
    private final AchievementManager achievementManager;

    public AchievementServiceImpl(AchievementManager achievementManager) {
        this.achievementManager = achievementManager;
    }

    @Override 
    public AchievementCategory registerAchievementCategory(String name, Material icon, String displayName) {
        AchievementCategory category = new AchievementCategory(name, icon, displayName);
        achievementManager.registerCategory(category);
        return category;
    }

    @Override
    public boolean registerAchievement(Achievement achievement, AchievementCategory category) {
        return achievementManager.registerAchievement(category, achievement);
    }

    @Override
    public void unlockAchievementType(Player player, Achievement achievement, AchievementCategory category, IAchievementType type) {
        achievementManager.unlockAchievementType(player, achievement, category, type);
    }

    @Override
    public void updateAchievementProgress(Player player, Achievement achievement, AchievementCategory category, IAchievementType type, int progress) {
        achievementManager.updateAchievementProgress(player, achievement, category, type, progress);
    }

    @Override
    public List<Achievement> getAchievements(AchievementCategory category) {
        return achievementManager.getAchievements(category);
    }

    @Override
    public List<AchievementCategory> getCategories() {
        return achievementManager.getCategories();
    }
    
    @Override
    public int getAmountDone(Player player, AchievementCategory category) {
        return achievementManager.getAmountDone(player, category);
    }

    @Override
    public IAchievementType getAchievementType(Achievement achievement, String typeName) {
        return achievement.getAchievementType(typeName);
    }
}
