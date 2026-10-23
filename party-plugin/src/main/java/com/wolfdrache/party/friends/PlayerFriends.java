package com.wolfdrache.party.friends;

import java.util.Set;

import org.bukkit.OfflinePlayer;

public class PlayerFriends {
    public boolean unlimitedPermission;
    public final Set<OfflinePlayer> friends;
    public final Set<OfflinePlayer> incomingRequests;
    public final Set<OfflinePlayer> sentRequests; 

    public PlayerFriends(boolean unlimitedPermission, Set<OfflinePlayer> friends, Set<OfflinePlayer> incomingRequests, Set<OfflinePlayer> sentRequests) {
        this.unlimitedPermission = unlimitedPermission;
        this.friends = friends;
        this.incomingRequests = incomingRequests;
        this.sentRequests = sentRequests;
    }
}
