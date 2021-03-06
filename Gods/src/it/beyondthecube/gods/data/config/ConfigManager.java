package it.beyondthecube.gods.data.config;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import it.beyondthecube.gods.Lang;
import it.beyondthecube.gods.Utility;
import it.beyondthecube.gods.data.database.DatabaseManager;
import it.beyondthecube.gods.gods.God.GodType;

public class ConfigManager 
{
    private static FileConfiguration config=Bukkit.getPluginManager().getPlugin("GodsCraft").getConfig();
    private static MySQLConfig msqlc=new MySQLConfig(config.getString("mysql.host"), config.getString("mysql.user"), config.getString("mysql.pass"),config.getString("mysql.dbname"),'-');
    private static VaultLinker pex=new VaultLinker();
    private static boolean sound=config.getBoolean("misc.level-sound");
    private static Lang lang=Lang.valueOf(config.getString("misc.lang"));
	public boolean isConnectedToDB()
	{
		try 
		{
			return msqlc.getConnection()!=null;
		} 
		catch (SQLException e)
		{
			return false;
		}
	}
	public static GodConfig getGodConfig(GodType type)
	{
		if(type==null) return null;
		String path="gods.";
		switch(type)
		{
			case MINER:path+="miner.";break;
			case NATURE:path+="nature.";break;
			case WAR:path+="war.";break;
			case DEATH:path+="death.";break;
		}		
		if(config.getBoolean(path+"activated"))
		{
			OfflinePlayer p=DatabaseManager.getChamp(type);
			return new GodConfig(config.getString(path+"name"), config.getInt(path+"cap"), getItemStackList(type),p);			
		} 
		else return null;		
	}
	private static ArrayList<ItemStack> getItemStackList(GodType type) 
	{
		ArrayList<ItemStack> itemList=new ArrayList<ItemStack>();
		String path="gods.";
		switch(type)
		{
			case MINER:path+="miner.";break;
			case NATURE:path+="nature.";break;
			case WAR:path+="war.";break;
			case DEATH:path+="death.";break;
		}
		String items=config.getString(path+"item");
		if(items==null) return null;
		List<String> itms=Arrays.asList(items.split("\\s*,\\s*"));
		for(String s:itms)
		{
			itemList.add(Utility.getItem(s));
		}
		return itemList;
	}
	public static VaultLinker getPexConfig()
	{
		return pex;
	}
	public static MySQLConfig getMySQLConfig()
	{
		return msqlc;
	}
	public static void reloadConfig()
	{
		config=YamlConfiguration.loadConfiguration(new File(Bukkit.getPluginManager().getPlugin("GodsCraft").getDataFolder()+File.separator+"config.yml"));
		msqlc=new MySQLConfig(config.getString("mysql.host"), config.getString("mysql.user"), config.getString("mysql.pass"),config.getString("mysql.dbname"),'�');
	}
	public static boolean isSoundEnabled()
	{
		return sound;
	}
	public static Lang getLang()
	{
		return lang;
	}
	public static void checkVersion()
	{
			
	}
}