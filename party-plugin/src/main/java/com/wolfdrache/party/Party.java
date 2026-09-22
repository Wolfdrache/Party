package com.wolfdrache.party;

import com.wolfdrache.party.api.PartyApi;
import com.wolfdrache.party.api.PartyService;
import com.wolfdrache.party.service.PartyServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Party extends JavaPlugin implements PartyApi {

    private PartyService partyService;

    @Override
    public void onEnable() {
        this.partyService = new PartyServiceImpl();

        Bukkit.getServicesManager().register(PartyApi.class, this, this, ServicePriority.Normal);
        getLogger().info("Party has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(PartyApi.class, this);
        getLogger().info("Party has been disabled!");
    }

    @Override
    public PartyService getPartyService() {
        return this.partyService;
    }
}