package factionsbrasil.drugs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import factionsbrasil.FactionsBrasil;

public class CannabisActivator {
	
	private static HashMap<Block, CannabisActivator> activators = new HashMap<>();
	
	private int state;
	private Block block;
	
	public CannabisActivator(Block block, boolean create) {
		this.block = block;
		
		try {
			if (create) {
				PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("INSERT INTO `activators` "
						+ "(`state`, `world`, `x`, `y`, `z`) VALUES (0, ?, ?, ?, ?);");
				st.setString(1, block.getWorld().getName());
				st.setInt(2, block.getX());
				st.setInt(3, block.getY());
				st.setInt(4, block.getZ());
				st.executeUpdate();
				
				state = 0;
			}
			
			activators.put(block, this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getState() {
		return state;
	}
	
	public Block getBlock() {
		return block;
	}
	
	@SuppressWarnings("deprecation")
	public void upgradeState() {
		if ((block.getType() == Material.SPONGE) && (block.getData() == 1)) {
			if (state == 5) {
				growChunk();
				state = 0;
			} else
				state++;
		} else
			removeActivator(block);
	}
	
	@SuppressWarnings("deprecation")
	public void growChunk() {
		int x = block.getX(), z = block.getZ();
		int minX = x - 8, minZ = z - 8;
		int maxX = x + 8, maxZ = z + 8;
		for (int xx = minX; xx <= maxX; xx++) {
			for (int zz = minZ; zz <= maxZ; zz++) {
				if ((xx == x) && (zz == z)) continue;
				Block b = block.getWorld().getBlockAt(xx, block.getY() + 1, zz);
				if ((b.getType() == Material.LONG_GRASS) && (b.getData() == 2)) {
					b.setType(Material.DOUBLE_PLANT);
					b.setData((byte) 3);
					Block b2 = b.getRelative(BlockFace.UP);
					b2.setType(Material.DOUBLE_PLANT);
					b2.setData((byte) 10);
				}
			}
		}
	}
	
	public static void startTask() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Collection<CannabisActivator> values = activators.values();
				for (CannabisActivator ca : values.toArray(new CannabisActivator[values.size()]))
					ca.upgradeState();
			}
		}.runTaskTimer(FactionsBrasil.getPlugin(), 100L, 100L);
	}
	
	public static void loadAll(World w) {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("SELECT * FROM `activators`"
					+ " WHERE `world` = ?;");
			st.setString(1, w.getName());
			
			ResultSet rs = st.executeQuery();
			int colState = rs.findColumn("state");
			int colX = rs.findColumn("x"), colY = rs.findColumn("y"), colZ = rs.findColumn("z");
			while (rs.next()) {
				int state = rs.getInt(colState);
				int x = rs.getInt(colX), y = rs.getInt(colY), z = rs.getInt(colZ);
				CannabisActivator ca = new CannabisActivator(w.getBlockAt(x, y, z), false);
				ca.state = state;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveAll() {
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("UPDATE `activators` SET `state` "
					+ "= ? WHERE `world` = ? AND `x` = ? AND `y` = ? AND `z` = ?;");
			
			Collection<CannabisActivator> values = activators.values();
			for (CannabisActivator ca : values.toArray(new CannabisActivator[values.size()])) {
				st.setInt(1, ca.getState());
				st.setString(2, ca.getBlock().getWorld().getName());
				st.setInt(3, ca.getBlock().getX());
				st.setInt(4, ca.getBlock().getY());
				st.setInt(5, ca.getBlock().getZ());
				st.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeActivator(Block block) {
		activators.remove(block);
		
		try {
			PreparedStatement st = FactionsBrasil.getConnection().prepareStatement("DELETE FROM `activators` "
					+ "WHERE `world` = ? AND `x` = ? AND `y` = ? AND `z` = ?;");
			st.setString(1, block.getWorld().getName());
			st.setInt(2, block.getX());
			st.setInt(3, block.getY());
			st.setInt(4, block.getZ());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
