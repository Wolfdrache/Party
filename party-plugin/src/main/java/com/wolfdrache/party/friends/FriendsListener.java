package com.wolfdrache.party.friends;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolfdrache.friends.FriendsUpdateEvent;
import com.wolfdrache.party.general.MessageHelper;

public class FriendsListener implements Listener {
    private final FriendsManager friendsManager;

    public FriendsListener(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @EventHandler 
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        friendsManager.loadPlayerInCache(player);
        List<Player> onlineFriends = friendsManager.getOnlineFriends(player);
        if (!onlineFriends.isEmpty()) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§a" + onlineFriends.size() + " deiner Freunde sind online.");
            for (Player friend : onlineFriends) {
                MessageHelper.sendMessage(friend, MessageHelper.Type.FRIEND, "§e" + player.getName() + "§a ist nun online.");
                friendsManager.fireFriendsUpdate(friend, player, FriendsUpdateEvent.FriendsUpdateType.FRIEND_ONLINE);
            }
        }
        List<OfflinePlayer> newFriendRequests = friendsManager.getNewFriendRequests(player);
        if (!newFriendRequests.isEmpty()) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§aDu hast " + newFriendRequests.size() + " neue Freundschaftsanfragen.");
            PlayerFriends playerFriends = friendsManager.getPlayerFriends(player);
            playerFriends.newFriendRequests.clear();
            friendsManager.savePlayerFriends(player, playerFriends, false);
        }
    }

    @EventHandler 
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        List<Player> onlineFriends = friendsManager.getOnlineFriends(player);
        for (Player friend : onlineFriends) {
            MessageHelper.sendMessage(friend, MessageHelper.Type.FRIEND, "§e" + player.getName() + "§c ist nun offline.");
            friendsManager.fireFriendsUpdate(friend, player, FriendsUpdateEvent.FriendsUpdateType.FRIEND_OFFLINE);
        }
        friendsManager.savePlayerFriends(player, true);
    }
}
