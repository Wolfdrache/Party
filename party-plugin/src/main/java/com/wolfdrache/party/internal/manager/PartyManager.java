package com.wolfdrache.party.internal.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.wolfdrache.party.internal.models.PartyData;

public class PartyManager {
    private final Map<Player, PartyData> partyDataMap = new HashMap<>();
    private final Map<Player, Set<Player>> pendingInvites = new HashMap<>();

    public void invitePlayer(Player player, Player target) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            partyData = new PartyData(player);
            partyDataMap.put(player, partyData);
        }
        target.sendMessage(player.getName() + " hat dich zu einer Party eingeladen.");
        partyData.invited.add(target);
        
        pendingInvites.computeIfAbsent(target, k -> new HashSet<>()).add(player);
    }

    public void kickPlayer(Player player, Player target) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            return;
        }
        if (partyData.leader.equals(player)) {
            partyData.members.remove(target);
            partyDataMap.remove(target);
        }
    }

    public void leaveParty(Player player) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            return;
        }

        for (Player member : getPartyMembers(player)) {
            member.sendMessage(player.getName() + " hat die Party verlassen.");
        }

        if (partyData.leader.equals(player)) {
            if (!partyData.members.isEmpty()) {
                Player newLeader = null;
                newLeader = partyData.members.get(0);
                partyData.members.remove(0);
                partyData.leader = newLeader;
            }
            partyDataMap.remove(player);
        } else {
            partyData.members.remove(player);
            partyDataMap.remove(player);
        }
    }

    public void disbandParty(Player player) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            return;
        }
        if (partyData.leader.equals(player)) {
            for (Player member : partyData.members) {
                partyDataMap.remove(member);
                member.sendMessage("Die Party wurde aufgelöst.");
            }
            partyDataMap.remove(player);
            player.sendMessage("Die Party wurde aufgelöst.");
        }
    }

    public void acceptInvite(Player player, Player target) {
        PartyData partyData = partyDataMap.get(target);
        if (partyData == null || !partyData.invited.contains(player) || !pendingInvites.getOrDefault(player, Set.of()).contains(target)) {
            player.sendMessage("Die Einladung von " + target.getName() + " existiert nicht.");
            return;
        }
        partyData.members.add(player);
        partyDataMap.put(player, partyData);
        partyData.invited.remove(player);
        pendingInvites.getOrDefault(player, Set.of()).remove(target);
        if (pendingInvites.getOrDefault(player, Set.of()).isEmpty()) {
            pendingInvites.remove(player);
        }
    }

    public List<Player> getPartyMembers(Player player) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            return List.of();
        }
        List<Player> returnList = new ArrayList<>();
        returnList.add(partyData.leader);
        returnList.addAll(partyData.members);
        return returnList;
    }

    public boolean isInParty(Player player) {
        PartyData partyData = partyDataMap.get(player);
        return partyData != null;
    }

    public boolean isPartyLeader(Player player) {
        PartyData partyData = partyDataMap.get(player);
        return partyData != null && partyData.leader.equals(player);
    }

    public Set<Player> getPendingInvites(Player player) {
        return pendingInvites.getOrDefault(player, Set.of());
    }

    public Player getPartyLeader(Player player) {
        PartyData partyData = partyDataMap.get(player);
        return partyData != null ? partyData.leader : null;
    }
}
