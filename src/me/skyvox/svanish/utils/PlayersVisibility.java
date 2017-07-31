package me.skyvox.svanish.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.skyvox.svanish.events.VisibilityToggleEvent;
import me.skyvox.svanish.files.ConfigFile;

public class PlayersVisibility {
	private static List<UUID> playersVanishList;
	private static List<UUID> playersCache;
	
	public PlayersVisibility() {
		playersVanishList = new ArrayList<>();
		playersCache = new ArrayList<>();
	}
	
	public static void togglePlayerVisibility(Player player) {
		VisibilityToggleEvent event = new VisibilityToggleEvent(player, isOtherPlayersVisible(player));
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if (isOtherPlayersVisible(player)) {
				showPlayers(player);
				if (ConfigFile.get().contains("Messages.PlayersVisibility.EnableVisible")) {
					for (String msgs : ConfigFile.get().getStringList("Messages.PlayersVisibility.EnableVisible")) {
						String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()));
						player.sendMessage(msg);
					}
				}
				playersVanishList.remove(player.getUniqueId());
			} else {
				hidePlayers(player);
				if (ConfigFile.get().contains("Messages.PlayersVisibility.DisableVisible")) {
					for (String msgs : ConfigFile.get().getStringList("Messages.PlayersVisibility.DisableVisible")) {
						String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()));
						player.sendMessage(msg);
					}
				}
				playersVanishList.add(player.getUniqueId());
			}
		}
	}
	
	public static void showPlayers(Player player) {
		playersCache.remove(player.getUniqueId());
		for (Player players : Bukkit.getOnlinePlayers()) {
			for (UUID uuid : VanishManager.getVanishList()) {
				Player target = Bukkit.getPlayer(uuid);
				if (players == target) continue;
				player.showPlayer(players);
			}
		}
	}
	
	public static void hidePlayers(Player player) {
		playersCache.add(player.getUniqueId());
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players == player) continue;
			player.hidePlayer(players);
		}
	}
	
	public static boolean isOtherPlayersVisible(Player player) {
		if (playersVanishList.contains(player.getUniqueId())) {
			return true;
		}
		return false;
	}

	public static List<UUID> getPlayersVanishList() {
		return playersVanishList;
	}

	public static List<UUID> getPlayersCache() {
		return playersCache;
	}
}