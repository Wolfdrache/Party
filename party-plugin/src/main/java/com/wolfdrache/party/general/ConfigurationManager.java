package com.wolfdrache.party.general;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.serverapi.configuration.ConfigurationService.ConfigurationKey;
import com.wolfdrache.serverapi.configuration.ConfigurationService.FriendRequestMode;
import com.wolfdrache.serverapi.configuration.ConfigurationService.MsgMode;
import com.wolfdrache.serverapi.configuration.ConfigurationService.PartyInviteMode;
import com.wolfdrache.serverapi.configuration.ConfigurationService.Status;

public class ConfigurationManager {
    public static class PlayerConfiguration {
        public MsgMode msgMode;
        public PartyInviteMode partyInviteMode;
        public FriendRequestMode friendRequestMode;

        public PlayerConfiguration(MsgMode msgMode, PartyInviteMode partyInviteMode, FriendRequestMode friendRequestMode) {
            this.msgMode = msgMode;
            this.partyInviteMode = partyInviteMode;
            this.friendRequestMode = friendRequestMode;
        }
    }
    private final FileManager fileManager;

    public ConfigurationManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public PlayerConfiguration getPlayerConfiguration(OfflinePlayer player) {
        return fileManager.getPlayerConfiguration(player);
    }

    public void savePlayerConfiguration(OfflinePlayer player, PlayerConfiguration configuration) {
        fileManager.savePlayerConfiguration(player, configuration);
    }

    public MsgMode rotateMsgStatus(Player player, boolean forward) {
        PlayerConfiguration configuration = getPlayerConfiguration(player);
        int ordinal = configuration.msgMode.ordinal();
        int length = MsgMode.values().length;
        ordinal = forward ? (ordinal + 1) % length : (ordinal - 1 + length) % length;
        configuration.msgMode = MsgMode.values()[ordinal];
        savePlayerConfiguration(player, configuration);
        return configuration.msgMode;
    }

    public PartyInviteMode rotatePartyInviteStatus(Player player, boolean forward) {
        PlayerConfiguration configuration = getPlayerConfiguration(player);
        int ordinal = configuration.partyInviteMode.ordinal();
        int length = PartyInviteMode.values().length;
        ordinal = forward ? (ordinal + 1) % length : (ordinal - 1 + length) % length;
        configuration.partyInviteMode = PartyInviteMode.values()[ordinal];
        savePlayerConfiguration(player, configuration);
        return configuration.partyInviteMode;
    }

    public FriendRequestMode rotateFriendRequestStatus(Player player, boolean forward) {
        PlayerConfiguration configuration = getPlayerConfiguration(player);
        int ordinal = configuration.friendRequestMode.ordinal();
        int length = FriendRequestMode.values().length;
        ordinal = forward ? (ordinal + 1) % length : (ordinal - 1 + length) % length;
        configuration.friendRequestMode = FriendRequestMode.values()[ordinal];
        savePlayerConfiguration(player, configuration);
        return configuration.friendRequestMode;
    }

    public Status rotateStatus(Player player, ConfigurationKey key, boolean forward) {
        return switch (key) {
            case MSG -> rotateMsgStatus(player, forward);
            case PARTY -> rotatePartyInviteStatus(player, forward);
            case FRIEND -> rotateFriendRequestStatus(player, forward);
            default -> throw new IllegalArgumentException("Unknown ConfigurationKey: " + key);
        };
    }
}
