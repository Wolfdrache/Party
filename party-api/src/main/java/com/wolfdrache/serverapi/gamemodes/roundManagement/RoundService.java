package com.wolfdrache.serverapi.gamemodes.roundManagement;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface RoundService {
    boolean registerDebugFunction(BiFunction<Player, IRound, Void> debugFunction, Plugin plugin);
    boolean registerEditFunction(BiFunction<Player, IRound, Void> editFunction, Plugin plugin);
    boolean registerSaveFunction(Function<IRound, Void> saveFunction, Plugin plugin);
    boolean registerSpectateFunction(BiFunction<Player, IRound, Void> spectateFunction, Plugin plugin);
    boolean registerJoinFunction(BiFunction<Player, IRound, Void> joinFunction, Plugin plugin);
    boolean registerLeaveFunction(BiFunction<Player, IRound, Void> leaveFunction, Plugin plugin);
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
    void registerPluginData(Plugin plugin, Material displayItem);
    @Nullable RoundData getActiveRoundDataForPlayer(Player player);
    List<PluginData> getAllPluginData();
}
