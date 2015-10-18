package it.beyondthecube.gods.data.config;
 
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

public class VaultLinker 
{
	public Permission permission;
	public VaultLinker() 
    {
		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) 
        {
            permission = permissionProvider.getProvider();
        }
    }
    public boolean hasPerm(Player p,String perm)
    {
    	if(permission.has(p,"gods.admin")) return true;
        return permission.has(p,perm);
    }  
}