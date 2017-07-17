package factionsbrasil.player;

import org.bukkit.Bukkit;

import factionsbrasil.Faction;

public class FPlayer {
	
	private String name;
	private Role role;
	private Faction faction;
	private double power;
	
	public FPlayer(String name, Faction faction, Role role, double power) {
		this.name = name;
		this.faction = faction;
		this.role = role;
		this.power = power;
	}

	public String getName() {
		return name;
	}

	public Faction getFaction() {
		return faction;
	}
	
	public Role getRole() {
		return role;
	}
	
	public double getPower() {
		return power;
	}
	
	public void setPower(double newPower) {
		this.power = newPower;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public boolean isLeader() {
		return role == Role.LIDER;
	}
	
	public boolean isOnline() {
		return Bukkit.getPlayerExact(name) != null;
	}
	
	public void save() {
		FPlayers.savePlayer(this);
	}
	
	public void destroy() {
		FPlayers.destroyPlayer(this);
	}

}
