package com.wolfdrache.party.internal.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.internal.helper.MessageHelper;
import com.wolfdrache.party.internal.manager.PartyManager;

public class PartyCommand implements TabExecutor {
    private final PartyManager partyManager;
    
    private final List<String> subCommands = List.of(
        "invite",
        "kick",
        "leave",
        "disband",
        "accept",
        "list",
        "decline"
    );
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
            MessageHelper.sendMessage(player, "§cNutzung: /party <subcommand>");
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (!subCommands.contains(subCommand)) {
            MessageHelper.sendMessage(player, "§cUnbekannter Unterbefehl: " + subCommand);
            return true;
        }

        switch (subCommand) {
            case "invite" -> {
                if (args.length < 2) {
                    MessageHelper.sendMessage(player, "§cNutzung: /party invite <Spieler>");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    MessageHelper.sendMessage(player, "§cSpieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.invitePlayer(player, target);
            }
            case "kick" -> {
                if (args.length < 2) {
                    MessageHelper.sendMessage(player, "§cNutzung: /party kick <Spieler>");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    MessageHelper.sendMessage(player, "§cSpieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.kickPlayer(player, target);
            }
            case "leave" -> {
                partyManager.leaveParty(player);
            }
            case "disband" -> {
                partyManager.disbandParty(player);
            }
            case "accept" -> {
                if (args.length < 2) {
                    MessageHelper.sendMessage(player, "§cNutzung: /party accept <Spieler>");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    MessageHelper.sendMessage(player, "§cSpieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.acceptInvite(player, target);
            }
            case "list" -> {
                List<Player> members = partyManager.getPartyMembers(player);
                MessageHelper.sendMessage(player, "§aMitglieder der Party: ");
                for (Player member : members) {
                    MessageHelper.sendMessage(player, "§7- " + member.getName());
                }
            }
            case "decline" -> {
                if (args.length < 2) {
                    MessageHelper.sendMessage(player, "§cNutzung: /party decline <Spieler>");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    MessageHelper.sendMessage(player, "§cSpieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.declineInvite(player, target);
            }
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
