package com.wolfdrache.party.gamemodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.wolfdrache.serverapi.gamemodes.GameService.CommandType;

public class GameCommandManager {
    private final Map<CommandType, Map<Plugin, TabExecutor>> commands = new HashMap<>();
    private final Map<Player, Plugin> playerPluginMap = new HashMap<>();
    private final Map<Plugin, String> pluginAliases = new HashMap<>();
    private final List<Function<OfflinePlayer, ItemStack>> statsDisplayItems = new ArrayList<>();

    public void registerCommand(Plugin plugin, CommandType command, TabExecutor executor) {
        commands.computeIfAbsent(command, k -> new HashMap<>()).put(plugin, executor);
    }

    public List<String> getRegisteredPluginsForCommand(CommandType command) {
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

    public Plugin getPluginForCommand(CommandType command, String pluginName) {
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

    public TabExecutor getExecutorForCommand(CommandType command, Plugin plugin) {
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

    public void registerStatsDisplayItem(Function<OfflinePlayer, ItemStack> itemSupplier) {
        statsDisplayItems.add(itemSupplier);
    }

    public List<ItemStack> getStatsItems(OfflinePlayer player) {
        List<ItemStack> items = new ArrayList<>();
        for (Function<OfflinePlayer, ItemStack> itemSupplier : statsDisplayItems) {
            items.add(itemSupplier.apply(player));
        }
        return items;
    }
}
