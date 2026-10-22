package com.wolfdrache.party;

import com.wolfdrache.party.friends.*;
import com.wolfdrache.party.party.*;
import com.wolfdrache.party.service.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Party extends JavaPlugin {

    private PartyService partyService;
    private PartyManager partyManager;

    private FriendsManager friendsManager;

    @Override
    public void onEnable() {

        this.partyManager = new PartyManager();
        this.partyService = new PartyServiceImpl(partyManager);
        Bukkit.getServicesManager().register(PartyService.class, this.partyService, this, ServicePriority.Normal);
        
        getCommand("party").setExecutor(new PartyCommand(partyManager));
        getCommand("partychat").setExecutor(new PartyChatCommand(partyManager));

        this.friendsManager = new FriendsManager();
        getCommand("friends").setExecutor(new FriendsCommand(friendsManager));

        getServer().getPluginManager().registerEvents(new ConnectionListener(partyManager), this);
        getLogger().info("Party has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(PartyService.class, this.partyService);
        getLogger().info("Party has been disabled!");
    }
}