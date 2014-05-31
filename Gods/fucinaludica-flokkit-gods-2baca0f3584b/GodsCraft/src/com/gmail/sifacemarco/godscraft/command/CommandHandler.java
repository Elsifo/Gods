package me.elsifo92.gods.command;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.elsifo92.gods.*;
import me.elsifo92.gods.data.blocks.BlockManager;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.GodManager;
import me.elsifo92.gods.gods.altar.AltarManager;
import me.elsifo92.gods.gods.altar.AltarType;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.data.scoreboard.HideTask;
import me.elsifo92.gods.data.scoreboard.ScoreType;
import me.elsifo92.gods.data.scoreboard.ScoreboardManager;

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
			    	    case "create":
			    	    {
			    		    if(args.length>1)
			    		    {
			    		    	if(args.length<2) return false;
			    		    	if(!args[1].equals("throne")) 
			    		    	{
			    		    		p.sendMessage(Utility.formattedMessage("Solo un prete pu£ eseguire questo comando (WIP)"));
			    		    		return true;
			    		    	}			    		    	
			    		        Block b=p.getTargetBlock(null, 10);
			    		        God g=GodManager.getGod(args[2]);
			    		        if(g==null) 
			    		        {
			    		        	p.sendMessage(Utility.formattedMessage("Il nome del dio specificato non £ valido"));
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
			    		    if(args.length>1)
			    		    {
			    		    	if(args[1].equals("Sulex"))
			    		    	{
			    		    		//Sulex s=new Sulex(player);
			    		    		//s.run();
			    		    		p.sendMessage(Utility.formattedMessage("Tranquillo, un giorno Sulex arriver£ da te :)"));
			    		    		p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 15, 5);
			    		    	}
			    		    	return true;
			    		    }
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
			    		        	AltarManager.removeAltar(p,p.getTargetBlock(null, 10));
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
			    	    case "verify":
			    	    {
			    		    if(!ConfigManager.getPexConfig().hasPerm(p, "gods.verify.altar")) 
			    		    {
			    		    	p.sendMessage(Utility.formattedMessage("Permessi insufficienti"));
			    		    	return true;
			    		    }
			    		    Block b=p.getTargetBlock(null, 10);
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
			    		    	if(BlockManager.verifyBlock(p.getTargetBlock(null, 10))) p.sendMessage(Utility.formattedMessage("Questo blocco non £ naturale"));
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
			    	    }
			    	    case "set":
			    	    {
			    	    	if(args.length>2)
			    	    	switch(args[1])
			    	    	{
			    	    		case "priest":
			    	    		{
			    	    			if(Bukkit.getPlayer(args[2])==null) 
			    	    			{
			    	    				p.sendMessage(Utility.formattedMessage("Nome del player non valido"));
			    	    			}
			    	    			GodManager.setPriest(p,Bukkit.getPlayer(args[2]));
			    	    		}
			    	    	}
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
