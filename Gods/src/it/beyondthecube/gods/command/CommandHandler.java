package it.beyondthecube.gods.command;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import it.beyondthecube.gods.*;
import it.beyondthecube.gods.data.blocks.BlockManager;
import it.beyondthecube.gods.data.config.ConfigManager;
import it.beyondthecube.gods.data.scoreboard.ScoreType;
import it.beyondthecube.gods.data.scoreboard.ScoreboardManager;
import it.beyondthecube.gods.gods.God;
import it.beyondthecube.gods.gods.GodManager;
import it.beyondthecube.gods.gods.altar.AltarManager;
import it.beyondthecube.gods.gods.altar.AltarType;
import it.beyondthecube.gods.gods.follower.FollowerManager;
import it.beyondthecube.gods.misc.particles.ParticleEffects;
import it.beyondthecube.gods.tasks.HideTask;
import it.beyondthecube.gods.tasks.ParticleTask;

public class CommandHandler implements CommandExecutor 
{
	private GodsCraft plugin;
	public CommandHandler(Plugin plugin)
	{
		this.plugin=(GodsCraft)plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p=(Player) sender;
			if(cmd.getName().equals("gods"))
		    {
				if(args.length>0)
			    {
			    	switch(args[0])
			    	{
			    		case "list":
			    		{
			    			String ris="";
			    			for(God g:GodManager.getGodsList())
			    			{
			    				ris+=g.getGodName()+":"+g.getGodType().toString()+"; ";
			    			}
			    			p.sendMessage(Utility.formattedMessage("List of active gods: "+ris));
			    			return true;
			    		}
			    	    case "create":
			    	    {
			    		    if(args.length>1)
			    		    {
			    		    	if(args.length<2) return false;
			    		        Block b=p.getTargetBlock((Set<Material>) null, 10);
			    		        God g=GodManager.getGod(args[2]);
			    		        if(g==null) 
			    		        {
			    		        	p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.altars.noone")));
			    		        	return true;
			    		        }
			    		        if(b.getType().toString().equals("WALL_SIGN"))
			    		  	    {			    		  	       
			    		        	switch(args[1])
						    	    {
						    		    case "throne":
						    		    {
						    		    	if(ConfigManager.getPexConfig().hasPerm(p, "gods.throne.create"))
						    		    	{
						    		    		AltarManager.newAltar(p,GodManager.getGod(args[2]),AltarType.THRONE,0);
						    		    	}
						    		    	else Utility.sendPermissionMessage(p);
						    		    }break;
						    		    case "kneeler":AltarManager.newAltar(p,GodManager.getGod(args[2]),AltarType.PRAY,-1);break;
						    		    case "healer":AltarManager.newAltar(p,GodManager.getGod(args[2]),AltarType.HEAL,-1);break;
						    		}			 
			    		        	return true;
			    		  	   	}
			    		        else
			    		        {
			    		        	p.sendMessage(Utility.formattedMessage("Il comando pu£ essere applicato solo ad un cartello posto su di una parete"));
			    		        	return true;
			    		        }
			    		    }
			    		    else return false;
			    		}
			    	    case "evoke":
			    	    {
							GodManager.godEvoked(p);
			    	    	return true;
			    		}
			    	    case "delete":
			    	    {
			    		    if(args.length>1)
			    		    {
			    		        if(args[1].equals("altar"))
			    		        {
			    		        	if(!ConfigManager.getPexConfig().hasPerm(p, "gods.delete.altar"))
			    		        	{
			    		        		Utility.sendPermissionMessage(p);
			    		        		return true;
			    		        	}
			    		        	AltarManager.removeAltar(p,p.getTargetBlock((Set<Material>) null, 10));
			    		        	return true;
			    		        }
			    		        else return false;
			    		    }			    		  	   
			    		}
			    	    case "unfollow":
			    	    {
			    	    	if(!(FollowerManager.isFollower(p)))
			    	    	{
			    	    		p.sendMessage(Utility.formattedMessage("Non segui alcun dio"));
			    	    		return true;
			    	    	}
			    	    	FollowerManager.removeFollower(p,false);
							p.sendMessage(Utility.formattedMessage("Ora non segui pi£ alcun dio"));
			    	    }
			    	    return true;
			    	    case "reputation":
			    	    {
			    	    	if(!FollowerManager.isFollower(p))
			    	    	{
			    	    		p.sendMessage(Utility.formattedMessage("Non segui alcun dio"));
			    	    		return true;
			    	    	}
			    	    	if(args.length>1)
			    	    	{
			    	    		switch(args[1])
			    	    		{
			    	    			case "show":
			    	    			{
			    	    				ScoreboardManager.showScoreboard(p);
			    	    				if(args.length>2)
			    	    				{
			    	    					if(args[2].equals("fixed")) FollowerManager.setFixedReputation(p);
			    	    				}
			    	    				(new HideTask(p,ScoreType.REPUTATION)).runTaskLater(plugin, 60);
			    	    				break;
			    	    			}
			    	    			case "hide":
			    	    			{
			    	    				ScoreboardManager.hideScoreboard(p);
			    	    				FollowerManager.removeFixedReputation(p);
			    	    				break;
			    	    			}
			    	    			case "add":
			    	    			{
			    	    				if(ConfigManager.getPexConfig().hasPerm(p, "gods.reputation.modify"))
			    	    				{
			    	    					if(args.length==4)
			    	    					{
			    	    						FollowerManager.updateReputationLevel(Bukkit.getPlayer(args[2]), Integer.parseInt(args[3]));
			    	    					}
				    	    			}
			    	    			}
			    	    			break;
			    	    			case "remove":
			    	    			{
			    	    				if(ConfigManager.getPexConfig().hasPerm(p, "gods.reputation.modify"))
			    	    				{
			    	    					if(args.length==4)
			    	    					{
			    	    						FollowerManager.updateReputationLevel(Bukkit.getPlayer(args[2]), -1*Integer.parseInt(args[3]));
			    	    					}
				    	    			}
			    	    			}
			    	    			break;
			    	    			default:return false;
			    	    		}
			    	    		return true;
			    	    	}
			    	    	return false;
			    	    }
			            
			    	    case "champOf":
			    	    {
			    	    	p.sendMessage(Utility.formattedMessage("Te sei campione di: "+GodManager.getGodNameWhomImChamp(p)));
			    	    	return true;
			    	    }
			    	    case "lang":
			    	    {
			    	    	if(ConfigManager.getPexConfig().hasPerm(p, "gods.admin") && args.length>1)
			    	    	{
			    	    		Utility.setLang(Lang.valueOf(args[1]));
			    	    	}
			    	    	break;
			    	    }
			    	    case "verify":
			    	    {
			    		    if(!ConfigManager.getPexConfig().hasPerm(p, "gods.verify.altar")) 
			    		    {
			    		    	p.sendMessage(Utility.formattedMessage("Permessi insufficienti"));
			    		    	return true;
			    		    }
			    		    Block b=p.getTargetBlock((Set<Material>) null, 10);
			    		    if(b.getType().toString().equals("WALL_SIGN"))
			    		    {
			    		    	
			    		    	Sign s=(Sign) b.getState();
			    		        if(AltarManager.getAltar(s)!=null) p.sendMessage(Utility.formattedMessage("Questo altare £ registrato"));
			    		        else p.sendMessage(Utility.formattedMessage("Altare non registrato"));
			    		    }
			    		    else 
			    		    {
			    		    	if(!ConfigManager.getPexConfig().hasPerm(p, "gods.verify.block"))
			    		    	{
			    		    		p.sendMessage(Utility.formattedMessage("Permessi insufficienti"));
			    		    		return true;
			    		    	}
			    		    	if(BlockManager.verifyBlock(p.getTargetBlock((Set<Material>) null, 10))) p.sendMessage(Utility.formattedMessage("Questo blocco non £ naturale"));
			    		    	else p.sendMessage(Utility.formattedMessage("Questo blocco £ naturale"));  	
			    		    }
			    		    return true;
			    	    }
			    	    case "top":
			    	    {
			    	    	God g=null;
			    	    	switch(args.length)
			    	    	{
			    	    		case 1: g=FollowerManager.getGodFollowed(p);break;
			    	    		case 2: g=GodManager.getGod(args[1]);break;
			    	    		default: return false;
			    	    	}
			    	    	if(g==null)
			    	    	{
			    	    		p.sendMessage(Utility.formattedMessage("Nome del dio non valido"));
			    	    		return true;
			    	    	}
			    	    	ScoreboardManager.showTopScoreboard(p, g.getGodType());
			    	    	break;
			    	    }
			    	    case "lock":
			    	    {
			    	    	if(ConfigManager.getPexConfig().hasPerm(p, "gods.lock")) plugin.lock();
			    	    	else Utility.sendPermissionMessage(p);
			    	    	return true;
			    	    }
			    	    case "test":
			    	    {
			    	    	for(int i=0; i<10; i++)
			    	    	{
			    	    		Location l=p.getLocation();
			    	    		l.setZ(l.getZ()+i);
			    	    		(new ParticleTask(ParticleEffects.FLAME,p,p.getLocation(),0,0,0,0,100)).runTask(Bukkit.getPluginManager().getPlugin("GodsCraft"));
			    	    
			    	    	}
			    	    	return true;
			    	    }
			    	    default:return false;
			    	}
			    }
			}
		    return true;
		}
		return false;
	}
}