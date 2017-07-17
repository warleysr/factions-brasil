package factionsbrasil.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import factionsbrasil.Messages;
import factionsbrasil.player.FPlayer;
import factionsbrasil.player.FPlayers;
import factionsbrasil.slums.Slum;
import factionsbrasil.slums.SlumManager;
import factionsbrasil.utils.Utils;

public class SlumCommand extends SubCommand {

	public SlumCommand(String permission, String description) {
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
			Messages.send(p, "ajuda-favela");
			return;
		}
		Location loc = p.getLocation();
		if (args[0].equalsIgnoreCase("nome")) {
			if (args.length < 2) {
				Messages.send(p, "ajuda-favela-nome");
				return;
			}
			if (!(SlumManager.hasSlumAt(loc))) {
				Messages.send(p, "nenhuma-favela");
				return;
			}
			Slum slum = SlumManager.getSlumAt(loc);
			if (!(slum.isOwner(p))) {
				Messages.send(p, "nao-e-dono");
				return;
			}
			
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i]);
				sb.append(" ");
			}
			String name = sb.toString().trim();
			
			boolean b = slum.setName(name);
			if (!(b)) {
				return;
			}
			
			p.sendMessage(Messages.get("nome-trocado")
					.replace("@nome", name));
			
		} else if (args[0].equalsIgnoreCase("info")) {
			if (!(SlumManager.hasSlumAt(loc))) {
				Messages.send(p, "nenhuma-favela");
				return;
			}
			Slum slum = SlumManager.getSlumAt(loc);
			
			String members = slum.getMembers().isEmpty() 
					? "Ninguem" 
					: slum.getMembers().toString().replace("[", "").replace("]", "");
			
			p.sendMessage(Messages.get("favela-info")
					.replace("@nome", slum.getName())
					.replace("@dono", slum.getOwner())
					.replace("@faccao", slum.getFaction().getTag())
					.replace("@membros", members));
			
		} else if (args[0].equalsIgnoreCase("adicionar")) {
			if (args.length < 2) {
				Messages.send(p, "ajuda-favela-adicionar");
				return;
			}
			if (!(SlumManager.hasSlumAt(loc))) {
				Messages.send(p, "nenhuma-favela");
				return;
			}
			Slum slum = SlumManager.getSlumAt(loc);
			if (!(slum.isOwner(p))) {
				Messages.send(p, "nao-e-dono");
				return;
			}
			if (!(Utils.hasFaction(args[1]))) {
				Messages.send(p, "nao-possui-faccao-outros");
				return;
			}
			
			FPlayer fp = FPlayers.get(p);
			FPlayer friend = FPlayers.get(args[1]);
			
			if (fp.equals(friend)) {
				Messages.send(p, "voce-mesmo");
				return;
			}
			if (!(friend.getFaction().equals(fp.getFaction()))) {
				Messages.send(p, "outra-faccao");
				return;
			}
			if (slum.getMembers().contains(friend.getName())) {
				Messages.send(p, "ja-adicionado");
				return;
			}
			if (slum.getMembers().size() == 5) {
				Messages.send(p, "limite-membros");
				return;
			}
			
			slum.addMember(friend.getName());
			
			p.sendMessage(Messages.get("adicionado")
					.replace("@jogador", friend.getName())
					.replace("@favela", slum.getName()));
			
		} else if (args[0].equalsIgnoreCase("remover")) {
			if (args.length < 2) {
				Messages.send(p, "ajuda-favela-remover");
				return;
			}
			if (!(SlumManager.hasSlumAt(loc))) {
				Messages.send(p, "nenhuma-favela");
				return;
			}
			Slum slum = SlumManager.getSlumAt(loc);
			if (!(slum.isOwner(p))) {
				Messages.send(p, "nao-e-dono");
				return;
			}
			if (!(Utils.hasFaction(args[1]))) {
				Messages.send(p, "nao-adicionado");
				return;
			}
			
			FPlayer fp = FPlayers.get(p);
			FPlayer friend = FPlayers.get(args[1]);
			
			if (fp.equals(friend)) {
				Messages.send(p, "voce-mesmo");
				return;
			}
			if (!(slum.getMembers().contains(friend.getName()))) {
				Messages.send(p, "nao-adicionado");
				return;
			}
			
			slum.removeMember(friend.getName());
			
			p.sendMessage(Messages.get("removido")
					.replace("@jogador", friend.getName())
					.replace("@favela", slum.getName()));
		}
	}
	
}
