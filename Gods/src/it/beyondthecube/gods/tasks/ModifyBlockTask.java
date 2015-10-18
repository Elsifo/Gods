package it.beyondthecube.gods.tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import it.beyondthecube.gods.data.blocks.BlockManager;

public class ModifyBlockTask extends BukkitRunnable
{
	private Block block;
	private Material finalMaterial;
	private byte data;
	public ModifyBlockTask(Block block,Material finalMaterial, byte data)
	{
		this.block=block;
		this.finalMaterial=finalMaterial;
		this.data=data;
	}
	public void run()
	{
		BlockManager.setMaterial(block,finalMaterial,data);
	}
}
