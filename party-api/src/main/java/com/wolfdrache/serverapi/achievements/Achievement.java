package com.wolfdrache.serverapi.achievements;

import org.bukkit.OfflinePlayer;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Achievement {
    private int id;
    private final String name;
    private final String description;
    private final Material material;
    private final int maxProgress;
    private final int reward;
    private final Map<OfflinePlayer, AchievementProgress> progress = new HashMap<>();

    public Achievement(int id, String name, String description, Material material, int maxProgress, int reward) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
    }

    public int getCurrentProgress(OfflinePlayer player) {
        AchievementProgress ap = progress.get(player);
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

    public int getMaxProgress() {
        return maxProgress;
    }

    public String getName() {
        return name;
    }

    public Map<OfflinePlayer, AchievementProgress> getProgress() {
        return progress;
    }

    public int getReward() {
        return reward;
    }

    public boolean isCompleted(OfflinePlayer player) {
        AchievementProgress ap = progress.get(player);
        return ap != null && ap.isCompleted();
    }

    public void setProgress(Map<OfflinePlayer, AchievementProgress> progress) {
        this.progress.clear();
        this.progress.putAll(progress);
    }
}
