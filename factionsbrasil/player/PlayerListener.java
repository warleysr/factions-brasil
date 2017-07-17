package factionsbrasil.player;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import factionsbrasil.Faction;
import factionsbrasil.Messages;
import factionsbrasil.utils.Utils;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (Utils.hasFaction(p)) {
				Faction faction = FPlayers.get(p).getFaction();
				Faction other = null;
				Player o = null;
				if (e.getDamager() instanceof Player) {
					Player d = (Player) e.getDamager();
					if (Utils.hasFaction(d)) {
						other = FPlayers.get(d).getFaction();
						o = d;
					}
				} else if (e.getDamager() instanceof Projectile) {
					Projectile pjct = (Projectile) e.getDamager();
					if (pjct.getShooter() instanceof Player) {
						Player s = (Player) pjct.getShooter();
						if (Utils.hasFaction(s)) {
							other = FPlayers.get(s).getFaction();
							o = s;
						}
					}
				}
				if (faction.equals(other)) {
					e.setCancelled(true);
					Messages.send(o, "nao-agredir");
				}
			}
		}
	}

}
