
package com.wolfdrache.helper;

import org.bukkit.OfflinePlayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.group.Group;

public class Helper {

    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    private static User getUser(OfflinePlayer player) {
        return luckPerms.getUserManager().getUser(player.getUniqueId());
    }

    public static String getRankPrefix(OfflinePlayer player) {
        User user = getUser(player);

        if (user == null) return "";

        String prefix = user.getCachedData()
                .getMetaData()
                .getPrefix();

        return prefix != null ? prefix.replace("&", "§") : "";
    }

    public static String getRankSuffix(OfflinePlayer player) {
        User user = getUser(player);

        if (user == null) return "";

        String suffix = user.getCachedData()
                .getMetaData()
                .getSuffix();

        return suffix != null ? suffix.replace("&", "§") : "";
    }

    public static String getRankDisplayName(OfflinePlayer player) {
        User user = getUser(player);

        if (user == null) return "";

        String playerGroup = user.getPrimaryGroup();

        Group group = luckPerms.getGroupManager().getGroup(playerGroup);

        if (group == null) return playerGroup;

        String rankName = group.getDisplayName();

        return rankName != null
                ? rankName.replace("&", "§")
                : playerGroup;
    }

    public static String getPlayerRankName(OfflinePlayer player) {
        String prefix = getRankPrefix(player);
        String suffix = getRankSuffix(player);

        String name = player.getName() != null
                ? player.getName()
                : "Unknown";

        return prefix + name + suffix;
    }

    public static Component createComponent(String text) {
        return LegacyComponentSerializer.legacySection()
                .deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }
}
