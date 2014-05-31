package me.elsifo92.gods.plugins;

public abstract class PluginCheck 
{
	private boolean enabled;
	public PluginCheck(boolean enabled)
	{
		this.enabled=enabled;
	}
	public boolean isEnabled()
	{
		return enabled;
	}
}
