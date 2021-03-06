/**
 * 
 */
/**
 * @author Marco
 *
 */

package me.elsifo92.gods;

import java.io.File;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.elsifo92.gods.command.CommandHandler;
import me.elsifo92.gods.command.FailsafeHandler;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.data.config.GodConfig;
import me.elsifo92.gods.data.database.DatabaseManager;
import me.elsifo92.gods.data.database.FollowerData;
import me.elsifo92.gods.gods.*;
import me.elsifo92.gods.gods.God.GodType;
import me.elsifo92.gods.gods.follower.FollowerManager;
import me.elsifo92.gods.listener.BlockListener;
import me.elsifo92.gods.listener.ChunkListener;
import me.elsifo92.gods.listener.LevelUpListener;
import me.elsifo92.gods.listener.LockListener;
import me.elsifo92.gods.listener.PlayerListener;
import me.elsifo92.gods.listener.SignListener;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public final class GodsCraft extends JavaPlugin
{
	private PlayerListener ps;
	private SignListener sl;
	private LevelUpListener lu;
	private Scoreboard b;
	private Objective objective;
	private BlockListener bl;
	private ChunkListener cl;
	private LockListener ll;
	private boolean lock;
	public boolean isLocked()
	{
		return lock;
	}
	public void onEnable()
	{				 
		load();
	}	
	public void onDisable()
	{
		if(this.lock) return;
		save();
	}
	public Objective getObjective()
	{
		return objective;
	}
	public Scoreboard getScoreboard() 
	{
		return this.b;
	}
	public void reload(Player p) 
	{
		if(lock)
		{
			Bukkit.getServer().broadcastMessage(Utility.formattedMessage(Utility.getMessage("msg.misc.reload")));
			ll.unregister();
			this.save();
			ConfigManager.reloadConfig();
			this.load();			
			Bukkit.getConsoleSender().sendMessage("Ricaricamento completato");
		}
		else p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.errore.failsafen")));
	}
	private void load() 
	{
		Bukkit.getConsoleSender().sendMessage("====================    GODSCRAFT    ========================");
		lock=false;
		File f=new File(this.getDataFolder()+File.separator+"config.yml");
		if(!(f.exists()))
		{
			saveDefaultConfig();
		}		
		Lang lang=ConfigManager.getLang();
		Utility.setLang(lang);
		switch(lang)
		{
			case F:Bukkit.getConsoleSender().sendMessage("Language selected: FRENCH");
			case G:Bukkit.getConsoleSender().sendMessage("Language selected: GERMAN");
			case I:Bukkit.getConsoleSender().sendMessage("Language selected: ITALIAN");
			default:Bukkit.getConsoleSender().sendMessage("Language selected: ENGLISH");
		}
		try 
		{
			DatabaseManager.connectToDB();
			DatabaseManager.testDB();
		} 
		catch (MySQLSyntaxErrorException e) 
		{
			Bukkit.getConsoleSender().sendMessage("Database non trovato");
			lock();
			return;
		} 
		catch (SQLException e) 
		{
			Bukkit.getConsoleSender().sendMessage("Connessione al db non riuscita");
			lock();
			return;
		}	
		ConfigManager.checkVersion();
		b=Bukkit.getScoreboardManager().getNewScoreboard();
		objective=b.registerNewObjective("Reputation", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Reputation");
		this.getCommand("gods").setExecutor(new CommandHandler(this));
		GodManager.loadGods(this);
		sl=new SignListener();
		ps=new PlayerListener();
		bl=new BlockListener();
		cl=new ChunkListener();
		lu=new LevelUpListener();
		getServer().getPluginManager().registerEvents(cl, this);
		getServer().getPluginManager().registerEvents(lu, this);
		getServer().getPluginManager().registerEvents(sl, this);
		getServer().getPluginManager().registerEvents(ps, this);
		getServer().getPluginManager().registerEvents(bl, this);	
		DatabaseManager.loadAltars();
		DatabaseManager.loadThrones();
		DatabaseManager.loadChamps();
		for(int i=1; i<GodManager.getActiveGods()+1; i++)
		{
			GodType t=Utility.getGodTypeFromDBNumber(i);
			GodConfig gc=ConfigManager.getGodConfig(t);
			if(gc!=null)
			{
				if(gc.hasItemList())
				{
					Bukkit.getConsoleSender().sendMessage("Caricamento della lista dei doni per "+GodManager.getGod(t).getGodName());
					for(ItemStack is:gc.getItemsList())
					{
						GodManager.getGod(t).addItem(is);
					}
				}
			}
		}	
		for(Player p:Bukkit.getOnlinePlayers())
		{
			FollowerData fd=DatabaseManager.getFollower(p);
			if(fd==null) return;
			FollowerManager.addFollower(p, fd.getReputation(), fd.getPoints(), fd.getGod(), fd.getLastPrayed(), fd.getLastHealed(), true);
		}
		for(World w:Bukkit.getServer().getWorlds())
		{
			for(Chunk c:w.getLoadedChunks())
			{
				DatabaseManager.loadBlocks(c);
			}
		}
		Bukkit.getConsoleSender().sendMessage("=============================================================");
	}
	public void save()
	{
		Bukkit.getConsoleSender().sendMessage("====================    GODSCRAFT    ========================");
		for(Player p:Bukkit.getOnlinePlayers())
		{
			Bukkit.getConsoleSender().sendMessage("Salvataggio player");
			FollowerManager.saveFollower(p);
		}
		for(World w:Bukkit.getWorlds())
		{
			Bukkit.getConsoleSender().sendMessage("Salvataggio chunk per world:"+w.getName());
			for(Chunk c:w.getLoadedChunks())
			{
				DatabaseManager.saveBlocks(c);
			}
		}
		DatabaseManager.saveThrones();
		DatabaseManager.saveAltars();
		Bukkit.getConsoleSender().sendMessage("=============================================================");
	}
	public void lock() 
	{
		lock=true;
		Bukkit.getConsoleSender().sendMessage(""+ChatColor.BOLD+ChatColor.UNDERLINE+ChatColor.RED+"***SERVER LOCKED***");
		Bukkit.getServer().broadcastMessage(Utility.formattedMessage(""+ChatColor.BOLD+ChatColor.UNDERLINE+ChatColor.RED+"***SERVER LOCKED***"));
		ll=new LockListener();
		getServer().getPluginManager().registerEvents(ll, this);
		this.getCommand("gods").setExecutor(new FailsafeHandler(this));
		return;
	}
}
