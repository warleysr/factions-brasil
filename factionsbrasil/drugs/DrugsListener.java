package factionsbrasil.drugs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import factionsbrasil.Messages;
import factionsbrasil.utils.Utils;

public class DrugsListener implements Listener {
	
	public static final ItemStack COCA_LEAVES = new ItemStack(Material.SUGAR_CANE);
	public static final ItemStack COCAINE = new ItemStack(Material.SUGAR);
	public static final ItemStack CANNABIS = new ItemStack(Material.LONG_GRASS, 1, (short) 2);
	public static final ItemStack BICARBONATE = new ItemStack(Material.INK_SACK, 1, (short) 15);
	public static final ItemStack CRACK = new ItemStack(Material.PUMPKIN_SEEDS);
	
	public static final List<PotionEffect> COCAINE_EFFECTS = new ArrayList<>();
	public static final List<PotionEffect> CANNABIS_EFFECTS = new ArrayList<>();
	public static final List<PotionEffect> CRACK_EFFECTS = new ArrayList<>();
	
	public DrugsListener() {
		ItemMeta im = COCA_LEAVES.getItemMeta();
		im.setDisplayName(Utils.color("&rFolhas de Coca"));
		im.setLore(Arrays.asList(Utils.color("&7Erythroxylum coca")));
		COCA_LEAVES.setItemMeta(im);
		
		im = COCAINE.getItemMeta();
		im.setDisplayName(Utils.color("&rCocaina"));
		COCAINE.setItemMeta(im);
		
		im = CANNABIS.getItemMeta();
		im.setDisplayName(Utils.color("&rMaconha"));
		im.setLore(Arrays.asList(Utils.color("&7Cannabis sativa")));
		CANNABIS.setItemMeta(im);
		
		im = BICARBONATE.getItemMeta();
		im.setDisplayName(Utils.color("&rBicarbonato"));
		BICARBONATE.setItemMeta(im);
		
		im = CRACK.getItemMeta();
		im.setDisplayName(Utils.color("&rPedra de Crack"));
		CRACK.setItemMeta(im);
		
		COCAINE_EFFECTS.add(new PotionEffect(PotionEffectType.SPEED, 600, 1));
		COCAINE_EFFECTS.add(new PotionEffect(PotionEffectType.FAST_DIGGING, 600, 1));
		COCAINE_EFFECTS.add(new PotionEffect(PotionEffectType.JUMP, 600, 0));
		COCAINE_EFFECTS.add(new PotionEffect(PotionEffectType.BLINDNESS, 600, 0));
		
		CANNABIS_EFFECTS.add(new PotionEffect(PotionEffectType.HUNGER, 600, 0));
		CANNABIS_EFFECTS.add(new PotionEffect(PotionEffectType.SLOW, 600, 0));
		CANNABIS_EFFECTS.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 0));
		CANNABIS_EFFECTS.add(new PotionEffect(PotionEffectType.REGENERATION, 600, 0));
		
		CRACK_EFFECTS.add(new PotionEffect(PotionEffectType.WITHER, 600, 0));
		CRACK_EFFECTS.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0));
	}
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {
		if (e.getRecipe().getResult().getType() == Material.SUGAR)
			e.getInventory().setResult(COCAINE);
	}
	
	@EventHandler
	public void onSpawn(ItemSpawnEvent e) {
		if (e.getEntity().getItemStack().getType() == Material.SUGAR_CANE)
			e.getEntity().getItemStack().setItemMeta(COCA_LEAVES.getItemMeta());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBoneMeal(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem() 
				&& (e.getItem().getType() == Material.INK_SACK) && (e.getItem().getDurability() == 15)
				&& (e.getClickedBlock().getType() == Material.LONG_GRASS) 
				&& (e.getClickedBlock().getData() == 2))
			e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent e) {
		if (e.getBlock().getType() == Material.DOUBLE_PLANT) {
			byte data = e.getBlock().getData();
			if (data == 3) {
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				ItemStack drop = new ItemStack(CANNABIS);
				drop.setAmount(new Random().nextInt(4) + 2);
				e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
				
			} else if (data == 10) {
				if (e.getBlock().getRelative(BlockFace.DOWN).getData() == 3) {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					ItemStack drop = new ItemStack(CANNABIS);
					drop.setAmount(new Random().nextInt(4) + 2);
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop);
				}
			}
		} else if ((e.getBlock().getType() == Material.LONG_GRASS) && (e.getBlock().getData() == 2)) {
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), CANNABIS);
		
		} else if ((e.getBlock().getType() == Material.SPONGE) && (e.getBlock().getData() == 1))
			CannabisActivator.removeActivator(e.getBlock());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlaceActivator(BlockPlaceEvent e) {
		if ((e.getBlock().getType() == Material.SPONGE) && (e.getBlock().getData() == 1))
			new CannabisActivator(e.getBlock(), true);
	}
	
	@EventHandler
	public void onClickBrewer(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.BREWING_STAND) 
				&& isCocaine(p.getInventory().getItemInMainHand())) {
			e.setCancelled(true);
			
			int b = 0;
			int c = 0;
			
			for (ItemStack bi : p.getInventory().all(BICARBONATE.getType()).values())
				if (isBicarbonate(bi))
					b += bi.getAmount();
			
			for (ItemStack co : p.getInventory().all(COCAINE.getType()).values())
				if (isCocaine(co))
					c += co.getAmount();
			
			if (b == 0) {
				Messages.send(p, "sem-bicarbonato");
				return;
			}
			
			int combinations = Math.min(b, c);
			
			int remaningBi = b - combinations;
			int remaningCo = c - combinations;
			
			for (ItemStack is : p.getInventory().getContents())
				if (isBicarbonate(is) || isCocaine(is))
					p.getInventory().remove(is);
			
			if (remaningBi != 0) {
				ItemStack bi = BICARBONATE.clone();
				bi.setAmount(remaningBi);
				p.getInventory().addItem(bi);
			}
			
			if (remaningCo != 0) {
				ItemStack bi = COCAINE.clone();
				bi.setAmount(remaningCo);
				p.getInventory().addItem(bi);
			}
			
			ItemStack transformed = CRACK.clone();
			transformed.setAmount(combinations * 2);
			p.getInventory().addItem(transformed);
			
			Messages.send(p, "crack-transformado");
		}
	}
	
	@EventHandler
	public void oinJoin(PlayerJoinEvent e) {
		e.getPlayer().getInventory().addItem(COCAINE, BICARBONATE);
	}
	
	public static boolean isCocaine(ItemStack is) {
		return (is != null) && is.getType().equals(COCAINE.getType()) 
				&& is.hasItemMeta() && is.getItemMeta().equals(COCAINE.getItemMeta());
	}
	
	public static boolean isCannabis(ItemStack is) {
		return (is != null) && is.getType().equals(CANNABIS.getType()) 
				&& is.hasItemMeta() && is.getItemMeta().equals(CANNABIS.getItemMeta());
	}
	
	public static boolean isCrack(ItemStack is) {
		return (is != null) && is.getType().equals(CRACK.getType()) 
				&& is.hasItemMeta() && is.getItemMeta().equals(CRACK.getItemMeta());
	}
	
	public static boolean isBicarbonate(ItemStack is) {
		return (is != null) && is.getType().equals(BICARBONATE.getType()) 
				&& is.hasItemMeta() && is.getItemMeta().equals(BICARBONATE.getItemMeta());
	}
	
}
