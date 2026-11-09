package com.wolfdrache.helper;

import org.bukkit.OfflinePlayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class Helper {
    private final static LuckPerms luckPerms = LuckPermsProvider.get();

    public static String getRankPrefix(OfflinePlayer player) {
        return luckPerms.getPlayerAdapter(OfflinePlayer.class).getUser(player)
                .getCachedData().getMetaData().getPrefix().replace("&", "§");
    }

    public static String getRankSuffix(OfflinePlayer player) {
        return luckPerms.getPlayerAdapter(OfflinePlayer.class).getUser(player)
                .getCachedData().getMetaData().getSuffix().replace("&", "§");
    }

    public static String getRankDisplayName(OfflinePlayer player) {
        String playerGroup = luckPerms.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        String rankName = luckPerms.getGroupManager().getGroup(playerGroup).getDisplayName();
        return rankName != null ? rankName.replace("&", "§") : playerGroup;
    }

    public static String getPlayerRankName(OfflinePlayer player) {
        String prefix = getRankPrefix(player);
        String suffix = getRankSuffix(player);
        return (prefix != null ? prefix : "") + player.getName() + (suffix != null ? suffix : "");
    }

    public static Component createComponent(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }
}
