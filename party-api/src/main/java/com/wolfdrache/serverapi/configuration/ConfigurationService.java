package com.wolfdrache.serverapi.configuration;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface ConfigurationService {
    public interface Status {
        Material getMaterial();
        String getDisplayName();
    }
    public enum MsgMode implements Status {
        ALL (Material.LIGHT_BLUE_DYE, "§bAlle"), FRIENDS_PARTY (Material.CYAN_DYE, "§3Freunde & Party"), FRIENDS (Material.LIME_DYE, "§aFreunde"), COMMON_FRIEND (Material.GREEN_DYE, "§2Gemeinsame Freunde"), PARTY (Material.BLUE_DYE, "§9Party"), NONE (Material.GRAY_DYE, "§7Keine");
        private final Material material;
        private final String displayName;
        MsgMode(Material material, String displayName) {
            this.material = material;
            this.displayName = displayName;
        }
        @Override
        public Material getMaterial() {
            return material;
        }
        @Override
        public String getDisplayName() {
            return displayName;
        }
    }
    public enum PartyInviteMode implements Status {
        ALL (Material.LIGHT_BLUE_DYE, "§bAlle"), FRIENDS (Material.LIME_DYE, "§aFreunde"), COMMON_FRIEND (Material.GREEN_DYE, "§2Gemeinsame Freunde"), NONE (Material.GRAY_DYE, "§7Keine");
        private final Material material;
        private final String displayName;
        PartyInviteMode(Material material, String displayName) {
            this.material = material;
            this.displayName = displayName;
        }
        @Override
        public Material getMaterial() {
            return material;
        }
        @Override
        public String getDisplayName() {
            return displayName;
        }
    }
    public enum FriendRequestMode implements Status {
        ALL (Material.LIGHT_BLUE_DYE, "§bAlle"), COMMON_FRIEND (Material.GREEN_DYE, "§2Gemeinsame Freunde"), PARTY (Material.BLUE_DYE, "§9Party"), NONE (Material.GRAY_DYE, "§7Keine");
        private final Material material;
        private final String displayName;
        FriendRequestMode(Material material, String displayName) {
            this.material = material;
            this.displayName = displayName;
        }
        @Override
        public Material getMaterial() {
            return material;
        }
        @Override
        public String getDisplayName() {
            return displayName;
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
