package com.wolfdrache.party.gamemodes;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.serverapi.gamemodes.GameService.CommandType;

public class GameCommands implements TabExecutor {
    private final GameCommandManager gameCommandManager;
    private final CommandType gameCommandType;

    public GameCommands(GameCommandManager gameCommandManager, CommandType gameCommandType) {
        this.gameCommandManager = gameCommandManager;
        this.gameCommandType = gameCommandType;
    }
    
    @Override 
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }
        Plugin plugin = null;
        TabExecutor executor = null;
        if (args.length == 0) {
            if (gameCommandType.requiresPlugin) {
                MessageHelper.sendMessage(player, MessageHelper.Type.SYSTEM, "You must provide a subcommand.");
                return true;
            }
            plugin = gameCommandManager.getPluginForPlayer(player);
            if (plugin == null) {
                MessageHelper.sendMessage(player, MessageHelper.Type.SYSTEM, "You are not associated with any plugin.");
                return true;
            }
            executor = gameCommandManager.getExecutorForCommand(gameCommandType, plugin);
            if (executor != null) {
                executor.onCommand(sender, command, label, args);
            }
        } else {
            plugin = gameCommandManager.getPluginForCommand(gameCommandType, args[0]);
            if (plugin == null) {
                MessageHelper.sendMessage(player, MessageHelper.Type.SYSTEM, "Invalid plugin specified.");
                return true;
            }
            executor = gameCommandManager.getExecutorForCommand(gameCommandType, plugin);
            if (executor != null) {
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subArgs, 0, subArgs.length);
                executor.onCommand(sender, command, label, subArgs);
            }
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return gameCommandManager.getRegisteredPluginsForCommand(gameCommandType).stream()
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .toList();
        } else if (args.length >= 2) {
            Plugin plugin = gameCommandManager.getPluginForCommand(gameCommandType, args[0]);
            if (plugin != null) {
                TabExecutor executor = gameCommandManager.getExecutorForCommand(gameCommandType, plugin);
                if (executor != null) {
                    String[] subArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, subArgs, 0, subArgs.length);
                    return executor.onTabComplete(sender, command, alias, subArgs);
                }
            }
        }
        return List.of();
    }
}
