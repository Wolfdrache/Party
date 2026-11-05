package com.wolfdrache.party.service;

import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wolfdrache.party.gamemodes.GameCommandManager;
import com.wolfdrache.serverapi.gamemodes.GameCommandTypes;
import com.wolfdrache.serverapi.gamemodes.GameService;

public class GameServiceImpl implements GameService {
    private final GameCommandManager gameCommandManager;

    public GameServiceImpl(GameCommandManager gameCommandManager) {
        this.gameCommandManager = gameCommandManager;
    }

    @Override
    public void registerCommand(Plugin plugin, GameCommandTypes command, TabExecutor executor) {
        gameCommandManager.registerCommand(plugin, command, executor);
    }

    @Override
    public void joinPlugin(Player player, Plugin plugin) {
        gameCommandManager.setPluginForPlayer(player, plugin);
    }

    @Override
    public void leaveGame(Player player, Plugin plugin) {
        gameCommandManager.removePluginForPlayer(player, plugin);
    }
    
    @Override
    public void registerPluginAlias(Plugin plugin, String alias) {
        gameCommandManager.registerPluginAlias(plugin, alias);
    }
}
