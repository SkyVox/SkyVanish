package me.skyvox.svanish;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.skyvox.svanish.commands.VanishCmd;
import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.files.MySQLFile;
import me.skyvox.svanish.listeners.PlayerInfoMenuClick;
import me.skyvox.svanish.listeners.PlayerJoinListener;
import me.skyvox.svanish.listeners.PlayerQuitListener;
import me.skyvox.svanish.listeners.VanishedMenuClick;
import me.skyvox.svanish.utils.ChatUtil;
import me.skyvox.svanish.utils.PlayersVisibility;
import me.skyvox.svanish.utils.VanishManager;
import me.skyvox.svanish.utils.mysql.MySQLPlayerSetup;
import me.skyvox.svanish.utils.mysql.MySQLSetup;
import me.skyvox.svanish.utils.updatecheck.Update;

/*
 * In future updates:
 * TODO: Bungee, More specifics events.
 */
public class Vanish extends JavaPlugin {
	private static Vanish VANISH;
	public static VanishManager vanishManager;
	public static PlayersVisibility playersVisibility;
	public static MySQLSetup data;
	public static MySQLPlayerSetup playerSetup;
	
	@Override
	public void onEnable() {
		getServer().getConsoleSender().sendMessage(ChatUtil.lines);
		getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Enabling " + ChatUtil.vanishtag.replace("> ", " ") + ChatColor.GRAY + "Version: " + ChatColor.GREEN + getDescription().getVersion() + ChatColor.GRAY + ".");
		VANISH = this;
		vanishManager = new VanishManager();
		playersVisibility = new PlayersVisibility();
		playerSetup = new MySQLPlayerSetup();
		ConfigFile.setup();
		MySQLFile.setup();
		data = new MySQLSetup();
		listeners();
		commands();
		
		if (MySQLFile.get().getString("MySQL.enabled").contentEquals("true")) {
			if (Vanish.data.getTable() == null) {
				try {
					Bukkit.broadcastMessage("Creating..");
					System.out.println(ChatColor.RED + "The table that was informed is not valid, It will create a table automatically. (Table name: 'vanish')!");
					Statement statement = data.getConnection().createStatement();
					statement.executeQuery("CREATE TABLE IF NOT EXISTS `vanish` (UUID varchar(36), REAL_NAME varchar(16) NOT NULL UNIQUE, VANISHED boolean NOT NULL, PRIMARY KEY(UUID))");
					MySQLFile.get().set("MySQL.table", String.valueOf("vanish"));
					MySQLFile.save();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (Vanish.playerSetup.getPlayer(players.getUniqueId())) {
					for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
						onlinePlayers.hidePlayer(players);
						VanishManager.getVanishList().add(players.getUniqueId());
					}
				}
			}
		}
		
		Update update = new Update(this, 44373);
		if (update.needsUpdate()) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "A new update is available, Click on this link to download the latest version: " + ChatColor.YELLOW + "https://www.spigotmc.org/resources/skyvanish-vanish-toggle-visibility.44373/history");
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "This plugin is up to date! :D");
		}
		
		getServer().getConsoleSender().sendMessage(ChatUtil.vanishtag + ChatColor.GREEN + "Has been enabled!");
		getServer().getConsoleSender().sendMessage(ChatUtil.lines);
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatUtil.lines);
		VANISH = null;
		/*if (MySQLFile.get().getString("MySQL.enabled").contentEquals("true")) {
			for (UUID uuid : VanishManager.getVanishList()) {
				playerSetup.setPlayer(uuid, true);
			}
		} */
		for (Player players : Bukkit.getOnlinePlayers()) {
			for (UUID uuid : VanishManager.getVanishList()) {
				Player player = Bukkit.getPlayer(uuid);
				players.showPlayer(player);
			}
		}
		VanishManager.getVanishList().clear();
		vanishManager = null;
		playersVisibility = null;
		try {
			if (!data.getConnection().isClosed()) {
				data.closeConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		data = null;
		getServer().getConsoleSender().sendMessage(ChatUtil.vanishtag + ChatColor.GREEN + "Has been disabled!");
		getServer().getConsoleSender().sendMessage(ChatUtil.lines);
	}
	
	private void listeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new VanishedMenuClick(), this);
		pm.registerEvents(new PlayerInfoMenuClick(), this);
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerQuitListener(), this);
	}
	
	private void commands() {
		getCommand("vanish").setExecutor(new VanishCmd());
	}
	
	/**
	 * @return the VANISH
	 */
	public static Vanish getInstance() {
		return VANISH;
	}
}