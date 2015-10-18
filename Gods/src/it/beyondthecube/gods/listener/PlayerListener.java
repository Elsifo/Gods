package it.beyondthecube.gods.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.data.database.DatabaseManager;
import it.beyondthecube.gods.data.database.FollowerData;
import it.beyondthecube.gods.gods.follower.FollowerManager;

public class PlayerListener implements Listener
{
	public PlayerListener()
	{
		
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{		
		e.getPlayer().sendMessage(Utility.formattedMessage("Gods v."+Bukkit.getPluginManager().getPlugin("GodsCraft").getDescription().getVersion()+" - OK"));
		FollowerData f=DatabaseManager.getFollower(e.getPlayer());
		if(f==null) 
		{
			Bukkit.getConsoleSender().sendMessage("YOLO");
			return;
		}
		FollowerManager.addFollower(e.getPlayer(), f.getReputation(), f.getPoints(), f.getGod(), f.getLastPrayed(), f.getLastHealed(), true);
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		FollowerManager.saveFollower(e.getPlayer(),true);
	}
}
