package com.wolfdrache.serverapi.achievements;

import org.bukkit.Material;

public class AchievementCategory {
    private final String name;
    private final Material material;
    public AchievementCategory(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
