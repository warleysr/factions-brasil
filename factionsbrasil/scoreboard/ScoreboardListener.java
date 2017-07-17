package factionsbrasil.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import factionsbrasil.FactionsBrasil;
import factionsbrasil.event.PlayerJoinFactionEvent;
import factionsbrasil.event.PlayerLeaveFactionEvent;

public class ScoreboardListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(ScoreboardHandler.getScoreboard());
		ScoreboardHandler.update(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onJoinFaction(PlayerJoinFactionEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(FactionsBrasil.getPlugin(), new Runnable() {
			@Override
			public void run() {
				ScoreboardHandler.update(e.getPlayer());
			}
		}, 5L);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onLeaveFaction(PlayerLeaveFactionEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(FactionsBrasil.getPlugin(), new Runnable() {
			@Override
			public void run() {
				ScoreboardHandler.update(e.getPlayer());
			}
		}, 5L);
	}

}
