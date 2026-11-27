package com.wolfdrache.serverapi.configuration;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface ConfigurationService {
    public enum ConfigurationKey {
        PARTY, MSG, FRIEND, FRIEND_ONLINE_STATUS
    }
    public interface Status {
        Material getMaterial();
        String getDisplayName();
        ConfigurationKey getConfigurationKey();
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
        @Override
        public ConfigurationKey getConfigurationKey() {
            return ConfigurationKey.MSG;
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
        @Override
        public ConfigurationKey getConfigurationKey() {
            return ConfigurationKey.PARTY;
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
        @Override
        public ConfigurationKey getConfigurationKey() {
            return ConfigurationKey.FRIEND;
        }
    }
    public enum FriendOnlineStatusMode implements Status {
        ALL (Material.LIME_DYE, "§aAn", true, true), JOIN (Material.GREEN_DYE, "§2Online", true, false), LEAVE (Material.RED_DYE, "§cOffline", false, true), OFF (Material.GRAY_DYE, "§7Aus", false, false);
        private final Material material;
        private final String displayName;
        public final boolean notifyOnJoin;
        public final boolean notifyOnLeave;
        FriendOnlineStatusMode(Material material, String displayName, boolean notifyOnJoin, boolean notifyOnLeave) {
            this.material = material;
            this.displayName = displayName;
            this.notifyOnJoin = notifyOnJoin;
            this.notifyOnLeave = notifyOnLeave;
        }
        @Override
        public Material getMaterial() {
            return material;
        }
        @Override
        public String getDisplayName() {
            return displayName;
        }
        @Override
        public ConfigurationKey getConfigurationKey() {
            return ConfigurationKey.FRIEND_ONLINE_STATUS;
        }
    }
    void setMsgStatus(Player player, MsgMode mode);
    void setPartyInviteStatus(Player player, PartyInviteMode mode);
    void setFriendRequestStatus(Player player, FriendRequestMode mode);
    void setFriendOnlineStatus(Player player, FriendOnlineStatusMode mode);

    MsgMode getMsgStatus(Player player);
    PartyInviteMode getPartyInviteStatus(Player player);
    FriendRequestMode getFriendRequestStatus(OfflinePlayer player);
    FriendOnlineStatusMode getFriendOnlineStatus(Player player);

    MsgMode rotateMsgStatus(Player player);
    PartyInviteMode rotatePartyInviteStatus(Player player);
    FriendRequestMode rotateFriendRequestStatus(Player player);
    FriendOnlineStatusMode rotateFriendOnlineStatus(Player player);
    Status rotateStatus(Player player, ConfigurationKey key);
    
    Status rotateStatus(Player player, ConfigurationKey key, boolean forward);
    MsgMode rotateMsgStatus(Player player, boolean forward);
    PartyInviteMode rotatePartyInviteStatus(Player player, boolean forward);
    FriendRequestMode rotateFriendRequestStatus(Player player, boolean forward);
    FriendOnlineStatusMode rotateFriendOnlineStatus(Player player, boolean forward);
}
