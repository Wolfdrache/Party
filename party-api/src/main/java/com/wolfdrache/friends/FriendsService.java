package com.wolfdrache.friends;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface FriendsService {
    List<FriendData> getFriends(OfflinePlayer player);
    List<FriendData> getFriendRequests(OfflinePlayer player);
    List<FriendData> getSentFriendRequests(OfflinePlayer player);
    void cancelFriendRequest(Player player, OfflinePlayer target);
    void replyFriendRequest(Player player, OfflinePlayer requester, boolean accept);
    void sendFriendRequest(Player player, OfflinePlayer target);
    void removeFriend(Player player, OfflinePlayer friend);
}
