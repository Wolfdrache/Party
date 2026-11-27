package com.wolfdrache.serverapi.gamemodes.roundManagement;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface RoundService {
    boolean registerDebugFunction(BiConsumer<Player, IRound> debugFunction, Plugin plugin);
    boolean registerEditFunction(BiConsumer<Player, IRound> editFunction, Plugin plugin);
    boolean registerSaveFunction(Consumer<IRound> saveFunction, Plugin plugin);
    boolean registerSpectateFunction(BiConsumer<Player, IRound> spectateFunction, Plugin plugin);
    boolean registerJoinFunction(BiConsumer<Player, IRound> joinFunction, Plugin plugin);
    boolean registerLeaveFunction(BiConsumer<Player, IRound> leaveFunction, Plugin plugin);
    boolean registerPlayerToRound(Player player, IRound round, Plugin plugin);
    void removePlayerFromRound(Player player);
    boolean registerAllRoundsForPlugin(Plugin plugin, Map<IRound, Material> rounds);
    Map<IRound, Material> getRoundsWithDisplayItemForPlugin(Plugin plugin);
    List<IRound> getRoundsForPlugin(Plugin plugin);
    boolean registerRound(IRound round, Material displayItem, Plugin plugin);
    boolean executeDebugFunction(Player player, IRound round);
    boolean executeEditFunction(Player player, IRound round);
    boolean executeSaveFunction(IRound round);
    boolean executeSpectateFunction(Player player, IRound round);
    boolean executeJoinFunction(Player player, IRound round);
    boolean executeLeaveFunction(Player player, IRound round);
    void registerPluginData(Plugin plugin, Material displayItem, String displayName);
    @Nullable RoundData getActiveRoundDataForPlayer(Player player);
    List<PluginData> getAllPluginData();
}
