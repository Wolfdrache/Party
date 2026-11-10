package com.wolfdrache.party.msg;

import org.bukkit.entity.Player;

import com.wolfdrache.helper.Helper;
import com.wolfdrache.party.friends.FriendsManager;
import com.wolfdrache.party.general.ConfigurationManager;
import com.wolfdrache.party.general.MessageHelper;
import com.wolfdrache.party.party.PartyManager;
import com.wolfdrache.party.general.ConfigurationManager.PlayerConfiguration;

public class MsgManager {
    private final ConfigurationManager configurationManager;
    private final FriendsManager friendsManager;
    private final PartyManager partyManager;

    public MsgManager(ConfigurationManager configurationManager, FriendsManager friendsManager, PartyManager partyManager) {
        this.configurationManager = configurationManager;
        this.friendsManager = friendsManager;
        this.partyManager = partyManager;
    }

    public void msgMessage(Player player, Player target, String message) {
        PlayerConfiguration targetConfig = configurationManager.getPlayerConfiguration(target);
        if (player.hasPermission("social.msgAllways")) {
            MessageHelper.sendMsgMessage(player, target, message);
            return;
        }
        switch (targetConfig.msgMode) {
            case ALL:
                break;
            case FRIENDS_PARTY:
                if (!friendsManager.areFriends(player, target) && !partyManager.areInSameParty(player, target)) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.MSG, Helper.getPlayerRankName(target) + "§7 empfängt nur Nachrichten von Freunden oder Party-Mitgliedern.");
                    return;
                }
                break;
            case COMMON_FRIEND:
                if (!friendsManager.haveCommonFriends(player, target) && !friendsManager.areFriends(player, target)) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.MSG, Helper.getPlayerRankName(target) + "§7 empfängt nur Nachrichten von gemeinsamen Freunden.");
                    return;
                }
                break;
            case FRIENDS:
                if (!friendsManager.areFriends(player, target)) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.MSG, Helper.getPlayerRankName(target) + "§7 empfängt nur Nachrichten von Freunden.");
                    return;
                }
                break;
            case PARTY:
                if (!partyManager.areInSameParty(player, target)) {
                    MessageHelper.sendMessage(player, MessageHelper.Type.MSG, Helper.getPlayerRankName(target) + "§7 empfängt nur Nachrichten von Party-Mitgliedern.");
                    return;
                }
                break;
            case NONE:
                MessageHelper.sendMessage(player, MessageHelper.Type.MSG, Helper.getPlayerRankName(target) + "§7 empfängt keine Nachrichten.");
                return;
        }
        MessageHelper.sendMsgMessage(player, target, message);
    }
}
