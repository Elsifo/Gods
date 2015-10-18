package it.beyondthecube.gods.data.scoreboard;

import it.beyondthecube.gods.gods.God.GodType;

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