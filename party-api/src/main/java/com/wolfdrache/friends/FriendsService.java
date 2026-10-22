package com.wolfdrache.friends;

import java.util.Set;

import org.bukkit.OfflinePlayer;

public interface FriendsService {
    Set<OfflinePlayer> getFriends(OfflinePlayer player);
    Set<OfflinePlayer> getFriendRequests(OfflinePlayer player);
    Set<OfflinePlayer> getSentFriendRequests(OfflinePlayer player);
    void cancelFriendRequest(OfflinePlayer player, OfflinePlayer target);
    void replayFriendRequest(OfflinePlayer player, OfflinePlayer requester, boolean accept);
    void sendFriendRequest(OfflinePlayer player, OfflinePlayer target);
    void removeFriend(OfflinePlayer player, OfflinePlayer friend);
}
