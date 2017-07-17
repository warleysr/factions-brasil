package factionsbrasil.chat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import factionsbrasil.FactionsBrasil;
import factionsbrasil.player.FPlayers;
import factionsbrasil.utils.Utils;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(ChatMessageEvent e) {
		if (e.getTags().contains("faction") && Utils.hasFaction(e.getSender()))
			e.setTagValue("faction", FPlayers.get(e.getSender()).getFaction().getTag());
		
		FileConfiguration cfg = FactionsBrasil.getPlugin().getConfig();
		if (e.getTags().contains("reidofumo") && cfg.getString("ReiDoFumo").equals(e.getSender().getName()))
			e.setTagValue("reidofumo", Utils.color(cfg.getString("Tags.rei-do-fumo")));
		
		if (e.getTags().contains("reidopo") && cfg.getString("ReiDoPo").equals(e.getSender().getName()))
			e.setTagValue("reidopo", Utils.color(cfg.getString("Tags.rei-do-po")));
		
		if (e.getTags().contains("reidocrack") && cfg.getString("ReiDoCrack").equals(e.getSender().getName()))
			e.setTagValue("reidocrack", Utils.color(cfg.getString("Tags.rei-do-crack")));
	}

}
