package com.wolfdrache.party.api;

import org.bukkit.entity.Player;
import java.util.List;

public interface PartyService {
    boolean isInParty(Player player);
    boolean isPartyLeader(Player player);
    List<Player> getPartyMembers(Player player);
}