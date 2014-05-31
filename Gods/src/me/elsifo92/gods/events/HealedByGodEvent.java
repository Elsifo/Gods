package me.elsifo92.gods.events;

import me.elsifo92.gods.gods.God;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HealedByGodEvent extends Event
{
	private Player p;
	private God g;
	private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() 
	{
		return handlers;
	}
	public static HandlerList getHandlerList() 
	{
	    return handlers;
	}
	public HealedByGodEvent(Player p, God g)
	{
		this.p=p;
		this.g=g;
		this.cancelled=false;
	}
	public Player getPlayer()
	{
		return p;
	}
	public God getGod()
	{
		return g;
	}
	public void setCancelled()
	{
		this.cancelled=true;
	}
	public boolean isCancelled() 
	{
		return cancelled;
	}
}