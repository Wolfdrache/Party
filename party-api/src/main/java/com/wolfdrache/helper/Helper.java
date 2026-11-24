
package com.wolfdrache.helper;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.group.Group;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public class Helper {

    private static final NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    private static User getUser(OfflinePlayer player) {
        return luckPerms.getUserManager().getUser(player.getUniqueId());
    }

    public static String getRankColor(OfflinePlayer player) {
        String color = getRankDisplayName(player);
        if (color == null || color.length() < 2) return "§7";
        return color.substring(0, 2);
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

    public static String formatNumber(int number) {
        return format.format(number);
    }

    public static ItemStack createPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
        }
        return head;
    }
    public static ItemStack createTexturedHead(String texture) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", texture));

        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createNamedItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(createComponent(displayName));
            item.setItemMeta(meta);
        }
        return item;
    }
    public static ItemStack createNamedItem(ItemStack item, String displayName) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(createComponent(displayName));
            item.setItemMeta(meta);
        }
        return item;
    }
    public static ItemStack createItemWithLore(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(createComponent(displayName));
            meta.lore(Arrays.stream(lore)
                    .map(Helper::createComponent)
                    .toList());
            item.setItemMeta(meta);
        }
        return item;
    }
    public static ItemStack createItemWithLore(ItemStack item, String displayName, String... lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(createComponent(displayName));
            meta.lore(Arrays.stream(lore)
                    .map(Helper::createComponent)
                    .toList());
            item.setItemMeta(meta);
        }
        return item;
    }
    public static ItemStack hideAttributes(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }
}
