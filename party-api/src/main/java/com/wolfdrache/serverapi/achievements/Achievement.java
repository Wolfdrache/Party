package com.wolfdrache.serverapi.achievements;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

public class Achievement {
    public static interface IAchievementType{
        String getName();
        @Nullable Function<OfflinePlayer, Integer> getSyncFunction();
    }

    private final int id;
    private final String name;
    private final List<String> description = new ArrayList<>();
    private final Material material;
    private final Map<IAchievementType, Supplier<Integer>> maxProgress;
    private final int reward;
    private final boolean descriptionHidden;

    public Achievement(int id, String name, String description, Material material, Map<IAchievementType, Supplier<Integer>> maxProgress, int reward, boolean descriptionHidden) {
        this.name = name;
        this.description.add(description);
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
        this.descriptionHidden = descriptionHidden;
    }
    public Achievement(int id, String name, List<String> description, Material material, Map<IAchievementType, Supplier<Integer>> maxProgress, int reward, boolean descriptionHidden) {
        this.name = name;
        this.description.addAll(description);
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
        this.descriptionHidden = descriptionHidden;
    }
    public Achievement(int id, String name, String description, Material material, Map<IAchievementType, Supplier<Integer>> maxProgress, int reward) {
        this.name = name;
        this.description.add(description);
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
        this.descriptionHidden = false;
    }
    public Achievement(int id, String name, List<String> description, Material material, Map<IAchievementType, Supplier<Integer>> maxProgress, int reward) {
        this.name = name;
        this.description.addAll(description);
        this.material = material;
        this.maxProgress = maxProgress;
        this.reward = reward;
        this.id = id;
        this.descriptionHidden = false;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxProgress(IAchievementType type) {
        return maxProgress.get(type).get();
    }

    public String getName() {
        return name;
    }

    public int getReward() {
        return reward;
    }
    
    public boolean isDescriptionHidden() {
        return descriptionHidden;
    }

    public List<IAchievementType> getTypes() {
        return new ArrayList<>(maxProgress.keySet());
    }
}
