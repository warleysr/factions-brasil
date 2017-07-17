package factionsbrasil.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import factionsbrasil.Faction;
import factionsbrasil.Messages;
import factionsbrasil.event.PlayerDominateSlumEvent;
import factionsbrasil.player.FPlayer;
import factionsbrasil.player.FPlayers;
import factionsbrasil.slums.Slum;
import factionsbrasil.slums.SlumManager;
import factionsbrasil.utils.Utils;

public class DominateCommand extends SubCommand {
	
	public DominateCommand(String permission, String description) {
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
		Location loc = p.getLocation();
		if (SlumManager.hasSlumAt(loc)) {
			Slum slum = SlumManager.getSlumAt(loc);
			if (slum.isOwner(p)) {
				Messages.send(p, "ja-e-dono");
				return;
			}
			if (slum.getFaction().equals(fp.getFaction())) {
				Messages.send(p, "mesma-faccao");
				return;
			}
			if (fp.getPower() <= slum.getPower()) {
				Messages.send(p, "sem-poder");
				return;
			}
			Faction previous = slum.getFaction();
			
			PlayerDominateSlumEvent event = new PlayerDominateSlumEvent(p, slum, false, previous);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) return;
			
			boolean b = slum.setOwner(p);
			if (!(b)) {
				Messages.send(p, "falha-dominar");
				return;
			}
			
			p.sendMessage(Messages.get("dominou-faccao")
					.replace("@faccao", previous.getTag()));
		} else {
			if (SlumManager.isConnectedEnemy(loc.getChunk(), fp.getFaction())) {
				p.sendMessage("Voce ta conectado ao inimigo.");
				return;
			}
			Slum slum = new Slum(-1, "Sem nome", p.getName(), loc.getChunk(), fp.getFaction(), null);
			Slum check = SlumManager.getSlumAt(loc);
			if (slum.equals(check)) {
				Messages.send(p, "dominou-livre");
			}
		}
	}

}
