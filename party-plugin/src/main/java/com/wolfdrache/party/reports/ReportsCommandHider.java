package com.wolfdrache.party.reports;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class ReportsCommandHider implements Listener {
    @EventHandler 
    public void onCommandSend(PlayerCommandSendEvent event) {
        event.getCommands().remove("reportsteam");
    }
}
