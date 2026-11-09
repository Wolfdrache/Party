package com.wolfdrache.friends;

import org.bukkit.OfflinePlayer;

public class FriendData {
    public final OfflinePlayer friend;
    public final String timeStamp;

    public FriendData(OfflinePlayer friend, String timeStamp) {
        this.friend = friend;
        this.timeStamp = timeStamp;
    }
}
