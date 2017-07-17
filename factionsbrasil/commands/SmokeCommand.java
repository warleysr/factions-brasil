package factionsbrasil.commands;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import factionsbrasil.FactionsBrasil;
import factionsbrasil.Messages;
import factionsbrasil.drugs.DrugType;
import factionsbrasil.drugs.DrugsListener;
import factionsbrasil.event.PlayerUseDrugEvent;

public class SmokeCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("fumar")) {
			if (!(sender instanceof Player)) {
				return false;
			}
			Player p = (Player) sender;
			ItemStack is = p.getInventory().getItemInMainHand();
			if (DrugsListener.isCannabis(is) || DrugsListener.isCrack(is)) {
				boolean cannabis = DrugsListener.isCannabis(is);
				
				DrugType type = cannabis ? DrugType.CANNABIS : DrugType.CRACK;
				PlayerUseDrugEvent event = new PlayerUseDrugEvent(p, type);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled()) return false;
				
				int amount = is.getAmount();
				if ((amount - 1) == 0)
					p.getInventory().setItemInMainHand(null);
				else
					is.setAmount(amount - 1);
				
				if (cannabis) {
					startNewTask(p);
					p.addPotionEffects(DrugsListener.CANNABIS_EFFECTS);
					Messages.send(p, "fumou-maconha");
				} else {
					p.addPotionEffects(DrugsListener.CRACK_EFFECTS);
					Messages.send(p, "fumou-crack");
				}
			} else {
				Messages.send(p, "sem-maconha-crack");
			}
			
		}
		return true;
	}
	
	private void startNewTask(Player p) {
		if (inCannabisEffects(p)) return;
		
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				p.spawnParticle(Particle.SPELL_MOB, p.getEyeLocation(), 8, 1, 0, 1);
			}
		}.runTaskTimer(FactionsBrasil.getPlugin(), 0L, 20L);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				task.cancel();
			}
		}.runTaskLater(FactionsBrasil.getPlugin(), 600L);
	}
	
	private boolean inCannabisEffects(Player p) {
		for (PotionEffect pe : DrugsListener.CANNABIS_EFFECTS)
			if (!(p.hasPotionEffect(pe.getType())))
				return false;
		return true;
	}

}
