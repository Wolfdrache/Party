package com.wolfdrache.party.service;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.friends.FriendsService;
import com.wolfdrache.party.friends.FriendsManager;

public class FriendsServiceImpl implements FriendsService {
    private final FriendsManager friendsManager;

    public FriendsServiceImpl(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @Override 
    public List<OfflinePlayer> getFriends(OfflinePlayer player) {
        return friendsManager.getFriends(player);
    }

    @Override 
    public List<OfflinePlayer> getFriendRequests(OfflinePlayer player) {
        return friendsManager.getPlayerFriends(player).incomingRequests.stream().toList();
    }

    @Override 
    public List<OfflinePlayer> getSentFriendRequests(OfflinePlayer player) {
        return friendsManager.getPlayerFriends(player).sentRequests.stream().toList();
    }

    @Override 
    public void cancelFriendRequest(Player player, OfflinePlayer target) {
        friendsManager.cancelFriendRequest(player, target);
    }
    
    @Override 
    public void replyFriendRequest(Player player, OfflinePlayer requester, boolean accept) {
        friendsManager.replyFriendRequest(player, requester, accept);
    }
    
    @Override 
    public void sendFriendRequest(Player player, OfflinePlayer target) {
        friendsManager.addFriend(player, target);
    }
    
    @Override 
    public void removeFriend(Player player, OfflinePlayer friend) {
        friendsManager.removeFriend(player, friend);
    }
}
