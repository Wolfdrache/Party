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

    public void invitePlayer(Player player, Player invitedPlayer) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            partyData = new PartyData(player);
            partyDataMap.put(player, partyData);
        }
        if (!partyData.leader.equals(player)) {
            MessageHelper.sendMessage(player, "§cDu musst der Party-Leader sein, um Spieler einzuladen.");
            return;
        } else if (partyData.leader.equals(invitedPlayer) || partyData.members.contains(invitedPlayer)) {
            MessageHelper.sendMessage(player, "§c" + invitedPlayer.getName() + " ist bereits in der Party.");
            return;
        }
        MessageHelper.sendPartyInviteMessage(player, invitedPlayer);
        partyData.invited.add(invitedPlayer);
        
        pendingInvites.computeIfAbsent(invitedPlayer, k -> new HashSet<>()).add(player);
        for (Player member : getPartyMembers(player)) {
            MessageHelper.sendMessage(member, "§aEinladung an " + invitedPlayer.getName() + " gesendet.");
        }
    }

    public void kickPlayer(Player player, Player playerToKick) {
        PartyData partyData = partyDataMap.get(player);
        if (partyData == null) {
            return;
        }
        if (partyData.leader.equals(player)) {
            for (Player member : getPartyMembers(player)) {
                MessageHelper.sendMessage(member, playerToKick.getName() + " §cwurde aus der Party entfernt.");
            }
            partyData.members.remove(playerToKick);
            partyDataMap.remove(playerToKick);
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
            Player newLeader = null;
            Player oldLeader = partyData.leader;
            if (!partyData.members.isEmpty()) {
                newLeader = partyData.members.get(0);
                partyData.members.remove(0);
                for (Player member : getPartyMembers(player)) {
                    MessageHelper.sendMessage(member, "§a" + newLeader.getName() + " ist nun der Party-Leader.");
                }
                partyData.leader = newLeader;
            }
            for (Player invited : partyData.invited) {
                Set<Player> invites = pendingInvites.getOrDefault(invited, Set.of());
                invites.remove(oldLeader);
                if (newLeader != null) {
                    invites.add(newLeader);
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
            for (Player invited : partyData.invited) {
                Set<Player> invites = pendingInvites.getOrDefault(invited, Set.of());
                invites.remove(player);
                if (invites.isEmpty()) {
                    pendingInvites.remove(invited);
                }
            }
        }
    }

    public void acceptInvite(Player player, Player invitingPlayer) {
        if (isInParty(player)) {
            leaveParty(player);
        }
        PartyData partyData = partyDataMap.get(invitingPlayer);
        if (partyData == null || !partyData.invited.contains(player) || !pendingInvites.getOrDefault(player, Set.of()).contains(invitingPlayer)) {
            MessageHelper.sendMessage(player, "§cDie Einladung von " + invitingPlayer.getName() + " existiert nicht.");
            return;
        }
        partyData.members.add(player);
        partyDataMap.put(player, partyData);
        partyData.invited.remove(player);
        pendingInvites.getOrDefault(player, Set.of()).remove(invitingPlayer);
        if (pendingInvites.getOrDefault(player, Set.of()).isEmpty()) {
            pendingInvites.remove(player);
        }
        for (Player member : getPartyMembers(player)) {
            MessageHelper.sendMessage(member, player.getName() + " §aist der Party beigetreten.");
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

    public void declineInvite(Player player, Player invitingPlayer) {
        PartyData partyData = partyDataMap.get(invitingPlayer);
        if (partyData == null || !partyData.invited.contains(player) || !pendingInvites.getOrDefault(player, Set.of()).contains(invitingPlayer)) {
            MessageHelper.sendMessage(player, "§cDie Einladung von " + invitingPlayer.getName() + " existiert nicht.");
            return;
        }
        partyData.invited.remove(player);
        pendingInvites.getOrDefault(player, Set.of()).remove(invitingPlayer);
        if (pendingInvites.getOrDefault(player, Set.of()).isEmpty()) {
            pendingInvites.remove(player);
        }
        MessageHelper.sendMessage(player, "§cDu hast die Einladung von " + invitingPlayer.getName() + " abgelehnt.");
    }

    public void cancelPendingInvites(Player player) {
        Set<Player> invites = pendingInvites.remove(player);
        if (invites != null) {
            for (Player invitingPlayer : invites) {
                PartyData partyData = partyDataMap.get(invitingPlayer);
                if (partyData != null) {
                    partyData.invited.remove(player);
                }
            }
        }
    }
}
