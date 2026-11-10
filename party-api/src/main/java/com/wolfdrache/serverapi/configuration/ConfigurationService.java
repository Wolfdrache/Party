package com.wolfdrache.serverapi.configuration;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface ConfigurationService {
    public enum MsgMode {
        ALL (Material.LIGHT_BLUE_DYE), FRIENDS_PARTY (Material.CYAN_DYE), FRIENDS (Material.LIME_DYE), COMMON_FRIEND (Material.GREEN_DYE), PARTY (Material.BLUE_DYE), NONE (Material.GRAY_DYE);
        public final Material material;
        MsgMode(Material material) {
            this.material = material;
        }
    }
    public enum PartyInviteMode {
        ALL (Material.LIGHT_BLUE_DYE), FRIENDS (Material.LIME_DYE), COMMON_FRIEND (Material.GREEN_DYE), NONE (Material.GRAY_DYE);
        public final Material material;
        PartyInviteMode(Material material) {
            this.material = material;
        }
    }
    public enum FriendRequestMode {
        ALL (Material.LIGHT_BLUE_DYE), COMMON_FRIEND (Material.GREEN_DYE), PARTY (Material.BLUE_DYE), NONE (Material.GRAY_DYE);
        public final Material material;
        FriendRequestMode(Material material) {
            this.material = material;
        }
    }
    void setMsgStatus(Player player, MsgMode mode);
    void setPartyInviteStatus(Player player, PartyInviteMode mode);
    void setFriendRequestStatus(Player player, FriendRequestMode mode);

    MsgMode getMsgStatus(Player player);
    PartyInviteMode getPartyInviteStatus(Player player);
    FriendRequestMode getFriendRequestStatus(OfflinePlayer player);

    MsgMode rotateMsgStatus(Player player);
    PartyInviteMode rotatePartyInviteStatus(Player player);
    FriendRequestMode rotateFriendRequestStatus(Player player);
}
