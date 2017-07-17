package factionsbrasil.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import factionsbrasil.Faction;
import factionsbrasil.FactionList;
import factionsbrasil.player.FPlayers;
import factionsbrasil.utils.Utils;

public class ScoreboardHandler {
	
	private static Scoreboard scoreboard;
	
	public static void initialize() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		for (Faction f : FactionList.getFactions()) {
			Team t = scoreboard.registerNewTeam(String.valueOf(f.getId()));
			t.setPrefix("[" + f.getTag() + Utils.color("&r] "));
			
			Team tVip = scoreboard.registerNewTeam(String.valueOf(f.getId()) + "_vip");
			tVip.setPrefix("[" + f.getTag() + Utils.color("&r] &6"));
		}
		
		Team t = scoreboard.registerNewTeam("staff");
		t.setPrefix(Utils.color("&c"));
	}
	
	public static void update(Player p) {
		if (p.hasPermission("factionsbrasil.staff")) {
			Team t = scoreboard.getTeam("staff");
			if (!(t.hasEntry(p.getName())))
				t.addEntry(p.getName());
			return;
		}
		if (Utils.hasFaction(p)) {
			String id = String.valueOf(FPlayers.get(p).getFaction().getId());
			Team t = scoreboard.getTeam(id);
			if (p.hasPermission("factionsbrasil.vip"))
				t = scoreboard.getTeam(id + "_vip");
			t.addEntry(p.getName());
		} else {
			for (Team t : scoreboard.getTeams())
				if (t.hasEntry(p.getName()))
					t.removeEntry(p.getName());
		}
	}
	
	public static Scoreboard getScoreboard() {
		return scoreboard;
	}

}
