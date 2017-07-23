package me.skyvox.svanish.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skyvox.svanish.files.ConfigFile;

public class ChatUtil {
	public static String vanishtag = ChatColor.YELLOW + "SkyVanish" + ChatColor.GRAY + "> ";
	public static String lines = ChatColor.GRAY + "---------------";
	
	public static void noPermission(Player player) {
		if (ConfigFile.get().contains("Messages.No-Permission")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.No-Permission")));
		}
	}
	
	public static void noPermission(CommandSender player) {
		if (ConfigFile.get().contains("Messages.No-Permission")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.No-Permission")));
		}
	}
}