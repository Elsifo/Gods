package me.elsifo92.gods.listener;

import me.elsifo92.gods.data.database.DatabaseManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener
{
	public ChunkListener()
	{
		
	}
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e)
	{
		DatabaseManager.loadBlocks(e.getChunk());
	}
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e)
	{
		DatabaseManager.saveBlocks(e.getChunk());
	}
}
