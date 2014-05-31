package me.elsifo92.gods.events;

import me.elsifo92.gods.gods.God;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NewChampEvent extends Event
{
	private OfflinePlayer p;
	private God g;
	private static final HandlerList handlers = new HandlerList();
	public NewChampEvent(OfflinePlayer p, God g)
	{
		this.p=p;
		this.g=g;
	}
	public God getGod()
	{
		return g;
	}
	public OfflinePlayer getPlayer()
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