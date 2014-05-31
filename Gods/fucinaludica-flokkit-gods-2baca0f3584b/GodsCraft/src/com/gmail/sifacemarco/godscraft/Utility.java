package me.elsifo92.gods;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import me.elsifo92.gods.gods.God.GodType;

public class Utility
{
	private static YamlConfiguration lang=new YamlConfiguration();
	private static FileConfiguration customConfig = null;
	private static File customConfigFile = null;
    private static final long millisInDay=86400000;
	public static String formattedMessage(String msg)
    {
    	return new String(ChatColor.GOLD+"[GODS]"+ChatColor.AQUA+msg);
    }
	public static void reloadCustomConfig(String filename) 
	{
	    if (customConfigFile == null) {
	    customConfigFile = new File(Bukkit.getPluginManager().getPlugin("GodsCraft").getDataFolder(), filename);
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = Bukkit.getPluginManager().getPlugin("GodsCraft").getResource(filename);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	}
	public static ItemStack getItem(String id)
	{		
		ArrayList<String>  properties=(ArrayList<String>) Arrays.asList(id.split("-"));
		ItemStack i=new ItemStack(Material.getMaterial(properties.get(0)));
		if(i!=null)
		{
			ArrayList<String> enchs=(ArrayList<String>) Arrays.asList(properties.get(1).split("+"));
			for(String e:enchs)
			{
				ArrayList<String> ench=(ArrayList<String>) Arrays.asList(e.split("."));
				i.addEnchantment(Enchantment.getByName(ench.get(0)), Integer.parseInt(ench.get(1)));
			}
			i.setDurability(Short.parseShort(properties.get(2)));
		}
		return i;
	}
	public static void setLang(Lang l)
	{
		File f;		
		switch(l)
		{
			case F:;lang=YamlConfiguration.loadConfiguration(Bukkit.getPluginManager().getPlugin("GodsCraft").getResource("french.yml"));
			case G:lang=YamlConfiguration.loadConfiguration(Bukkit.getPluginManager().getPlugin("GodsCraft").getResource("german.yml"));
			case I:lang=YamlConfiguration.loadConfiguration(Bukkit.getPluginManager().getPlugin("GodsCraft").getResource("italian.yml"));
			default:lang=YamlConfiguration.loadConfiguration(Bukkit.getPluginManager().getPlugin("GodsCraft").getResource("english.yml"));break;
		}
	}
    public static void sendPermissionMessage(Player p)
    {
    	p.sendMessage(ChatColor.DARK_RED+"[GODS]"+ChatColor.AQUA+"Permessi insufficienti");
    }
    public static GodType getGodTypeFromDBNumber(int god)
    {
    	switch(god)
    	{
    		case 1:return GodType.MINER;
    		case 2:return GodType.NATURE;
    		case 3:return GodType.WAR;
    		case 4:return GodType.DEATH;
    		default:return null;
    	}
    }
    public static int getDBNumberFromGodType(GodType god)
    {
    	switch(god)
    	{
    		case MINER:return 1;
    		case NATURE:return 2;
    		case WAR:return 3;
    		case DEATH:return 4;
    		default:return -1;
    	}
    }
	public static int daysFromToday(GregorianCalendar d1)
    {
		return (int) ((Calendar.getInstance().getTimeInMillis()-d1.getTimeInMillis())/millisInDay);
    }
	public static String getMessage(String path)
	{
		return customConfig.getString(path);
	}
}
