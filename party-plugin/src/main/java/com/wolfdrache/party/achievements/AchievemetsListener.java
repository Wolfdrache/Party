package com.wolfdrache.party.achievements;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.wolfdrache.serverapi.achievements.Achievement.IAchievementType;
import com.wolfdrache.serverapi.achievements.AchievementCategory;

public class AchievemetsListener implements Listener {
    private final AchievementManager achievementManager;

    public AchievemetsListener(AchievementManager achievementManager) {
        this.achievementManager = achievementManager;
    }

    @EventHandler 
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (AchievementCategory category : achievementManager.getCategories()) {
            if (category.getProgress(player).getProgress().isEmpty()) {
                for (IAchievementType type : category.getAchievementTypes()) {
                    if (type.getSyncFunction() != null){
                        achievementManager.updateAchievementProgress(player, category, type, 0);
                    }
                }
            }
        }
    }
}
