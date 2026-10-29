package com.wolfdrache.party.general;

import java.time.Duration;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.serverapi.reports.ReportModel;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;

public class MessageHelper {
    public enum Type {
        PARTY("§9§lParty §7 >> §r"),
        FRIEND("§a§lFreunde §7 >> §r"),
        REPORT("§4§lReports §7 >> §r");

        public final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }
    }
    
    public static void sendMessage(Player player, Type type, String message) {
        player.sendMessage(type.prefix + message);
    }
    
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Component titleComponent = createComponent(title);
        Component subtitleComponent = createComponent(subtitle);
        Title titleObj = Title.title(titleComponent, subtitleComponent, Title.Times.times(Duration.ofMillis(fadeIn * 50), Duration.ofMillis(stay * 50), Duration.ofMillis(fadeOut * 50)));
        player.showTitle(titleObj);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    public static Component createComponent(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }

    public static void sendPartyInviteMessage(Player sender, Player target) {
        Component message = createComponent("Du wurdest von " + sender.getName() + " zu einer Party eingeladen. ")
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
        Component finalMessage = createComponent(Type.PARTY.prefix).append(message);
        target.sendMessage(finalMessage);
    }

    public static void sendFriendRequestMessage(OfflinePlayer sender, Player target) {
        Component message = createComponent("Du hast eine Freundschaftsanfrage von " + sender.getName() + " erhalten. ")
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
        Component finalMessage = createComponent(Type.FRIEND.prefix).append(message);
        target.sendMessage(finalMessage);
    }

    public static void sendReportMessage(Player player, ReportModel report) {
        Component message = createComponent(Type.REPORT.prefix + "§8Kategorie: §7" + report.category + 
            "\n§8Grund: §7" + report.reason + 
            "\n§8Nachricht: §7" + report.message + 
            "\n§8Gemeldet von: §7" + report.reporter.getName() + 
            "\n§8Zeitpunkt: §7" + report.timestamp);

        if (report.operator != null) {
            message = message.append(createComponent("\n§8Operator: §a" + report.operator.getName() +
                "\n§8Bearbeitungszeit: §a" + report.handledTimestamp +
                "\n§8Fazit: §a" + report.conclusion));
        } else if (player.hasPermission("reports.manage")) {
            message = message.append(createComponent("§8[§4Bann§8]"))
                .clickEvent(ClickEvent.runCommand("/reportsteam closeId " + report.getId() + " Bann")).append(createComponent("   "));
            message = message.append(createComponent("§8[§eMute§8]"))
                .clickEvent(ClickEvent.runCommand("/reportsteam closeId " + report.getId() + " Mute")).append(createComponent("   "));
            message = message.append(createComponent("§8[§aInkorrekt§8]"))
                .clickEvent(ClickEvent.runCommand("/reportsteam closeId " + report.getId() + " Inkorrekter Report"));
        }

        if (player.hasPermission("reports.delete")) {
            message = message.append(createComponent("\n§8[§cLöschen§8]"))
                .clickEvent(ClickEvent.runCommand("/reportsteam delete " + report.getId()));
        }
        
        player.sendMessage(message);
    }
}
