package me.elsifo92.gods.tasks;

import me.elsifo92.gods.misc.particles.ParticleEffects;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;

public class ParticleTask extends BukkitRunnable
{
	private ParticleEffects pe;
	private Player p;
	private Location l;
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	private float speed;
	private int count;

	public ParticleTask(ParticleEffects pe, Player p, Location l, float offsetX, float offsetY, float offsetZ, float speed, int count)
	{
		this.pe=pe;
		this.p=p;
		this.l=l;
		this.offsetX=offsetX;
		this.offsetY=offsetY;
		this.offsetZ=offsetZ;
		this.speed=speed;
		this.count=count;
	}
	@Override
	public void run() 
	{
		try 
		{
			pe.sendToPlayer(p, l, offsetX, offsetY, offsetZ, speed, count);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
}