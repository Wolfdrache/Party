package com.wolfdrache.serverapi.gamemodes.roundManagement;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoundStatusUpdateEvent extends Event {
    private final Plugin plugin;
    private final IRound round;
    private final IRoundMode newStatus;

    public RoundStatusUpdateEvent(Plugin plugin, IRound round, IRoundMode newStatus) {
        this.plugin = plugin;
        this.round = round;
        this.newStatus = newStatus;
    }
    public Plugin getPlugin() {
        return plugin;
    }
    public IRound getRound() {
        return round;
    }
    public IRoundMode getNewStatus() {
        return newStatus;
    }
    public List<Player> getPlayersInRound() {
        return round.getPlayersInRound();
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
