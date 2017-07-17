package factionsbrasil;

import java.io.File;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import factionsbrasil.utils.Utils;

public class Messages {
	
	private static HashMap<String, String> messages = new HashMap<>();
	
	protected static void loadMessages() {
		File f = new File(FactionsBrasil.getPlugin().getDataFolder(), "mensagens.yml");
		if (!(f.exists()))
			FactionsBrasil.getPlugin().saveResource("mensagens.yml", false);
		FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		for (String key : fc.getKeys(false)) {
			if (fc.isList(key)) {
				String msg = "";
				for (String s : fc.getStringList(key))
					msg += s + "\n";
				messages.put(key, Utils.color(msg));
			} else
				messages.put(key, Utils.color(fc.getString(key)));
		}
	}
	
	public static void send(CommandSender sender, String... keys) {
		for (String key : keys)
			sender.sendMessage(get(key));
	}
	
	public static String get(String key) {
		return messages.containsKey(key) ? messages.get(key) : key + " - Erro. Contate um staff.";
	}

}
