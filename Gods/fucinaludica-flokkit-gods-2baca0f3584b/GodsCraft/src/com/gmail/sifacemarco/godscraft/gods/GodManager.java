package me.elsifo92.gods.gods;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.World;

import me.elsifo92.gods.GodsCraft;
import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.data.config.GodConfig;
import me.elsifo92.gods.data.database.DatabaseManager;
import me.elsifo92.gods.events.NewChampEvent;
import me.elsifo92.gods.gods.God.GodType;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.gods.types.DeathGod;
import me.elsifo92.gods.gods.types.MinerGod;
import me.elsifo92.gods.gods.types.NatureGod;
import me.elsifo92.gods.gods.types.WarGod;
import me.elsifo92.gods.data.scoreboard.ScoreboardManager;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class GodManager
{
	private static HashMap<GodType,God> gods=new HashMap<>();
	private static void loadGod(GodType gt)
	{
		GodConfig config=ConfigManager.getGodConfig(gt);
		if(config!=null)
		{
			God g;
			switch(gt)
			{
				case MINER:g=new MinerGod(gt,config);break;
				case NATURE:g=new NatureGod(gt,config);break;
				case WAR:g=new WarGod(gt,config);break;
				case DEATH:g=new DeathGod(gt,config);break;
				default:g=null;
			}
			Bukkit.getConsoleSender().sendMessage("Caricamento del dio '"+config.getName()+"', tipo: "+gt.toString());
			gods.put(gt,g);
			Bukkit.getServer().getPluginManager().registerEvents(g,Bukkit.getPluginManager().getPlugin("GodsCraft"));
	    	ScoreboardManager.loadGod(gt);
		}			
	}
	public static void loadGods(GodsCraft plugin)
	{	
		for(GodType gt:GodType.values())
		{
			loadGod(gt);
		}
	}
	public static void godEvoked(Player p)
	{
		DatabaseManager.loadChamps();
		if(!FollowerManager.isFollower(p)) return;
		for(int i=0; i<gods.size(); i++)
		{
			God g=FollowerManager.getGodFollowed(p);
			if(g.hasChamp() && g.getChamp().equals(p))
			{
				if(g.activatePower(p))
				{				    
					for(Player q : Bukkit.getOnlinePlayers())
					{
		                q.playSound(p.getLocation(), Sound.PORTAL_TRAVEL, 2, (float) 1.5);		                
		            }
					Bukkit.getServer().broadcastMessage(Utility.formattedMessage(p.getName()+Utility.getMessage("msg.gods.power.activate")+FollowerManager.getGodFollowed(p).getGodName()));
				}
			}
		}
	}
	public static int getActiveGods()
	{
		return gods.size();
	}
	public static God getGod(GodType type)
	{
		return gods.get(type);
	}
	public static God getGod(String name)
	{
		for(Entry<GodType,God> i:gods.entrySet())
		{
			God g=i.getValue();
			if(g.getGodName().toLowerCase().equals(name.toLowerCase()))
			{
				return g;
			}
		}
		return null;
	}	
	public static void newChamp(God g) 
	{
		DatabaseManager.setChamp(g, null);
		Player p=FollowerManager.getNewChampCandidate(g);
		if(p!=null) applyChamp(p,g);
		else Bukkit.broadcastMessage(Utility.formattedMessage(g.getGodName()+Utility.getMessage("msg.gods.champ.noone")));
	}
	public static String getGodNameWhomImChamp(Player player) 
	{
		for(int i=0; i<gods.size(); i++)
		{
			God g=gods.get(i);
			if(g.hasChamp() && g.getChamp().equals(player)) return g.getGodName();
		}
		return null;
	}
	public static void applyChamp(Player p, God gd) 
	{
		gd.setChamp(p);
		DatabaseManager.setChamp(gd, p);
		Bukkit.getServer().broadcastMessage(Utility.formattedMessage(p.getName()+Utility.getMessage("msg.god.champ.new")+gd.getGodName()));
		Bukkit.getServer().getPluginManager().callEvent(new NewChampEvent(p, gd));
	}
	public static void setPriest(Player sender, Player player) 
	{
		Town t=null;
		for(World w:Bukkit.getWorlds())
		{
			try 
			{
				for(Town u:TownyUniverse.getWorld(w.getName()).getTowns())
				{
					if(TownyUniverse.getPlayer(u.getMayor()).equals(sender)) player.sendMessage(Utility.formattedMessage("Sei autorizzato ad eseguire questo comando"));
					else player.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.misc.priestfail")));
				}
			} 
			catch (NotRegisteredException e) 
			{
				e.printStackTrace();
			} 
			catch (TownyException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}