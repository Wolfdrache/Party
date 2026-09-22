package com.wolfdrache.party.internal.helper;

import java.time.Duration;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;

public class MessageHelper {
    
    private static final String prefix = "§9§lParty §7 >> §r";
    
    public static void sendMessage(Player player, String message) {
        player.sendMessage(prefix + message);
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
}
