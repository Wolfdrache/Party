package com.wolfdrache.party.friends;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.friends.FriendsUpdateEvent;
import com.wolfdrache.friends.FriendData;
import com.wolfdrache.helper.Helper;
import com.wolfdrache.party.general.ConfigurationManager;
import com.wolfdrache.party.general.ConfigurationManager.PlayerConfiguration;
import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.party.party.PartyManager;

public class FriendsManager {
    private final FileManager fileManager;
    private final ConfigurationManager configurationManager;
    private PartyManager partyManager;

    private final Map<OfflinePlayer, PlayerFriends> playerFriendsCache = new HashMap<>();

    public FriendsManager(FileManager fileManager, ConfigurationManager configurationManager) {
        this.fileManager = fileManager;
        this.configurationManager = configurationManager;
    }
    public void setPartyManager(PartyManager partyManager) {
        this.partyManager = partyManager;
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

    public List<FriendData> getFriends(OfflinePlayer player) {
        List<FriendData> friends = new ArrayList<>(getPlayerFriends(player).friends);
        friends.sort((f1, f2) -> Boolean.compare(f2.friend.isOnline(), f1.friend.isOnline()));
        return friends;
    }

    public List<Player> getOnlineFriends(OfflinePlayer player) {
        List<FriendData> friends = getFriends(player);
        List<Player> onlineFriends = friends.stream()
            .filter(fd -> fd.friend.isOnline())
            .map(fd -> Bukkit.getPlayer(fd.friend.getUniqueId()))
            .toList();
        return onlineFriends;
    }

    public void addFriend(Player player, OfflinePlayer target) {
        if (player.getUniqueId().equals(target.getUniqueId())) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu kannst dich nicht selbst als Freund hinzufügen.");
            return;
        }
        PlayerConfiguration targetConfig = configurationManager.getPlayerConfiguration(target);
        switch (targetConfig.friendRequestMode) {
            case NONE:
                MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + Helper.getPlayerRankName(target) + "§c akzeptiert keine Freundschaftsanfragen.");
                return;
            case COMMON_FRIEND:
                if (!haveCommonFriends(player, target)) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + Helper.getPlayerRankName(target) + "§c akzeptiert nur Freundschaftsanfragen von gemeinsamen Freunden.");
                    return;
                }
                break;
            case PARTY:
                Player targetPlayer = Bukkit.getPlayer(target.getUniqueId());
                if (targetPlayer == null) break;
                if (!partyManager.areInSameParty(player, targetPlayer)) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + Helper.getPlayerRankName(target) + "§c akzeptiert nur Freundschaftsanfragen von Party-Mitgliedern.");
                    return;
                }
                break;
            case ALL:
                break;
        }
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (playerFriends.friends.stream().map(fd -> fd.friend).toList().contains(target)) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu bist bereits mit §7" + Helper.getPlayerRankName(target) + "§c befreundet.");
            return;
        } else if (playerFriends.sentRequests.stream().map(fd -> fd.friend).toList().contains(target)) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu hast bereits eine Freundschaftsanfrage an §7" + Helper.getPlayerRankName(target) + "§c gesendet.");
            return;
        } else if (playerFriends.incomingRequests.stream().map(fd -> fd.friend).toList().contains(target)) {
            replyFriendRequest(player, target, true);
            return;
        }
        if (!playerFriends.unlimitedPermission && playerFriends.friends.size() >= fileManager.getFriendsConfig(FileManager.FriendsConfig.maxFriends)) {
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + Helper.getPlayerRankName(target) + "§c kann die Freundschaftsanfrage nicht annehmen, da du die maximale Anzahl an Freunden erreicht hast.");
        }
        String timestamp = Instant.now().atZone(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        playerFriends.sentRequests.add(new FriendData(target, timestamp));
        targetFriends.incomingRequests.add(new FriendData(player, timestamp));
        if (target.isOnline()) {
            Player targetPlayer = (Player) target;
            MessageHelper.sendFriendRequestMessage(player, targetPlayer);
            fireFriendsUpdate(targetPlayer, player, FriendsUpdateEvent.FriendsUpdateType.REQUEST_SENT);
        } else {
            targetFriends.newFriendRequests.add(player);
        }
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§aDu hast eine Freundschaftsanfrage an §7" + Helper.getPlayerRankName(target) + "§a gesendet.");
    }

    public void removeFriend(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (!playerFriends.friends.stream().map(fd -> fd.friend).toList().contains(target)) {
            return;
        }
        playerFriends.friends.removeIf(fd -> fd.friend.equals(target));
        targetFriends.friends.removeIf(fd -> fd.friend.equals(player));
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        if (target.isOnline()) {
            Player targetPlayer = (Player) target;
            MessageHelper.sendMessage(targetPlayer, MessageHelper.Type.FRIEND, Helper.getPlayerRankName(player) + " §chat dich aus der Freundesliste entfernt.");
            fireFriendsUpdate(targetPlayer, player, FriendsUpdateEvent.FriendsUpdateType.FRIEND_REMOVED);
        }
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu bist nicht mehr mit §7" + Helper.getPlayerRankName(target) + "§c befreundet.");
    }

    public void listFriends(Player player, int page) {
        List<FriendData> friends = getFriends(player);
        int maxFriendsPerPage = fileManager.getFriendsConfig(FileManager.FriendsConfig.maxFriendsPerPage);
        int totalPages = (int) Math.ceil((double) friends.size() / maxFriendsPerPage);
        if (page < 1 || page > totalPages) {
            page = 1;
        }
        int start = (page - 1) * maxFriendsPerPage;
        int end = Math.min(start + maxFriendsPerPage, friends.size());
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§aDeine Freunde §7(Seite " + page + "/" + totalPages + "):");
        for (int i = start; i < end; i++) {
            FriendData friend = friends.get(i);
            MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, (friend.friend.isOnline() ? "§a✔" : "§c✖") + "§7 " + Helper.getPlayerRankName(friend.friend) + " §8(" + friend.timeStamp + ")");
        }
    }

    public void replyFriendRequest(Player player, OfflinePlayer target, boolean accept) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (!playerFriends.incomingRequests.stream().map(fd -> fd.friend).toList().contains(target)) {
            return;
        }
        if (accept) {
            int maxFriends = fileManager.getFriendsConfig(FileManager.FriendsConfig.maxFriends);
            if (!playerFriends.unlimitedPermission && playerFriends.friends.size() >= maxFriends) {
                MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu hast die maximale Anzahl an Freunden erreicht.");
                return;
            } else if (!targetFriends.unlimitedPermission && targetFriends.friends.size() >= maxFriends) {
                MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§7" + Helper.getPlayerRankName(target) + "§c hat die maximale Anzahl an Freunden erreicht.");
                return;
            }
            String timestamp = Instant.now().atZone(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            playerFriends.friends.add(new FriendData(target, timestamp));
            targetFriends.friends.add(new FriendData(player, timestamp));
        }
        playerFriends.incomingRequests.removeIf(fd -> fd.friend.equals(target));
        targetFriends.sentRequests.removeIf(fd -> fd.friend.equals(player));
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        if (target.isOnline()) {
            Player targetPlayer = (Player) target;
            MessageHelper.sendMessage(targetPlayer, MessageHelper.Type.FRIEND, Helper.getPlayerRankName(player) + (accept ? "§a hat deine Freundschaftsanfrage akzeptiert." : "§c hat deine Freundschaftsanfrage abgelehnt."));
            fireFriendsUpdate(targetPlayer, player, accept ? FriendsUpdateEvent.FriendsUpdateType.REQUEST_ACCEPTED : FriendsUpdateEvent.FriendsUpdateType.REQUEST_DECLINED);
        }
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, (accept ? "§aDu hast die Freundschaftsanfrage von §7" + Helper.getPlayerRankName(target) + "§a akzeptiert." : "§cDu hast die Freundschaftsanfrage von §7" + Helper.getPlayerRankName(target) + "§c abgelehnt."));
    }

    public void cancelFriendRequest(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        if (!playerFriends.sentRequests.stream().map(fd -> fd.friend).toList().contains(target)) {
            return;
        }
        playerFriends.sentRequests.removeIf(fd -> fd.friend.equals(target));
        targetFriends.incomingRequests.removeIf(fd -> fd.friend.equals(player));
        if (target.isOnline()) {
            Player targetPlayer = (Player) target;
            MessageHelper.sendMessage(targetPlayer, MessageHelper.Type.FRIEND, Helper.getPlayerRankName(player) + "§c hat seine Freundschaftsanfrage zurückgezogen.");
            fireFriendsUpdate(targetPlayer, player, FriendsUpdateEvent.FriendsUpdateType.REQUEST_CANCELLED);
        } else {
            targetFriends.newFriendRequests.remove(player);
        }
        savePlayerFriends(player, playerFriends, false);
        savePlayerFriends(target, targetFriends, false);
        MessageHelper.sendMessage(player, MessageHelper.Type.FRIEND, "§cDu hast die Freundschaftsanfrage an §7" + Helper.getPlayerRankName(target) + "§c zurückgezogen.");
    }

    public List<OfflinePlayer> getNewFriendRequests(Player player) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        List<OfflinePlayer> newFriendRequests = new ArrayList<>(playerFriends.newFriendRequests);
        return newFriendRequests;
    }

    public void fireFriendsUpdate(Player player, OfflinePlayer target, FriendsUpdateEvent.FriendsUpdateType updateType) {
        FriendsUpdateEvent updateEvent = new FriendsUpdateEvent(player, target, updateType);
        Bukkit.getPluginManager().callEvent(updateEvent);
    }

    public boolean areFriends(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        return playerFriends.friends.stream().map(fd -> fd.friend).toList().contains(target);
    }

    public boolean haveCommonFriends(Player player, OfflinePlayer target) {
        PlayerFriends playerFriends = getPlayerFriends(player);
        PlayerFriends targetFriends = getPlayerFriends(target);
        Set<OfflinePlayer> playerFriendSet = playerFriends.friends.stream().map(fd -> fd.friend).collect(Collectors.toSet());
        Set<OfflinePlayer> targetFriendSet = targetFriends.friends.stream().map(fd -> fd.friend).collect(Collectors.toSet());
        playerFriendSet.retainAll(targetFriendSet);
        return !playerFriendSet.isEmpty();
    }
}
