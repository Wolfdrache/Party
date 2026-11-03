package com.wolfdrache.serverapi.gamemodes;

import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface GameService {
    void registerCommand(Plugin plugin, GameCommandTypes command, TabExecutor executor);
    void joinPlugin(Player player, Plugin plugin);
    void leaveGame(Player player, Plugin plugin);
}
