package me.elsifo92.gods.events;

import me.elsifo92.gods.gods.God;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GodPrayedEvent extends Event
{
	private God g;
	private Player p;	
	private static final HandlerList handlers = new HandlerList();
	public GodPrayedEvent(God g, Player p)
	{
		this.g=g;
		this.p=p;
	}
	public God getGodName()
	{
		return g;
	}
	public Player getPlayer()
	{
		return p;
	}
	public HandlerList getHandlers() 
	{
	    return handlers;
	}
	public static HandlerList getHandlerList() 
	{
	    return handlers;
	}
}