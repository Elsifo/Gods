package it.beyondthecube.gods.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import it.beyondthecube.gods.GodsCraft;
import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.data.config.ConfigManager;
import it.beyondthecube.gods.data.database.DatabaseManager;

public class FailsafeHandler implements CommandExecutor 
{
	private GodsCraft plugin;
	public FailsafeHandler(Plugin plugin)
	{
		this.plugin=(GodsCraft)plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p=(Player) sender;
			if(cmd.getName().equals("gods"))
		    {
				if(!ConfigManager.getPexConfig().hasPerm(p, "gods.lock")) 
				{
					Utility.sendPermissionMessage(p);
					return true;
				}
				if(args.length>0)
			    {
			    	switch(args[0])
			    	{
			    	    case "createdb":
			    	    {
			    	    	if(args.length==1)
			    	    	{
			    	    		p.sendMessage(Utility.formattedMessage(Utility.getMessage("misc.dbcreatefail")));
			    	    	}
			    	    	DatabaseManager.createDatabase(args[1]);
			    	    	return true;
			    	    }
			    	    case "reload":
			    	    {
			    	    	plugin.reload(p);
			    	    	return true;
			    	    }
			    	    default:return false;
			    	}
			    }
				return false;
		    }
			return false;
		}
		return false;
	}
}