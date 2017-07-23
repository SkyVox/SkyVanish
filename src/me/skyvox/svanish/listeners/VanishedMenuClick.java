package me.skyvox.svanish.listeners;

import java.util.UUID;

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
import me.skyvox.svanish.utils.menu.PlayerInfoMenu;

public class VanishedMenuClick implements Listener {
	
	@EventHandler
	public void onVanishListClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		String name = ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.Inventory-Name"));
		if (name.length() > 32) {
			name = name.substring(0, Math.min(name.length(), 32));
		}
		if (inv.getTitle().equalsIgnoreCase(name)) {
			event.setCancelled(true);
			if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType() == Material.AIR)) {
				event.setCancelled(true);
				return;
			} else {
				Material material = Material.valueOf(ConfigFile.get().getString("Menu.VanishedList.CloseItemType").toUpperCase());
				String itemName = ConfigFile.get().contains("Menu.VanishedList.CloseItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.CloseItemName").replace("%player%", player.getDisplayName()).replace("%displayname%", player.getDisplayName())) : ChatColor.GREEN + "Close menu";
				String refrshType = ConfigFile.get().getString("Menu.VanishedList.RefreshItemType");
				String refreshName = ConfigFile.get().contains("Menu.VanishedList.RefreshItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.RefreshItemName").replace("%player%", player.getDisplayName()).replace("%displayname%", player.getDisplayName())) : ChatColor.YELLOW + "Refresh page";
				
				if (event.getCurrentItem().getType() == material) {
					if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(itemName)) {
						player.closeInventory();
						player.updateInventory();
					}
				} else if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
					for (UUID uuid : VanishManager.getVanishList()) {
						Player target = Bukkit.getPlayer(uuid);
						if (player != target) {
							if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().contains(target.getName())) {
								PlayerInfoManager.getPlayerInfo().put(player.getUniqueId(), target.getUniqueId());
								PlayerInfoMenu.playerInfoMenu(player);
								player.updateInventory();
								break;
							}
						} else {
							if (ConfigFile.get().contains("Messages.ClickOnYourSkull")) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Messages.ClickOnYourSkull").replace("%player%", player.getDisplayName()).replace("%displayname%", player.getDisplayName())));
							}
						}
					}
				} else if (event.getCurrentItem().getType() == Material.valueOf(refrshType.toUpperCase())) {
					if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(refreshName)) {
						player.updateInventory();
					}
				}
			}
		}
	}
}