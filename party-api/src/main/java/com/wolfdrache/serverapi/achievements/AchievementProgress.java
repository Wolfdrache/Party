package com.wolfdrache.serverapi.achievements;

import org.bukkit.OfflinePlayer;

public class AchievementProgress {
    private final OfflinePlayer player;
    private int progress;
    private boolean completed;

    public AchievementProgress(OfflinePlayer player, int progress, boolean completed) {
        this.player = player;
        this.progress = progress;
        this.completed = completed;
    }
    
    public OfflinePlayer getPlayer() {
        return player;
    }
    public int getProgress() {
        return progress;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
