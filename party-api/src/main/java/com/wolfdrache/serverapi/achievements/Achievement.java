package com.wolfdrache.serverapi.achievements;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Achievement {
    public static interface IAchievementType{
        String getName();
        Function<OfflinePlayer, Integer> getSyncFunction();
    }

    private final int id;
    private final String name;
    private final String description;
    private final Material material;
    private final Map<IAchievementType, Integer> maxProgress;
    private final int reward;

    public Achievement(int id, String name, String description, Material material, Map<IAchievementType, Integer> maxProgress, int reward) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
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

    public int getReward() {
        return reward;
    }

    public List<IAchievementType> getTypes() {
        return new ArrayList<>(maxProgress.keySet());
    }
}
