package com.wolfdrache.party.service;

import java.util.Set;

import org.bukkit.OfflinePlayer;

import com.wolfdrache.friends.FriendsService;

public class FriendsServiceImpl implements FriendsService {

    @Override 
    public Set<OfflinePlayer> getFriends(OfflinePlayer player) {
        return null;
    }

    @Override 
    public Set<OfflinePlayer> getFriendRequests(OfflinePlayer player) {
        return null;
    }

    @Override 
    public Set<OfflinePlayer> getSentFriendRequests(OfflinePlayer player) {
        return null;
    }

    @Override 
    public void cancelFriendRequest(OfflinePlayer player, OfflinePlayer target) {
        
    }
    
    @Override 
    public void replayFriendRequest(OfflinePlayer player, OfflinePlayer requester, boolean accept) {
        
    }
    
    @Override 
    public void sendFriendRequest(OfflinePlayer player, OfflinePlayer target) {
        
    }
    
    @Override 
    public void removeFriend(OfflinePlayer player, OfflinePlayer friend) {
        
    }
}
