package com.wolfdrache.serverapi.gamemodes.roundManagement;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class RoundData {
    private final IRound round;
    private final Material displayItem;
    private final Plugin plugin;

    public RoundData(IRound round, Material displayItem, Plugin plugin) {
        this.round = round;
        this.displayItem = displayItem;
        this.plugin = plugin;
    }

    public IRound getRound() {
        return round;
    }
    public Material getDisplayItem() {
        return displayItem;
    }
    public Plugin getPlugin() {
        return plugin;
    }
}
