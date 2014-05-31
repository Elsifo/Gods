package me.elsifo92.gods.gods.types;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.blocks.BlockManager;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.data.config.GodConfig;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.misc.particles.ParticleEffects;
import me.elsifo92.gods.plugins.PluginManager;
import me.elsifo92.gods.tasks.ModifyBlockTask;
import me.elsifo92.gods.tasks.ParticleTask;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R2.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;
import org.bukkit.material.Tree;

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
	private boolean isRipe(Crops c)
	{
		Bukkit.getServer().broadcastMessage("LOLOLOLOL");
		if(c.getState().equals(CropState.RIPE)) Bukkit.getServer().broadcastMessage("YOLO:56");
		if(c.getState()==CropState.RIPE) Bukkit.getServer().broadcastMessage("SWAG:57");
		return c.getState().equals(CropState.RIPE);
	}
	/**
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE) return;
		if(!PluginManager.getTownyCheck().canBreak(event.getPlayer(),event.getBlock())) return;
		Player p=event.getPlayer();
		if(FollowerManager.follows(p,this))
		{
			Block b=event.getBlock();
			Material id=b.getType();
			int aum=0;
			p.sendMessage(""+cropsexp);
		    switch(id)
		    {
		      	case CROPS:if(isRipe((Crops) b.getState().getData())) aum=cropsexp;break; 
		      	case POTATO:if(((CraftBlock) b).getData()==7) aum=potatoexp;break;
		      	case CARROT:if(((CraftBlock) b).getData()==7) aum=carrotexp;break;
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
		if(!ConfigManager.getPexConfig().hasPerm(p, "gods.admin"))
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
						{
							//((Crops) b.getState().getData()).setState(CropState.RIPE);
							//TODO remove when works again with crops
							((CraftBlock) b).setData((byte) 7);
							//(new ModifyBlockTask(b,b.getType(),(byte)7)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
							used=true;
							for(Player pl: Bukkit.getOnlinePlayers())
							{
								(new ParticleTask(ParticleEffects.HAPPY_VILLAGER,pl,temp, 1, 1, 1, 2, 10)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
							}
						}
						break;
						case POTATO:
						case CARROT:
						case MELON_STEM:	
						case PUMPKIN_STEM:
						case DIRT:
						{
							(new ModifyBlockTask(b,Material.GRASS,(byte)0)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
							used=true;
						}	
						break;
						case SAPLING:
						{
							b.getWorld().generateTree(b.getLocation(), TreeType.valueOf(((Tree) b).getSpecies().toString()));
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