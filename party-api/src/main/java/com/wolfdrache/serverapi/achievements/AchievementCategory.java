package com.wolfdrache.serverapi.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.serverapi.achievements.Achievement.IAchievementType;

public class AchievementCategory {
    private final String name;
    private final Material material;
    private final String displayName;
    private final List<Integer> achievementIds = new ArrayList<>();
    private final Map<OfflinePlayer, AchievementProgress> progress = new HashMap<>();
    private final Map<String, IAchievementType> types = new HashMap<>();

    public AchievementCategory(String name, Material material, String displayName, Set<Achievement.IAchievementType> types) {
        this.name = name;
        this.material = material;
        this.displayName = displayName;
        for (Achievement.IAchievementType type : types) {
            this.types.put(type.getName(), type);
        }
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean hasAchievement(int achievementId) {
        return achievementIds.contains(achievementId);
    }

    public void addAchievement(int achievementId) {
        if (!achievementIds.contains(achievementId)) {
            achievementIds.add(achievementId);
        }
    }

    public Set<Achievement.IAchievementType> getAchievementTypes() {
        return new HashSet<>(types.values());
    }

    public void setProgress(Map<OfflinePlayer, AchievementProgress> progress) {
        this.progress.clear();
        this.progress.putAll(progress);
    }

    public void setProgress(OfflinePlayer player, AchievementProgress playerProgress) {
        progress.put(player, playerProgress);
    }

    public Map<OfflinePlayer, AchievementProgress> getProgress() {
        return progress;
    }

    public AchievementProgress getProgress(OfflinePlayer player) {
        return progress.getOrDefault(player, new AchievementProgress(player, new HashMap<>(), new HashSet<>()));
    }

    public IAchievementType getAchievementType(String typeName) {
        return types.get(typeName);
    }

    public boolean isCompleted(Player player, Achievement achievement) {
        AchievementProgress playerProgress = getProgress(player);
        return playerProgress.isCompleted(achievement.getId());
    }

    public void completeAchievement(Player player, Achievement achievement) {
        AchievementProgress playerProgress = getProgress(player);
        playerProgress.markCompleted(achievement.getId());
    }
}
