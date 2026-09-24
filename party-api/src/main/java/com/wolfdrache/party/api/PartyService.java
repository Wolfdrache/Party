package com.wolfdrache.party.api;

import org.bukkit.entity.Player;
import java.util.List;

public interface PartyService {
    boolean isInParty(Player player);
    boolean isPartyLeader(Player player);
    List<Player> getPartyMembers(Player player);
    Player getPartyLeader(Player player);
    void invitePlayer(Player player, Player invitedPlayer);
    void kickPlayer(Player player, Player playerToKick);
    void leaveParty(Player player);
    void disbandParty(Player player);
    void acceptInvite(Player player, Player invitingPlayer);
}