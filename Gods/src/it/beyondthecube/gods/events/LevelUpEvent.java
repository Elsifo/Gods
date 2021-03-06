package it.beyondthecube.gods.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import it.beyondthecube.gods.gods.God;

public class LevelUpEvent extends Event
{
	private int l;
	private Player p;
	private God g;
	private static final HandlerList handlers = new HandlerList();
	public LevelUpEvent(int l, Player p, God g)
	{
		this.l=l;
		this.p=p;
		this.g=g;
	}	
	public God getGod()
	{
		return g;
	}
	public int getLevel()
	{
		return l;
	}
	public Player getPlayer()
	{
		return p;
	}
	public HandlerList getHandlers() {
	    return handlers;
	}	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}