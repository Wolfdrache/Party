package com.wolfdrache.party.service;

import com.wolfdrache.party.api.PartyService;
import com.wolfdrache.party.internal.manager.PartyManager;

import java.util.List;
import org.bukkit.entity.Player;

public class PartyServiceImpl implements PartyService {
    private final PartyManager partyManager;

    public PartyServiceImpl(PartyManager partyManager) {
        this.partyManager = partyManager;
    }
    
    @Override
    public boolean isInParty(Player player) {
        return partyManager.isInParty(player);
    }
    
    @Override
    public boolean isPartyLeader(Player player) {
        return partyManager.isPartyLeader(player);
    }

    @Override
    public void invitePlayer(Player player, Player target) {
        partyManager.invitePlayer(player, target);
    }

    @Override
    public void kickPlayer(Player player, Player target) {
        partyManager.kickPlayer(player, target);
    }

    @Override
    public void leaveParty(Player player) {
        partyManager.leaveParty(player);
    }

    @Override
    public void disbandParty(Player player) {
        partyManager.disbandParty(player);
    }

    @Override
    public void acceptInvite(Player player, Player target) {
        partyManager.acceptInvite(player, target);
    }

    @Override
    public Player getPartyLeader(Player player) {
        return partyManager.getPartyLeader(player);
    }

    @Override
    public List<Player> getPartyMembers(Player player) {
        return partyManager.getPartyMembers(player);
    }
}