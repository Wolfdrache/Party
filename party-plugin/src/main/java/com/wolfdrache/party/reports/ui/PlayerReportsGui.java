package com.wolfdrache.party.reports.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
import com.wolfdrache.party.reports.PlayerReports;
import com.wolfdrache.party.reports.ReportManager;
import com.wolfdrache.serverapi.reports.ReportModel;

public class PlayerReportsGui implements Listener {
    private static PlayerReportsGui instance;
    private static class Data {
        public final int page;
        public final Inventory gui;
        public final OfflinePlayer target;
        public final Map<Integer, ReportModel> slotToReport;

        public Data(int page, Inventory gui, OfflinePlayer target, Map<Integer, ReportModel> slotToReport) {
            this.page = page;
            this.gui = gui;
            this.target = target;
            this.slotToReport = slotToReport;
        }
    }

    private final ReportManager reportManager;
    private final ReportManagerGui reportManagerGui;

    private final Map<Player, Data> playerData = new HashMap<>();
    public PlayerReportsGui(ReportManager reportManager, ReportManagerGui reportManagerGui) {
        this.reportManager = reportManager;
        this.reportManagerGui = reportManagerGui;
        instance = this;
    }

    public static void open(Player player, OfflinePlayer target) {
        instance.open(player, target, 0);
    }

    private void open(Player player, OfflinePlayer target, int page) {
        PlayerReports playerReports = reportManager.getPlayerReports(target);
        List<ReportModel> reports = new ArrayList<>();
        reports.addAll(playerReports.receivedReportsOpen);
        reports.addAll(playerReports.receivedReportsClosed);
        reports = reports.subList(page * 45, Math.min((page + 1) * 45, reports.size()));

        int size = getSize(reports);
        Inventory gui = Bukkit.createInventory(null, size, MessageHelper.createComponent("§cReports an: " + target.getName()));
        Map<Integer, ReportModel> slotToReport = new HashMap<>();
        for (int i = 0; i < reports.size(); i++) {
            slotToReport.put(i, reports.get(i));
            gui.setItem(i, ItemHelper.createReportHead(reports.get(i)));
        }
        
        for (int i = size - 9; i < size; i++) {
            gui.setItem(i, ItemHelper.createFillerItem(Material.GRAY_STAINED_GLASS_PANE));
        }
        gui.setItem(size - 9, (page > 0) ? NavItems.previousArrow : ItemHelper.createFillerItem(Material.WHITE_STAINED_GLASS_PANE));
        gui.setItem(size - 5, NavItems.returnItem);
        gui.setItem(size - 1, (page < getMaxPages(reports.size())) ? NavItems.nextArrow : ItemHelper.createFillerItem(Material.WHITE_STAINED_GLASS_PANE));

        player.openInventory(gui);
        playerData.put(player, new Data(page, gui, target, slotToReport));
    }

    @EventHandler 
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!playerData.containsKey(player)) return;
        Data data = playerData.get(player);
        if (!event.getView().getTopInventory().equals(data.gui)) return;
        event.setCancelled(true);
        if (!event.getInventory().equals(data.gui)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        else if (clickedItem.equals(NavItems.previousArrow)) { open(player, data.target, data.page - 1); return; }
        else if (clickedItem.equals(NavItems.nextArrow)) { open(player, data.target, data.page + 1); return; }
        else if (clickedItem.equals(NavItems.returnItem)) { ReportsGui.open(player); return; }

        int slot = event.getSlot();
        ReportModel report = data.slotToReport.get(slot);
        if (report != null) {
            reportManagerGui.open(player, report);
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

    private int getSize(List<ReportModel> reports) {
        int size = 9;
        while (size < reports.size()) {
            size += 9;
        }
        size = size + 9;
        return size;
    }

    private int getMaxPages(int totalReports) {
        int pages = totalReports / 45;
        if (totalReports % 45 == 0) pages--;
        return pages;
    }
}
