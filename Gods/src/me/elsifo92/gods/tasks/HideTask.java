package me.elsifo92.gods.tasks;

import me.elsifo92.gods.data.scoreboard.ScoreType;
import me.elsifo92.gods.data.scoreboard.ScoreboardManager;
import me.elsifo92.gods.gods.follower.FollowerManager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HideTask extends BukkitRunnable
{
	private Player p;
	private ScoreType st;
	public HideTask(Player p, ScoreType st)
	{
		this.p=p;
		this.st=st;
	}
	public void run()
	{
		if(!(st==ScoreType.REPUTATION && FollowerManager.hasFixedReputation(p))) ScoreboardManager.hideScoreboard(p);
	}
}
