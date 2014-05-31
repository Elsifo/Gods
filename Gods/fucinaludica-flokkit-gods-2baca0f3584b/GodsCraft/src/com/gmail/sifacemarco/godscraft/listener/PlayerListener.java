package me.elsifo92.gods.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.database.DatabaseManager;
import me.elsifo92.gods.data.database.FollowerData;
import me.elsifo92.gods.gods.follower.FollowerManager;

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
		if(f==null) return;
		FollowerManager.addFollower(e.getPlayer(), f.getReputation(), f.getPoints(), f.getGod(), f.getLastPrayed(), f.getLastHealed(), true);
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		FollowerManager.saveFollower(e.getPlayer());
	}
}
