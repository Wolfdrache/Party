package com.wolfdrache.friends;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface FriendsService {
    List<OfflinePlayer> getFriends(OfflinePlayer player);
    List<OfflinePlayer> getFriendRequests(OfflinePlayer player);
    List<OfflinePlayer> getSentFriendRequests(OfflinePlayer player);
    void cancelFriendRequest(Player player, OfflinePlayer target);
    void replyFriendRequest(Player player, OfflinePlayer requester, boolean accept);
    void sendFriendRequest(Player player, OfflinePlayer target);
    void removeFriend(Player player, OfflinePlayer friend);
}
