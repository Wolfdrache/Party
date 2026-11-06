package com.wolfdrache.leaderboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class LeaderBoardManager {
    private final Function<OfflinePlayer, IPlayerStats> playerStatsProvider;

    private final List<UUID> leaderboard = new ArrayList<>();

    private final ArmorstandLB armorstandRank1;
    private final ArmorstandLB armorstandRank2;
    private final ArmorstandLB armorstandRank3;

    public LeaderBoardManager(Plugin plugin, Function<OfflinePlayer, IPlayerStats> playerStatsProvider) {
        this.playerStatsProvider = playerStatsProvider;
        LeaderBoardFileManager leaderBoardFileManager = new LeaderBoardFileManager(plugin);

        this.leaderboard.addAll(leaderBoardFileManager.getLeaderboard());
        sortLeaderboard();
        this.armorstandRank1 = leaderBoardFileManager.loadArmorstandLB(1);
        this.armorstandRank2 = leaderBoardFileManager.loadArmorstandLB(2);
        this.armorstandRank3 = leaderBoardFileManager.loadArmorstandLB(3);

        removeOldArmorstands();

        updateArmorstands();
    }

    public void removeOldArmorstands() {
        for (ArmorstandLB armLB : new ArmorstandLB[]{armorstandRank1, armorstandRank2, armorstandRank3}) {
            if (armLB == null || armLB.location == null || armLB.location.getWorld() == null) continue;
            Chunk armorstandChunk = armLB.location.getChunk();
            armorstandChunk.load();
            for (ArmorStand armorStand : armLB.location.getWorld().getEntitiesByClass(ArmorStand.class)) {
                if (ArmorstandLB.isLeaderboardArmorstand(armorStand)) {
                    armorStand.remove();
                }
            }
        }
    }

    public void onPlayerLoaded(Player player) {
        if (!leaderboard.contains(player.getUniqueId())) {
            leaderboard.add(player.getUniqueId());
            updateLeaderboard();
        }
    }

    private void sortLeaderboard() {
        leaderboard.sort((uuid1, uuid2) -> {
            IPlayerStats stats1 = playerStatsProvider.apply(Bukkit.getOfflinePlayer(uuid1));
            IPlayerStats stats2 = playerStatsProvider.apply(Bukkit.getOfflinePlayer(uuid2));
            return Integer.compare(stats2.getPoints(), stats1.getPoints());
        });
    }

    public void updateLeaderboard() {
        sortLeaderboard();

        updateArmorstands();
    }

    public int getRank(OfflinePlayer player) {
        return leaderboard.indexOf(player.getUniqueId()) + 1;
    }

    private void updateArmorstands() {
        updateArmorstand(armorstandRank1, 1);
        updateArmorstand(armorstandRank2, 2);
        updateArmorstand(armorstandRank3, 3);
    }

    private void updateArmorstand(ArmorstandLB armorstandLB, int rank) {
        if (leaderboard.size() < rank) return;
        if (armorstandLB == null) return;
        if (armorstandLB.armorStand == null || armorstandLB.armorStand.isDead()) {
            armorstandLB.summon(rank);
        }
        OfflinePlayer topPlayer = Bukkit.getOfflinePlayer(leaderboard.get(rank - 1));
        ItemStack playerHead = createPlayerHead(topPlayer);
        int points = playerStatsProvider.apply(topPlayer).getPoints();
        armorstandLB.armorStand.customName(createComponent("§6" + rank + ". " + topPlayer.getName() + " §7- §e" + points));
        EntityEquipment equipment = armorstandLB.armorStand.getEquipment();
        equipment.setHelmet(playerHead);
    }

    private ItemStack createPlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
        }
        return head;
    }

    private Component createComponent(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }
}
