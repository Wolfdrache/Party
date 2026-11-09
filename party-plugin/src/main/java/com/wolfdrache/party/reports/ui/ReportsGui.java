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

import com.wolfdrache.helper.Helper;
import com.wolfdrache.party.general.ItemHelper;
import com.wolfdrache.party.general.NavItems;
import com.wolfdrache.party.reports.PlayerReports;
import com.wolfdrache.party.reports.ReportManager;

public class ReportsGui implements Listener {
    private static ReportsGui instance;

    private static class Data {
        public final int page;
        public final Inventory gui;
        public final Map<Integer, OfflinePlayer> slotToPlayer;

        public Data(int page, Inventory inventory, Map<Integer, OfflinePlayer> slotToPlayer) {
            this.page = page;
            this.gui = inventory;
            this.slotToPlayer = slotToPlayer;
        }
    }

    private final ReportManager reportManager;

    private final Map<Player, Data> playerData = new HashMap<>();

    public ReportsGui(ReportManager reportManager) {
        this.reportManager = reportManager;
        instance = this;
    }

    public static void open(Player player) {
        instance.open(player, 0);
    }

    private void open(Player player, int page) {
        Map<OfflinePlayer, PlayerReports> reports = reportManager.getAllReports();
        List<PlayerReports> playerReportsList = new ArrayList<>(reports.values());
        playerReportsList.sort((a, b) -> b.receivedReportsOpen.size() - a.receivedReportsOpen.size());
        playerReportsList = playerReportsList.subList(page * 45, Math.min((page + 1) * 45, playerReportsList.size()));
        int size = getSize(playerReportsList);
        
        Inventory gui = Bukkit.createInventory(null, size, Helper.createComponent("§cReports"));
        Map<Integer, OfflinePlayer> slotToPlayer = new HashMap<>();
        for (int i = 0; i < playerReportsList.size(); i++) {
            PlayerReports playerReports = playerReportsList.get(i);
            OfflinePlayer offlinePlayer = playerReports.player;
            gui.setItem(i, ItemHelper.createReportsHead(offlinePlayer, playerReports));
            slotToPlayer.put(i, offlinePlayer);
        }

        for (int i = size - 9; i < size; i++) {
            gui.setItem(i, ItemHelper.createFillerItem(Material.GRAY_STAINED_GLASS_PANE));
        }
        gui.setItem(size - 9, (page > 0) ? NavItems.previousArrow : ItemHelper.createFillerItem(Material.WHITE_STAINED_GLASS_PANE));
        gui.setItem(size - 1, (page < getMaxPages(reports.size())) ? NavItems.nextArrow : ItemHelper.createFillerItem(Material.WHITE_STAINED_GLASS_PANE));

        player.openInventory(gui);
        playerData.put(player, new Data(page, gui, slotToPlayer));
    }

    @EventHandler 
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Data data = playerData.get(player);
        if (data == null) return;
        if (!event.getView().getTopInventory().equals(data.gui)) return;
        event.setCancelled(true);
        if (!event.getInventory().equals(data.gui)) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        else if (clickedItem.equals(NavItems.previousArrow)) { open(player, data.page - 1); return; }
        else if (clickedItem.equals(NavItems.nextArrow)) { open(player, data.page + 1); return; }

        int slot = event.getRawSlot();
        if (slot >= 0 && slot < 45) {
            OfflinePlayer target = data.slotToPlayer.get(slot);
            if (target != null) {
                PlayerReportsGui.open(player, target);
            }
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

    private int getSize(List<PlayerReports> playerReportsList) {
        int size = 9;
        while (size < playerReportsList.size()) {
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
