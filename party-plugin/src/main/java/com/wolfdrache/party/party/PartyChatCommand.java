package com.wolfdrache.party.party;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.MessageHelper;

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
            MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cDu bist in keiner Party.");
            return true;
        }
        if (args.length > 0) {
            String message = String.join(" ", args);
            message = message.replace("&", "§");
            for (Player member : partyManager.getPartyMembers(player)) {
                MessageHelper.sendMessage(member, MessageHelper.Type.PARTY, player.getName() + ": " + message);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
