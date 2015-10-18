package it.beyondthecube.gods.gods.follower;

import java.util.GregorianCalendar;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import it.beyondthecube.gods.gods.God;

public class Follower 
{
    private Player p;
    private int rep;
    private int points;
    private God god;
    private GregorianCalendar lastPrayed;
    private GregorianCalendar lastHealed;
	private boolean fixed;
	private boolean isPriest;
    public Follower(Player p, int rep, int points, God god, GregorianCalendar lastPrayed,GregorianCalendar lastHealed)
    {
    	this.fixed=false;
	    this.p=p;
	    this.rep=rep;
	    this.points=points;
	    this.god=god;
	    this.lastPrayed=lastPrayed;
	    this.lastHealed=lastHealed;
	    this.isPriest=false;
    }
    public void setLastPrayed(GregorianCalendar lastPrayed)
    {
    	this.lastPrayed=lastPrayed;
    }
    public GregorianCalendar getLastPrayed()
    {
		return lastPrayed;
    }
    public int getReputation()
    {
    	return rep;
    }
    public int getPoints()
    {
    	return points;
    }
    public void setPoints(int points)
    {
    	this.points=points;
    }
    public void addReputation(int rep)
    {
    	this.rep+=rep;
    }
    public void setLastHealed(GregorianCalendar lastHealed)
    {
    	this.lastHealed=lastHealed;
    }
    public GregorianCalendar getLastHealed()
    {
    	return lastHealed;
    }
    public Player getPlayer()
    {
    	return p;
    }
    public God getGod()
    {
    	return god;
    }
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p.getName() == null) ? 0 : p.getName().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Follower)) return false;
		Follower other = (Follower) obj;
		if (p == null) 
		{
			if (other.p != null) return false;
		} 
		else if (!p.equals(other.p)) return false;
		return true;
	}
	public void deleteScoreboard()
	{
		p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		p.getScoreboard().resetScores(p);
	}
	@Override
	public String toString()
	{
		return p.getName()+rep+god.getGodName()+this.lastHealed.getTime().toString()+this.lastPrayed.getTime().toString();
	}
	public void removeRepuation(int level)
	{
		rep=rep-level;		
	}
	public void setFixedReputation(boolean b) 
	{
		this.fixed=b;		
	}
	public boolean hasFixedReputation()
	{
		return fixed;
	}
	public void setPriest(boolean b) 
	{
		this.isPriest=b;		
	}
	public boolean isPriest()
	{
		return isPriest;
	}
}