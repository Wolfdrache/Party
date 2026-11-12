package com.wolfdrache.serverapi.achievements;

import org.bukkit.OfflinePlayer;
import org.bukkit.Material;
import java.util.Map;

public interface IAchievement {
    int getReward();
    String getName();
    String getDescription();
    int getMaxProgress();
    int getCurrentProgress(OfflinePlayer player);
    int getId();
    Material getMaterial();
    boolean isCompleted(OfflinePlayer player);
    void setId(int id);
    void setProgress(Map<OfflinePlayer, AchievementProgress> progress);
    Map<OfflinePlayer, AchievementProgress> getProgress();
}
