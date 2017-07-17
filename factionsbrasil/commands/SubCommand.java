package factionsbrasil.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
	
	private String description;
	private String permission;
	
	public SubCommand(String permission, String description) {
		this.permission = permission;
		this.description = description;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean hasPermission(CommandSender sender) {
		if (permission == null) return true;
		return sender.hasPermission(permission);
	}
	
	public void run(CommandSender sender, String[] args) {}
	
}
