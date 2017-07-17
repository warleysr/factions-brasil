package factionsbrasil.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import factionsbrasil.Messages;
import factionsbrasil.drugs.DrugType;
import factionsbrasil.drugs.DrugsListener;
import factionsbrasil.event.PlayerUseDrugEvent;

public class SniffCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("cheirar")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player p = (Player) sender;
			ItemStack is = p.getInventory().getItemInMainHand();
			if (!(DrugsListener.isCocaine(is))) {
				Messages.send(p, "sem-cocaina");
				return false;
			}
			
			PlayerUseDrugEvent event = new PlayerUseDrugEvent(p, DrugType.COCAINE);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) return false;
			
			int amount = is.getAmount();
			if ((amount - 1) == 0)
				p.getInventory().setItemInMainHand(null);
			else
				is.setAmount(amount - 1);
			
			p.addPotionEffects(DrugsListener.COCAINE_EFFECTS);
			
			Messages.send(p, "cheirou");
		}
		return true;
	}

}
