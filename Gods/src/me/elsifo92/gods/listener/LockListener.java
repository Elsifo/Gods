package me.elsifo92.gods.listener;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.ConfigManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class LockListener implements Listener
{
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p=e.getPlayer();
		p.sendMessage(Utility.formattedMessage(""+ChatColor.BOLD+ChatColor.RED+"  *****FAILSAFE MODE*****"));
		if(ConfigManager.getPexConfig().hasPerm(p, "gods.admin"))p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.error.failsafe")));
	}	
	public void onEntityDamage(EntityDamageEvent e)
	{
		e.setCancelled(true);
	}	
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		e.setCancelled(true);
	}	
	public void onBlockPistonExtend(BlockPistonExtendEvent e)
	{
		e.setCancelled(true);
	}	
	public void onBlockPistonRetract(BlockPistonRetractEvent e)
	{
		e.setCancelled(true);
	}
	public void unregister() 
	{
		EntityDamageEvent.getHandlerList().unregister(this);
		PlayerTeleportEvent.getHandlerList().unregister(this);
		BlockPistonExtendEvent.getHandlerList().unregister(this);
		BlockPistonRetractEvent.getHandlerList().unregister(this);
		PlayerMoveEvent.getHandlerList().unregister(this);
		BlockBreakEvent.getHandlerList().unregister(this);
		BlockPlaceEvent.getHandlerList().unregister(this);
		PlayerInteractEvent.getHandlerList().unregister(this);
	}	
}