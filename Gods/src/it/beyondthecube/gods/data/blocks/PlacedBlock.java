package it.beyondthecube.gods.data.blocks;

import java.util.GregorianCalendar;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class PlacedBlock
{
	private Location b;
	private GregorianCalendar dayPlaced;
	private boolean marked;
	private int idDB;
	public PlacedBlock(Location b, GregorianCalendar dayPlaced,int idDB)
	{
		this.b=b;
		this.dayPlaced=dayPlaced;
		this.marked=false;
		this.idDB=idDB;
	}
	public boolean isInDB()
	{
		return idDB>0;
	}
	public void mark()
	{
		marked=true;
	}
	public GregorianCalendar getDayPlaced()
	{
		return dayPlaced;
	}
	public Block getBlock()
	{
		return b.getBlock();
	}
	public boolean isMarked()
	{
		return marked;
	}
	public int getIdDB() 
	{
		return this.idDB;
	}
	@Override
	public String toString()
	{
		String ris=""+b.toString()+dayPlaced.getTime().toString()+marked+idDB;
		return ris;
	}
}