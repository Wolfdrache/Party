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

    public MsgMode rotateMsgStatus(Player player) {
        PlayerConfiguration configuration = getPlayerConfiguration(player);
        configuration.msgMode = MsgMode.values()[(configuration.msgMode.ordinal() + 1) % MsgMode.values().length];
        savePlayerConfiguration(player, configuration);
        return configuration.msgMode;
    }

    public PartyInviteMode rotatePartyInviteStatus(Player player) {
        PlayerConfiguration configuration = getPlayerConfiguration(player);
        configuration.partyInviteMode = PartyInviteMode.values()[(configuration.partyInviteMode.ordinal() + 1) % PartyInviteMode.values().length];
        savePlayerConfiguration(player, configuration);
        return configuration.partyInviteMode;
    }

    public FriendRequestMode rotateFriendRequestStatus(Player player) {
        PlayerConfiguration configuration = getPlayerConfiguration(player);
        configuration.friendRequestMode = FriendRequestMode.values()[(configuration.friendRequestMode.ordinal() + 1) % FriendRequestMode.values().length];
        savePlayerConfiguration(player, configuration);
        return configuration.friendRequestMode;
    }

    public Status rotateStatus(Player player, ConfigurationKey key) {
        switch (key) {
            case MSG:
                return rotateMsgStatus(player);
            case PARTY:
                return rotatePartyInviteStatus(player);
            case FRIEND:
                return rotateFriendRequestStatus(player);
            default:
                throw new IllegalArgumentException("Unknown ConfigurationKey: " + key);
        }
    }
}
