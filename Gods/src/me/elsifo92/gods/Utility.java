package me.elsifo92.gods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import me.elsifo92.gods.gods.God.GodType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class Utility
{
	private static YamlConfiguration lang=new YamlConfiguration();
    private static final long millisInDay=86400000;
	public static String formattedMessage(String msg)
    {
    	return new String(ChatColor.GOLD+"[GODS]"+ChatColor.AQUA+msg);
    }
	public static ItemStack getItem(String id)
	{	
		ArrayList<String>  properties=new ArrayList<>(getStringArray(id,"-"));
		ItemStack i=new ItemStack(Material.getMaterial(properties.get(0)));
		if(i!=null)
		{
			ArrayList<String> enchs=getStringArray(properties.get(1),".");
			for(String e:enchs)
			{
				ArrayList<String> ench=getStringArray(e,"'");
				if(i.getType().equals(Material.ENCHANTED_BOOK))
				{	
					EnchantmentStorageMeta im = (EnchantmentStorageMeta) i.getItemMeta();
					im.addStoredEnchant(Enchantment.getByName(ench.get(0)), Integer.parseInt(ench.get(1)), true);
					i.setItemMeta(im);
				}
				else
				{
					i.addUnsafeEnchantment(Enchantment.getByName(ench.get(0)), Integer.parseInt(ench.get(1)));
				}
			}				
			i.setDurability(Short.parseShort(properties.get(2)));
		}
		return i;
	}
	public static void setLang(Lang l)
	{
		String filename=null;
		switch(l)
		{
			case F:filename="french.yml";break;
			case G:filename="german.yml";break;
			case I:filename="italian.yml";break;
			default:filename="english.yml";break;
		}
		lang=YamlConfiguration.loadConfiguration(Bukkit.getPluginManager().getPlugin("GodsCraft").getResource(filename));
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
		Bukkit.getConsoleSender().sendMessage(path);
		return lang.getString(path);
	}
	public static ArrayList<String> getStringArray(String s, String string)
	{
		ArrayList<String> str=new ArrayList<>();
		String tmp=s;
		while(tmp!=null)
		{
			int o=tmp.indexOf(string);
			if(o==-1)
			{
				str.add(tmp);
				break;
			}	
			str.add(tmp.substring(0, o));
			tmp=tmp.substring(o+1);
		}
		return str;
	}
}