package com.wolfdrache.party.party;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.MessageHelper;

public class PartyCommand implements TabExecutor {
    private final PartyManager partyManager;
    
    private final List<String> subCommands = List.of("invite", "kick", "leave", "disband", "accept", "list", "decline");
    public PartyCommand(PartyManager partyManager) {
        this.partyManager = partyManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length == 0) {
            MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cNutzung: /party <subcommand>");
            return true;
        }

        String subCommand = args.length > 0 ? args[0] : "";
        if (!subCommands.stream().anyMatch(sub -> sub.equalsIgnoreCase(subCommand))) {
            player.sendMessage("Invalid subcommand.");
            return true;
        }
        
        if (subCommand.equalsIgnoreCase("invite")) {
            if (args.length < 2) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cNutzung: /party invite <Spieler>");
                return true;
            }
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cSpieler nicht gefunden: " + targetName);
                return true;
            }
            partyManager.invitePlayer(player, target);
        }
        else if (subCommand.equalsIgnoreCase("kick")) {
            if (args.length < 2) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cNutzung: /party kick <Spieler>");
                return true;
            }
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cSpieler nicht gefunden: " + targetName);
                return true;
            }
            partyManager.kickPlayer(player, target);
        } else if (subCommand.equalsIgnoreCase("leave")) {
            partyManager.leaveParty(player);
        } else if (subCommand.equalsIgnoreCase("disband")) {
            partyManager.disbandParty(player);
        } else if (subCommand.equalsIgnoreCase("accept")) {
            if (args.length < 2) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cNutzung: /party accept <Spieler>");
                return true;
            }
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cSpieler nicht gefunden: " + targetName);
                return true;
            }
            partyManager.acceptInvite(player, target);
        } else if (subCommand.equalsIgnoreCase("list")) {
            List<Player> members = partyManager.getPartyMembers(player);
            MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§aMitglieder der Party: ");
            for (Player member : members) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§7- " + member.getName());
            }
        } else if (subCommand.equalsIgnoreCase("decline")) {
            if (args.length < 2) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cNutzung: /party decline <Spieler>");
                return true;
            }
            String targetName = args[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                MessageHelper.sendMessage(player, MessageHelper.Type.PARTY, "§cSpieler nicht gefunden: " + targetName);
                return true;
            }
            partyManager.declineInvite(player, target);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.stream()
                .filter(subCommand -> subCommand.startsWith(args[0]))
                .toList();
        } else if (args.length == 2) {
            String subCommand = args[0];
            if ("invite".equalsIgnoreCase(subCommand)) {
                return Bukkit.getOnlinePlayers().stream()
                    .map(player -> player.getName())
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
            } else if ("kick".equalsIgnoreCase(subCommand)) {
                return partyManager.getPartyMembers((Player) sender).stream()
                    .map(player -> player.getName())
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
            } else if ("accept".equalsIgnoreCase(subCommand) || "decline".equalsIgnoreCase(subCommand)) {
                return partyManager.getPendingInvites((Player) sender).stream()
                    .map(player -> player.getName())
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
            }
        }
        return List.of();
    }
}
