package me.skyvox.svanish.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.skyvox.svanish.Vanish;
import me.skyvox.svanish.events.VanishToggleEvent;
import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.files.MySQLFile;

public class VanishManager {
	private static List<UUID> vanishList;
	
	public VanishManager() {
		vanishList = new ArrayList<>();
	}
	
	public static void toggle(Player player) {
		VanishToggleEvent event = new VanishToggleEvent(player, !isVanished(player));
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if (isVanished(player)) {
				showPlayer(player);
				if (ConfigFile.get().contains("UnVanish-Lightning")) {
					if (ConfigFile.get().getString("UnVanish-Lightning").equalsIgnoreCase("true")) {
						player.getWorld().strikeLightningEffect(player.getLocation());
					}
				}
				if (ConfigFile.get().contains("Messages.UnVanish") && event.isDefaultMessage()) {
					for (String msgs : ConfigFile.get().getStringList("Messages.UnVanish")) {
						String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()));
						player.sendMessage(msg);
					}
				}
			} else {
				hidePlayer(player);
				if (ConfigFile.get().contains("Vanish-Lightning")) {
					if (ConfigFile.get().getString("Vanish-Lightning").equalsIgnoreCase("true")) {
						player.getWorld().strikeLightningEffect(player.getLocation());
					}
				}
				if (ConfigFile.get().contains("Messages.Vanish") && event.isDefaultMessage()) {
					for (String msgs : ConfigFile.get().getStringList("Messages.Vanish")) {
						String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()));
						player.sendMessage(msg);
					}
				}
			}
		}
	}
	
	public static void hidePlayer(Player player) {
		vanishList.add(player.getUniqueId());
		if (MySQLFile.get().getString("MySQL.enabled").contentEquals("true")) {
			Vanish.playerSetup.refresh(player.getUniqueId(), true);
		}
		for (Player players : Bukkit.getOnlinePlayers()) {
			if ((ConfigFile.get().contains("Vanished-can-see-others")) && (ConfigFile.get().getString("Vanished-can-see-others").equalsIgnoreCase("true"))) {
				for (UUID uuid : vanishList) {
					Player vanishedPlayer = Bukkit.getPlayer(uuid);
					if (vanishedPlayer != null) {
						if (vanishedPlayer == players) {
							continue;
						}
					}
				}
			}
			if (players.hasPermission("vanish.seeothers") || (players == player)) {
				continue;
			}
			players.hidePlayer(player);
		}
	}
	
	private static void showPlayer(Player player) {
		vanishList.remove(player.getUniqueId());
		if (MySQLFile.get().getString("MySQL.enabled").contentEquals("true")) {
			Vanish.playerSetup.refresh(player.getUniqueId(), false);
		}
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.showPlayer(player);
		}
	}
	
	public static boolean isVanished(Player player) {
		if (vanishList.contains(player.getUniqueId())) {
			return true;
		}
		return false;
	}
	
	public static boolean isVanished(OfflinePlayer player) {
		if (vanishList.contains(player.getUniqueId())) {
			return true;
		}
		return false;
	}
	
	public static List<UUID> getVanishList() {
		return vanishList;
	}
}