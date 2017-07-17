package factionsbrasil.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import factionsbrasil.drugs.DrugType;

public class PlayerUseDrugEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private DrugType type;
	private boolean cancelled;
	
	public PlayerUseDrugEvent(Player player, DrugType type) {
		this.player = player;
		this.type = type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public DrugType getType() {
		return type;
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
