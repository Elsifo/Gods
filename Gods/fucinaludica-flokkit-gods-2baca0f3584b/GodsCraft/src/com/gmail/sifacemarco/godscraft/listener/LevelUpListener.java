package me.elsifo92.gods.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.events.LevelUpEvent;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.GodManager;
import me.elsifo92.gods.gods.follower.FollowerManager;

public class LevelUpListener implements Listener 
{
	public LevelUpListener()
	{
		
	}
	@EventHandler
	public void onLevelUpEvent(LevelUpEvent e)
	{
		e.getPlayer().sendMessage(Utility.formattedMessage(Utility.getMessage("msg.followers.reputation.raise")+e.getGod().getGodName()));
		if(ConfigManager.isSoundEnabled()) e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 2, 1);
		FollowerManager.updateScore(e.getPlayer());
		if(e.getLevel()>=1000)
		{
			boolean newChamp=false;
			Player p=e.getPlayer();
			God gd=FollowerManager.getGodFollowed(p);
			if(!(gd.hasChamp()))
			{
				newChamp=true;
			}
			else
			{
				Player champ=gd.getChamp().getPlayer();
				if(p.getName().equals(champ.getName())) return;
				if(FollowerManager.getReputation(champ)<FollowerManager.getReputation(p)) 
				{
					newChamp=true;
				}
			}
			if(newChamp)
			{
				GodManager.applyChamp(p,gd);
			}
		}
	}
}
