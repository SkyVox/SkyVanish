package me.skyvox.svanish.listeners.vanishedlisteners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.skyvox.svanish.utils.VanishManager;

@SuppressWarnings("deprecation")
public class PickupItemListener implements Listener {
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (VanishManager.isVanished(player)) {
			event.setCancelled(true);
		}
	}
}