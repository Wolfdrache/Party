package com.wolfdrache.serverapi.gamemodes.roundManagement;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PluginData {
    private Function<IRound, Void> saveFunction;
    private BiFunction<Player, IRound, Void> debugFunction;
    private BiFunction<Player, IRound, Void> editFunction;
    private BiFunction<Player, IRound, Void> spectateFunction;
    private BiFunction<Player, IRound, Void> joinFunction;
    private BiFunction<Player, IRound, Void> leaveFunction;
    private final List<IRound> rounds = new ArrayList<>();
    private final Material displayItem;

    public PluginData(Material displayItem) {
        this.displayItem = displayItem;
    }

    public Material getDisplayItem() {
        return displayItem;
    }

    public void setSaveFunction(Function<IRound, Void> saveFunction) {
        this.saveFunction = saveFunction;
    }
    public void setDebugFunction (BiFunction<Player, IRound, Void> debugFunction) {
        this.debugFunction = debugFunction;
    }
    public void setEditFunction(BiFunction<Player, IRound, Void> editFunction) {
        this.editFunction = editFunction;
    }
    public void setSpectateFunction(BiFunction<Player, IRound, Void> spectateFunction) {
        this.spectateFunction = spectateFunction;
    }
    public void setJoinFunction(BiFunction<Player, IRound, Void> joinFunction) {
        this.joinFunction = joinFunction;
    }
    public void setLeaveFunction(BiFunction<Player, IRound, Void> leaveFunction) {
        this.leaveFunction = leaveFunction;
    }

    public void addRound(IRound round) {
        rounds.add(round);
    }

    public List<IRound> getRounds() {
        return rounds;
    }

    public BiFunction<Player, IRound, Void> getDebugFunction() {
        return debugFunction;
    }
    public BiFunction<Player, IRound, Void> getEditFunction() {
        return editFunction;
    }
    public Function<IRound, Void> getSaveFunction() {
        return saveFunction;
    }
    public BiFunction<Player, IRound, Void> getSpectateFunction() {
        return spectateFunction;
    }
    public BiFunction<Player, IRound, Void> getJoinFunction() {
        return joinFunction;
    }
    public BiFunction<Player, IRound, Void> getLeaveFunction() {
        return leaveFunction;
    }
}
