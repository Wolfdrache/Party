package com.wolfdrache.party.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.general.MessageHelper;

public class FriendsManager {
    private final FileManager fileManager;

    private final Map<OfflinePlayer, PlayerFriends> playerFriendsCache = new HashMap<>();

    public FriendsManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void loadPlayerInCache(Player player) {
        playerFriendsCache.put(player, getPlayerFriends(player));
    }

    public PlayerFriends getPlayerFriends(OfflinePlayer player) {
        PlayerFriends playerFriends;
        if (playerFriendsCache.containsKey(player)) {
            playerFriends = playerFriendsCache.get(player);
        } else {
            playerFriends = fileManager.getPlayerFriends(player);
        }
        if (player instanceof Player bukkitPlayer) {
            playerFriends.unlimitedPermission = bukkitPlayer.hasPermission("social.friends.unlimited");
        }
        return playerFriends;
    }

    public void savePlayerFriends(OfflinePlayer player, boolean removeFromCache) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        savePlayerFriends(player, playerFriends, removeFromCache);
    }

    public void savePlayerFriends(OfflinePlayer player, PlayerFriends playerFriends, boolean removeFromCache) {
        fileManager.savePlayerFriends(player, playerFriends);
        if (removeFromCache) {
            playerFriendsCache.remove(player);
        }
    }

    public List<OfflinePlayer> getFriends(OfflinePlayer player) {
        List<OfflinePlayer> friends = new ArrayList<>(getPlayerFriends(player).friends);
        friends.sort((p1, p2) -> Boolean.compare(p2.isOnline(), p1.isOnline()));
        return friends;
    }

    public List<Player> getOnlineFriends(OfflinePlayer player) {
        List<OfflinePlayer> friends = getFriends(player);
        List<Player> onlineFriends = friends.stream()
            .filter(OfflinePlayer::isOnline)
            .map(friend -> (Player) friend)
            .toList();
        return onlineFriends;
    }

    public void addFriend(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (playerFriends.friends.contains(target)) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu bist bereits mit §7" + target.getName() + "§c befreundet.");
            return;
        } else if (playerFriends.sentRequests.contains(target)) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu hast bereits eine Freundschaftsanfrage an §7" + target.getName() + "§c gesendet.");
            return;
        } else if (playerFriends.incomingRequests.contains(target)) {
            replyFriendRequest(player, target, true);
            return;
        }
        if (!playerFriends.unlimitedPermission && playerFriends.friends.size() >= fileManager.getFriendsConfig(FileManager.FriendsConfig.maxFriends)) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + target.getName() + "§c kann die Freundschaftsanfrage nicht annehmen, da du die maximale Anzahl an Freunden erreicht hast.");
        }
        playerFriends.sentRequests.add(target);
        targetFriends.incomingRequests.add(player);
        if (target.isOnline()) {
            MessageHelper.sendFriendRequestMessage(player, (Player) target);
        } else {
            targetFriends.newFriendRequests.add(player);
        }
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§aDu hast eine Freundschaftsanfrage an §7" + target.getName() + "§a gesendet.");
    }

    public void removeFriend(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (!playerFriends.friends.contains(target)) {
            return;
        }
        playerFriends.friends.remove(target);
        targetFriends.friends.remove(player);
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        if (target.isOnline()) {
            MessageHelper.sendMessage((Player) target, MessageHelper.Type.FRIEND, player.getName() + " §chat dich aus der Freundesliste entfernt.");
        }
    }

    public void listFriends(Player player, int page) {
        List<OfflinePlayer> friends = getFriends(player);
        int maxFriendsPerPage = fileManager.getFriendsConfig(FileManager.FriendsConfig.maxFriendsPerPage);
        int totalPages = (int) Math.ceil((double) friends.size() / maxFriendsPerPage);
        if (page < 1 || page > totalPages) {
            page = 1;
        }
        int start = (page - 1) * maxFriendsPerPage;
        int end = Math.min(start + maxFriendsPerPage, friends.size());
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§aDeine Freunde §7(Seite " + page + "/" + totalPages + "):");
        for (int i = start; i < end; i++) {
            OfflinePlayer friend = friends.get(i);
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, (friend.isOnline() ? "§a✔" : "§c✖") + "§7 " + friend.getName());
        }
    }

    public void replyFriendRequest(Player player, OfflinePlayer target, boolean accept) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (!playerFriends.incomingRequests.contains(target)) {
            return;
        }
        if (accept) {
            int maxFriends = fileManager.getFriendsConfig(FileManager.FriendsConfig.maxFriends);
            if (!playerFriends.unlimitedPermission && playerFriends.friends.size() >= maxFriends) {
                MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu hast die maximale Anzahl an Freunden erreicht.");
                return;
            } else if (!targetFriends.unlimitedPermission && targetFriends.friends.size() >= maxFriends) {
                MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + target.getName() + "§c hat die maximale Anzahl an Freunden erreicht.");
                return;
            }
            playerFriends.friends.add(target);
            targetFriends.friends.add(player);
        }
        playerFriends.incomingRequests.remove(target);
        targetFriends.sentRequests.remove(player);
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        if (target.isOnline()) {
            MessageHelper.sendMessage((Player) target, MessageHelper.Type.FRIEND, player.getName() + (accept ? "§a hat deine Freundschaftsanfrage akzeptiert." : "§c hat deine Freundschaftsanfrage abgelehnt."));
        }
    }

    public void cancelFriendRequest(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (!playerFriends.sentRequests.contains(target)) {
            return;
        }
        playerFriends.sentRequests.remove(target);
        targetFriends.incomingRequests.remove(player);
        if (target.isOnline()) {
            MessageHelper.sendMessage((Player) target, MessageHelper.Type.FRIEND, player.getName() + "§c hat seine Freundschaftsanfrage zurückgezogen.");
        } else {
            targetFriends.newFriendRequests.remove(player);
        }
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
    }

    public List<OfflinePlayer> getNewFriendRequests(Player player) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        List<OfflinePlayer> newFriendRequests = new ArrayList<>(playerFriends.newFriendRequests);
        return newFriendRequests;
    }
}
