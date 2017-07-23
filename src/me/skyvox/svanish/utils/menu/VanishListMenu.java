package me.skyvox.svanish.utils.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.skyvox.svanish.files.ConfigFile;
import me.skyvox.svanish.utils.VanishManager;

public class VanishListMenu {
	
	public static void vanishedPlayersMenu(Player player) {
		String name = ChatColor.GRAY + "Vanished list";
		if (ConfigFile.get().contains("Menu.VanishedList.Inventory-Name")) {
			name = ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.Inventory-Name"));
			if (name.length() > 32) {
				name = name.substring(0, Math.min(name.length(), 32));
			}
		}
		Inventory inventory = Bukkit.createInventory(null, 9*6, name);
		
		if (ConfigFile.get().contains("Menu.VanishedList.CloseItemShow") && ConfigFile.get().contains("Menu.VanishedList.CloseItemType") && ConfigFile.get().contains("Menu.VanishedList.CloseItemLore") && ConfigFile.get().contains("Menu.VanishedList.CloseItemName") || ConfigFile.get().contains("Menu.VanishedList.CloseItemSlot")) {
			if (ConfigFile.get().getString("Menu.VanishedList.CloseItemShow").equalsIgnoreCase("true")) {
				try {
					String itemType = ConfigFile.get().getString("Menu.VanishedList.CloseItemType");
					List<String> itemCloseLore = new ArrayList<>();
					for (String lore : ConfigFile.get().getStringList("Menu.VanishedList.CloseItemLore")) {
						lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()));
						itemCloseLore.add(lore);
					}
					ItemStack item = new ItemStack(Material.valueOf(itemType.toUpperCase()));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ConfigFile.get().contains("Menu.VanishedList.CloseItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.CloseItemName").replace("%player%", player.getDisplayName()).replace("%displayname%", player.getDisplayName())) : ChatColor.GREEN + "Close menu");
					meta.setLore(itemCloseLore);
					item.setItemMeta(meta);
					inventory.setItem(ConfigFile.get().getInt("Menu.VanishedList.CloseItemSlot"), item);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (ConfigFile.get().contains("Menu.VanishedList.RefreshShowItem") && ConfigFile.get().contains("Menu.VanishedList.RefreshItemType") && ConfigFile.get().contains("Menu.VanishedList.RefreshItemLore") && ConfigFile.get().contains("Menu.VanishedList.RefreshItemName") || ConfigFile.get().contains("Menu.VanishedList.RefreshItemSlot")) {
			if (ConfigFile.get().getString("Menu.VanishedList.RefreshShowItem").equalsIgnoreCase("true")) {
				try {
					String refrshType = ConfigFile.get().getString("Menu.VanishedList.RefreshItemType");
					List<String> refreshItemLore = new ArrayList<>();
					for (String lore : ConfigFile.get().getStringList("Menu.VanishedList.RefreshItemLore")) {
						lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()));
						refreshItemLore.add(lore);
					}
					ItemStack item = new ItemStack(Material.valueOf(refrshType.toUpperCase()));
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ConfigFile.get().contains("Menu.VanishedList.RefreshItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.RefreshItemName").replace("%player%", player.getDisplayName()).replace("%displayname%", player.getDisplayName())) : ChatColor.YELLOW + "Refresh page");
					meta.setLore(refreshItemLore);
					item.setItemMeta(meta);
					inventory.setItem(ConfigFile.get().getInt("Menu.VanishedList.RefreshItemSlot"), item);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (ConfigFile.get().contains("Menu.VanishedList.SkullItemName") && ConfigFile.get().contains("Menu.VanishedList.SkullItemLore")) {
			for (UUID uuid : VanishManager.getVanishList()) {
				Player p = Bukkit.getPlayer(uuid);
				try {
					String skullName = ConfigFile.get().contains("Menu.VanishedList.SkullItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.VanishedList.SkullItemName").replace("%player%", p.getName()).replace("%displayname%", p.getDisplayName())) : ChatColor.DARK_BLUE + p.getName();
					List<String> skullLore = new ArrayList<>();
					for (String lore : ConfigFile.get().getStringList("Menu.VanishedList.SkullItemLore")) {
						lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%player%", p.getName()));
						skullLore.add(lore);
					}
					ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
					SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
					skullMeta.setOwner(p.getName());
					skullMeta.setDisplayName(skullName);
					skullMeta.setLore(skullLore);
					skullItem.setItemMeta(skullMeta);
					
					inventory.addItem(skullItem);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
		
		player.openInventory(inventory);
	}
}