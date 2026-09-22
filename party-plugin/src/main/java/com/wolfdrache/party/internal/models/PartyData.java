package com.wolfdrache.party.internal.models;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PartyData {
    public Player leader;
    public List<Player> members = new ArrayList<>(); 
    public List<Player> invited = new ArrayList<>(); 

    public PartyData(Player leader) {
        this.leader = leader;
    }
}
