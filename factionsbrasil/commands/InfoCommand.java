package factionsbrasil.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import factionsbrasil.Messages;
import factionsbrasil.player.FPlayer;
import factionsbrasil.player.FPlayers;
import factionsbrasil.utils.Utils;

public class InfoCommand extends SubCommand {
	
	public InfoCommand(String permission, String description) {
		super(permission, description);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				Messages.send(sender, "uso-info");
				return;
			}
			Player p = (Player) sender;
			if (!(Utils.hasFaction(p))) {
				Messages.send(p, "nao-possui-faccao");
				return;
			}
			FPlayer fp = FPlayers.get(p);
			
			sendInfo(p, fp);
			
			return;
		}
		if (!(Utils.hasFaction(args[0]))) {
			Messages.send(sender, "nao-possui-faccao-outros");
			return;
		}
		FPlayer fp = FPlayers.get(args[0]);
		
		sendInfo(sender, fp);
	}
	
	private void sendInfo(CommandSender sender, FPlayer fp) {
		sender.sendMessage(Messages.get("info")
				.replace("@player", fp.getName())
				.replace("@faccao", fp.getFaction().getTag())
				.replace("@cargo", fp.getRole().name()));
	}

}
