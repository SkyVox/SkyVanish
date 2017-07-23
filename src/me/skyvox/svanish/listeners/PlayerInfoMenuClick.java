package me.skyvox.svanish.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.utils.PlayerInfoManager;
import me.skyvox.svanish.utils.VanishManager;

public class PlayerInfoMenuClick implements Listener {
	
	@EventHandler
	public void playerInfoMenu(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (PlayerInfoManager.getPlayerInfo().containsKey(player.getUniqueId())) {
			Inventory inv = event.getInventory();
			Player target = Bukkit.getPlayer(PlayerInfoManager.getPlayerInfo().get(player.getUniqueId()));
			String name = ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.Inventory-Name").replace("%clickedplayer%", target.getName()).replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()));
			if (name.length() > 32) {
				name = name.substring(0, Math.min(name.length(), 32));
			}
			if (inv.getTitle().equalsIgnoreCase(name)) {
				event.setCancelled(true);
				if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType() == Material.AIR)) {
					event.setCancelled(true);
					return;
				} else {
					String teleportType = ConfigFile.get().getString("Menu.PlayerInfo.TeleportItemType");
					String teleportName = ConfigFile.get().contains("Menu.PlayerInfo.TeleportItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.TeleportItemName").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())) : ChatColor.DARK_BLUE + target.getName();
					String unvanishType = ConfigFile.get().getString("Menu.PlayerInfo.UnvanishItemType");
					String unvanishName = ConfigFile.get().contains("Menu.PlayerInfo.UnvanishItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.UnvanishItemName").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())) : ChatColor.DARK_BLUE + target.getName();
					
					if (event.getCurrentItem().getType() == Material.valueOf(teleportType)) {
						if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(teleportName)) {
							if (player.hasPermission("vanish.teleport") || player.hasPermission("vanish.tp")) {
								player.teleport(target.getLocation());
								if (ConfigFile.get().contains("Messages.Teleport")) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.Teleport").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())));
								}
								player.closeInventory();
								player.updateInventory();
							}
						}
					} else if (event.getCurrentItem().getType() == Material.valueOf(unvanishType)) {
						if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(unvanishName)) {
							if (VanishManager.isVanished(target)) {
								if (ConfigFile.get().contains("Messages.UnVanish-Others")) {
									for (String msgs : ConfigFile.get().getStringList("Messages.UnVanish-Others")) {
										String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()).replace("%target%", target.getName()));
										player.sendMessage(msg);
									}
								}
							} else {
								if (ConfigFile.get().contains("Messages.Vanish-Others")) {
									for (String msgs : ConfigFile.get().getStringList("Messages.Vanish-Others")) {
										String msg = ChatColor.translateAlternateColorCodes('&', msgs.replace("%player%", player.getName()).replace("%displayName%", player.getDisplayName()).replace("%target%", target.getName()));
										player.sendMessage(msg);
									}
								}
							}
							VanishManager.toggle(target);
							player.closeInventory();
							player.updateInventory();
						}
					}
				}
			}
		}
	}
}