package com.wolfdrache.party.friends;

import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class FriendsCommand implements TabExecutor {
    private final Set<String> subCommands = Set.of("accept", "add", "deny", "remove", "list");

    private final FriendsManager friendsManager;

    public FriendsCommand(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
