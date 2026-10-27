package com.wolfdrache.friends;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FriendsUpdateEvent extends Event {
    public enum FriendsUpdateType {
        REQUEST_SENT,
        REQUEST_ACCEPTED,
        REQUEST_DECLINED,
        REQUEST_CANCELLED,
        FRIEND_REMOVED,
        FRIEND_ONLINE,
        FRIEND_OFFLINE
    }

    private final Player player;
    private final OfflinePlayer target;
    private final FriendsUpdateType updateType;

    public FriendsUpdateEvent(Player player, OfflinePlayer target, FriendsUpdateType updateType) {
        this.player = player;
        this.target = target;
        this.updateType = updateType;
    }
    
    public Player getPlayer() {
        return player;
    }

    public OfflinePlayer getTarget() {
        return target;
    }

    public FriendsUpdateType getUpdateType() {
        return updateType;
    }
    
    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
