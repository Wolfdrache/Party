package com.wolfdrache.party.internal.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolfdrache.party.internal.manager.PartyManager;

public class ConnectionListener implements Listener {
    private final PartyManager partyManager;

    public ConnectionListener(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @EventHandler 
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        if (partyManager.isInParty(event.getPlayer())) {
            partyManager.leaveParty(event.getPlayer());
        }
        partyManager.cancelPendingInvites(event.getPlayer());
    }
}
