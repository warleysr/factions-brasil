package factionsbrasil.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import factionsbrasil.Faction;
import factionsbrasil.slums.Slum;

public class PlayerDominateSlumEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Slum slum;
	private boolean created;
	private Faction faction;
	private boolean cancelled;
	
	public PlayerDominateSlumEvent(Player player, Slum slum, boolean created, Faction faction) {
		this.player = player;
		this.slum = slum;
		this.created = created;
		this.faction = faction;
	}
	
	public Player getPlayer() {
		return player;
	}

	public Slum getSlum() {
		return slum;
	}

	public boolean isCreated() {
		return created;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}
