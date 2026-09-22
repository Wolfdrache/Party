package com.wolfdrache.party.internal.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.wolfdrache.party.internal.helper.MessageHelper;
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
        MessageHelper.sendPartyInviteMessage(player, target);
        partyData.invited.add(target);
        
        pendingInvites.computeIfAbsent(target, k -> new HashSet<>()).add(player);
    }

    public void kickPlayer(Player player, Player target) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            return;
        }
        if (partyData.leader.equals(player)) {
            for (Player member : getPartyMembers(player)) {
                MessageHelper.sendMessage(member, target.getName() + " §cwurde aus der Party entfernt.");
            }
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
            MessageHelper.sendMessage(member, player.getName() + " §chat die Party verlassen.");
        }

        if (partyData.leader.equals(player)) {
            if (!partyData.members.isEmpty()) {
                Player newLeader = null;
                newLeader = partyData.members.get(0);
                partyData.members.remove(0);
                partyData.leader = newLeader;
                for (Player member : getPartyMembers(player)) {
                    MessageHelper.sendMessage(member, "§a" + newLeader.getName() + " ist nun der Party-Leader.");
                }
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
            for (Player member : getPartyMembers(player)) {
                partyDataMap.remove(member);
                MessageHelper.sendMessage(member, "§cDie Party wurde aufgelöst.");
            }
        }
    }

    public void acceptInvite(Player player, Player target) {
        if (isInParty(player)) {
            leaveParty(player);
        }
        PartyData partyData = partyDataMap.get(target);
        if (partyData == null || !partyData.invited.contains(player) || !pendingInvites.getOrDefault(player, Set.of()).contains(target)) {
            MessageHelper.sendMessage(player, "§cDie Einladung von " + target.getName() + " existiert nicht.");
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

    public void declineInvite(Player player, Player target) {
        PartyData partyData = partyDataMap.get(target);
        if (partyData == null || !partyData.invited.contains(player) || !pendingInvites.getOrDefault(player, Set.of()).contains(target)) {
            MessageHelper.sendMessage(player, "§cDie Einladung von " + target.getName() + " existiert nicht.");
            return;
        }
        partyData.invited.remove(player);
        pendingInvites.getOrDefault(player, Set.of()).remove(target);
        if (pendingInvites.getOrDefault(player, Set.of()).isEmpty()) {
            pendingInvites.remove(player);
        }
        MessageHelper.sendMessage(player, "§cDu hast die Einladung von " + target.getName() + " abgelehnt.");
    }
}
