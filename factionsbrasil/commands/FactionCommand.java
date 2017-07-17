package factionsbrasil.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import factionsbrasil.Messages;

public class FactionCommand implements CommandExecutor {
	
	private static HashMap<String, SubCommand> subCommands = new LinkedHashMap<>();
	
	public FactionCommand() {
		subCommands.put("aliar", new JoinCommand(null, "Se alia a uma faccao"));
		subCommands.put("sair", new LeaveCommand(null, "Abandona a faccao"));
		subCommands.put("info", new InfoCommand(null, "Ve suas informacoes ou de alguem"));
		subCommands.put("dominar", new DominateCommand(null, "Domina uma area"));
		subCommands.put("favela", new SlumCommand(null, "Gerencia uma favela"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("faccao")) {
			if (args.length == 0) {
				sendHelp(sender);
				return false;
			}
			SubCommand cmd = subCommands.get(args[0].toLowerCase());
			if (cmd == null) {
				sendHelp(sender);
				return false;
			}
			if (!(cmd.hasPermission(sender))) {
				Messages.send(sender, "sem-permissao");
				return false;
			}
			String[] subArgs = {};
			if (args.length >= 2)
				subArgs = Arrays.copyOfRange(args, 1, args.length);
			cmd.run(sender, subArgs);
		}
		return true;
	}
	
	private void sendHelp(CommandSender sender) {
		Messages.send(sender, "ajuda-cabecalho");
		for (Entry<String, SubCommand> entry : subCommands.entrySet()) {
			SubCommand cmd = entry.getValue();
			if (cmd.hasPermission(sender))
				sender.sendMessage(Messages.get("ajuda-corpo")
						.replace("@comando", entry.getKey())
						.replace("@descricao", cmd.getDescription()));
		}
	}
	
}
