package factionsbrasil;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import factionsbrasil.player.FPlayer;
import factionsbrasil.player.FPlayers;
import factionsbrasil.utils.Utils;

public class Faction {
	
	private int id;
	private String name;
	private String tag;
	private List<FPlayer> members = new ArrayList<>();
	private FPlayer leader;
	
	public Faction(int id, String name, String tag) {
		this.id = id;
		this.name = name;
		this.tag = Utils.color(tag);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void addMember(FPlayer player) {
		members.add(player);
	}
	
	public List<FPlayer> getMembers() {
		return members;
	}
	
	public boolean isMember(Player p) {
		FPlayer fp = FPlayers.get(p.getName());
		if (fp == null) return false;
		return members.contains(fp);
	}
	
	public FPlayer getLeader() {
		return leader;
	}
	
	public void setLeader(FPlayer leader) {
		this.leader = leader;
	}
	
}
