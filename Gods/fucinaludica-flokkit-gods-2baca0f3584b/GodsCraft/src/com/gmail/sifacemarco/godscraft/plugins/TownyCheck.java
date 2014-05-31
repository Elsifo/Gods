package me.elsifo92.gods.plugins;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

public class TownyCheck 
{
	private boolean loaded;
	public TownyCheck(boolean b)
	{
		this.loaded=b;
	}
	public boolean isPriest(Player p) 
	{
		return false;
	}
	@SuppressWarnings("deprecation")
	public boolean canBreak(Player player, Block block)
	{
		if(!loaded) return true;
		TownBlock tb=TownyUniverse.getTownBlock(block.getLocation());
		if(tb==null) return true;
		if(PlayerCacheUtil.getCachePermission(player,block.getLocation() , block.getTypeId(), TownyPermission.ActionType.DESTROY)) return true;
		return false;
	}
	public boolean isMayor(Player p)
	{
		//TODO
		return false;
	}
}
