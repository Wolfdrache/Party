package com.wolfdrache.party.reports;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.MessageHelper;

public class ReportCommand implements TabExecutor {
    private final ReportManager reportManager;

    public ReportCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }
    
    @Override 
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length < 3) {
            player.sendMessage("Usage: /report <player> <category> <reason> <message>");
            return true;
        }
        String target = args[0];
        String category = args[1];
        String reason = args[2];

        ReportCategory reportCategory = reportManager.getReportCategory(category);
        if (reportCategory == null) {
            player.sendMessage("Invalid report category.");
            return true;
        }

        if (!reportCategory.reasons.contains(reason.toLowerCase())) {
            player.sendMessage("Invalid reason for this category.");
            return true;
        }

        String message = "";
        if (args.length > 3) {
            for (int i = 3; i < args.length; i++) {
                message += args[i] + " ";
            }
            message = message.trim();
        }

        OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(target);
        reportManager.createReport(reportedPlayer, player, category, reason, message);
        
        MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "§8Dein Report an §c" + reportedPlayer.getName() + " §8wurde erstellt.");
        MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "§cAusnutzen von Reports ist verboten.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        } else if (args.length == 2) {
            return reportManager.getReportCategoryNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        } else if (args.length == 3) {
            ReportCategory reportCategory = reportManager.getReportCategory(args[1]);
            if (reportCategory != null) {
                return reportCategory.reasons.stream()
                        .filter(reason -> reason.toLowerCase().startsWith(args[2].toLowerCase()))
                        .toList();
            }
        }
        return List.of();
    }
}
