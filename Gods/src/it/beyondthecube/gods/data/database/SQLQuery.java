package it.beyondthecube.gods.data.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import it.beyondthecube.gods.data.config.MySQLConfig;

class SQLQuery 
{
	private MySQLConfig msqlc;
	private String query;
	public SQLQuery(String query, MySQLConfig msqlc)
	{
		this.msqlc=msqlc;
		this.query=query.replace("£", msqlc.getDBName());
		
	}
	public ResultSet excecuteQuery() throws MySQLSyntaxErrorException
	{
		Bukkit.getConsoleSender().sendMessage(query);
		try
		{
			PreparedStatement ps=(PreparedStatement) msqlc.getConnection().prepareStatement(query);
			ResultSet r=ps.executeQuery();
			return r;
		}
		catch(MySQLSyntaxErrorException e)
		{
			throw e;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	public void excecuteUpdate()
	{
		Bukkit.getConsoleSender().sendMessage(query);
		try 
		{
			PreparedStatement ps=(PreparedStatement) msqlc.getConnection().prepareStatement(query);
			ps.executeUpdate();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}