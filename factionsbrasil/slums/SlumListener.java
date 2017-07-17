package factionsbrasil.slums;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import factionsbrasil.Messages;
import factionsbrasil.event.PlayerDominateSlumEvent;
import factionsbrasil.event.PlayerLeaveFactionEvent;
import factionsbrasil.utils.Utils;

public class SlumListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {
		e.setCancelled(checkEvent(e, e.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		e.setCancelled(checkEvent(e, e.getPlayer()));
		e.getPlayer().updateInventory();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			if (!(Utils.hasFaction(p))) {
				Messages.send(p, "nao-possui-faccao");
				return;
			}
			Location loc = p.getLocation();
			if (SlumManager.hasSlumAt(loc)) {
				Slum slum = SlumManager.getSlumAt(loc);
				if (!(slum.isOwner(p)) && !(slum.isMember(p))) {
					e.setCancelled(true);
					Messages.send(p, "sem-acesso");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onDominate(PlayerDominateSlumEvent e) {
		if (e.isCreated()) {
			Chunk chunk = e.getSlum().getChunk();
			int x = chunk.getX() << 4;
			int z = chunk.getZ() << 4;
			Block b1 = chunk.getWorld().getHighestBlockAt(x, z);
			Block b2 = chunk.getWorld().getHighestBlockAt(x + 15, z);
			Block b3 = chunk.getWorld().getHighestBlockAt(x, z + 15);
			Block b4 = chunk.getWorld().getHighestBlockAt(x + 15, z + 15);
			for (Block b : new Block[]{b1, b2, b3, b4})
				b.setType(Material.GLASS);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onLeaveFaction(PlayerLeaveFactionEvent e) {
		for (Slum slum : SlumManager.getSlums(e.getPlayer().getName()))
			slum.delete();
		
		for (Slum slum : SlumManager.getSlumsWhereMember(e.getPlayer().getName()))
			slum.removeMember(e.getPlayer().getName());
	}
	
	private boolean checkEvent(BlockEvent event, Player p) {
		if (!(Utils.hasFaction(p))) {
			Messages.send(p, "nao-possui-faccao");
			return true;
		}
		Location loc = event.getBlock().getLocation();
		if (SlumManager.hasSlumAt(loc)) {
			Slum slum = SlumManager.getSlumAt(loc);
			if (!(slum.isOwner(p)) && !(slum.isMember(p))) {
				Messages.send(p, "sem-acesso");
				return true;
			}
		} else {
			Messages.send(p, "dominar");
			return true;
		}
		return false;
	}

}
