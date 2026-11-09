package com.wolfdrache.party.friends;

import java.util.Set;

import org.bukkit.OfflinePlayer;

import com.wolfdrache.friends.FriendData;

public class PlayerFriends {
    public boolean unlimitedPermission;
    public final Set<FriendData> friends;
    public final Set<FriendData> incomingRequests;
    public final Set<FriendData> sentRequests; 
    public final Set<OfflinePlayer> newFriendRequests;

    public PlayerFriends(boolean unlimitedPermission, Set<FriendData> friends, Set<FriendData> incomingRequests, Set<FriendData> sentRequests, Set<OfflinePlayer> newFriendRequests) {
        this.unlimitedPermission = unlimitedPermission;
        this.friends = friends;
        this.incomingRequests = incomingRequests;
        this.sentRequests = sentRequests;
        this.newFriendRequests = newFriendRequests;
    }
}
