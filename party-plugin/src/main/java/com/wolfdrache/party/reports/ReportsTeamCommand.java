package com.wolfdrache.party.reports;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.serverapi.reports.ReportModel;

public class ReportsTeamCommand implements TabExecutor {
    private final ReportManager reportManager;

    private final List<String> subCommands = List.of("close", "list", "listplayer");

    public ReportsTeamCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override 
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage("Invalid subcommand. Available subcommands: " + String.join(", ", subCommands));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "close" -> {
                if (args.length < 2) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Usage: /" + label + " close <player>");
                    return true;
                }
                
                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (target == null) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Player not found or has no reports.");
                    return true;
                }
                String message = args.length >= 3 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "";
                reportManager.closeReports(target, player, message);
            }
            case "list" -> {
                Map<OfflinePlayer, PlayerReports> reports = reportManager.getAllReports();
                if (reports.isEmpty()) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "No reports found.");
                } else {
                    for (Map.Entry<OfflinePlayer, PlayerReports> entry : reports.entrySet()) {
                        OfflinePlayer reported = entry.getKey();
                        PlayerReports playerReports = entry.getValue();
                        if (!playerReports.receivedReportsOpen.isEmpty()) {
                            MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Open reports for " + reported.getName() + ": " + playerReports.receivedReportsOpen.size());
                        }
                    }
                }
            }
            case "listplayer" -> {
                if (args.length < 2) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Usage: /" + label + " listplayer <player>");
                    return true;
                }
                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                if (target == null) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Player not found or has no reports.");
                    return true;
                }
                PlayerReports playerReports = reportManager.getAllReports().get(target);
                if (playerReports == null) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Player not found or has no reports.");
                    return true;
                }

                if (playerReports.receivedReportsOpen.isEmpty() && playerReports.receivedReportsClosed.isEmpty()) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Player has no reports.");
                    return true;
                }

                if (!playerReports.receivedReportsOpen.isEmpty()) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "§cOffene Reports an §7" + target.getName() + "§8: §c" + playerReports.receivedReportsOpen.size());
                }
                for (ReportModel report : playerReports.receivedReportsOpen) {
                    MessageHelper.sendReportMessage(player, report);
                }

                if (!playerReports.receivedReportsClosed.isEmpty()) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "§aGeschlossene Reports an §7" + target.getName() + "§8: §a" + playerReports.receivedReportsClosed.size());
                }
                for (ReportModel report : playerReports.receivedReportsClosed) {
                    MessageHelper.sendReportMessage(player, report);
                }
            }
            case "closeid" -> {
                if (args.length < 2) {
                    return true;
                }
                int reportId = Integer.parseInt(args[1]);
                ReportModel report = reportManager.getReportById(reportId);
                if (report == null) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Report not found.");
                    return true;
                }
                String message = args.length >= 3 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "";
                reportManager.closeReport(report, player, message);
            }
            case "delete" -> {
                if (!player.hasPermission("reports.delete")) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "You do not have permission to delete reports.");
                    return true;
                }
                if (args.length < 2) {
                    return true;
                }
                int reportId = Integer.parseInt(args[1]);
                ReportModel report = reportManager.getReportById(reportId);
                if (report == null) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.REPORT, "Report not found.");
                    return true;
                }
                reportManager.deleteReport(report);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.stream()
                    .filter(sub -> sub.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        } else if (args.length == 2 && args[0].equalsIgnoreCase("close")) {
            return reportManager.getAllReports().keySet().stream()
                    .map(OfflinePlayer::getName)
                    .toList();
        } else if (args.length == 2 && args[0].equalsIgnoreCase("listplayer")) {
            return reportManager.getAllReports().keySet().stream()
                    .map(OfflinePlayer::getName)
                    .toList();
        }
        return List.of();
    }
}
