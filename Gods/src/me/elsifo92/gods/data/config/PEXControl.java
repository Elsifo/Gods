package me.elsifo92.gods.data.config;
 
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PEXControl 
{
	public PEXControl() 
    {
 
    }
    public boolean hasPerm(Player p,String perm)
    {
    	if(PermissionsEx.getPermissionManager().has(p,"gods.admin")) return true;
        return PermissionsEx.getPermissionManager().has(p,perm);
    }  
}