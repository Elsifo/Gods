package me.elsifo92.gods.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.elsifo92.gods.gods.God;

public class NewChampEvent extends Event
{
	private Player p;
	private God g;
	private static final HandlerList handlers = new HandlerList();
	public NewChampEvent(Player p, God g)
	{
		this.p=p;
		this.g=g;
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
