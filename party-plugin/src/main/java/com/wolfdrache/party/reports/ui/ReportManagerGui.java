package com.wolfdrache.party.reports.ui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.wolfdrache.party.general.ItemHelper;
import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.party.general.NavItems;
import com.wolfdrache.party.reports.ReportManager;
import com.wolfdrache.serverapi.reports.ReportModel;

public class ReportManagerGui implements Listener {
    private static class Data {
        public final Inventory gui;
        public final ReportModel report;

        public Data(Inventory gui, ReportModel report) {
            this.gui = gui;
            this.report = report;
        }
    }

    private final ReportManager reportManager;

    private final Map<Player, Data> playerData = new HashMap<>();
    public ReportManagerGui(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    public void open(Player player, ReportModel report) {
        Inventory gui = Bukkit.createInventory(null, 27, MessageHelper.createComponent("§cReport"));
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, ItemHelper.createFillerItem(Material.GRAY_STAINED_GLASS_PANE));
        }
        boolean done = report.operator != null;
        gui.setItem(4, ItemHelper.createReportManagerHead(report));
        gui.setItem(10, done ? NavItems.disabledItem : NavItems.innocentReportItem);
        gui.setItem(11, done ? NavItems.disabledItem : NavItems.warningReportItem);
        gui.setItem(19, done ? NavItems.disabledItem : NavItems.muteReportItem);
        gui.setItem(20, done ? NavItems.disabledItem : NavItems.banReportItem);
        gui.setItem(13, player.hasPermission("reports.delete") ? NavItems.deleteReportItem : NavItems.disabledItem);
        gui.setItem(15, NavItems.returnItem);
        gui.setItem(16, done ? NavItems.disabledItem : NavItems.customReportItem);
        player.openInventory(gui);
        playerData.put(player, new Data(gui, report));
    }

    @EventHandler 
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Data data = playerData.get(player);
        if (data == null) return;
        if (!event.getView().getTopInventory().equals(data.gui)) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        if (clickedItem.equals(NavItems.returnItem)) {
            PlayerReportsGui.open(player, data.report.reportedPlayer);
            return;
        } else if (clickedItem.equals(NavItems.deleteReportItem)) {
            reportManager.deleteReport(data.report);
            player.closeInventory();
            return;
        } else if (clickedItem.equals(NavItems.innocentReportItem)) {
            reportManager.closeReport(data.report, player, "Inkorrekt");
            player.closeInventory();
            return;
        } else if (clickedItem.equals(NavItems.warningReportItem)) {
            reportManager.closeReport(data.report, player, "Warnung");
            player.closeInventory();
            return;
        } else if (clickedItem.equals(NavItems.muteReportItem)) {
            reportManager.closeReport(data.report, player, "Mute");
            player.closeInventory();
            return;
        } else if (clickedItem.equals(NavItems.banReportItem)) {
            reportManager.closeReport(data.report, player, "Bann");
            player.closeInventory();
            return;
        } else if (clickedItem.equals(NavItems.customReportItem)) {
            // Handle custom report action here
            return;
        }
    }

    @EventHandler 
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Data data = playerData.get(player);
        if (data == null) return;
        if (event.getView().getTopInventory().equals(data.gui)) {
            playerData.remove(player);
        }
    }
}
