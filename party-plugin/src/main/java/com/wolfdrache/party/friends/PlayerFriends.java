package com.wolfdrache.party.friends;

import java.util.Set;

import org.bukkit.OfflinePlayer;

public class PlayerFriends {
    public final Set<OfflinePlayer> friends;
    public final Set<OfflinePlayer> pendingRequests;
    public final Set<OfflinePlayer> sentRequests; 

    public PlayerFriends(Set<OfflinePlayer> friends, Set<OfflinePlayer> pendingRequests, Set<OfflinePlayer> sentRequests) {
        this.friends = friends;
        this.pendingRequests = pendingRequests;
        this.sentRequests = sentRequests;
    }
}
