package com.wolfdrache.party.service;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wolfdrache.party.general.ConfigurationManager;
import com.wolfdrache.serverapi.configuration.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationManager configurationManager;

    public ConfigurationServiceImpl(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Override
    public void setMsgStatus(Player player, MsgMode mode) {
        ConfigurationManager.PlayerConfiguration configuration = configurationManager.getPlayerConfiguration(player);
        configuration.msgMode = mode;
        configurationManager.savePlayerConfiguration(player, configuration);
    }

    @Override
    public void setPartyInviteStatus(Player player, PartyInviteMode mode) {
        ConfigurationManager.PlayerConfiguration configuration = configurationManager.getPlayerConfiguration(player);
        configuration.partyInviteMode = mode;
        configurationManager.savePlayerConfiguration(player, configuration);
    }

    @Override
    public void setFriendRequestStatus(Player player, FriendRequestMode mode) {
        ConfigurationManager.PlayerConfiguration configuration = configurationManager.getPlayerConfiguration(player);
        configuration.friendRequestMode = mode;
        configurationManager.savePlayerConfiguration(player, configuration);
    }

    @Override
    public MsgMode getMsgStatus(Player player) {
        ConfigurationManager.PlayerConfiguration configuration = configurationManager.getPlayerConfiguration(player);
        return configuration.msgMode;
    }

    @Override
    public PartyInviteMode getPartyInviteStatus(Player player) {
        ConfigurationManager.PlayerConfiguration configuration = configurationManager.getPlayerConfiguration(player);
        return configuration.partyInviteMode;
    }

    @Override
    public FriendRequestMode getFriendRequestStatus(OfflinePlayer player) {
        ConfigurationManager.PlayerConfiguration configuration = configurationManager.getPlayerConfiguration(player);
        return configuration.friendRequestMode;
    }

    @Override
    public MsgMode rotateMsgStatus(Player player) {
        return configurationManager.rotateMsgStatus(player);
    }

    @Override
    public PartyInviteMode rotatePartyInviteStatus(Player player) {
        return configurationManager.rotatePartyInviteStatus(player);
    }

    @Override
    public FriendRequestMode rotateFriendRequestStatus(Player player) {
        return configurationManager.rotateFriendRequestStatus(player);
    }
    
    @Override
    public Status rotateStatus(Player player, ConfigurationKey key) {
        return configurationManager.rotateStatus(player, key);
    }
}
