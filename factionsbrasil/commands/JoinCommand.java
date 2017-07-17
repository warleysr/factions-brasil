package factionsbrasil.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import factionsbrasil.Faction;
import factionsbrasil.FactionList;
import factionsbrasil.Messages;
import factionsbrasil.event.PlayerJoinFactionEvent;
import factionsbrasil.player.FPlayer;
import factionsbrasil.player.Role;
import factionsbrasil.utils.Utils;

public class JoinCommand extends SubCommand {
	
	public JoinCommand(String permission, String description) {
		super(permission, description);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Messages.send(sender, "apenas-players");
			return;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			Messages.send(p, "uso-aliar");
			return;
		}
		Faction f = FactionList.getByName(args[0]);
		if (f == null) {
			Messages.send(p, "faccao-invalida");
			return;
		}
		if (Utils.hasFaction(p)) {
			Messages.send(p, "ja-possui-faccao");
			return;
		}
		PlayerJoinFactionEvent event = new PlayerJoinFactionEvent(p, f);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		
		FPlayer fp = new FPlayer(p.getName(), f, Role.TRAFICANTE, 0);
		fp.save();
		
		p.sendMessage(Messages.get("aliou-faccao").replace("@faccao", f.getTag()));
	}

}
