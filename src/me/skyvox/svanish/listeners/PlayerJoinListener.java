package me.skyvox.svanish.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.skyvox.svanish.Vanish;
import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.files.MySQLFile;
import me.skyvox.svanish.utils.PlayersVisibility;
import me.skyvox.svanish.utils.VanishManager;
import me.skyvox.svanish.utils.cooldown.CooldownAPI;

public class PlayerJoinListener implements Listener {
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (MySQLFile.get().getString("MySQL.enabled").contentEquals("true")) {
			if (Vanish.playerSetup.getPlayer(player.getUniqueId())) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.hidePlayer(player);
					VanishManager.getVanishList().add(player.getUniqueId());
				}
			}
		}
		for (Player players : Bukkit.getOnlinePlayers()) {
			setup(player, players);
		}
		
		if (ConfigFile.get().contains("Join.ReceiveItemWhenJoin") && ConfigFile.get().getString("Join.ReceiveItemWhenJoin").equalsIgnoreCase("true")) {
			addItem(player);
		}
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.LOW)
	public void onInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (item == null || (item.getType() == Material.AIR)) {
			return;
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
			if ((item.getType() == getEnableItem(player).getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(getEnableItem(player).getItemMeta().getDisplayName())) || (item.getType() == getDisabledItem(player).getType() && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(getDisabledItem(player).getItemMeta().getDisplayName()))) {
				if (!player.hasPermission("vanish.bypass.cooldown")) {
					if (!CooldownAPI.isOnCooldown(player.getUniqueId(), "VanishToggle")) {
						CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "VanishToggle", ConfigFile.get().getInt("WaitToToggleVanish"));
						cooldown.start();
					} else {
						if (ConfigFile.get().contains("Messages.IsOnCooldown")) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.IsOnCooldown").replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()).replace("%time%", "" + CooldownAPI.getTimeLeft(player.getUniqueId(), "VanishToggle"))));
						}
						return;
					}
				}
				PlayersVisibility.togglePlayerVisibility(player);
				addItem(player);
			}
		}
	}
	
	private void addItem(Player player) {
		if (PlayersVisibility.isOtherPlayersVisible(player)) {
			player.getInventory().setItem(ConfigFile.get().getInt("Join.VisibilityDisableSlot"), getDisabledItem(player));
		} else {
			player.getInventory().setItem(ConfigFile.get().getInt("Join.VisibilityEnableSlot"), getEnableItem(player));
		}
	}
	
	private static void setup(Player player, Player onlinePlayers) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				player.showPlayer(onlinePlayers);
				for (UUID uuid : VanishManager.getVanishList()) {
					Player vanishedPlayer = Bukkit.getPlayer(uuid);
					if (vanishedPlayer != null) {
						onlinePlayers.hidePlayer(vanishedPlayer);
					}
				}
				for (UUID uuid : PlayersVisibility.getPlayersCache()) {
					Player p = Bukkit.getPlayer(uuid);
					if (p != null) {
						p.hidePlayer(onlinePlayers);
					}
				}
			}
		}.runTaskLater(Vanish.getInstance(), 20);
	}
	
	private static ItemStack getEnableItem(Player player) {
		int itemByte = 0;
		ItemStack item = null;
		if (ConfigFile.get().getString("Join.VisibilityEnableType").contains(":")) {
			String[] iByte = ConfigFile.get().getString("Join.VisibilityEnableType").split(":");
			itemByte = Integer.parseInt(iByte[1]);
			item = new ItemStack(Material.getMaterial(iByte[0].toUpperCase()), 1, Byte.valueOf((byte) itemByte));
		} else {
			item = new ItemStack(Material.getMaterial(ConfigFile.get().getString("Join.VisibilityEnableType").toUpperCase()));
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ConfigFile.get().contains("Join.VisibilityEnableName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Join.VisibilityEnableName").replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName())) : "Players: " + ChatColor.GREEN + "Visible " + ChatColor.GRAY + "(Right Click)");
		List<String> itemLore = new ArrayList<>();
		for (String lore : ConfigFile.get().getStringList("Join.VisibilityEnableLore")) {
			lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()));
			itemLore.add(lore);
		}
		meta.setLore(itemLore);
		item.setItemMeta(meta);
		return item;
	}
	
	private static ItemStack getDisabledItem(Player player) {
		int itemByte = 0;
		ItemStack item = null;
		if (ConfigFile.get().getString("Join.VisibilityDisableType").contains(":")) {
			String[] iByte = ConfigFile.get().getString("Join.VisibilityDisableType").split(":");
			itemByte = Integer.parseInt(iByte[1]);
			item = new ItemStack(Material.getMaterial(iByte[0].toUpperCase()), 1, Byte.valueOf((byte) itemByte));
		} else {
			item = new ItemStack(Material.getMaterial(ConfigFile.get().getString("Join.VisibilityDisableType").toUpperCase()));
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ConfigFile.get().contains("Join.VisibilityDisableName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Join.VisibilityDisableName").replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName())) : "Players: " + ChatColor.RED + "Hidden " + ChatColor.GRAY + "(Right Click)");
		List<String> itemLore = new ArrayList<>();
		for (String lore : ConfigFile.get().getStringList("Join.VisibilityDisableLore")) {
			lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()));
			itemLore.add(lore);
		}
		meta.setLore(itemLore);
		item.setItemMeta(meta);
		return item;
	}
}