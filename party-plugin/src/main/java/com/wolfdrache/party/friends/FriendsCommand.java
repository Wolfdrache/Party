package com.wolfdrache.party.friends;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class FriendsCommand implements TabExecutor {
    private final List<String> subCommands = List.of("accept", "add", "deny", "remove", "list", "cancel");

    private final FriendsManager friendsManager;

    public FriendsCommand(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        String subCommand = args.length > 0 ? args[0] : "";
        if (!subCommands.stream().anyMatch(sub -> sub.equalsIgnoreCase(subCommand))) {
            player.sendMessage("Invalid subcommand.");
            return true;
        }

        if (subCommand.equalsIgnoreCase("add")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /friends add <player>");
                return true;
            }
            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName); 
            friendsManager.addFriend(player, target);
            return true;
        } else if (subCommand.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /friends remove <player>");
                return true;
            }
            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            friendsManager.removeFriend(player, target);
            return true;
        } else if (subCommand.equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length >= 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Usage: /friends list [page]");
                    return true;
                }
            }
            friendsManager.listFriends(player, page);
            return true;
        } else if (subCommand.equalsIgnoreCase("accept")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /friends accept <player>");
                return true;
            }
            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            friendsManager.replyFriendRequest(player, target, true);
            return true;
        } else if (subCommand.equalsIgnoreCase("deny")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /friends deny <player>");
                return true;
            }
            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            friendsManager.replyFriendRequest(player, target, false);
            return true;
        } else if (subCommand.equalsIgnoreCase("cancel")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /friends cancel <player>");
                return true;
            }
            String targetName = args[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            friendsManager.cancelFriendRequest(player, target);
            return true;
        }

        return true; // Ensure a boolean is always returned
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.stream().filter(sub -> sub.startsWith(args[0])).toList();
        } else if (args.length == 2) {
            String subCommand = args[0];
            PlayerFriends playerFriends = friendsManager.getPlayerFriends((Player) sender);
            if (subCommand.equalsIgnoreCase("add")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.startsWith(args[1]))
                        .filter(name -> !playerFriends.friends.contains(Bukkit.getOfflinePlayer(name)))
                        .filter(name -> !playerFriends.sentRequests.contains(Bukkit.getOfflinePlayer(name)))
                        .toList();
            } else if (subCommand.equalsIgnoreCase("accept") || subCommand.equalsIgnoreCase("deny")) {
                return playerFriends.incomingRequests.stream()
                        .map(OfflinePlayer::getName)
                        .filter(name -> name.startsWith(args[1]))
                        .toList();
            } else if (subCommand.equalsIgnoreCase("cancel")) {
                return playerFriends.sentRequests.stream()
                        .map(OfflinePlayer::getName)
                        .filter(name -> name.startsWith(args[1]))
                        .toList();
            } else if (subCommand.equalsIgnoreCase("remove")) {
                return playerFriends.friends.stream()
                        .map(OfflinePlayer::getName)
                        .filter(name -> name.startsWith(args[1]))
                        .toList();
            }
        }
        return List.of();
    }
}
