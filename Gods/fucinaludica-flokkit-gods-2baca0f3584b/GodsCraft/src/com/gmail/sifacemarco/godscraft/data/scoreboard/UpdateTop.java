package me.elsifo92.gods.data.scoreboard;

import me.elsifo92.gods.gods.God.GodType;

public class UpdateTop implements Runnable
{
	private String s;
	private Integer i;
	private GodType gt;
	public UpdateTop(String s, Integer i, GodType gt)
	{
		this.s=s;
		this.i=i;
		this.gt=gt;
	}
	@Override
	public void run()
	{
		ScoreboardManager.updateTop(s, i, gt);
	}	
}
