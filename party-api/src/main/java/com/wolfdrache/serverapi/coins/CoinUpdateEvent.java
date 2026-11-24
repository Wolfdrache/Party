package com.wolfdrache.serverapi.coins;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CoinUpdateEvent extends Event implements Cancellable {
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static enum UpdateType {
        DEPOSIT, SPEND
    }
    private final OfflinePlayer player;
    private final int changeAmount;
    private final UpdateType updateType;
    private boolean cancelled = false;

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
