package com.wolfdrache.party.general;

import java.time.Duration;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;

public class MessageHelper {
    public static enum Type {
        PARTY("§9§lParty §7 >> §r"),
        FRIEND("§a§lFriend §7 >> §r");

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
        return LegacyComponentSerializer.legacySection().deserialize(text);
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
}
