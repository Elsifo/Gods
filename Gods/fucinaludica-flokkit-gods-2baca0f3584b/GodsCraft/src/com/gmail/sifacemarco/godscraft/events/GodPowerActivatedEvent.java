package me.elsifo92.gods.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.elsifo92.gods.gods.God;

public class GodPowerActivatedEvent extends Event 
{
	private static final HandlerList handlers = new HandlerList();
	private God g;
	private Player p;
	public GodPowerActivatedEvent(God g, Player p)
	{
		this.g=g;
		this.p=p;
	}
	public God getGod()
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
