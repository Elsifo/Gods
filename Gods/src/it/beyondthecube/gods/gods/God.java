package it.beyondthecube.gods.gods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.data.config.ConfigManager;
import it.beyondthecube.gods.data.config.GodConfig;
import it.beyondthecube.gods.events.GiftedEvent;
import it.beyondthecube.gods.events.GodPowerActivatedEvent;
import it.beyondthecube.gods.gods.follower.FollowerManager;

public abstract class God implements Listener
{
	public enum GodType
	{
		MINER,
		NATURE,
		WAR,
		DEATH;
	}
	private ArrayList<ItemStack> items;
	private Sign throne;
	private String nome;
	private OfflinePlayer champ;
	private GodType type;
	private int cap;
	private GregorianCalendar lastActivated;
	public God(GodType type, GodConfig c)
    {
    	throne=null;
    	this.type=type;
    	this.nome=c.getName();
    	this.cap=c.getCap();
    	this.items=c.getItemsList();
    	this.champ=c.getChamp();
    	if(c.hasItemList())
    	{
    		ArrayList<ItemStack> items=new ArrayList<>(c.getItemsList());
    		for(ItemStack i:items)
    		{
    			addItem(i);
    		}
    	}    	
    }
	public GodType getGodType()
	{
		return type; 
	}
	public boolean hasChamp()
	{
		return (champ!=null?true:false);
	}
	public OfflinePlayer getChamp()
	{
		return champ;
	}
	public void setChamp(OfflinePlayer champ)
	{
		this.champ=champ;
	}
	public Sign getThrone()
	{
		return throne;
	}
    public boolean hasThrone()
    {
    	return throne!=null;
    }
    public void setThrone(Sign throne)
    {
    	this.throne=throne;
    }
	public boolean isThrone(Sign s) 
	{
		if(throne==null) return false;
		return throne.getLocation().equals(s.getLocation());
	}
	public void setName(String nome)
	{
		this.nome=nome;
	}
	public String getGodName()
	{
		return nome;		
	}
	public boolean activatePower(Player p)
	{
		if(lastActivated==null) lastActivated=new GregorianCalendar();
		lastActivated.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
		Bukkit.getPluginManager().callEvent(new GodPowerActivatedEvent(this, p));
		return true;
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof God)
		{
			God g=(God)o;
			if(g.getGodName().equals(this.nome)) return true;
		}
		return false;
	}
	public int getCap() 
	{
		return cap;
	}
	public void prayed(Player p)
	{
		if(items==null || items.isEmpty()) return;
		if(ConfigManager.getPexConfig().hasPerm(p, "gods.admin") || (new Random().nextInt(10000))<FollowerManager.getReputation(p))
		{
			Random r=new Random();
			int rnd=r.nextInt(items.size());
			ItemStack i=items.get(rnd);
			if(i==null) return;
			p.getWorld().dropItem(p.getLocation(), i);
			p.sendMessage(Utility.formattedMessage(getGodName()+Utility.getMessage("msg.gods.gift")));
			Bukkit.getServer().getPluginManager().callEvent(new GiftedEvent(this, i, p));
		}
	}
	public GregorianCalendar getLastActivated()
	{
		return lastActivated;
	}
	public void setLastActivated(GregorianCalendar c) 
	{
		this.lastActivated=c;	
	}
	public void addItem(ItemStack is) 
	{
		items.add(is);		
	}
}