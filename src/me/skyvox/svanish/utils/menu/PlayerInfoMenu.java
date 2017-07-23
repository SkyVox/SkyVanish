package me.skyvox.svanish.utils.menu;

import java.util.ArrayList;
import java.util.List;

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
import me.skyvox.svanish.utils.PlayerInfoManager;

public class PlayerInfoMenu {
	
	public static void playerInfoMenu(Player player) {
		if (PlayerInfoManager.getPlayerInfo().containsKey(player.getUniqueId())) {
			Player target = Bukkit.getPlayer(PlayerInfoManager.getPlayerInfo().get(player.getUniqueId()));
			
			String name = ChatColor.GRAY + "Player Info";
			if (ConfigFile.get().contains("Menu.PlayerInfo.Inventory-Name")) {
				name = ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.Inventory-Name").replace("%clickedplayer%", target.getName()).replace("%player%", player.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()));
				if (name.length() > 32) {
					name = name.substring(0, Math.min(name.length(), 32));
				}
			}
			Inventory inventory = Bukkit.createInventory(null, 9*3, name);
			
			try {
				String skullName = ConfigFile.get().contains("Menu.PlayerInfo.SkullItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.SkullItemName").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())) : ChatColor.DARK_BLUE + target.getName();
				List<String> skullLore = new ArrayList<>();
				for (String lore : ConfigFile.get().getStringList("Menu.PlayerInfo.SkullItemLore")) {
					lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName()));
					skullLore.add(lore);
				}
				ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
				SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
				skullMeta.setOwner(target.getName());
				skullMeta.setDisplayName(skullName);
				skullMeta.setLore(skullLore);
				skullItem.setItemMeta(skullMeta);
				inventory.setItem(ConfigFile.get().getInt("Menu.PlayerInfo.SkullItemSlot"), skullItem);
				
				if (ConfigFile.get().contains("Menu.PlayerInfo.ShowTeleportItem") && ConfigFile.get().getString("Menu.PlayerInfo.ShowTeleportItem").equalsIgnoreCase("true")) {
					String teleportType = ConfigFile.get().getString("Menu.PlayerInfo.TeleportItemType");
					String teleportName = ConfigFile.get().contains("Menu.PlayerInfo.TeleportItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.TeleportItemName").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())) : ChatColor.DARK_BLUE + target.getName();
					List<String> teleportLore = new ArrayList<>();
					for (String lore : ConfigFile.get().getStringList("Menu.PlayerInfo.TeleportItemLore")) {
						lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName()));
						teleportLore.add(lore);
					}
					ItemStack teleportItem = new ItemStack(Material.valueOf(teleportType.toUpperCase()), 1);
					ItemMeta teleportMeta = teleportItem.getItemMeta();
					teleportMeta.setDisplayName(teleportName);
					teleportMeta.setLore(teleportLore);
					teleportItem.setItemMeta(teleportMeta);
					inventory.setItem(ConfigFile.get().getInt("Menu.PlayerInfo.TeleportItemSlot"), teleportItem);
				}
				
				if (ConfigFile.get().contains("Menu.PlayerInfo.ShowInfoItem") && ConfigFile.get().getString("Menu.PlayerInfo.ShowInfoItem").equalsIgnoreCase("true")) {
					String infoType = ConfigFile.get().getString("Menu.PlayerInfo.InfoItemType");
					String infoName = ConfigFile.get().contains("Menu.PlayerInfo.InfoItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.InfoItemName").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())) : ChatColor.DARK_BLUE + target.getName();
					List<String> infoLore = new ArrayList<>();
					for (String lore : ConfigFile.get().getStringList("Menu.PlayerInfo.InfoItemLore")) {
						double life = target.getHealth();
						String loc = "X: " + target.getLocation().getX() + " Y: " + target.getLocation().getY() + " Z: " + target.getLocation().getZ();
						int level = target.getLevel();
						int food = target.getFoodLevel();
						lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName()).replace("%clickedlife%", "" + life).replace("%clickedloc%", loc).replace("%clickedlevel%", "" + level).replace("%clickedfood%", "" + food));
						infoLore.add(lore);
					}
					ItemStack infoItem = new ItemStack(Material.valueOf(infoType.toUpperCase()), 1);
					ItemMeta infoMeta = infoItem.getItemMeta();
					infoMeta.setDisplayName(infoName);
					infoMeta.setLore(infoLore);
					infoItem.setItemMeta(infoMeta);
					inventory.setItem(ConfigFile.get().getInt("Menu.PlayerInfo.InfoItemSlot"), infoItem);
				}
				
				if (ConfigFile.get().contains("Menu.PlayerInfo.ShowUnvanishItem") && ConfigFile.get().getString("Menu.PlayerInfo.ShowUnvanishItem").equalsIgnoreCase("true")) {
					String unvanishType = ConfigFile.get().getString("Menu.PlayerInfo.UnvanishItemType");
					String unvanishName = ConfigFile.get().contains("Menu.PlayerInfo.UnvanishItemName") ? ChatColor.translateAlternateColorCodes('&', ConfigFile.get().getString("Menu.PlayerInfo.UnvanishItemName").replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName())) : ChatColor.DARK_BLUE + target.getName();
					List<String> unvanishLore = new ArrayList<>();
					for (String lore : ConfigFile.get().getStringList("Menu.PlayerInfo.UnvanishItemLore")) {
						lore = ChatColor.translateAlternateColorCodes('&', lore.replace("%clickedplayer%", target.getName()).replace("%displayname%", player.getDisplayName()).replace("%clickeddisplayname%", target.getDisplayName()).replace("%player%", player.getName()));
						unvanishLore.add(lore);
					}
					ItemStack unvanishItem = new ItemStack(Material.valueOf(unvanishType.toUpperCase()), 1);
					ItemMeta unvanishMeta = unvanishItem.getItemMeta();
					unvanishMeta.setDisplayName(unvanishName);
					unvanishMeta.setLore(unvanishLore);
					unvanishItem.setItemMeta(unvanishMeta);
					inventory.setItem(ConfigFile.get().getInt("Menu.PlayerInfo.UnvanishItemSlot"), unvanishItem);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			
			player.openInventory(inventory);
		}
	}
}