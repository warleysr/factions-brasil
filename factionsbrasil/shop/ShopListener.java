package factionsbrasil.shop;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import factionsbrasil.FactionsBrasil;
import factionsbrasil.Messages;
import factionsbrasil.drugs.DrugsListener;
import net.md_5.bungee.api.ChatColor;

public class ShopListener implements Listener {
	
	private static final HashMap<String, ItemStack> SELL = new HashMap<>();
	
	private static final String PRICE_REGEX1 = "^\\d{1,} : \\d{1,}$";
	private static final String PRICE_REGEX2 = "^\\d{1,}$";
	
	public ShopListener() {
		SELL.put("bicarbonato", DrugsListener.BICARBONATE);
		SELL.put("cocaina", DrugsListener.COCAINE);
		SELL.put("maconha", DrugsListener.CANNABIS);
		SELL.put("crack", DrugsListener.CRACK);
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		if (e.getLine(0).trim().equals("[FactionsBrasil]")) {
			Player p = e.getPlayer();
			if (!(p.hasPermission("factionsbrasil.admin"))) {
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				Messages.send(p, "sem-permissao");
				return;
			}
			if (!(SELL.containsKey(e.getLine(1).toLowerCase().trim()))) {
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				Messages.send(p, "item-invalido");
				return;
			}
			String price = e.getLine(2).trim();
			if (!(price.matches(PRICE_REGEX1)) && !(price.matches(PRICE_REGEX2))) {
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				Messages.send(p, "preco-invalido");
				return;
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if ((e.getAction().name().endsWith("BLOCK")) && (e.getClickedBlock().getState() instanceof Sign)) {
			
			Sign s = (Sign) e.getClickedBlock().getState();
			if (!(s.getLine(0).equals("[FactionsBrasil]"))) return;
			
			Player p = e.getPlayer();
			if (p.getGameMode() != GameMode.SURVIVAL) return;
			
			String price = s.getLine(2);
			
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				int buy = 0;
				
				if (price.matches(PRICE_REGEX1)) {
					String[] args = price.split(" : ");
					buy = Integer.valueOf(args[0]);
				} else
					buy = Integer.valueOf(price);
				
				if (!(FactionsBrasil.getEconomy().has(p, buy))) {
					Messages.send(p, "sem-dinheiro");
					return;
				}
				if (p.getInventory().firstEmpty() == -1) {
					Messages.send(p, "inventario-cheio");
					return;
				}
				
				FactionsBrasil.getEconomy().withdrawPlayer(p, buy);
				
				String item = ChatColor.stripColor(s.getLine(1));
				
				p.getInventory().addItem(SELL.get(item.toLowerCase()));
				
				p.sendMessage(Messages.get("comprou")
						.replace("@item", item)
						.replace("@preco", Integer.toString(buy)));
			} else {
				int sell = 0;
				
				if (price.matches(PRICE_REGEX1)) {
					String[] args = price.split(" : ");
					sell = Integer.valueOf(args[1]);
				} else {
					Messages.send(p, "somente-comprar");
					return;
				}
				ItemStack is = SELL.get(s.getLine(1).toLowerCase());
				
				int amt = 0;
				for (ItemStack inv : p.getInventory().getContents())
					if ((inv != null) && (inv.getType() == is.getType()) 
							&& inv.hasItemMeta() && inv.getItemMeta().equals(is.getItemMeta()))
						amt += inv.getAmount();
				
				if (amt == 0) {
					Messages.send(p, "nada-para-vender");
					return;
				}
				
				for (ItemStack inv : p.getInventory().getContents())
					if ((inv != null) && (inv.getType() == is.getType()) 
							&& inv.hasItemMeta() && inv.getItemMeta().equals(is.getItemMeta()))
						p.getInventory().remove(inv);
				
				int totalValue = amt * sell;
				FactionsBrasil.getEconomy().depositPlayer(p, totalValue);
				
				p.sendMessage(Messages.get("vendeu")
						.replace("@item", s.getLine(1))
						.replace("@quantidade", Integer.toString(amt))
						.replace("@total", Integer.toString(totalValue)));
			}
		}
	}

}
