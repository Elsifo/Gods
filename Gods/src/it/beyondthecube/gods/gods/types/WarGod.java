package it.beyondthecube.gods.gods.types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
/*import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;*/
import org.bukkit.inventory.ItemStack;

import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.data.config.GodConfig;
import it.beyondthecube.gods.gods.God;
import it.beyondthecube.gods.gods.follower.FollowerManager;
import it.beyondthecube.gods.misc.particles.ParticleEffects;
import it.beyondthecube.gods.tasks.ParticleTask;

public class WarGod extends God 
{
	public WarGod(GodType type, GodConfig c)
	{
		super(type,c);
	}
	@EventHandler( priority=EventPriority.LOWEST,ignoreCancelled=true)
	public void onCraftItem(CraftItemEvent e)
	{
		if(!FollowerManager.follows((Player)e.getWhoClicked(), this)) return;
		ItemStack is=e.getInventory().getResult();
		int aum;
		switch(is.getType())
		{
			case DIAMOND_SWORD:aum=600;break;
			case DIAMOND_HELMET:aum=1500;break;
			case DIAMOND_CHESTPLATE:aum=2400;break;
			case DIAMOND_LEGGINGS:aum=2100;break;
			case DIAMOND_BOOTS:aum=1200;break;
			default:return;
		}
		FollowerManager.addReputation((Player)e.getView().getPlayer(), aum);
	}
	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void onEnchantItem(EnchantItemEvent e)
	{
		if(!FollowerManager.follows(e.getEnchanter(),this)) return;
		int aum=0;
		switch(e.getItem().getType())
		{
			case DIAMOND_SWORD:
			case DIAMOND_HELMET:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_LEGGINGS:
			case DIAMOND_BOOTS:aum=(100*e.getExpLevelCost())/2;break;
			default:return;
		}
		if(aum>0) FollowerManager.addReputation(e.getEnchanter(), aum);
	}
	@EventHandler(priority=EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent e)
	{
		/*if(e.getInventory() instanceof AnvilInventory)
		{
			AnvilInventory ai=(AnvilInventory) e.getInventory();
			Player p=((Player)e.getWhoClicked());
			p.sendMessage(""+ai.getContents().length);
			for(ItemStack i:ai.getContents())
			{
				p.sendMessage(i.toString());
			}
			InventoryView view = e.getView();
			int rawSlot = e.getRawSlot();
			if(rawSlot == view.convertSlot(rawSlot))
			{
				if(rawSlot == 2)
				{
					ItemStack item = e.getCurrentItem();
					if(item != null)
					{
						
					}
				}
			}
		}*/
		//TODO
		//FIXME
	}
	@Override
	public boolean activatePower(Player p) 
	{
		if(this.getLastActivated()!=null)
        {
        	if(Utility.daysFromToday(super.getLastActivated())<4)
        	{
        		p.sendMessage(Utility.formattedMessage(this.getGodName()+Utility.getMessage("msg.god.power.cooldown")));
        		return false;
        	}        	
        }
		p.giveExpLevels(30);
		(new ParticleTask(ParticleEffects.DRIP_LAVA,p,p.getLocation(),0,0,0,1,10)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
		super.activatePower(p);
		return true;
	}
}
