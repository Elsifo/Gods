package me.elsifo92.gods.gods.types;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.blocks.BlockManager;
import me.elsifo92.gods.data.config.GodConfig;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.plugins.PluginManager;

public class NatureGod extends God implements Listener
{
	private int cropsexp;
	private int potatoexp;
	private int carrotexp;
	private int melonexp;
	private int pumpkinexp;
	private int sugarcaneexp;
	private int shroomsexp;
	public NatureGod(GodType type,GodConfig c)
	{
		super(type,c);
	    FileConfiguration config=Bukkit.getPluginManager().getPlugin("GodsCraft").getConfig();
	    cropsexp=config.getInt("exp.nature.crops");
	    potatoexp=config.getInt("exp.nature.potato");
	    carrotexp=config.getInt("exp.nature.carrot");
	    sugarcaneexp=config.getInt("exp.nature.sugarcane");
	    melonexp=config.getInt("exp.nature.melon");
	    pumpkinexp=config.getInt("exp.nature.pumpkin");
	    shroomsexp=config.getInt("exp.nature.shrooms");
	    
	}
	
	/**
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		Bukkit.getConsoleSender().sendMessage("BlockBreak");
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE) return;
		if(!PluginManager.getTownyCheck().canBreak(event.getPlayer(),event.getBlock())) return;
		Player p=event.getPlayer();
		if(FollowerManager.follows(p,this))
		{
			Block b=event.getBlock();
			Material id=b.getType();
			int aum=0;
		    switch(id)
		    {
		      	case CROPS: if(b.getState().getData().getData()==7) aum=cropsexp;break;
		      	case POTATO: if(b.getState().getData().getData()==7) aum=potatoexp;break;
		      	case CARROT: if(b.getState().getData().getData()==7) aum=carrotexp;break;
		      	case SUGAR_CANE_BLOCK: if(BlockManager.isNatural(event.getBlock())) aum=sugarcaneexp;break;
		      	case MELON_BLOCK: if(BlockManager.isNatural(event.getBlock())) aum=melonexp;break;
		      	case PUMPKIN: if(BlockManager.isNatural(event.getBlock())) aum=pumpkinexp;break;
		      	case BROWN_MUSHROOM:
		      	case RED_MUSHROOM:if(BlockManager.isNatural(event.getBlock())) aum=shroomsexp;break;
		      	default:aum=0;break;
		    }
		    FollowerManager.addReputation(p,aum);
		}
	}
	@Override
	public boolean activatePower(Player p) 
	{
		if(this.getLastActivated()!=null)
        {
        	if(Utility.daysFromToday(super.getLastActivated())<4)
        	{
        		p.sendMessage(Utility.formattedMessage(this.getGodName()+Utility.getMessage("msg.god.power.cooldown")));
        		return false;
        	}        	
        }
		Location l=p.getLocation();
		int x=l.getBlockX();
		int y=l.getBlockY();
		int z=l.getBlockZ();
		boolean used=false;
		for(int cx=x-30; cx<=x+30; cx++)
		{
			for(int cy=y-10; cy<=y+10; cy++)
			{
				for(int cz=z-30; cz<=z+30; cz++)
				{
					Location temp=new Location(p.getWorld(),cx,cy,cz);
					Block b=temp.getBlock();
					switch(b.getType())
					{
						case CROPS:
						case POTATO:
						case CARROT:
						case MELON_STEM:	
						case PUMPKIN_STEM:
						{
							b.setData(CropState.RIPE.getData());
							b.getState().update();
							used=true;
						}
						break;
						case DIRT:
						{
							b.setType(Material.GRASS);
							b.getState().update();
							used=true;
						}	
						break;
						case SAPLING:
						{
							TreeType tt=TreeType.TREE;
							
							
							switch(b.getData())
							{
								case 1:tt=TreeType.REDWOOD;break;
								case 2:tt=TreeType.BIRCH;break;
								case 3:tt=TreeType.SMALL_JUNGLE;break;
							    
							}
							b.setType(Material.AIR);
							b.getWorld().generateTree(b.getLocation(), tt);
							used=true;
						}
						break;
						default:break;
					}					
				}
			}
		}
		if(used) super.activatePower(p);
		return used;		
	}		
}
