package com.wolfdrache.serverapi.gamemodes;

import java.util.List;
import java.util.function.Function;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface GameService {
    void registerCommand(Plugin plugin, GameCommandTypes command, TabExecutor executor);
    void joinPlugin(Player player, Plugin plugin);
    void leaveGame(Player player, Plugin plugin);
    void registerPluginAlias(Plugin plugin, String alias);
    void registerStatsDisplayItem(Function<OfflinePlayer, ItemStack> itemSupplier);
    List<ItemStack> getStatsItems(OfflinePlayer player);
}
