package me.elsifo92.gods.data.config;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;

public class MySQLConfig 
{
	MySQLConfig msqlc;
	private String host;
	private String user;
	private String pass;
	private String dbname;
	private java.sql.Connection conn;
	private char alias;
	public MySQLConfig(String host, String user, String pass, String dbname, char alias)
	{
		this.host=host;
		this.user=user;
		this.pass=pass;
		this.dbname=dbname;
		this.alias=alias;
		conn=null;
	}
	public Connection getConnection() throws SQLException
	{
		if(conn==null)
		{
			conn=DriverManager.getConnection(host, user, pass);
		}
		return conn;
	}
	public String getDBName()
	{
		return dbname;
	}
	public char getChar() 
	{
		return alias;
	}
}
