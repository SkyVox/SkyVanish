package me.skyvox.svanish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VanishToggleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player player;
    private boolean isVanish;

    public VanishToggleEvent(Player player, boolean isVanish) {
    	this.player = player;
    	this.isVanish = isVanish;
    }
    
	public Player getPlayer() {
		return player;
	}
	
	public boolean isVanish() {
		return isVanish;
	}

	public void setVanish(boolean isVanish) {
		this.isVanish = isVanish;
	}
    
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}