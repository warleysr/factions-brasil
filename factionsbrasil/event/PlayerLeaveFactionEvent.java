package factionsbrasil.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import factionsbrasil.Faction;

public class PlayerLeaveFactionEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Faction faction;
	private boolean cancelled;
	
	public PlayerLeaveFactionEvent(Player player, Faction faction) {
		this.player = player;
		this.faction = faction;
	}
	
	public Player getPlayer() {
		return player;
	}

	public Faction getFaction() {
		return faction;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
