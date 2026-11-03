package com.wolfdrache.party.reports.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.wolfdrache.party.general.ItemHelper;
import com.wolfdrache.party.general.MessageHelper;
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

    private final Map<Player, Data> playerData = new HashMap<>();
    public PlayerReportsGui(ReportManager reportManager) {
        this.reportManager = reportManager;
        instance = this;
    }

    public static void open(Player player, OfflinePlayer target) {
        instance.open(player, target, 0);
    }

    public void open(Player player, OfflinePlayer target, int page) {
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
        int slot = event.getSlot();
        ReportModel report = data.slotToReport.get(slot);
        if (report != null) {
            MessageHelper.sendReportMessage(player, report);
            player.closeInventory();
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
        while (size <= reports.size()) {
            size += 9;
        }
        return size;
    }
}
