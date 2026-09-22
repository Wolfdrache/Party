package com.wolfdrache.party.service;

import com.wolfdrache.party.api.PartyService;
import java.util.List;
import org.bukkit.entity.Player;

public class PartyServiceImpl implements PartyService {
    @Override
    public boolean isInParty(Player player) {
        return false;
    }
    
    @Override
    public boolean isPartyLeader(Player player) {
        return false;
    }

    @Override
    public List<Player> getPartyMembers(Player player) {
        return null;
    }
}