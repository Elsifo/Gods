package me.elsifo92.gods.gods.altar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.tasks.ModifyBlockTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AltarManager 
{
    private static HashMap<Location,Altar> altars=new HashMap<Location,Altar>();
    private static HashMap<String,Altar> kneelerowners=new HashMap<>();
    private static HashMap<String,Altar> healerowners=new HashMap<>();
    public static Altar getAltar(Sign s)
    {
    	if(altars.containsKey(s.getLocation()))
    	{
       		Altar a=altars.get(s.getLocation());
       		if(a.marked()) return null;
       		return a;
    	}
    	return null;
    }
    public static void altarInteraction(Player p, God g, Altar a)
    {
    	if(!FollowerManager.canInteract(p, a))
    	{
    		p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altars.deny")));
    		return;
    	}
    	if(!g.hasThrone())
    	{
    		p.sendMessage(Utility.formattedMessage(g.getGodName()+Utility.getMessage("msg.altar.failnew.secondnot")));
    		return;
    	}
    	switch(a.getType())
    	{
    		case PRAY:FollowerManager.pray(p);break; 
    		case HEAL:FollowerManager.heal(p);break;
    		case THRONE:
    		{
    			if(!FollowerManager.follows(p,g)) 
    			{
    				GregorianCalendar gc=(GregorianCalendar) Calendar.getInstance();
    				FollowerManager.addFollower(p,0,0,g,gc,gc,false);
    			}
    			else 
    			{
    				FollowerManager.pray(p);
    			}
    		}
    		break;
    	}
    }
	public static void loadAltar(God god,AltarType type,Location l,int idDB, Player owner)
    {
    	Sign s=(Sign)l.getBlock().getState();
    	if(!s.getLine(1).equalsIgnoreCase(god.getGodName()))
    	{
    		s.setLine(1, god.getGodName().toUpperCase());
    	}
    	Altar a=new Altar(l,god,type,idDB,owner);
    	altars.put(l,a);
    	switch(a.getType())
    	{
    		case PRAY:kneelerowners.put(owner.getName(), a);break;
    		case HEAL:healerowners.put(owner.getName(), a);break;
    		case THRONE:break;
    	}
    }
    public static void newAltar(Player p, God g, AltarType t,int idDB)
    {
    	boolean err=false;
    	switch(t)
    	{
    		case HEAL:if(FollowerManager.getReputation(p)<=250 || healerowners.get(p.getName())!=null) err=true;break;
    		case PRAY:if(FollowerManager.getReputation(p)<=500 || kneelerowners.get(p.getName())!=null) err=true;break;
		default:
			break;
    	}
    	if(err)
    	{
    		p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altars.ner")));
    		return;
    	}
    	Sign s=(Sign) ((CraftPlayer) p).getTargetBlock(null, 10).getState();
    	if(getAltar(s)!=null)
    	{
    		p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altar.already")));
    		return;
    	}
    	boolean created=true;
    	s.setLine(0, "[GOD]");
    	s.setLine(1, g.getGodName().toUpperCase());
    	switch(t)
    	{    	
    		case PRAY:if(!g.hasThrone()){created=false;break;}s.setLine(3, "Pray me"); FollowerManager.addReputation(p,25);break;
    		case HEAL:if(!g.hasThrone()){created=false;break;}s.setLine(3, "Heal you"); FollowerManager.addReputation(p,50);break;
    		case THRONE:
    		{
    			if(g.hasThrone()){created=false;break;}
    			s.setLine(3, "Follow me");
    			g.setThrone(s);
    		}break;
    	}
    	if(!created)
    	{
    		p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altars.failnew.first"))+g.getGodName()+((t==AltarType.THRONE)?Utility.getMessage("msg.altars.failnew.secondt"):Utility.getMessage("msg.altars.failnew.secondnot")));
    		s.getBlock().breakNaturally();
    		return;
    	}
    	s.update(true); 
    	Altar a=new Altar(s.getLocation(),g,t, idDB,p);
    	switch(t)
    	{
    		case PRAY:kneelerowners.put(p.getName(), a);break;
    		case HEAL:healerowners.put(p.getName(), a);break;
		default:
			break;
    	}
    	altars.put(s.getLocation(),a);
    	p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altars.create")));
    	
    }
    public static Set<Entry<Location, Altar>> getAltars()
    {
    	return altars.entrySet();
    }
    public static void removeAltar(Player p,Block b)
    {
    	if(b.getState() instanceof Sign)
    	{
    		Altar a=getAltar((Sign) b.getState());
    		if(a!=null)
    		{
    			if(a.getType()==AltarType.THRONE)
    			{
    				if(!ConfigManager.getPexConfig().hasPerm(p, "gods.throne.delete")) 
    				{
    					Bukkit.getConsoleSender().sendMessage(Utility.formattedMessage(Utility.getMessage("msg.misc.permfail")));
    					return;
    				}
    				a.getGod().setThrone(null);    				
    			}
    			a.mark();
				(new ModifyBlockTask(b,Material.AIR,(byte)0)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
    		}
    	}
    }
	public static boolean isAltar(Sign s)
	{	
		Altar a=altars.get(s.getLocation());
		if(a==null) return false;
		if(a.marked()) return false;
		return true;
	}
}