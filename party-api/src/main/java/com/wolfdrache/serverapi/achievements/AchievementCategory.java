package com.wolfdrache.serverapi.achievements;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class AchievementCategory {
    private final String name;
    private final Material material;
    private final String displayName;
    private final List<Integer> achievementIds = new ArrayList<>();

    public AchievementCategory(String name, Material material, String displayName) {
        this.name = name;
        this.material = material;
        this.displayName = displayName;
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
}
