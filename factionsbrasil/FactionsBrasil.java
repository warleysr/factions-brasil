package factionsbrasil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import factionsbrasil.commands.FactionCommand;
import factionsbrasil.commands.SmokeCommand;
import factionsbrasil.commands.SniffCommand;
import factionsbrasil.drugs.CannabisActivator;
import factionsbrasil.drugs.DrugsListener;
import factionsbrasil.player.FPlayers;
import factionsbrasil.player.PlayerListener;
import factionsbrasil.scoreboard.ScoreboardHandler;
import factionsbrasil.scoreboard.ScoreboardListener;
import factionsbrasil.shop.ShopListener;
import factionsbrasil.slums.SlumListener;
import factionsbrasil.slums.SlumManager;
import net.milkbowl.vault.economy.Economy;

public class FactionsBrasil extends JavaPlugin {
	
	private static Plugin plugin;
	private static Connection conn;
	private static Economy econ;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			getLogger().severe("*** Nenhum plugin de economia encontrado ***");
			Bukkit.shutdown();
			return;
		}
		econ = rsp.getProvider();
		
		saveDefaultConfig();
		reloadConfig();
		
		String host = getConfig().getString("MySQL.host").trim(), user = getConfig().getString("MySQL.user").trim(),
			   pass = getConfig().getString("MySQL.pass").trim(), db = getConfig().getString("MySQL.db").trim();
		int port = getConfig().getInt("MySQL.port");
		
		try {
			// CONECTAR AO BANCO DE DADOS
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
			
			// CRIAR TABELAS SE NAO EXISTIR
			conn.prepareStatement("CREATE TABLE IF NOT EXISTS `players` "
					+ "(`player` VARCHAR(16) PRIMARY KEY, `faction` INT, `role` INT, `power` DOUBLE);")
			.executeUpdate();
			
			conn.prepareStatement("CREATE TABLE IF NOT EXISTS `activators` "
					+ "(`world` VARCHAR(16), `state` INT, `x` INT, `y` INT, `z` INT);").executeUpdate();
			
			conn.prepareStatement("CREATE TABLE IF NOT EXISTS `slums` "
					+ "(`id` INT PRIMARY KEY AUTO_INCREMENT, `name` VARCHAR(16), `owner` VARCHAR(16), "
					+ "`faction` INT, `chunk_coord` VARCHAR(40), `members` VARCHAR(84));").executeUpdate();
			
			conn.prepareStatement("CREATE TABLE IF NOT EXISTS `drugs_stats` "
					+ "(`player` VARCHAR(16), `drug` INT, `selled` INT, `used` INT);").executeUpdate();
			
		} catch (SQLException e) {
			getLogger().severe("*** Falha ao iniciar MySQL ***");
			getLogger().severe(e.getMessage());
			Bukkit.shutdown();
			return;
		}
		
		FPlayers.loadPlayers();
		
		SlumManager.loadSlums();
		
		for (World w : Bukkit.getWorlds())
			CannabisActivator.loadAll(w);
		
		ScoreboardHandler.initialize();
		
		Messages.loadMessages();
		
		getCommand("faccao").setExecutor(new FactionCommand());
		getCommand("cheirar").setExecutor(new SniffCommand());
		getCommand("fumar").setExecutor(new SmokeCommand());
		
		Bukkit.getPluginManager().registerEvents(new DrugsListener(), this);
		Bukkit.getPluginManager().registerEvents(new ScoreboardListener(), this);
		Bukkit.getPluginManager().registerEvents(new SlumListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
		
		CannabisActivator.startTask();
	}
	
	@Override
	public void onDisable() {
		if (conn != null)
			CannabisActivator.saveAll();
	}
	
	public static Plugin getPlugin() {
		return plugin;
	}
	
	public static Connection getConnection() {
		return conn;
	}
	
	public static Economy getEconomy() {
		return econ;
	}

}
