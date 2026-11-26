package com.wolfdrache.party.service;

import com.wolfdrache.serverapi.gamemodes.roundManagement.PluginData;
import com.wolfdrache.serverapi.gamemodes.roundManagement.RoundData;
import com.wolfdrache.party.gamemodes.roundManagement.RoundManager;
import com.wolfdrache.serverapi.gamemodes.GameService;
import com.wolfdrache.serverapi.gamemodes.roundManagement.IRound;
import com.wolfdrache.serverapi.gamemodes.roundManagement.RoundService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RoundServiceImpl implements RoundService {
    private final RoundManager roundManager;
    private final GameService gameService;

    public RoundServiceImpl(RoundManager roundManager, GameService gameService) {
        this.roundManager = roundManager;
        this.gameService = gameService;
    }

    @Override
    public boolean registerDebugFunction(BiConsumer<Player, IRound> debugFunction, Plugin plugin) {
        return roundManager.registerDebugFunction(debugFunction, plugin);
    }

    @Override
    public boolean registerEditFunction(BiConsumer<Player, IRound> editFunction, Plugin plugin) {
        return roundManager.registerEditFunction(editFunction, plugin);
    }

    @Override
    public boolean registerSaveFunction(Consumer<IRound> saveFunction, Plugin plugin) {
        return roundManager.registerSaveFunction(saveFunction, plugin);
    }

    @Override
    public boolean registerSpectateFunction(BiConsumer<Player, IRound> spectateFunction, Plugin plugin) {
        return roundManager.registerSpectateFunction(spectateFunction, plugin);
    }

    @Override
    public boolean registerJoinFunction(BiConsumer<Player, IRound> joinFunction, Plugin plugin) {
        return roundManager.registerJoinFunction(joinFunction, plugin);
    }

    @Override
    public boolean registerLeaveFunction(BiConsumer<Player, IRound> leaveFunction, Plugin plugin) {
        return roundManager.registerLeaveFunction(leaveFunction, plugin);
    }

    @Override
    public boolean registerPlayerToRound(Player player, IRound round, Plugin plugin) {
        gameService.joinPlugin(player, plugin);
        return roundManager.registerPlayerToRound(player, round, plugin);
    }

    @Override
    public void removePlayerFromRound(Player player) {
        RoundData data = roundManager.removePlayerFromRound(player);
        if (data != null) {
            gameService.leaveGame(player, data.getPlugin());
        }
    }

    @Override
    public boolean registerAllRoundsForPlugin(Plugin plugin, Map<IRound, Material> rounds) {
        return roundManager.registerAllRoundsForPlugin(plugin, rounds);
    }

    @Override
    public Map<IRound, Material> getRoundsWithDisplayItemForPlugin(Plugin plugin) {
        return roundManager.getRoundsWithDisplayItemForPlugin(plugin);
    }

    @Override
    public List<IRound> getRoundsForPlugin(Plugin plugin) {
        return roundManager.getRoundsForPlugin(plugin);
    }

    @Override
    public boolean registerRound(IRound round, Material displayItem, Plugin plugin) {
        return roundManager.registerRound(round, displayItem, plugin);
    }

    @Override
    public boolean executeDebugFunction(Player player, IRound round) {
        return roundManager.executeDebugFunction(player, round);
    }

    @Override
    public boolean executeEditFunction(Player player, IRound round) {
        return roundManager.executeEditFunction(player, round);
    }

    @Override
    public boolean executeSaveFunction(IRound round) {
        return roundManager.executeSaveFunction(round);
    }

    @Override
    public boolean executeSpectateFunction(Player player, IRound round) {
        return roundManager.executeSpectateFunction(player, round);
    }

    @Override
    public boolean executeJoinFunction(Player player, IRound round) {
        return roundManager.executeJoinFunction(player, round);
    }

    @Override
    public boolean executeLeaveFunction(Player player, IRound round) {
        return roundManager.executeLeaveFunction(player, round);
    }

    @Override
    public void registerPluginData(Plugin plugin, Material displayItem) {
        roundManager.registerPluginData(plugin, displayItem);
    }

    @Override
    public @Nullable RoundData getActiveRoundDataForPlayer(Player player) {
        return roundManager.getActiveRoundDataForPlayer(player);
    }

    @Override
    public List<PluginData> getAllPluginData() {
        return roundManager.getAllPluginData();
    }
}
