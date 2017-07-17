package factionsbrasil.slums;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;

import factionsbrasil.Faction;
import factionsbrasil.FactionList;
import factionsbrasil.FactionsBrasil;
import factionsbrasil.utils.Utils;

public class SlumManager {
	
	private static HashMap<String, Slum> slums = new HashMap<>();
	
	public static void loadSlums() {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("SELECT * FROM `slums`;");
			
			ResultSet rs = st.executeQuery();
			int colId = rs.findColumn("id");
			int colName = rs.findColumn("name");
			int colOwner = rs.findColumn("owner");
			int colFaction = rs.findColumn("faction");
			int colCC = rs.findColumn("chunk_coord");
			int colMembers = rs.findColumn("members");
			
			while (rs.next()) {
				int id = rs.getInt(colId);
				String name = rs.getString(colName);
				String owner = rs.getString(colOwner);
				
				String cc = rs.getString(colCC);
				Chunk chunk = Utils.fromChunkCoord(cc);
				
				Faction faction = FactionList.getById(rs.getInt(colFaction));
				
				String membersStr = rs.getString(colMembers);
				List<String> members = new ArrayList<>();
				if (!(membersStr.isEmpty())) {
					String[] splitted = membersStr.split(",");
					members = new ArrayList<>(Arrays.asList(splitted));
				}
				
				Slum slum = new Slum(id, name, owner, chunk, faction, members);
				
				slums.put(cc, slum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void registerSlum(Slum slum) {
		slums.put(Utils.toChunkCoord(slum.getChunk()), slum);
	}
	
	public static void deleteSlum(Slum slum) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("DELETE FROM `slums` "
					+ "WHERE `id` = ?;");
			st.setInt(1, slum.getId());
			st.executeUpdate();
			
			slums.remove(Utils.toChunkCoord(slum.getChunk()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Slum getSlumAt(Location loc) {
		return slums.get(Utils.toChunkCoord(loc));
	}
	
	public static Slum getSlumAt(Chunk chunk) {
		return slums.get(Utils.toChunkCoord(chunk));
	}
	
	public static boolean hasSlumAt(Location loc) {
		return getSlumAt(loc) != null;
	}
	
	public static boolean isSlum(Chunk chunk) {
		return getSlumAt(chunk) != null;
	}
	
	public static Faction getFactionAt(Location loc) {
		return getSlumAt(loc).getFaction();
	}
	
	public static Faction getFactionAt(Chunk chunk) {
		return getSlumAt(chunk).getFaction();
	}
	
	public static int getSlumCount(String player) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("SELECT COUNT(*) AS `count` "
					+ " FROM `slums` WHERE `owner` = ?;");
			st.setString(1, player);
			
			ResultSet rs = st.executeQuery();
			return rs.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static List<Slum> getSlums(String player) {
		List<Slum> playerSlums = new ArrayList<>();
		
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("SELECT `chunk_coord` "
					+ "FROM `slums` WHERE `owner` = ?;");
			st.setString(1, player);
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Slum slum = slums.get(rs.getString(1));
				if (slum == null) continue;
				playerSlums.add(slum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return playerSlums;
	}
	
	public static List<Slum> getSlumsWhereMember(String player) {
	List<Slum> memberSlums = new ArrayList<>();
		
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("SELECT `chunk_coord` "
					+ "FROM `slums` WHERE `members` LIKE ?;");
			st.setString(1, "%" + player + "%");
			
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Slum slum = slums.get(rs.getString(1));
				if (slum == null) continue;
				memberSlums.add(slum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return memberSlums;
	}
	
	public static boolean isConnectedEnemy(Chunk chunk, Faction faction) {
		int x = chunk.getX(), z = chunk.getZ();
		Chunk c1 = chunk.getWorld().getChunkAt(x, z + 1);
		Chunk c2 = chunk.getWorld().getChunkAt(x, z - 1);
		Chunk c3 = chunk.getWorld().getChunkAt(x + 1, z);
		Chunk c4 = chunk.getWorld().getChunkAt(x - 1, z);
		Chunk c5 = chunk.getWorld().getChunkAt(x + 1, z + 1);
		Chunk c6 = chunk.getWorld().getChunkAt(x - 1, z - 1);
		Chunk[] relative = {c1, c2, c3, c4, c5, c6};
		for (Chunk c : relative)
			if (isSlum(c) && !(getFactionAt(c).equals(faction)))
				return true;
		return false;
	}
	
}
