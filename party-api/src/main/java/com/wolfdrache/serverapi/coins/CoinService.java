package com.wolfdrache.serverapi.coins;

import org.bukkit.OfflinePlayer;

public interface CoinService {
    int getCoins(OfflinePlayer player);
    double getCoinsDouble(OfflinePlayer player);
    double getPercentage(OfflinePlayer player);

    int getAllTimeCoins(OfflinePlayer player);
    double getAllTimeCoinsDouble(OfflinePlayer player);
    double getAllTimePercentage(OfflinePlayer player);

    int getCoinsFromAllPlayers();
    double getCoinsDoubleFromAllPlayers();

    int getAllTimeCoinsFromAllPlayers();

    int getSpentCoins(OfflinePlayer player);
    double getSpentCoinsDouble(OfflinePlayer player);

    boolean giveCoins(OfflinePlayer player, int amount);
    boolean spentCoins(OfflinePlayer player, int amount);
    boolean hasEnoughCoins(OfflinePlayer player, int amount);

    boolean loseCoins(OfflinePlayer player, int amount);
}
