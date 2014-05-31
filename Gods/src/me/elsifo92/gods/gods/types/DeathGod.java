package me.elsifo92.gods.gods.types;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.config.GodConfig;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.misc.particles.ParticleEffects;
import me.elsifo92.gods.tasks.ParticleTask;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DeathGod extends God implements Listener
{
	private int playerexp;
	private int mobexp;
	public DeathGod(GodType type, GodConfig c)
	{
		super(type, c);
	    FileConfiguration config=Bukkit.getPluginManager().getPlugin("GodsCraft").getConfig();
	    playerexp=config.getInt("exp.death.hit.player");
	    mobexp=config.getInt("exp.death.hit.mob");
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player)
		{
			Player p=(Player) event.getDamager();
			if(FollowerManager.follows(p, this))
			{
				int aum=0;
				p.sendMessage(""+aum);
				switch(event.getEntityType())
				{
					case ZOMBIE:aum=5;break;
					case BAT:aum=1;break;
					case ENDERMAN:aum=100;break;
					case CREEPER:aum=95;break;
					case BLAZE:aum=70;break;
					case SPIDER:aum=10;break;
					case SKELETON:aum=15;break;
					case WOLF:aum=15;break;
					case ENDER_DRAGON:aum=200;break;
					case WITHER_SKULL:aum=50;break;
					case PIG:aum=5;break;
					case COW:aum=5;break;
					case SHEEP:aum=5;break;
					case CHICKEN:aum=5;break;
					case GHAST:aum=75;break;
					case PIG_ZOMBIE:aum=70;break;
					case CAVE_SPIDER:aum=20;break;
					case MAGMA_CUBE:aum=60;break;
					case SLIME:aum=60;break;
					case SILVERFISH:aum=60;break;
					case MUSHROOM_COW:aum=5;break;
					case WITCH:aum=75;break;
					case PLAYER:aum=100;break;
				    default:break;
				}
				aum=(int) ((aum*(event.getEntity() instanceof Player?playerexp:mobexp)*event.getDamage())/20);
				FollowerManager.addReputation(p, aum);
			}
		}
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
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1200, 4));
		p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,1200,4));
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,1200,2));
		for(Player pl:Bukkit.getOnlinePlayers())
		{
			(new ParticleTask(ParticleEffects.ANGRY_VILLAGER,pl,p.getLocation(),0,0,0,1,10)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
		}
		super.activatePower(p);
		return true;
		
	}
}