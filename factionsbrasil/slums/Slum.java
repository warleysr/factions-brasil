package factionsbrasil.slums;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import factionsbrasil.Faction;
import factionsbrasil.FactionsBrasil;
import factionsbrasil.event.PlayerDominateSlumEvent;
import factionsbrasil.player.FPlayers;
import factionsbrasil.utils.Utils;

public class Slum {
	
	private int id;
	private String name;
	private String owner;
	private Chunk chunk;
	private Faction faction;
	private List<String> members;
	
	public Slum(int id, String name, String owner, Chunk chunk, Faction faction, List<String> members) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.chunk = chunk;
		this.faction = faction;
		if (members != null)
			this.members = members;
		else
			this.members = new ArrayList<>();
		
		PlayerDominateSlumEvent event = new PlayerDominateSlumEvent(Bukkit.getPlayerExact(owner), this, true, null);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		
		if (id == -1) {
			try {
				PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("INSERT INTO `slums` "
						+ "(`name`, `owner`, `faction`, `chunk_coord`, `members`) VALUES (?, ?, ?, ?, ?);");
				st.setString(1, name);
				st.setString(2, owner);
				st.setInt(3, faction.getId());
				st.setString(4, Utils.toChunkCoord(chunk));
				st.setString(5, "");
				st.executeUpdate();
				
				SlumManager.registerSlum(this);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public boolean isOwner(Player p) {
		return owner.equals(p.getName());
	}
	
	public boolean setName(String name) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("UPDATE `slums` SET `name` = ? "
					+ "WHERE `id` = ?;");
			st.setString(1, name);
			st.setInt(2, id);
			st.executeUpdate();
			
			this.name = name;
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setOwner(Player p) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("UPDATE `slums` SET `owner` = ? "
					+ "AND `faction` = ? WHERE `id` = ?;");
			Faction f = FPlayers.get(p).getFaction();
			st.setString(1, p.getName());
			st.setInt(2, f.getId());
			st.setInt(3, id);
			st.executeUpdate();
			
			owner = p.getName();
			faction = f;
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public Faction getFaction() {
		return faction;
	}
	
	public List<String> getMembers() {
		return members;
	}
	
	public boolean isMember(Player p) {
		return members.contains(p.getName());
	}
	
	public void addMember(String member) {
		members.add(member);
		
		updateMembers();
	}
	
	public void removeMember(String member) {
		members.remove(member);
		
		updateMembers();
	}
	
	private void updateMembers() {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("UPDATE `slums` SET `members` "
					+ "= ? WHERE `id` = ?;");
			st.setString(1, toMembersList());
			st.setInt(2, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public double getPower() {
		double money = FactionsBrasil.getEconomy().getBalance(owner);
		int slumCount = SlumManager.getSlumCount(owner);
		double playerPower =  FPlayers.get(owner).getPower();
		
		int perSlumMoney = (int) money / slumCount;
		
		return (perSlumMoney / 100000) * playerPower;
	}
	
	public void delete() {
		SlumManager.deleteSlum(this);
	}
	
	private String toMembersList() {
		String str = "";
		for (String member : members)
			str += "," + member;
		return str.replaceFirst(",", "");
	}
	
}
