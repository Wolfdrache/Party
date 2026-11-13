package com.wolfdrache.party;

import com.wolfdrache.friends.FriendsService;
import com.wolfdrache.leaderboard.LeaderBoardListener;
import com.wolfdrache.party.achievements.*;
import com.wolfdrache.party.api.PartyService;
import com.wolfdrache.party.friends.*;
import com.wolfdrache.party.gamemodes.*;
import com.wolfdrache.party.general.*;
import com.wolfdrache.party.msg.*;
import com.wolfdrache.party.party.*;
import com.wolfdrache.party.reports.*;
import com.wolfdrache.party.reports.ui.*;
import com.wolfdrache.party.service.*;
import com.wolfdrache.serverapi.achievements.AchievementService;
import com.wolfdrache.serverapi.configuration.ConfigurationService;
import com.wolfdrache.serverapi.gamemodes.GameService;
import com.wolfdrache.serverapi.gamemodes.GameService.CommandType;
import com.wolfdrache.serverapi.reports.*;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Party extends JavaPlugin {
    private Economy economy;
    
    private CoinManager coinManager;
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

    private MsgManager msgManager;

    private ConfigurationService configurationService;
    private ConfigurationManager configurationManager;

    private AchievementManager achievementManager;
    private AchievementService achievementService;

    @Override
    public void onEnable() {
        economy = setupService(Economy.class);
        if (economy == null) {
            getLogger().severe("Vault economy service not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.fileManager = new FileManager(this);
        this.coinManager = new CoinManager(economy);

        this.configurationManager = new ConfigurationManager(fileManager);
        this.configurationService = new ConfigurationServiceImpl(configurationManager);
        Bukkit.getServicesManager().register(ConfigurationService.class, this.configurationService, this, ServicePriority.Normal);

        this.friendsManager = new FriendsManager(fileManager, configurationManager);
        this.friendsService = new FriendsServiceImpl(friendsManager);
        Bukkit.getServicesManager().register(FriendsService.class, this.friendsService, this, ServicePriority.Normal);

        getCommand("friends").setExecutor(new FriendsCommand(friendsManager));
        getServer().getPluginManager().registerEvents(new FriendsListener(friendsManager), this);

        this.partyManager = new PartyManager(configurationManager, friendsManager);
        friendsManager.setPartyManager(partyManager);
        this.partyService = new PartyServiceImpl(partyManager);
        Bukkit.getServicesManager().register(PartyService.class, this.partyService, this, ServicePriority.Normal);
        
        getCommand("party").setExecutor(new PartyCommand(partyManager));
        getCommand("partychat").setExecutor(new PartyChatCommand(partyManager));
        getServer().getPluginManager().registerEvents(new PartyListener(partyManager), this);

        this.msgManager = new MsgManager(configurationManager, friendsManager, partyManager);
        getCommand("msg").setExecutor(new MsgCommand(msgManager));

        this.reportManager = new ReportManager(fileManager);
        this.reportService = new ReportServiceImpl(reportManager);
        this.reportManagerGui = new ReportManagerGui(reportManager);
        this.playerReportsGui = new PlayerReportsGui(reportManager, reportManagerGui);
        this.reportsGui = new ReportsGui(reportManager);
        Bukkit.getServicesManager().register(ReportService.class, this.reportService, this, ServicePriority.Normal);

        ReportsTeamCommand reportsTeamCommand = new ReportsTeamCommand(reportManager);
        getCommand("report").setExecutor(new ReportCommand(reportManager));
        getCommand("reportsteam").setExecutor(reportsTeamCommand);
        getServer().getPluginManager().registerEvents(reportsGui, this);
        getServer().getPluginManager().registerEvents(playerReportsGui, this);
        getServer().getPluginManager().registerEvents(reportManagerGui, this);
        getServer().getPluginManager().registerEvents(new ReportsCommandHider(), this);

        this.gameManager = new GameCommandManager();
        this.gameService = new GameServiceImpl(gameManager);
        Bukkit.getServicesManager().register(GameService.class, this.gameService, this, ServicePriority.Normal);

        for (CommandType commandType : CommandType.values()) {
            getCommand(commandType.name().toLowerCase()).setExecutor(new GameCommands(gameManager, commandType));
        }
        gameService.registerPluginAlias(this, "reports");
        gameService.registerCommand(this, CommandType.ADMIN, reportsTeamCommand);

        getServer().getPluginManager().registerEvents(new LeaderBoardListener(this), this);

        this.achievementManager = new AchievementManager(fileManager, coinManager);
        this.achievementService = new AchievementServiceImpl(achievementManager);
        Bukkit.getServicesManager().register(AchievementService.class, this.achievementService, this, ServicePriority.Normal);
        getServer().getPluginManager().registerEvents(new AchievemetsListener(achievementManager), this);

        getLogger().info("Party has been enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregister(PartyService.class, this.partyService);
        Bukkit.getServicesManager().unregister(FriendsService.class, this.friendsService);
        Bukkit.getServicesManager().unregister(ReportService.class, this.reportService);
        Bukkit.getServicesManager().unregister(GameService.class, this.gameService);
        Bukkit.getServicesManager().unregister(AchievementService.class, this.achievementService);
        getLogger().info("Party has been disabled!");
    }

    private <T> T setupService(Class<T> serviceClass) {
        RegisteredServiceProvider<T> rsp =
                getServer().getServicesManager().getRegistration(serviceClass);

        if (rsp == null) {
            return null;
        }

        return rsp.getProvider();
    }
}