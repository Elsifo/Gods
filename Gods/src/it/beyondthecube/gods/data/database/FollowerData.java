package it.beyondthecube.gods.data.database;

import java.util.GregorianCalendar;

import it.beyondthecube.gods.gods.God;

public class FollowerData
{
	private int rep;
	private God god;
	private GregorianCalendar lastPrayed;
	private GregorianCalendar lastHealed;
	private boolean isLoggingIn;
	private int points;
	public FollowerData(int rep, int points, God god, GregorianCalendar lastPrayed,GregorianCalendar lastHealed, boolean b)
	{
		this.rep=rep;
		this.points=points;
		this.god=god;
		this.lastPrayed=lastPrayed;
		this.lastHealed=lastHealed;
	}
	public void setLastPrayed(GregorianCalendar lastPrayed)
	{
	  	this.lastPrayed=lastPrayed;
	}
	public boolean isLoggingIn()
	{
		return isLoggingIn;
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
    public God getGod()
    {
    	return god;
    }
}