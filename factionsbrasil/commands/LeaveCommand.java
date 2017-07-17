package factionsbrasil.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import factionsbrasil.Faction;
import factionsbrasil.Messages;
import factionsbrasil.event.PlayerLeaveFactionEvent;
import factionsbrasil.player.FPlayer;
import factionsbrasil.player.FPlayers;
import factionsbrasil.utils.Utils;

public class LeaveCommand extends SubCommand {

	public LeaveCommand(String permission, String description) {
		super(permission, description);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Messages.send(sender, "apenas-players");
			return;
		}
		Player p = (Player) sender;
		if (!(Utils.hasFaction(p))) {
			Messages.send(p, "nao-possui-faccao");
			return;
		}
		FPlayer fp = FPlayers.get(p);
		Faction f = fp.getFaction();
		
		PlayerLeaveFactionEvent event = new PlayerLeaveFactionEvent(p, f);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		
		fp.destroy();
		
		Messages.send(p, "saiu-faccao");
	}

}
