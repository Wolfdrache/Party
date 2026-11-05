package com.wolfdrache.party.gamemodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wolfdrache.serverapi.gamemodes.GameCommandTypes;

public class GameCommandManager {
    private final Map<GameCommandTypes, Map<Plugin, TabExecutor>> commands = new HashMap<>();
    private final Map<Player, Plugin> playerPluginMap = new HashMap<>();
    private final Map<Plugin, String> pluginAliases = new HashMap<>();

    public void registerCommand(Plugin plugin, GameCommandTypes command, TabExecutor executor) {
        commands.computeIfAbsent(command, k -> new HashMap<>()).put(plugin, executor);
    }

    public List<String> getRegisteredPluginsForCommand(GameCommandTypes command) {
        Map<Plugin, TabExecutor> pluginMap = commands.get(command);
        if (pluginMap == null) {
            return new ArrayList<>();
        }
        List<String> pluginNames = new ArrayList<>();
        for (Plugin plugin : pluginMap.keySet()) {
            if (pluginAliases.containsKey(plugin)) {
                pluginNames.add(pluginAliases.get(plugin));
            } else {
                pluginNames.add(plugin.getName());
            }
        }
        return pluginNames;
    }

    public Plugin getPluginForCommand(GameCommandTypes command, String pluginName) {
        Map<Plugin, TabExecutor> pluginMap = commands.get(command);
        if (pluginMap == null) {
            return null;
        }
        for (Plugin plugin : pluginMap.keySet()) {
            String pluginAlias = pluginAliases.get(plugin);
            if (pluginAlias == null) pluginAlias = plugin.getName();
            if (pluginAlias.equalsIgnoreCase(pluginName)) {
                return plugin;
            }
        }
        return null;
    }

    public TabExecutor getExecutorForCommand(GameCommandTypes command, Plugin plugin) {
        Map<Plugin, TabExecutor> pluginMap = commands.get(command);
        if (pluginMap == null) {
            return null;
        }
        return pluginMap.get(plugin);
    }

    public Plugin getPluginForPlayer(Player player) {
        return playerPluginMap.get(player);
    }

    public void setPluginForPlayer(Player player, Plugin plugin) {
        playerPluginMap.put(player, plugin);
    }

    public void removePluginForPlayer(Player player) {
        playerPluginMap.remove(player);
    }

    public void removePluginForPlayer(Player player, Plugin plugin) {
        if (playerPluginMap.get(player) == plugin) {
            playerPluginMap.remove(player);
        }
    }

    public void registerPluginAlias(Plugin plugin, String alias) {
        pluginAliases.put(plugin, alias);
    }
}
