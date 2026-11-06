package com.wolfdrache.leaderboard;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

public class LeaderBoardListener implements Listener {

    public LeaderBoardListener(Plugin plugin) {
        ArmorstandLB.setNamespacedKey(plugin);
    }

    @EventHandler
    public void onLeaderboardArmorstandInteract(PlayerArmorStandManipulateEvent event) {
        ArmorStand armorStand = event.getRightClicked();
        if (!ArmorstandLB.isLeaderboardArmorstand(armorStand)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeaderboardArmorstandDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof ArmorStand armorStand)) return;
        if (!ArmorstandLB.isLeaderboardArmorstand(armorStand)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeaderboardArmorstandTeleport(EntityTeleportEvent event) {
        if (!(event.getEntity() instanceof ArmorStand armorStand)) return;
        if (!ArmorstandLB.isLeaderboardArmorstand(armorStand)) return;
        event.setCancelled(true);
    }

    @EventHandler 
    public void onLeaderboardArmorstandRename(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand armorStand)) return;
        if (!ArmorstandLB.isLeaderboardArmorstand(armorStand)) return;
        event.setCancelled(true);
    }
}
