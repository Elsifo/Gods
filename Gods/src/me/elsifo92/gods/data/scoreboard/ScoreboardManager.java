package me.elsifo92.gods.data.scoreboard;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.database.DatabaseManager;
import me.elsifo92.gods.gods.GodManager;
import me.elsifo92.gods.gods.God.GodType;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.tasks.HideTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager 
{
	private static HashMap<String,Scoreboard> scores=new HashMap<String,Scoreboard>();
	private static HashMap<GodType,ArrayList<Entry<String,Integer>>> tops=new HashMap<GodType,ArrayList<Entry<String,Integer>>>();
	private static ScoreboardManager instance;
	public static ScoreboardManager getInstance()
	{
		if(instance==null) instance=new ScoreboardManager();
		return instance;
	}
	public static void loadGod(GodType gt)
	{
		ArrayList<Entry<String,Integer>> l=DatabaseManager.getTopFollowers(GodManager.getGod(gt));
		for(Entry<String, Integer> e:l)
		{
			(new UpdateTop(e.getKey(), e.getValue(), gt)).run();
		}
	}
	static synchronized void updateTop(String key, Integer value, GodType gt)
	{
		if(value==0) return;
		if(!tops.containsKey(gt)) tops.put(gt,new ArrayList<Entry<String,Integer>>());
		ArrayList<Entry<String,Integer>> t=tops.get(gt);
		SimpleEntry<String, Integer> entry=new SimpleEntry<String,Integer>(key,value);
		if(t.isEmpty()) t.add(entry);
		if(t.contains(entry)) t.remove(entry);
		int count=0;
		for(Entry<String,Integer>e:t)
		{
			if(e.getValue()<value) break;
			count++;
		}
		if(count<11) t.add(entry);
		if(t.size()>10) t.remove(10);
	}
	public static void newPlayer(Player p)
	{
		Scoreboard b=Bukkit.getScoreboardManager().getNewScoreboard();
		b.registerNewObjective("Reputazione", "dummy");
		scores.put(p.getName(), b);		
		updateScore(p,true);
	}
	public static void updateScore(Player p, boolean newPlayer)
	{
		Scoreboard s=scores.get(p.getName());
    	Objective o=s.getObjective("Reputazione");
    	o.setDisplaySlot(DisplaySlot.SIDEBAR);
    	Score r=o.getScore(Bukkit.getOfflinePlayer("Level"));
    	Score pr=o.getScore(Bukkit.getOfflinePlayer("Remaining points"));
    	int level=FollowerManager.getReputation(p);
    	r.setScore(level);
    	pr.setScore(FollowerManager.getRemainingPoints(p));
    	p.setScoreboard(s);
    	(new HideTask(p, ScoreType.REPUTATION)).runTaskLater(Bukkit.getPluginManager().getPlugin("GodsCraft"), 60);
    	(new UpdateTop(p.getName(),level,FollowerManager.getGodFollowed(p).getGodType())).run();
	}
	public static void showScoreboard(Player p)
	{
		p.setScoreboard(scores.get(p.getName()));
	}
	public static void showTopScoreboard(Player p, GodType gt)
	{
		ArrayList<Entry<String,Integer>> top=tops.get(gt);
		if(top==null) 
		{
			p.sendMessage(Utility.formattedMessage("Nessun dato da visualizzare"));
			return;
		}
		String objname="Top - "+GodManager.getGod(gt).getGodName();
		Scoreboard s=Bukkit.getScoreboardManager().getNewScoreboard();
		s.registerNewObjective(objname, "dummy");
		Objective o=s.getObjective(objname);
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(Entry<String,Integer> e:top)
		{
			o.getScore(Bukkit.getOfflinePlayer(e.getKey())).setScore(e.getValue());
		}
		p.setScoreboard(s);
		(new HideTask(p,ScoreType.TOP)).runTaskLater(Bukkit.getPluginManager().getPlugin("GodsCraft"), 100);
	}
	public static void hideScoreboard(Player p)
	{
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}