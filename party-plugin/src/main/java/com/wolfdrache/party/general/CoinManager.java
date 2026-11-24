package com.wolfdrache.party.general;

import com.wolfdrache.serverapi.coins.CoinUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.Economy;

public class CoinManager {
    private final Economy economy;
    private final FileManager fileManager;

    public CoinManager(Economy economy, FileManager fileManager) {
        this.economy = economy;
        this.fileManager = fileManager;
    }
    
    public boolean depositCoins(OfflinePlayer player, double amount) {
        CoinUpdateEvent event = new CoinUpdateEvent(player, (int) amount, CoinUpdateEvent.UpdateType.DEPOSIT);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        economy.depositPlayer(player, amount);
        return true;
    }

    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    public int getCoins(OfflinePlayer player) {
        return (int) economy.getBalance(player);
    }

    public int getAllCoins() {
        int totalBalance = 0;
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
            totalBalance += getCoins(player);
        }
        return totalBalance;
    }

    public double getAllCoinsDouble() {
        double totalBalance = 0;
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
            totalBalance += getBalance(player);
        }
        return totalBalance;
    }

    public double getPercentage(OfflinePlayer player) {
        int coins = getCoins(player);
        int allCoins = getAllCoins();
        if (allCoins == 0) return 0;
        return Math.round((double) coins / allCoins * 10000.0) / 100.0;
    }

    public double getAllTimePercentage(OfflinePlayer player) {
        int allTimeCoins = getAllTimeCoins(player);
        int allTimeCoinsFromAllPlayers = getAllTimeCoinsFromAllPlayers();
        if (allTimeCoinsFromAllPlayers == 0) return 0;
        return Math.round((double) allTimeCoins / allTimeCoinsFromAllPlayers * 10000.0) / 100.0;
    }

    public int getAllTimeCoinsFromAllPlayers() {
        int totalAllTimeCoins = 0;
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
            totalAllTimeCoins += getAllTimeCoins(player);
        }
        return totalAllTimeCoins;
    }

    public int getAllTimeCoins(OfflinePlayer player) {
        return getCoins(player) + getSpentCoins(player);
    }

    public int getSpentCoins(OfflinePlayer player) {
        return fileManager.getSpentCoins(player);
    }

    public boolean hasEnoughCoins(OfflinePlayer player, int amount) {
        return getCoins(player) >= amount;
    }

    public boolean spentCoins(OfflinePlayer player, int amount) {
        if (hasEnoughCoins(player, amount)) {
            CoinUpdateEvent event = new CoinUpdateEvent(player, amount, CoinUpdateEvent.UpdateType.SPEND);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
            economy.withdrawPlayer(player, amount);
            int spentCoins = getSpentCoins(player);
            fileManager.setSpentCoins(player, spentCoins + amount);
            return true;
        }
        return false;
    }
}
