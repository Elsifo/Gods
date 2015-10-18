package it.beyondthecube.gods.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import it.beyondthecube.gods.gods.God;

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