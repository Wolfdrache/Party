package com.wolfdrache.party.service;

import com.wolfdrache.party.general.CoinManager;
import com.wolfdrache.serverapi.coins.CoinService;
import org.bukkit.OfflinePlayer;

public class CoinServiceImpl implements CoinService {
    private final CoinManager coinManager;

    public CoinServiceImpl(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    @Override
    public int getCoins(OfflinePlayer player) {
        return coinManager.getCoins(player);
    }

    @Override
    public double getCoinsDouble(OfflinePlayer player) {
        return coinManager.getBalance(player);
    }

    @Override
    public double getPercentage(OfflinePlayer player) {
        return coinManager.getPercentage(player);
    }

    @Override
    public int getAllTimeCoins(OfflinePlayer player) {
        return coinManager.getAllTimeCoins(player);
    }

    @Override
    public double getAllTimeCoinsDouble(OfflinePlayer player) {
        return getCoinsDouble(player) + getSpentCoinsDouble(player);
    }

    @Override
    public double getAllTimePercentage(OfflinePlayer player) {
        return coinManager.getAllTimePercentage(player);
    }

    @Override
    public int getCoinsFromAllPlayers() {
        return coinManager.getAllCoins();
    }

    @Override
    public double getCoinsDoubleFromAllPlayers() {
        return coinManager.getAllCoinsDouble();
    }

    @Override
    public int getAllTimeCoinsFromAllPlayers() {
        return coinManager.getAllTimeCoinsFromAllPlayers();
    }

    @Override
    public int getSpentCoins(OfflinePlayer player) {
        return coinManager.getSpentCoins(player);
    }

    @Override
    public double getSpentCoinsDouble(OfflinePlayer player) {
        return getSpentCoins(player);
    }

    @Override
    public boolean giveCoins(OfflinePlayer player, int amount) {
        return coinManager.depositCoins(player, amount);
    }

    @Override
    public boolean spentCoins(OfflinePlayer player, int amount) {
        return coinManager.spentCoins(player, amount);
    }

    @Override
    public boolean hasEnoughCoins(OfflinePlayer player, int amount) {
        return coinManager.hasEnoughCoins(player, amount);
    }
}
