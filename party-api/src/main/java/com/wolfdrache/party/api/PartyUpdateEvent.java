package com.wolfdrache.party.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartyUpdateEvent extends Event {
    public enum PartyUpdateType {
        PARTY_LEFT,
        PARTY_DISBANDED,
        PARTY_INVITE_RECEIVED,
        PARTY_INVITE_SENT,
        INVITE_ACCEPTED,
        INVITE_DECLINED
    }

    private final Player player;
    private final OfflinePlayer target;
    private final PartyUpdateType updateType;

    public PartyUpdateEvent(Player player, OfflinePlayer target, PartyUpdateType updateType) {
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

    public PartyUpdateType getUpdateType() {
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
