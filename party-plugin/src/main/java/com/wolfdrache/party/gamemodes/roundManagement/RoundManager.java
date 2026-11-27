package com.wolfdrache.party.gamemodes.roundManagement;

import com.wolfdrache.serverapi.gamemodes.roundManagement.IRound;
import com.wolfdrache.serverapi.gamemodes.roundManagement.PluginData;
import com.wolfdrache.serverapi.gamemodes.roundManagement.RoundData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RoundManager {
    private final Map<Player, RoundData> playerRoundMap = new HashMap<>();
    private final Map<Plugin, PluginData> pluginDataMap = new HashMap<>();
    private final Map<IRound, RoundData> roundDataMap = new HashMap<>();

    public void registerPluginData(Plugin plugin, Material displayMaterial, String displayName) {
        pluginDataMap.put(plugin, new PluginData(displayMaterial, displayName));
    }

    public boolean registerDebugFunction(BiConsumer<Player, IRound> debugFunction, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        pluginData.setDebugFunction(debugFunction);
        return true;
    }

    public boolean registerEditFunction(BiConsumer<Player, IRound> editFunction, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        pluginData.setEditFunction(editFunction);
        return true;
    }

    public boolean registerSaveFunction(Consumer<IRound> saveFunction, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        pluginData.setSaveFunction(saveFunction);
        return true;
    }

    public boolean registerSpectateFunction(BiConsumer<Player, IRound> spectateFunction, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        pluginData.setSpectateFunction(spectateFunction);
        return true;
    }

    public boolean registerJoinFunction(BiConsumer<Player, IRound> joinFunction, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        pluginData.setJoinFunction(joinFunction);
        return true;
    }

    public boolean registerLeaveFunction(BiConsumer<Player, IRound> leaveFunction, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        pluginData.setLeaveFunction(leaveFunction);
        return true;
    }

    public boolean registerPlayerToRound(Player player, IRound round, Plugin plugin) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) {
            return false;
        }
        playerRoundMap.put(player, roundData);
        return true;
    }

    public RoundData removePlayerFromRound(Player player) {
        return playerRoundMap.remove(player);
    }

    public boolean registerAllRoundsForPlugin(Plugin plugin, Map<IRound, Material> rounds) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        for (IRound round : rounds.keySet()) {
            roundDataMap.put(round, new RoundData(round, rounds.get(round), plugin));
            pluginData.addRound(round);
        }
        return true;
    }

    public Map<IRound, Material> getRoundsWithDisplayItemForPlugin(Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return Map.of();
        }
        Map<IRound, Material> roundsWithDisplayItem = new HashMap<>();
        for (IRound round : pluginData.getRounds()) {
            RoundData roundData = roundDataMap.get(round);
            if (roundData != null) {
                roundsWithDisplayItem.put(round, roundData.getDisplayItem());
            }
        }
        return roundsWithDisplayItem;
    }

    public List<IRound> getRoundsForPlugin(Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return List.of();
        }
        return List.copyOf(pluginData.getRounds());
    }

    public boolean executeDebugFunction(Player player, IRound round) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) return false;
        Plugin plugin = roundData.getPlugin();
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null || pluginData.getDebugFunction() == null) return false;
        pluginData.getDebugFunction().accept(player, round);
        return true;
    }
    public boolean executeEditFunction(Player player, IRound round) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) return false;
        Plugin plugin = roundData.getPlugin();
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null || pluginData.getEditFunction() == null) return false;
        pluginData.getEditFunction().accept(player, round);
        return true;
    }
    public boolean executeSaveFunction(IRound round) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) return false;
        Plugin plugin = roundData.getPlugin();
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null || pluginData.getSaveFunction() == null) return false;
        pluginData.getSaveFunction().accept(round);
        return true;
    }
    public boolean executeSpectateFunction(Player player, IRound round) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) return false;
        Plugin plugin = roundData.getPlugin();
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null || pluginData.getSpectateFunction() == null) return false;
        pluginData.getSpectateFunction().accept(player, round);
        return true;
    }
    public boolean executeJoinFunction(Player player, IRound round) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) return false;
        Plugin plugin = roundData.getPlugin();
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null || pluginData.getJoinFunction() == null) return false;
        pluginData.getJoinFunction().accept(player, round);
        return true;
    }
    public boolean executeLeaveFunction(Player player, IRound round) {
        RoundData roundData = roundDataMap.get(round);
        if (roundData == null) return false;
        Plugin plugin = roundData.getPlugin();
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null || pluginData.getLeaveFunction() == null) return false;
        pluginData.getLeaveFunction().accept(player, round);
        return true;
    }

    public boolean registerRound(IRound round, Material displayItem, Plugin plugin) {
        PluginData pluginData = pluginDataMap.get(plugin);
        if (pluginData == null) {
            return false;
        }
        RoundData roundData = new RoundData(round, displayItem, plugin);
        roundDataMap.put(round, roundData);
        pluginData.addRound(round);
        return true;
    }

    public @Nullable RoundData getActiveRoundDataForPlayer(Player player) {
        return playerRoundMap.get(player);
    }

    public List<PluginData> getAllPluginData() {
        return List.copyOf(pluginDataMap.values());
    }
}
