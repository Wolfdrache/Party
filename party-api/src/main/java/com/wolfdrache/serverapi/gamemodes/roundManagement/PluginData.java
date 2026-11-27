package com.wolfdrache.serverapi.gamemodes.roundManagement;

import com.wolfdrache.helper.Helper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PluginData {
    private Consumer<IRound> saveFunction;
    private BiConsumer<Player, IRound> debugFunction;
    private BiConsumer<Player, IRound> editFunction;
    private BiConsumer<Player, IRound> spectateFunction;
    private BiConsumer<Player, IRound> joinFunction;
    private BiConsumer<Player, IRound> leaveFunction;
    private final List<IRound> rounds = new ArrayList<>();
    private final Material displayMaterial;
    private final String displayName;

    public PluginData(Material displayItem, String displayName) {
        this.displayMaterial = displayItem;
        this.displayName = displayName;
    }

    public ItemStack getDisplayItem() {
        return Helper.createNamedItem(displayMaterial, displayName);
    }

    public Material getDisplayMaterial() {
        return displayMaterial;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setSaveFunction(Consumer<IRound> saveFunction) {
        this.saveFunction = saveFunction;
    }
    public void setDebugFunction(BiConsumer<Player, IRound> debugFunction) {
        this.debugFunction = debugFunction;
    }
    public void setEditFunction(BiConsumer<Player, IRound> editFunction) {
        this.editFunction = editFunction;
    }
    public void setSpectateFunction(BiConsumer<Player, IRound> spectateFunction) {
        this.spectateFunction = spectateFunction;
    }
    public void setJoinFunction(BiConsumer<Player, IRound> joinFunction) {
        this.joinFunction = joinFunction;
    }
    public void setLeaveFunction(BiConsumer<Player, IRound> leaveFunction) {
        this.leaveFunction = leaveFunction;
    }

    public void addRound(IRound round) {
        rounds.add(round);
    }

    public List<IRound> getRounds() {
        return rounds;
    }

    public BiConsumer<Player, IRound> getDebugFunction() {
        return debugFunction;
    }
    public BiConsumer<Player, IRound> getEditFunction() {
        return editFunction;
    }
    public Consumer<IRound> getSaveFunction() {
        return saveFunction;
    }
    public BiConsumer<Player, IRound> getSpectateFunction() {
        return spectateFunction;
    }
    public BiConsumer<Player, IRound> getJoinFunction() {
        return joinFunction;
    }
    public BiConsumer<Player, IRound> getLeaveFunction() {
        return leaveFunction;
    }
}
