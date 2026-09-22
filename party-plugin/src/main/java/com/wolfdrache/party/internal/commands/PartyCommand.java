package com.wolfdrache.party.internal.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.internal.manager.PartyManager;

public class PartyCommand implements TabExecutor {
    private final PartyManager partyManager;
    
    private final List<String> subCommands = List.of(
        "invite",
        "kick",
        "leave",
        "disband",
        "accept",
        "list"
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
            player.sendMessage("Nutzung: /party <subcommand>");
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (!subCommands.contains(subCommand)) {
            player.sendMessage("Unbekannter Unterbefehl: " + subCommand);
            return true;
        }

        switch (subCommand) {
            case "invite" -> {
                if (args.length < 2) {
                    player.sendMessage("Nutzung: /party invite <Spieler>");
                    return true;
                }
                if (partyManager.isInParty(player) && !partyManager.isPartyLeader(player)) {
                    player.sendMessage("Du musst der Party-Leader sein, um Spieler einzuladen.");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    player.sendMessage("Spieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.invitePlayer(player, target);
                player.sendMessage("Einladung an " + targetName + " gesendet.");
            }
            case "kick" -> {
                if (args.length < 2) {
                    player.sendMessage("Nutzung: /party kick <Spieler>");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    player.sendMessage("Spieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.kickPlayer(player, target);
                player.sendMessage("Spieler " + targetName + " aus der Party entfernt.");
            }
            case "leave" -> {
                partyManager.leaveParty(player);
                player.sendMessage("Du hast die Party verlassen.");
            }
            case "disband" -> {
                partyManager.disbandParty(player);
                player.sendMessage("Die Party wurde aufgelöst.");
            }
            case "accept" -> {
                if (args.length < 2) {
                    player.sendMessage("Nutzung: /party accept <Spieler>");
                    return true;
                }
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    player.sendMessage("Spieler nicht gefunden: " + targetName);
                    return true;
                }
                partyManager.acceptInvite(player, target);
                player.sendMessage("Einladung von " + targetName + " akzeptiert.");
            }
            case "list" -> {
                List<Player> members = partyManager.getPartyMembers(player);
                player.sendMessage("Mitglieder der Party: " + members.stream().map(Player::getName).toList());
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
            } else if ("accept".equalsIgnoreCase(subCommand)) {
                return partyManager.getPendingInvites((Player) sender).stream()
                    .map(player -> player.getName())
                    .filter(name -> name.startsWith(args[1]))
                    .toList();
            }
        }
        return List.of();
    }
}
