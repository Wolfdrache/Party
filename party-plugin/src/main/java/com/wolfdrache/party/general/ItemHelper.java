package com.wolfdrache.party.general;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.wolfdrache.party.reports.PlayerReports;
import com.wolfdrache.serverapi.reports.ReportModel;

import net.kyori.adventure.text.Component;

public class ItemHelper {
    public static ItemStack createFillerItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageHelper.createComponent(" "));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createPlayerHead(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createReportsHead(OfflinePlayer offlinePlayer, PlayerReports playerReports) {
        ItemStack head = createPlayerHead(offlinePlayer);
        ItemMeta meta = head.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageHelper.createComponent("§c" + offlinePlayer.getName()));
            List<Component> lore = new ArrayList<>();
            lore.add(MessageHelper.createComponent("§7Offene Reports: §c" + playerReports.receivedReportsOpen.size()));
            lore.add(MessageHelper.createComponent("§7Geschlossene Reports: §e" + playerReports.receivedReportsClosed.size()));
            lore.add(MessageHelper.createComponent("§7Alle Reports: §8" + (playerReports.receivedReportsOpen.size() + playerReports.receivedReportsClosed.size())));
            lore.add(MessageHelper.createComponent("§7Gemachte Reports: §a" + playerReports.madeReports.size())); 
            meta.lore(lore);
            head.setItemMeta(meta);
        }
        return head;
    }

    public static ItemStack createReportHead(ReportModel report) {
        ItemStack head = createPlayerHead(report.reporter);
        ItemMeta meta = head.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageHelper.createComponent("§c" + report.reporter.getName()));
            List<Component> lore = new ArrayList<>();
            lore.add(MessageHelper.createComponent("§7Gemeldet: §c" + report.reportedPlayer.getName()));
            lore.add(MessageHelper.createComponent("§7Grund: §e" + report.category + ": " + report.reason));
            if (report.message != null && !report.message.isEmpty()) lore.add(MessageHelper.createComponent("§7Nachricht: §e" + report.message));
            lore.add(MessageHelper.createComponent("§7Zeitpunkt: §8" + report.timestamp));
            if (report.operator != null) {
                lore.add(MessageHelper.createComponent("§7Bearbeitet von: §a" + report.operator.getName()));
                lore.add(MessageHelper.createComponent("§7Zeitpunkt: §a" + report.handledTimestamp));
                lore.add(MessageHelper.createComponent("§7Fazit: §e" + report.conclusion));
            }
            meta.lore(lore);
            head.setItemMeta(meta);
        }
        return head;
    }

    public static ItemStack createReportManagerHead(ReportModel report) {
        ItemStack head = createPlayerHead(report.reportedPlayer);
        ItemMeta meta = head.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageHelper.createComponent("§c" + report.reportedPlayer.getName()));
            List<Component> lore = new ArrayList<>();
            lore.add(MessageHelper.createComponent("§7Gemeldet von: §c" + report.reporter.getName()));
            lore.add(MessageHelper.createComponent("§7Grund: §e" + report.category + ": " + report.reason));
            if (report.message != null && !report.message.isEmpty()) lore.add(MessageHelper.createComponent("§7Nachricht: §e" + report.message));
            lore.add(MessageHelper.createComponent("§7Zeitpunkt: §8" + report.timestamp));
            if (report.operator != null) {
                lore.add(MessageHelper.createComponent("§7Bearbeitet von: §a" + report.operator.getName()));
                lore.add(MessageHelper.createComponent("§7Zeitpunkt: §a" + report.handledTimestamp));
                lore.add(MessageHelper.createComponent("§7Fazit: §e" + report.conclusion));
            }
            meta.lore(lore);
            head.setItemMeta(meta);
        }
        return head;
    }
    
    public static ItemStack createNextArrow(boolean next) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (next) {
                meta.displayName(MessageHelper.createComponent("§aNexte Seite"));
            } else {
                meta.displayName(MessageHelper.createComponent("§aVorherige Seite"));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createNavItem (String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageHelper.createComponent(name));
            item.setItemMeta(meta);
        }
        return item;
    }
}
