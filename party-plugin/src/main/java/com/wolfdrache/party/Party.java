package com.wolfdrache.party;

import com.wolfdrache.friends.FriendsService;
import com.wolfdrache.party.api.PartyService;
import com.wolfdrache.party.friends.*;
import com.wolfdrache.party.gamemodes.GameCommandManager;
import com.wolfdrache.party.gamemodes.GameCommands;
import com.wolfdrache.party.general.FileManager;
import com.wolfdrache.party.party.*;
import com.wolfdrache.party.reports.*;
import com.wolfdrache.party.reports.ui.*;
import com.wolfdrache.party.service.*;
import com.wolfdrache.serverapi.gamemodes.GameCommandTypes;
import com.wolfdrache.serverapi.gamemodes.GameService;
import com.wolfdrache.serverapi.reports.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Party extends JavaPlugin {
    private FileManager fileManager;

    private PartyService partyService;
    private PartyManager partyManager;

    private FriendsManager friendsManager;
    private FriendsService friendsService;

    private ReportService reportService;
    private ReportManager reportManager;
    private ReportsGui reportsGui;
    private PlayerReportsGui playerReportsGui;
    private ReportManagerGui reportManagerGui;

    private GameService gameService;
    private GameCommandManager gameManager;

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

        this.reportManager = new ReportManager(fileManager);
        this.reportService = new ReportServiceImpl(reportManager);
        this.reportManagerGui = new ReportManagerGui(reportManager);
        this.playerReportsGui = new PlayerReportsGui(reportManager, reportManagerGui);
        this.reportsGui = new ReportsGui(reportManager);
        Bukkit.getServicesManager().register(ReportService.class, this.reportService, this, ServicePriority.Normal);

        getCommand("report").setExecutor(new ReportCommand(reportManager));
        getServer().getPluginManager().registerEvents(reportsGui, this);
        getServer().getPluginManager().registerEvents(playerReportsGui, this);
        getServer().getPluginManager().registerEvents(reportManagerGui, this);

        this.gameManager = new GameCommandManager();
        this.gameService = new GameServiceImpl(gameManager);
        Bukkit.getServicesManager().register(GameService.class, this.gameService, this, ServicePriority.Normal);

        for (GameCommandTypes commandType : GameCommandTypes.values()) {
            getCommand(commandType.name().toLowerCase()).setExecutor(new GameCommands(gameManager, commandType));
        }
        gameService.registerPluginAlias(this, "reports");
        gameService.registerCommand(this, GameCommandTypes.ADMIN, new ReportsTeamCommand(reportManager));

        getLogger().info("Party has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(PartyService.class, this.partyService);
        Bukkit.getServicesManager().unregister(FriendsService.class, this.friendsService);
        Bukkit.getServicesManager().unregister(ReportService.class, this.reportService);
        Bukkit.getServicesManager().unregister(GameService.class, this.gameService);
        getLogger().info("Party has been disabled!");
    }
}