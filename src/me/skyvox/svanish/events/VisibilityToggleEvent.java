package me.skyvox.svanish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VisibilityToggleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player player;
    private boolean isVisibility;
    private boolean isDefaultMessage = true;

    public VisibilityToggleEvent(Player player, boolean isVisibility) {
    	this.player = player;
    	this.isVisibility = isVisibility;
    }
    
	public Player getPlayer() {
		return player;
	}
	
	public boolean isOtherPlayersVisible() {
		return isVisibility;
	}

	public void setVisibility(boolean isVisibility) {
		this.isVisibility = isVisibility;
	}
	
	public boolean isDefaultMessage() {
		return isDefaultMessage;
	}

	public void setDefaultMessage(boolean isDefaultMessage) {
		this.isDefaultMessage = isDefaultMessage;
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