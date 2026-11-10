package com.wolfdrache.party.general;

import java.time.Duration;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.helper.Helper;
import com.wolfdrache.serverapi.reports.ReportModel;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;

public class MessageHelper {
    public enum Type {
        PARTY("§9§lParty §7» §r"),
        FRIEND("§a§lFreunde §7» §r"),
        REPORT("§4§lReports §7» §r"),
        SYSTEM("§e§lSystem §7» §r"),
        MSG("§3§lMSG §7§l» §r");

        public final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }
    }
    
    public static void sendMessage(Player player, Type type, String message) {
        player.sendMessage(type != null ? type.prefix + message : message);
    }
    
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Component titleComponent = Helper.createComponent(title);
        Component subtitleComponent = Helper.createComponent(subtitle);
        Title titleObj = Title.title(titleComponent, subtitleComponent, Title.Times.times(Duration.ofMillis(fadeIn * 50), Duration.ofMillis(stay * 50), Duration.ofMillis(fadeOut * 50)));
        player.showTitle(titleObj);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    public static void sendPartyInviteMessage(Player sender, Player target) {
        Component message = Helper.createComponent("Du wurdest von " + Helper.getPlayerRankName(sender) + " zu einer Party eingeladen. ")
            .append(
                Component.text("[Annehmen]")
                    .color(NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.runCommand("/party accept " + sender.getName()))
            )
            .append(Component.text(" "))
            .append(
                Component.text("[Ablehnen]")
                    .color(NamedTextColor.RED)
                    .clickEvent(ClickEvent.runCommand("/party decline " + sender.getName()))
            );
        Component finalMessage = Helper.createComponent(Type.PARTY.prefix).append(message);
        target.sendMessage(finalMessage);
    }

    public static void sendFriendRequestMessage(OfflinePlayer sender, Player target) {
        Component message = Helper.createComponent("Du hast eine Freundschaftsanfrage von " + Helper.getPlayerRankName(sender) + " erhalten. ")
            .append(
                Component.text("[Annehmen]")
                    .color(NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.runCommand("/friends accept " + sender.getName()))
            )
            .append(Component.text(" "))
            .append(
                Component.text("[Ablehnen]")
                    .color(NamedTextColor.RED)
                    .clickEvent(ClickEvent.runCommand("/friends deny " + sender.getName()))
            );
        Component finalMessage = Helper.createComponent(Type.FRIEND.prefix).append(message);
        target.sendMessage(finalMessage);
    }

    public static void sendReportMessage(Player player, ReportModel report) {
        Component message = Helper.createComponent(Type.REPORT.prefix + 
            "\n§8Kategorie: §7" + report.category + " §8Grund: §7" + report.reason + 
            (report.message != null && !report.message.isEmpty() ? "\n§8Nachricht: §7" + report.message : "") + 
            "\n§8Gemeldet von: §7" + Helper.getPlayerRankName(report.reporter) + " §8Zeitpunkt: §7" + report.timestamp);

        if (report.operator != null) {
            message = message.append(Helper.createComponent(
                "\n§8Operator: §a" + Helper.getPlayerRankName(report.operator) + " §8Bearbeitungszeit: §a" + report.handledTimestamp +
                "\n§8Fazit: §a" + report.conclusion));
        } else if (player.hasPermission("reports.manage")) {
            message = message.append(Helper.createComponent("\n§8[§4Bann§8]")
                .clickEvent(ClickEvent.runCommand("/reportsteam closeId " + report.getId() + " Bann"))).append(Helper.createComponent(" "));
            message = message.append(Helper.createComponent("§8[§eMute§8]")
                .clickEvent(ClickEvent.runCommand("/reportsteam closeId " + report.getId() + " Mute"))).append(Helper.createComponent(" "));
            message = message.append(Helper.createComponent("§8[§aInkorrekt§8]")
                .clickEvent(ClickEvent.runCommand("/reportsteam closeId " + report.getId() + " Inkorrekter Report")));
        }

        if (player.hasPermission("reports.delete")) {
            message = message.append(Helper.createComponent("\n§8[§cLöschen§8]"))
                .clickEvent(ClickEvent.runCommand("/reportsteam delete " + report.getId()));
        }
        
        player.sendMessage(message);
    }

    public static void sendMsgMessage(Player player, Player target, String message) {
        String completeMessage = Helper.getRankColor(player) + player.getName() + " §7➜ " + Helper.getRankColor(target) + target.getName() + "§8: §r" + message.replace("&", "§");
        sendMessage(player, Type.MSG, completeMessage);
        sendMessage(target, Type.MSG, completeMessage);
    }
}
