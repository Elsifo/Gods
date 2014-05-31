package me.elsifo92.gods.listener;

import java.util.List;

import me.elsifo92.gods.data.blocks.BlockManager;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener
{
	@EventHandler(ignoreCancelled=true)
	public void onBlockPistonExtend(BlockPistonExtendEvent e)
	{
		BlockFace dir=e.getDirection();
		List<Block> l=(List<Block>) e.getBlocks();
		for(Block b:l)
		{
			BlockManager.mark(b);
		}
		for(Block b:l)
		{
			BlockManager.blockPlaced(b.getRelative(dir));
		}
	}
	@EventHandler(ignoreCancelled=true)
	public void onBlockPlace(BlockPlaceEvent e)
	{
		switch(e.getBlock().getType())
		{
			case STONE:
			case COAL_ORE:
			case DIAMOND_ORE:
			case REDSTONE_ORE:
			case IRON_ORE:
			case EMERALD_ORE:
			case GOLD_ORE:
			case LAPIS_ORE:
			case SUGAR_CANE_BLOCK:
			case PUMPKIN:
			case MELON_BLOCK:
			{
				BlockManager.blockPlaced(e.getBlock());
			}
			break;
			default:return;
		}
	}
	@EventHandler(ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent e)
	{
		switch(e.getBlock().getType())
		{
			case STONE:
			case COAL_ORE:
			case DIAMOND_ORE:
			case REDSTONE_ORE:
			case IRON_ORE:
			case EMERALD_ORE:
			case GOLD_ORE:
			case LAPIS_ORE:
			case SUGAR_CANE:
			{
				BlockManager.mark(e.getBlock());
			}
			break;
			default:return;
		}
	}
}
