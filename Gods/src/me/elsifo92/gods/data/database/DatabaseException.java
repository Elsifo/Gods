package me.elsifo92.gods.data.database;

public class DatabaseException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum DatabaseExceptionType 
	{
		CONNECTION,
		NOT_FOUND,
		NOT_COMPLETE;
	}
	private DatabaseExceptionType type;
	public DatabaseException(String msg, DatabaseExceptionType type)
	{
		super(msg);
		this.type=type;
	}
	public DatabaseExceptionType getType() 
	{
		return type;
	}
}