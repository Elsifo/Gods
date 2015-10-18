package it.beyondthecube.gods.listener;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.gods.GodManager;
import it.beyondthecube.gods.gods.altar.AltarManager;

public class SignListener implements Listener
{
	public SignListener()
    {

    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
    	Player p=event.getPlayer();
    	Block b=event.getPlayer().getTargetBlock((Set<Material>) null, 10);
    	if(b.getType().toString().equals("WALL_SIGN"))
  	    {
    		Sign s=(Sign) b.getState();
    		if(!AltarManager.isAltar(s)) return;
  	    	{
    			if(event.getAction()==Action.RIGHT_CLICK_BLOCK)
    			{
    				String god=s.getLine(1);
  	    			AltarManager.altarInteraction(p,GodManager.getGod(god),AltarManager.getAltar(s));
  	    		}
    		}    		
  	    }
    }
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
    	Player p=event.getPlayer();
    	Block b=event.getBlock();
    	if(b.getType().toString().equals("WALL_SIGN"))
  	    {
    		Sign s=(Sign) b.getState();
    		if(s.getLine(0).equals(new String("[GOD]")))
  	    	{
    			if(AltarManager.getAltar(s)!=null)
    	    	{
    				event.setCancelled(true);
    	    		p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altars.break")));
    	    	}    	    	
    		}    		
  	    }
    }
    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event)
    {
    	Block b=event.getBlock();
    	if(b.getType().toString().equals("WALL_SIGN"))
    	{
    		Sign s=(Sign)b.getState();
    		if(AltarManager.getAltar(s)!=null) event.setCancelled(true); 
    	}
    }
}
