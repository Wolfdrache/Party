package com.wolfdrache.serverapi.coins;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CoinUpdateEvent extends Event {
    public static enum UpdateType {
        DEPOSIT, SPEND, LOST
    }
    private final OfflinePlayer player;
    private final int changeAmount;
    private final UpdateType updateType;

    public CoinUpdateEvent(OfflinePlayer player, int changeAmount, UpdateType updateType) {
        this.player = player;
        this.changeAmount = changeAmount;
        this.updateType = updateType;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public UpdateType getUpdateType() {
        return updateType;
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
