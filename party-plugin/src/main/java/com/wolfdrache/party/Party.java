package com.wolfdrache.party;

import com.wolfdrache.friends.FriendsService;
import com.wolfdrache.party.api.PartyService;
import com.wolfdrache.party.friends.*;
import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.party.*;
import com.wolfdrache.party.service.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Party extends JavaPlugin {
    private FileManager fileManager;

    private PartyService partyService;
    private PartyManager partyManager;

    private FriendsManager friendsManager;
    private FriendsService friendsService;

    @Override
    public void onEnable() {
        this.fileManager = new FileManager(this);

        this.partyManager = new PartyManager();
        this.partyService = new PartyServiceImpl(partyManager);
        Bukkit.getServicesManager().register(PartyService.class, this.partyService, this, ServicePriority.Normal);
        
        getCommand("party").setExecutor(new PartyCommand(partyManager));
        getCommand("partychat").setExecutor(new PartyChatCommand(partyManager));
        getServer().getPluginManager().registerEvents(new PartyListener(partyManager), this);

        this.friendsManager = new FriendsManager(fileManager);
        this.friendsService = new FriendsServiceImpl(friendsManager);
        Bukkit.getServicesManager().register(FriendsService.class, this.friendsService, this, ServicePriority.Normal);
        getCommand("friends").setExecutor(new FriendsCommand(friendsManager));
        
        getServer().getPluginManager().registerEvents(new FriendsListener(friendsManager), this);

        getLogger().info("Party has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(PartyService.class, this.partyService);
        Bukkit.getServicesManager().unregister(FriendsService.class, this.friendsService);
        getLogger().info("Party has been disabled!");
    }
}