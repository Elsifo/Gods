package me.elsifo92.gods.gods.follower;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import me.elsifo92.gods.Reputation;
import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.data.database.DatabaseManager;
import me.elsifo92.gods.events.HealedByGodEvent;
import me.elsifo92.gods.events.LevelUpEvent;
import me.elsifo92.gods.events.GodPrayedEvent;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.GodManager;
import me.elsifo92.gods.gods.altar.Altar;
import me.elsifo92.gods.gods.altar.AltarType;
import me.elsifo92.gods.data.scoreboard.HideTask;
import me.elsifo92.gods.data.scoreboard.ScoreType;
import me.elsifo92.gods.data.scoreboard.ScoreboardManager;

public class FollowerManager 
{
	private static HashMap<String,Follower> followers=new HashMap<String,Follower>();
	private static Follower getFollower(Player p)
	{
		return followers.get(p.getName());
	}
	public static void addReputation(Player p,int points)
	{
		Follower f=getFollower(p);
		if(f!=null)
		{
			if(f.getReputation()>=f.getGod().getCap()) return;
			int pointsTo=Reputation.getPointsTo(f.getReputation()+1);
			f.setPoints(f.getPoints()+points);
			if(f.getPoints()>=Reputation.getPointsTo(f.getReputation()+1))
			{
				f.addReputation(1);
				f.setPoints(f.getPoints()-pointsTo);
				Bukkit.getServer().getPluginManager().callEvent(new LevelUpEvent(f.getReputation(), p,f.getGod()));			
			}
		}
	}
	public static int getReputation(Player p) 
	{
		return followers.get(p.getName()).getReputation();
	}
	public static void addFollower(Player p, int rep, int points, God g, GregorianCalendar lastPrayed, GregorianCalendar lastHealed, boolean isLoggingIn)
	{
		Follower f=getFollower(p);
		if(f==null)
		{
			followers.put(p.getName(), new Follower(p,rep,points,g,lastPrayed,lastHealed));
			if(!isLoggingIn)p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.followers.new")+WordUtils.capitalize(g.getGodName().toLowerCase())));
		}
		else
		{
			if(f.getGod().equals(g)) Utility.formattedMessage(Utility.getMessage("msg.followers.already")+WordUtils.capitalize(g.getGodName().toLowerCase()));
			else
			{
				removeFollower(p,false);						
				p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.followers.new")+WordUtils.capitalize(g.getGodName().toLowerCase())));
				followers.put(p.getName(), new Follower(p,rep,points,g,lastPrayed,lastHealed));
			}
		}
		ScoreboardManager.newPlayer(p);
	}
	public static void removeFollower(Player p, boolean isDisconnecting)
	{
		God g=getFollower(p).getGod();
		followers.remove(getFollower(p));
		if(!isDisconnecting && g.hasChamp() && g.getChamp().equals(p))
		{
			g.setChamp(null);
			GodManager.newChamp(g);
		}		
	}
	public static God getGodFollowed(Player p) 
	{
		if(followers.containsKey(p.getName())) return getFollower(p).getGod();
		return null;
	}
	public static void pray(Player p)
	{
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 3, 1);
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 3, 4);
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 3, 5);
		Follower f=getFollower(p);
		if(f==null) return;
		f.setLastPrayed(new GregorianCalendar());
		addReputation(p,1500);
		p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.god.pray")));
		Bukkit.getServer().getPluginManager().callEvent(new GodPrayedEvent(f.getGod(),p));
		f.getGod().prayed(p);
	}
	public static boolean follows(Player p, God g)
	{
		Follower f=getFollower(p);
		if(f==null) return false;
		else return (f.getGod().equals(g))?true:false;
	}
	public static GregorianCalendar getLastHeal(Player player) 
	{
		Follower f=getFollower(player);
		return f.getLastHealed();
	}
	public static GregorianCalendar getLastPray(Player player) 
	{
		return getFollower(player).getLastPrayed();
	}
	public static int getTotalFollowers()
	{
		return followers.size();
	}
	public static Player getNewChampCandidate(God g)
	{
		Player p=null;
		int maxRep=0;
		for(Entry<String, Follower> e:followers.entrySet())
		{
			Follower f=e.getValue();
			if(f.getGod().equals(g) && f.getReputation()>maxRep) 
			{
				maxRep=f.getReputation();
				p=f.getPlayer();
			}
		}
		return p;
	}
	public static void heal(Player p) 
	{
		Follower f=getFollower(p);
		if(f==null) return;
		HealedByGodEvent e=new HealedByGodEvent(p,f.getGod());
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) return;
		p.setHealth(20);
		ArrayList<PotionEffect> pl=(ArrayList<PotionEffect>) p.getActivePotionEffects();
		for(PotionEffect eff:pl)
		{
			switch(eff.getType().getName())
			{
				case "POISON":
				case "BLINDNESS":
				case "CONFUSION":
				case "HUNGER":
				case "SLOW":
				case "WEAKNESS":
				case "WITHER":p.removePotionEffect(eff.getType());break;
				
			}
		}
		p.setExhaustion(0);
		Location l=p.getLocation();
		l.getWorld().playEffect(l,Effect.POTION_BREAK,0);
		p.sendMessage(Utility.formattedMessage(f.getGod().getGodName()+Utility.getMessage("msg.gods.heal")));
		GregorianCalendar gc=(GregorianCalendar) GregorianCalendar.getInstance();
		f.setLastHealed(gc);
		
	}
	public void setPrayedToday(Player p) 
	{
		GregorianCalendar g=new GregorianCalendar();
		g.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
		Follower f=getFollower(p);
		if(f!=null) f.setLastPrayed(g);		
	}
	public static boolean canInteract(Player p, Altar a)
	{
		if(ConfigManager.getPexConfig().hasPerm(p, "gods.admin")) return true;
		Follower f=getFollower(p);
		if(f==null) return true;
		GregorianCalendar pray=f.getLastPrayed();
		GregorianCalendar heal=f.getLastHealed();
		AltarType at=a.getType();
		if(at==AltarType.HEAL)
		{
			if(Utility.daysFromToday(heal)<=1) return false;
			else return true;
		}
		if(at==AltarType.PRAY)
		{
			if(Utility.daysFromToday(pray)==0) return false;
			else return true;
		}
		if(at==AltarType.THRONE)
		{
			if(Utility.daysFromToday(pray)==0 && f.getGod().equals(a.getGod())) return false;
			else return true;
		}
		return false;
	}
	public static boolean isFollower(Player p)
	{
		return (getFollower(p)==null)?false:true;
	}
	public static void saveFollower(Player p)
	{
		Follower f=getFollower(p);
		if(f==null) return;
		DatabaseManager.saveFollower(f.getPlayer(),f.getReputation(),f.getPoints(),f.getGod(),f.getLastPrayed(),f.getLastHealed());
		removeFollower(p,true);		
	}
	public static void updateScore(Player p) 
	{
		Follower f=getFollower(p);
		if(f==null) return;
		ScoreboardManager.updateScore(p,false);		
	}
	public static void removeReputation(Player p, int rep) 
	{
		followers.get(p.getName()).removeRepuation(rep);
		p.sendMessage(Utility.formattedMessage("Reputazione ridotta."));
		ScoreboardManager.showScoreboard(p);
		(new HideTask(p,ScoreType.REPUTATION)).runTaskLater(Bukkit.getPluginManager().getPlugin("GosdCraft"), 60);
	}
	public static void setFixedReputation(Player p) 
	{
		getFollower(p).setFixedReputation(true);
	}
	public static void removeFixedReputation(Player p) 
	{
		getFollower(p).setFixedReputation(false);
	}
	public static boolean hasFixedReputation(Player p)
	{
		return getFollower(p).hasFixedReputation();
	}
	public static void updateReputationLevel(OfflinePlayer player, int rep) 
	{
		if(player.isOnline()) addReputation(player.getPlayer(), Reputation.getPointsTo(rep));
		else DatabaseManager.updateReputationLevel(player,rep);
	}
	public static void setPriest(Player p, boolean b)
	{
		followers.get(p.getName()).setPriest(b);
	}
}
