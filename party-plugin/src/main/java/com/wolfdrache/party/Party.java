package com.wolfdrache.party;

import com.wolfdrache.party.api.PartyService;
import com.wolfdrache.party.internal.commands.PartyCommand;
import com.wolfdrache.party.internal.listener.ConnectionListener;
import com.wolfdrache.party.internal.manager.PartyManager;
import com.wolfdrache.party.service.PartyServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Party extends JavaPlugin {

    private PartyService partyService;

    private PartyManager partyManager;

    @Override
    public void onEnable() {

        this.partyManager = new PartyManager();
        this.partyService = new PartyServiceImpl(partyManager);

        Bukkit.getServicesManager().register(PartyService.class, this.partyService, this, ServicePriority.Normal);

        getCommand("party").setExecutor(new PartyCommand(partyManager));

        getServer().getPluginManager().registerEvents(new ConnectionListener(partyManager), this);
        getLogger().info("Party has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(PartyService.class, this.partyService);
        getLogger().info("Party has been disabled!");
    }
}