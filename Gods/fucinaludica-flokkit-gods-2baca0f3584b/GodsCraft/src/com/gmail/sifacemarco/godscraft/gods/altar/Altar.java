package me.elsifo92.gods.gods.altar;

import org.bukkit.Location;
import org.bukkit.block.Sign;

import me.elsifo92.gods.gods.God;

public class Altar 
{
    private Location l;
    private God g;
    private AltarType type;
    private boolean marked;
	private int dbId;
    public Altar(Location l,God g,AltarType type, int dbId)
    {
	    this.l=l;
	    this.g=g;
	    this.type=type;
	    marked=false;
	    this.dbId=dbId;
	    Sign s=(Sign) l.getBlock().getState();
	    if(!((s.getLine(1)).equalsIgnoreCase(g.getGodName())))
	    {
	    	s.setLine(1,g.getGodName().toUpperCase());
	    }
    }
    public void mark()
    {
    	this.marked=true;
    }
    public boolean marked()
    {
    	return marked;
    }
    public Location getLocation()
    {
	    return l;
    }
    public God getGod()
    {
 	    return g;
    }
    public AltarType getType()
    {
	    return type;
    }
    @Override
    public boolean equals(Object obj)
    {
    	if(obj instanceof Sign)
    	{
    		 return ((Sign) obj).getLocation().equals(l);
    	}
    	if(obj instanceof Location)
    	{
    		return obj.equals(l);
    	}
    	return false;
    }
	public int getDBId() 
	{
		return dbId;
	}
}
