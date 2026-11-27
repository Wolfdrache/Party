package com.wolfdrache.serverapi.gamemodes.roundManagement;

import org.bukkit.entity.Player;

import java.util.List;

public interface IRound {
    IRoundMode getRoundMode();
    String getRoundName();
    List<Player> getPlayersInRound();
}
