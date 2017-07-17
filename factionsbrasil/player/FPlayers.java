package factionsbrasil.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import factionsbrasil.Faction;
import factionsbrasil.FactionList;
import factionsbrasil.FactionsBrasil;

public class FPlayers {
	
	private static HashMap<String, FPlayer> players = new HashMap<>();
	
	public static FPlayer get(String name) {
		return players.get(name.toLowerCase());
	}
	
	public static FPlayer get(Player p) {
		return get(p.getName());
	}
	
	public static void loadPlayers() {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("SELECT * FROM `players`;");
			
			ResultSet rs = st.executeQuery();
			
			int colPlayer = rs.findColumn("player");
			int colFaction = rs.findColumn("faction");
			int colRole = rs.findColumn("role");
			int colPower = rs.findColumn("power");
			
			while (rs.next()) {
				String player = rs.getString(colPlayer);
				Faction faction = FactionList.getById(rs.getInt(colFaction));
				Role role = Role.getById(rs.getInt(colRole));
				double power = rs.getDouble(colPower);
				
				FPlayer fp = new FPlayer(player, faction, role, power);
				
				faction.addMember(fp);
				
				if (role == Role.LIDER)
					faction.setLeader(fp);
				
				players.put(player.toLowerCase(), fp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void savePlayer(FPlayer fp) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("INSERT INTO `players` "
					+ "(`player`, `faction`, `role`) VALUES (?, ?, ?) ON DUPLICATE KEY "
					+ "UPDATE `role` = ?, `power` = ?;");
			st.setString(1, fp.getName());
			st.setInt(2, fp.getFaction().getId());
			st.setInt(3, fp.getRole().getId());
			st.setInt(4, fp.getRole().getId());
			st.setDouble(5, fp.getPower());
			st.executeUpdate();
			
			players.put(fp.getName().toLowerCase(), fp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void destroyPlayer(FPlayer fp) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("DELETE FROM `players` "
					+ "WHERE `player` = ?");
			st.setString(1, fp.getName());
			st.executeUpdate();
			
			players.remove(fp.getName().toLowerCase());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
