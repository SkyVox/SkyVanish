package me.skyvox.svanish;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.skyvox.svanish.commands.VanishCmd;
import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.listeners.PlayerInfoMenuClick;
import me.skyvox.svanish.listeners.PlayerJoinListener;
import me.skyvox.svanish.listeners.VanishedMenuClick;
import me.skyvox.svanish.utils.ChatUtil;
import me.skyvox.svanish.utils.PlayersVisibility;
import me.skyvox.svanish.utils.VanishManager;
import me.skyvox.svanish.utils.updatecheck.Update;

/*
 * In future updates:
 * TODO: MySQL Compatibility, Bungee, More specifics events.
 */
public class Vanish extends JavaPlugin {
	private static Vanish VANISH;
	public static VanishManager vanishManager;
	public static PlayersVisibility playersVisibility;
	
	@Override
	public void onEnable() {
		getServer().getConsoleSender().sendMessage(ChatUtil.lines);
		getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Enabling " + ChatUtil.vanishtag.replace("> ", " ") + ChatColor.GRAY + "Version: " + ChatColor.GREEN + getDescription().getVersion() + ChatColor.GRAY + ".");
		VANISH = this;
		vanishManager = new VanishManager();
		playersVisibility = new PlayersVisibility();
		ConfigFile.setup();
		listeners();
		commands();
		
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
		for (Player players : Bukkit.getOnlinePlayers()) {
			for (UUID uuid : VanishManager.getVanishList()) {
				Player player = Bukkit.getPlayer(uuid);
				players.showPlayer(player);
			}
		}
		VanishManager.getVanishList().clear();
		vanishManager = null;
		playersVisibility = null;
		getServer().getConsoleSender().sendMessage(ChatUtil.vanishtag + ChatColor.GREEN + "Has been disabled!");
		getServer().getConsoleSender().sendMessage(ChatUtil.lines);
	}
	
	private void listeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new VanishedMenuClick(), this);
		pm.registerEvents(new PlayerInfoMenuClick(), this);
		pm.registerEvents(new PlayerJoinListener(), this);
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