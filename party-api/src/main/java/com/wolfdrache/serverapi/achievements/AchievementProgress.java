package com.wolfdrache.serverapi.achievements;

import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;

public class AchievementProgress {
    private final OfflinePlayer player;
    private final Map<Achievement.IAchievementType, Integer> progress;
    private final Set<Integer> completed;

    public AchievementProgress(OfflinePlayer player, Map<Achievement.IAchievementType, Integer> progress, Set<Integer> completed) {
        this.player = player;
        this.progress = progress;
        this.completed = completed;
    }
    
    public OfflinePlayer getPlayer() {
        return player;
    }
    public Map<Achievement.IAchievementType, Integer> getProgress() {
        return progress;
    }
    public Set<Integer> getCompleted() {
        return completed;
    }
    public boolean isCompleted(int achievementId) {
        return completed.contains(achievementId);
    }
    public int getProgress(Achievement.IAchievementType type) {
        if (type.getSyncFunction() == null) {
            return progress.getOrDefault(type, 0);
        }
        return type.getSyncFunction().apply(player);
    }
    public void setProgress(Achievement.IAchievementType type, int value) {
        progress.put(type, value);
    }
    public void markCompleted(int achievementId) {
        completed.add(achievementId);
    }
}
