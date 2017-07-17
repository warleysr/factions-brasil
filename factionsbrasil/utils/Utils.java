package factionsbrasil.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import factionsbrasil.player.FPlayers;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	
	public static String color(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static boolean hasFaction(Player player) {
		return FPlayers.get(player.getName()) != null;
	}
	
	public static boolean hasFaction(String player) {
		return FPlayers.get(player) != null;
	}
	
	public static String toChunkCoord(Location loc) {
		return toChunkCoord(loc.getChunk());
	}
	
	public static String toChunkCoord(Chunk chunk) {
		 return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
	}
	
	public static Chunk fromChunkCoord(String str) {
		String[] args = str.split(";");
		World w = Bukkit.getWorld(args[0]);
		int x = Integer.valueOf(args[1]), z = Integer.valueOf(args[2]);
		return w.getChunkAt(x, z);
	}

}
