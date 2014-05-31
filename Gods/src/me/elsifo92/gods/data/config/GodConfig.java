package me.elsifo92.gods.data.config;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class GodConfig 
{
	private String nome;
	private int cap;
	private ArrayList<ItemStack> items;
	private OfflinePlayer champ;
	public GodConfig(String nome, int cap, ArrayList<ItemStack> arrayList, OfflinePlayer p)
	{
		this.nome=nome;
		this.cap=cap;
		this.items=arrayList;
		this.champ=p;
	}
	public String getName()
	{
		return nome;
	}
	public int getCap()
	{
		return cap;
	}
	public ArrayList<ItemStack> getItemsList() 
	{		
		return items;
	}
	public OfflinePlayer getChamp()
	{
		return champ;
	}
	public boolean hasItemList() 
	{
		return items!=null;
	}
}