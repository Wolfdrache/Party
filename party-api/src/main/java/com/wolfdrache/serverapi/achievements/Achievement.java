package com.wolfdrache.serverapi.achievements;

import org.bukkit.OfflinePlayer;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Achievement {
    public static interface IAchievementType{
        String getName();
    }

    private final int id;
    private final String name;
    private final String description;
    private final Material material;
    private final Map<IAchievementType, Integer> maxProgress;
    private final int reward;
    private final Map<OfflinePlayer, Map<IAchievementType, AchievementProgress>> progress = new HashMap<>();
    private final Map<String, IAchievementType> types = new HashMap<>();

    public Achievement(int id, String name, String description, Material material, Map<IAchievementType, Integer> maxProgress, int reward) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
        for (IAchievementType type : maxProgress.keySet()) {
            types.put(type.getName(), type);
        }
    }

    public int getCurrentProgress(OfflinePlayer player, IAchievementType type) {
        Map<IAchievementType, AchievementProgress> playerProgress = progress.get(player);
        AchievementProgress ap = playerProgress != null ? playerProgress.get(type) : null;
        return ap != null ? ap.getProgress() : 0;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxProgress(IAchievementType type) {
        return maxProgress.get(type);
    }

    public String getName() {
        return name;
    }

    public IAchievementType getAchievementType(String typeName) {
        return types.get(typeName);
    }
    
    public Map<OfflinePlayer, Map<IAchievementType, AchievementProgress>> getProgress() {
        return progress;
    }

    public int getReward() {
        return reward;
    }

    public boolean isCompleted(OfflinePlayer player) {
        Map<IAchievementType, AchievementProgress> playerProgress = progress.get(player);
        boolean completed = true;
        if (playerProgress != null) {
            for (AchievementProgress ap : playerProgress.values()) {
                if (!ap.isCompleted()) {
                    completed = false;
                    break;
                }
            }
        } else {
            completed = true;
        }
        return completed;
    }

    public void setProgress(Map<OfflinePlayer, Map<IAchievementType, AchievementProgress>> progress) {
        this.progress.clear();
        this.progress.putAll(progress);
    }
}
