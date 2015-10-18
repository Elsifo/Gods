package it.beyondthecube.gods.data.blocks;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockManager 
{
	private static HashMap<Location,PlacedBlock> blocks=new HashMap<Location,PlacedBlock>();
	private static PlacedBlock getPlacedBlock(Block b)
	{
		if(blocks.containsKey(b.getLocation())) return blocks.get(b.getLocation());
		return null;
	}
	public static void mark(Block b)
	{
		PlacedBlock p=getPlacedBlock(b);
		if(p!=null)p.mark();
	}
	public static boolean isNatural(Block b)
	{
		if(blocks.containsKey(b.getLocation())) return false;
		else return true;
	}
	public static void loadBlock(Block block, GregorianCalendar g, int idDB) 
	{
		blocks.put(block.getLocation(),new PlacedBlock(block.getLocation(),g, idDB));		
	}
	public static void blockPlaced(Block block) 
	{
		blocks.put(block.getLocation(),new PlacedBlock(block.getLocation(),(GregorianCalendar) GregorianCalendar.getInstance(), 0));
	}
	public static Set<Entry<Location, PlacedBlock>> getPlacedBlocksIterator() 
	{
		return blocks.entrySet();
	}
	public static boolean verifyBlock(Block targetBlock)
	{
		if(blocks.containsKey(targetBlock.getLocation())) return true;
		else return false;
	}
	public static synchronized void setMaterial(Block block, Material finalMaterial, byte data) 
	{
		block.setType(finalMaterial);	
		//block.setData(data);
		block.getState().update();
	}
}