package com.wolfdrache.party.internal.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.internal.helper.MessageHelper;
import com.wolfdrache.party.internal.manager.PartyManager;

public class PartyChatCommand implements TabExecutor {
    private final PartyManager partyManager;

    public PartyChatCommand(PartyManager partyManager) {
        this.partyManager = partyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (!partyManager.isInParty(player)) {
            MessageHelper.sendMessage(player, "§cDu bist in keiner Party.");
            return true;
        }
        if (args.length > 0) {
            String message = String.join(" ", args);
            message = message.replace("&", "§");
            for (Player member : partyManager.getPartyMembers(player)) {
                MessageHelper.sendMessage(member, player.getName() + ": " + message);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
