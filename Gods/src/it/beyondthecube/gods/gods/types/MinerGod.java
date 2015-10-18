package it.beyondthecube.gods.gods.types;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.data.blocks.BlockManager;
import it.beyondthecube.gods.data.config.GodConfig;
import it.beyondthecube.gods.gods.God;
import it.beyondthecube.gods.gods.follower.FollowerManager;
import it.beyondthecube.gods.misc.particles.ParticleEffects;
import it.beyondthecube.gods.plugins.PluginManager;
import it.beyondthecube.gods.tasks.ModifyBlockTask;
import it.beyondthecube.gods.tasks.ParticleTask;

public class MinerGod extends God implements Listener
{
	private int stoneexp;
	private int coalexp;
	private int ironexp;
	private int quartzexp;
	private int redstoneexp;
	private int goldexp;
	private int lapisexp;
	private int diamondexp;
	private int emeraldexp;
	public MinerGod(GodType type, GodConfig c)
    {
		super(type,c);
	    FileConfiguration config=Bukkit.getPluginManager().getPlugin("GodsCraft").getConfig();
	    stoneexp=config.getInt("exp.miner.stone");
	    coalexp=config.getInt("exp.miner.coal");
	    ironexp=config.getInt("exp.miner.iron");
	    goldexp=config.getInt("exp.miner.gold");
	    quartzexp=config.getInt("exp.miner.quartz");
	    redstoneexp=config.getInt("exp.miner.redstone");
	    lapisexp=config.getInt("exp.miner.lapis");
	    diamondexp=config.getInt("exp.miner.diamond");
	    emeraldexp=config.getInt("exp.miner.emerald");
    }
	@EventHandler(priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		if(event.getPlayer().getGameMode()==GameMode.CREATIVE) return;
		if(!PluginManager.getTownyCheck().canBreak(event.getPlayer(),event.getBlock())) return;
		Block b=event.getBlock();
		if(!BlockManager.isNatural(b)) return;
		if(FollowerManager.follows(event.getPlayer(),this))
		{	
			Material m=b.getType();
			int aum=0;
			switch(m)
			{
		       	case STONE:aum=stoneexp;break;
		       	case GOLD_ORE:aum=goldexp;break;
		       	case IRON_ORE:aum=ironexp;break;
		       	case COAL_ORE:aum=coalexp;break;
		       	case LAPIS_ORE:aum=lapisexp;break;
		       	case DIAMOND_ORE:aum=diamondexp;break;
		      	case REDSTONE_ORE:aum=redstoneexp;break;
		      	case EMERALD_ORE:aum=emeraldexp;break;
		      	case QUARTZ_ORE:aum=quartzexp;break;
		      	default:aum=0;break;
			}
			FollowerManager.addReputation(event.getPlayer(),aum);
			return;
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
		Chunk c=p.getLocation().getChunk();
		Block t=c.getBlock(0, p.getLocation().getBlockY(),0);
		int x=t.getX();
		int y=p.getLocation().getBlockY();
		int z=t.getZ();
		Location lExpl=new Location(t.getWorld(),x+8,y,z+8);
		boolean used=false;
		for(int cx=x; cx<x+16; cx++)
		{
			for(int cy=y; cy<=y+5; cy++)
			{
				for(int cz=z; cz<z+16; cz++)
				{
					
					Block temp=(new Location(p.getWorld(),cx,cy,cz)).getBlock();
					if(temp.getType()==Material.STONE)
					{
						if(used==false)
						{
					    	(new ModifyBlockTask(temp,Material.TORCH,(byte) 0)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
						}
						else
						{
					    	(new ModifyBlockTask(temp,Material.AIR,(byte) 0)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));

						}
						used=true;					
					}
				}
			}
		}		
		for(Player pl:Bukkit.getOnlinePlayers())
		{
			(new ParticleTask(ParticleEffects.LARGE_EXPLODE,pl,lExpl,0,0,0,1,10)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
		}
		(new ParticleTask(ParticleEffects.LARGE_EXPLODE,p,p.getLocation(),0,0,0,1,10)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
		if(used) super.activatePower(p);
		return used;
		
	}	
}