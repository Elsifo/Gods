package me.elsifo92.gods.gods.types;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
/*import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;*/
import org.bukkit.inventory.ItemStack;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.GodConfig;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.follower.FollowerManager;

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
		switch(is.getTypeId())
		{
			case 276:aum=600;break;
			case 310:aum=1500;break;
			case 311:aum=2400;break;
			case 312:aum=2100;break;
			case 313:aum=1200;break;
			default:return;
		}
		FollowerManager.addReputation((Player)e.getView().getPlayer(), aum);
	}
	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void onEnchantItem(EnchantItemEvent e)
	{
		if(!FollowerManager.follows(e.getEnchanter(),this)) return;
		int id=e.getItem().getTypeId();
		int aum=0;
		switch(id)
		{
			case 276:
			case 310:
			case 311:
			case 312:
			case 313:aum=(100*e.getExpLevelCost())/2;
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
		p.giveExpLevels(50);
		super.activatePower(p);
		return true;
	}
}
