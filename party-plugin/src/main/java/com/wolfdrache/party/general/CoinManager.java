package com.wolfdrache.party.general;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class CoinManager {
    private final Economy economy;

    public CoinManager(Economy economy) {
        this.economy = economy;
    }
    
    public void depositCoins(Player player, double amount) {
        economy.depositPlayer(player, amount);
    }

    public void withdrawCoins(Player player, double amount) {
        economy.withdrawPlayer(player, amount);
    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }
}
