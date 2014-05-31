package me.elsifo92.gods.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import me.elsifo92.gods.gods.God;

public class GiftedEvent extends Event
{
	private God g;
	private ItemStack i;
	private Player p;
	private static final HandlerList handlers = new HandlerList();
	public GiftedEvent(God g, ItemStack i, Player p)
	{
		this.g=g;
		this.i=i;
		this.p=p;
	}
	public God getGod()
	{
		return g;
	}
	public ItemStack getItem()
	{
		return i;
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
