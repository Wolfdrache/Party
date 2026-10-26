package com.wolfdrache.party.general;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolfdrache.party.Party;
import com.wolfdrache.party.friends.PlayerFriends;

public class FileManager {
    public static enum FriendsConfig {
        maxFriendsPerPage(5),
        maxFriends(25);

        public final int defaultValue;
        FriendsConfig(int defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    private final Party plugin;

    private final File friendsFile;

    public FileManager(Party plugin) {
        this.plugin = plugin;
        this.friendsFile = new File(plugin.getDataFolder(), "friends.yml");
    }

    public PlayerFriends getPlayerFriends(OfflinePlayer player) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        UUID uuid = player.getUniqueId();

        boolean unlimitedPermission = config.getBoolean(uuid.toString() + ".unlimitedPermission", false);
        List<String> friendsStrings = config.getStringList(uuid.toString() + ".friends");
        List<String> requestsStrings = config.getStringList(uuid.toString() + ".requests");
        List<String> sentStrings = config.getStringList(uuid.toString() + ".sent");
        List<String> newFriendRequestsStrings = config.getStringList(uuid.toString() + ".newFriendRequests");

        Set<OfflinePlayer> friends = friendsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());
        Set<OfflinePlayer> requests = requestsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());
        Set<OfflinePlayer> sent = sentStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());
        Set<OfflinePlayer> newFriendRequests = newFriendRequestsStrings.stream().map(s -> plugin.getServer().getOfflinePlayer(UUID.fromString(s))).collect(Collectors.toSet());

        return new PlayerFriends(unlimitedPermission, friends, requests, sent, newFriendRequests);
    }

    public void savePlayerFriends(OfflinePlayer player, PlayerFriends playerFriends) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        UUID uuid = player.getUniqueId();

        List<String> friendsStrings = playerFriends.friends.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());
        List<String> requestsStrings = playerFriends.incomingRequests.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());
        List<String> sentStrings = playerFriends.sentRequests.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());
        List<String> newFriendRequestsStrings = playerFriends.newFriendRequests.stream().map(p -> p.getUniqueId().toString()).collect(Collectors.toList());

        config.set(uuid.toString() + ".name", player.getName());
        config.set(uuid.toString() + ".unlimitedPermission", playerFriends.unlimitedPermission);
        config.set(uuid.toString() + ".friends", friendsStrings);
        config.set(uuid.toString() + ".requests", requestsStrings);
        config.set(uuid.toString() + ".sent", sentStrings);
        config.set(uuid.toString() + ".newFriendRequests", newFriendRequestsStrings);

        try {
            config.save(friendsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getFriendsConfig(FriendsConfig friendsConfig) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(friendsFile);
        return config.getInt("config." + friendsConfig.name(), friendsConfig.defaultValue);
    }
}
