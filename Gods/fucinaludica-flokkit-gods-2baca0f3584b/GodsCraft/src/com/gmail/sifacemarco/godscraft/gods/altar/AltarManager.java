package me.elsifo92.gods.gods.altar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.follower.FollowerManager;

public class AltarManager 
{
    private static HashMap<Location,Altar> altars=new HashMap<Location,Altar>();
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
	public static void loadAltar(God god,AltarType type,Location l,int idDB)
    {
    	Sign s=(Sign)l.getBlock().getState();
    	if(!s.getLine(1).equalsIgnoreCase(god.getGodName()))
    	{
    		s.setLine(1, god.getGodName().toUpperCase());
    	}
    	altars.put(l,new Altar(l,god,type,idDB));
    }
    public static void newAltar(Player p, God g, AltarType t,int idDB)
    {
    	Sign s=(Sign) p.getTargetBlock(null, 10).getState();
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
    		p.sendMessage("Errore nella creazione dell'altare: "+g.getGodName()+((t==AltarType.THRONE)?" ha gi£ un trono":" non ha un trono"));
    	}
    	s.update(true);    	
    	altars.put(s.getLocation(),new Altar(s.getLocation(),g,t, idDB));
    	p.sendMessage(Utility.formattedMessage("Altare creato con successo."));
    	
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
				b.breakNaturally();
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
