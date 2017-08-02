package me.skyvox.svanish.listeners.vanishedlisteners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.skyvox.svanish.utils.VanishManager;

public class ChestInteractListener implements Listener {
	private HashMap<UUID, Location> chestLoc = new HashMap<>();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() == Material.CHEST) {
			if (VanishManager.isVanished(event.getPlayer())) {
				Chest chest = (Chest) event.getClickedBlock().getState();
				chestLoc.put(event.getPlayer().getUniqueId(), chest.getLocation());
				Inventory inventory = Bukkit.createInventory(chest, InventoryType.CHEST);
				for (int i = 0; i < chest.getInventory().getSize(); i++) {
					ItemStack item = chest.getInventory().getItem(i);
					if ((item != null) && (item.getType() != Material.AIR)) {
						inventory.setItem(i, item);
					}
				}
				chest.update();
				event.getPlayer().openInventory(inventory);
				event.setCancelled(true);
			}
		} else if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
			if (VanishManager.isVanished(event.getPlayer())) {
				Inventory inv = event.getPlayer().getEnderChest();
				event.getPlayer().openInventory(inv);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onCloseChest(InventoryCloseEvent event) {
		if (event.getInventory().getType() == InventoryType.CHEST) {
			Player player = (Player) event.getPlayer();
			if (chestLoc.containsKey(player.getUniqueId())) {
				Chest chest = (Chest) player.getWorld().getBlockAt(chestLoc.get(player.getUniqueId())).getState();
				chest.getInventory().setContents(event.getInventory().getContents());
				chestLoc.remove(player.getUniqueId());
			}
		}
	}
}