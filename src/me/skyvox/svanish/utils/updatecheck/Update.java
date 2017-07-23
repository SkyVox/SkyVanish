package me.skyvox.svanish.utils.updatecheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class Update {
	private Plugin plugin;
	private int resource;
	
	
	public Update(Plugin plugin, int resource) {
		this.plugin = plugin;
		this.resource = resource;
	}
	
	public boolean needsUpdate() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource).getBytes("UTF-8"));
			String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
			int spigotVersion = Integer.valueOf(version.replace(".", ""));
			int thisVersion = Integer.valueOf(plugin.getDescription().getVersion().replace(".", ""));
			if (spigotVersion > thisVersion) {
				return true;
			}
			connection.disconnect();
		} catch (Exception e) {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Failed to check for a update on Spigot. Exception: " + ChatColor.RED + e);
		}
		return false;
	}
}