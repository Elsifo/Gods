package it.beyondthecube.gods;

public class Reputation 
{
	private static final int base=3000;
	public static int getPointsTo(int level)
	{
		return (int) (base+4.0*Math.pow(level,1.5));
	}
}