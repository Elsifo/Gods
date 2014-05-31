package me.elsifo92.gods.data.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.elsifo92.gods.Utility;
import me.elsifo92.gods.data.blocks.BlockManager;
import me.elsifo92.gods.data.blocks.PlacedBlock;
import me.elsifo92.gods.data.config.ConfigManager;
import me.elsifo92.gods.data.config.MySQLConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import me.elsifo92.gods.gods.God;
import me.elsifo92.gods.gods.God.GodType;
import me.elsifo92.gods.gods.altar.Altar;
import me.elsifo92.gods.gods.altar.AltarManager;
import me.elsifo92.gods.gods.altar.AltarType;
import me.elsifo92.gods.gods.GodManager;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class DatabaseManager 
{
	private static MySQLConfig msqlc=ConfigManager.getMySQLConfig();
	public static OfflinePlayer getChamp(GodType type) 
	{
		OfflinePlayer p=null;
		ResultSet r=null;
		try 
		{
			r = (new SQLQuery((new String("select champ from £.gods where idGod="+String.valueOf(Utility.getDBNumberFromGodType(type)))),ConfigManager.getMySQLConfig()).excecuteQuery());
		} 
		catch (MySQLSyntaxErrorException e1) 
		{
			e1.printStackTrace();
		}		
		try 
		{
			while(r.next())
			{
				String s=r.getString(1);
				if(s!=null)
				{
					p=Bukkit.getPlayer(r.getString(1));
				}
			}
		}
		catch(SQLException e)
		{
			return null;
		}
		return p;
	}
	public static void testDB() throws MySQLSyntaxErrorException, SQLException
	{
		ResultSet r=new SQLQuery("select * from £.thrones;",msqlc).excecuteQuery();
	}
	public static FollowerData getFollower(Player p) 
	{
		try 
		{
			String sq="select reputation,god,lastpray,lastheal,points from £.followers where follower like '"+p.getName()+"';";
			ResultSet r=(new SQLQuery(new String(sq),msqlc)).excecuteQuery();
			boolean empty=true;
			while(r.next())
			{
				empty=false;
				break;
			}
			if(!empty)
			{
				r.first();
				GregorianCalendar c=new GregorianCalendar();
				Date c2=r.getDate(3),d2=r.getDate(4);
				if(c2==null) c=null;
				else c.setTimeInMillis(c2.getTime());	
				GregorianCalendar d=new GregorianCalendar();
				if(d2==null) d=null;
				else d.setTimeInMillis(d2.getTime());
				return new FollowerData(r.getInt(1),r.getInt(5),GodManager.getGod(Utility.getGodTypeFromDBNumber(r.getInt(2))),c,d, true);
			}
		}
		catch(SQLException ex)
		{
			p.sendMessage(Utility.formattedMessage(Utility.getMessage("msg.followers.loadfail")));
		}
		return null;
	}

	public static void setChamp(God g, OfflinePlayer p) 
	{
		(new SQLQuery(new String("update £.gods set champ="+((p==null)?"NULL":p.getName())+" where idGod="+(new Integer(Utility.getDBNumberFromGodType(g.getGodType())))),msqlc)).excecuteUpdate();
	}

	public static void saveFollower(Player p, int rep, int points, God g, GregorianCalendar lastPrayed, GregorianCalendar lastHealed)
	{
		String sr=new String("select id from £.followers where follower like '"+p.getName()+"';");
		ResultSet r=null;
		try
		{
			r = (new SQLQuery(sr,msqlc)).excecuteQuery();
		} 
		catch (MySQLSyntaxErrorException e) 
		{
			e.printStackTrace();
		}
		boolean empty=true;
		try 
		{
			String s;
			while(r.next())
			{
				empty=false;
			}
			if(empty) 
			{
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				s=new String("insert into £.followers(follower,reputation,points,god,lastpray,lastheal) values('"+p.getName())+"',"+rep+","+points+","+Utility.getDBNumberFromGodType(g.getGodType())+",'"+df.format(lastPrayed.getTime())+"','"+df.format(lastHealed.getTime()).toString()+"');";	
			}
			else if(rep==-1)
			{
				s=new String("delete from £.followers where id="+r.getInt(1)+";");
			}
			else
			{
				r.first();
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				s=new String("update £.followers set lastpray='"+df.format(lastPrayed.getTime())+"', points="+points+", reputation="+rep+", god="+Utility.getDBNumberFromGodType(g.getGodType())+", lastheal='"+df.format(lastHealed.getTime())+"' where id="+r.getInt(1)+";");
			}
			new SQLQuery(s,msqlc).excecuteUpdate();
		} 
		catch (SQLException e2) 
		{
			e2.printStackTrace();
		}
		
	}

	public static void loadAltars()
	{
		ResultSet r=null;
		try 
		{
			r = (new SQLQuery(new String("select a.god,a.type,l.x,l.y,l.z,l.world,a.id from £.altar a, £.locations l where a.location=l.id;"),msqlc)).excecuteQuery();
		} 
		catch (MySQLSyntaxErrorException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			if(r!=null)
			{
				while(r.next())
				{
					AltarManager.loadAltar(GodManager.getGod(Utility.getGodTypeFromDBNumber(r.getInt(1))),AltarType.valueOf(r.getString(2)),new Location(Bukkit.getServer().getWorld(r.getString(6)),r.getInt(3),r.getInt(4),r.getInt(5)), r.getInt(7));
				}
			}
		}
		catch(SQLException e)
		{
			return;
		}
	}

	public static void loadBlocks(Chunk chunk) 
	{
		ResultSet r=null;
		try 
		{
			r = (new SQLQuery("select l.world,l.x,l.y,l.z,b.dayPlaced,b.id from £.locations l, £.nonnatural b where l.id=b.location and l.chunkx="+chunk.getX()+" and l.chunkz="+chunk.getZ()+";",msqlc)).excecuteQuery();
		} 
		catch (MySQLSyntaxErrorException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			while(r.next())
			{
				GregorianCalendar g=new GregorianCalendar();
				g.setTime(r.getDate(5));
				BlockManager.loadBlock((new Location(Bukkit.getWorld(r.getString(1)),r.getInt(2),r.getInt(3),r.getInt(4))).getBlock(),g, r.getInt(6));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public static void loadThrones() 
	{
		for(int i=0; i<GodManager.getActiveGods(); i++)
		{
			String str="select l.x,l.y,l.z,g.hasThrone,l.world from £.thrones g,£.locations l where g.location=l.id and god="+(i+1)+";";
			ResultSet r=null;
			try
			{
				r = (new SQLQuery(str,msqlc)).excecuteQuery();
			} 
			catch (MySQLSyntaxErrorException e1) 
			{
				e1.printStackTrace();
			}
			GodType t=Utility.getGodTypeFromDBNumber(i+1);
			try {
				while(r.next())
				{
					if(r.getBoolean(4))
					{	
						Location l=new Location(Bukkit.getServer().getWorld(r.getString(5)), r.getInt(1), r.getInt(2), r.getInt(3));
						GodManager.getGod(t).setThrone((Sign) l.getBlock().getState());
						AltarManager.loadAltar(GodManager.getGod(t), AltarType.THRONE, l, 1);
						Sign s=(Sign) l.getBlock().getState();
						if(!s.getLine(1).equalsIgnoreCase(GodManager.getGod(t).getGodName())) s.setLine(1, GodManager.getGod(t).getGodName().toUpperCase());
					}
				}
			} 
			catch (IndexOutOfBoundsException e) 
			{
				e.printStackTrace();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
	}

	public static void loadChamps() 
	{
		for(int i=1; i<=GodManager.getActiveGods(); i++)
		{
			GodType t=Utility.getGodTypeFromDBNumber(i);
			ResultSet r=null;
			try 
			{
				r = (new SQLQuery(new String("select lastActivated,champ from £.gods where idGod="+i+";"),msqlc)).excecuteQuery();
			} 
			catch (MySQLSyntaxErrorException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try 
			{
				while(r.next())
				{
					GregorianCalendar c=null;
					Date d=r.getDate(1);
					if(d!=null)
					{
						c=new GregorianCalendar();
						c.setTime(r.getDate(1));
					}
					God g=GodManager.getGod(t);
					if(g!=null)
					{
						g.setLastActivated(c);
						String s=r.getString(2);
						if(s!=null)
						{
							OfflinePlayer p=Bukkit.getOfflinePlayer(r.getString(2));
							g.setChamp(p);
						}
					}
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
	}

	public static void saveThrones() 
	{
		for(int i=1; i<GodManager.getActiveGods()+1; i++)
		{
			God g=GodManager.getGod(Utility.getGodTypeFromDBNumber(i));
			if(g!=null)
			{
				if(g.hasThrone())
				{
					if(AltarManager.getAltar(g.getThrone()).getDBId()==0)
					{
						Location l=g.getThrone().getLocation();
						String s="insert into £.locations(world,chunkx,chunkz,x,y,z) values('"+l.getWorld().getName()+"',"+l.getChunk().getX()+","+l.getChunk().getZ()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ()+")"; 
						(new SQLQuery(s,msqlc)).excecuteUpdate();
						ResultSet r=null;
						try
						{
							r = (new SQLQuery("select max(id) from £.locations",msqlc)).excecuteQuery();
						} 
						catch (MySQLSyntaxErrorException e1)
						{
							e1.printStackTrace();
						}
						try 
						{
							r.first();
							s="update £.thrones set hasThrone=true, location="+r.getInt(1)+" where god="+i;
							(new SQLQuery(s,msqlc)).excecuteUpdate();
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					String s="select location from £.thrones where god="+i+";";
					ResultSet r=null;
					try {
						r=(new SQLQuery(s,msqlc)).excecuteQuery();
					} catch (MySQLSyntaxErrorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					s="update £.thrones set hasThrone=false, location=NULL where god="+i+";";
					(new SQLQuery(s,msqlc)).excecuteUpdate();
					try 
					{
						while(r.next())
						{
							s="delete from £.locations where id="+r.getInt(1)+";";
							(new SQLQuery(s,msqlc)).excecuteUpdate();
						}
					}
					catch (SQLException e) 
					{
						e.printStackTrace();
					}
				}
				GregorianCalendar gc=g.getLastActivated();
				if(gc!=null)
				{
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					String s="update £.gods set lastActivated='"+df.format(gc.getTime())+"' where idGod="+i+";";
					(new SQLQuery(s,msqlc)).excecuteUpdate();
				}
			}
		}
	}

	public static void saveAltars() 
	{
		Set<Entry<Location,Altar>> sal=AltarManager.getAltars();
		for(Entry<Location, Altar> al:sal)
		{
			try 
			{
				Altar a=al.getValue();
				if(a.marked())
				{
					ResultSet r=new SQLQuery("select location from £.altar where id="+a.getDBId()+";",msqlc).excecuteQuery();
					while(r.next())
					{
						new SQLQuery("delete from £.altar where id="+a.getDBId()+";",msqlc).excecuteUpdate();
						new SQLQuery("delete from £.locations where id="+r.getInt(1)+";",msqlc).excecuteUpdate();
					}
				}
				else
				{
					int idDB=a.getDBId();
					if(idDB==-1)
					{
						Location l=a.getLocation();
						new SQLQuery("insert into £.locations(world,chunkx,chunkz,x,y,z) values('"+l.getWorld().getName()+"',"+l.getChunk().getX()+","+l.getChunk().getZ()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ()+");",msqlc).excecuteUpdate();
						ResultSet r=new SQLQuery("select max(id) from £.locations",msqlc).excecuteQuery();
						while(r.next())
						{
							new SQLQuery("insert into £.altar(god,type,location) values("+Utility.getDBNumberFromGodType(a.getGod().getGodType())+",'"+a.getType()+"',"+r.getInt(1)+")",msqlc).excecuteUpdate();
						}
					}
				}
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}		
	}

	public static void saveBlocks(Chunk c) 
	{
		Set<Entry<Location, PlacedBlock>> i=BlockManager.getPlacedBlocksIterator();
		for(Entry<Location, PlacedBlock> lpb:i)
		{
			PlacedBlock b=lpb.getValue();
			if(!b.getBlock().getChunk().equals(c)) return;
			if(b.isInDB())
			{
				if(Utility.daysFromToday(b.getDayPlaced())>6) b.mark();
				if(b.isMarked())
				{
					ResultSet r=null;
					try {
						r=(new SQLQuery("select location from £.nonnatural where id="+b.getIdDB()+";",msqlc)).excecuteQuery();
					} catch (MySQLSyntaxErrorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try 
					{
						while(r.next())
						{
							(new SQLQuery("delete from £.locations where id="+r.getInt(1)+";",msqlc)).excecuteUpdate();
							(new SQLQuery("delete from £.nonnatural where id="+b.getIdDB()+";",msqlc)).excecuteUpdate();
						}
					} 
					catch (SQLException e) 
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				Location l=b.getBlock().getLocation();
				String s="insert into £.locations(world,chunkx,chunkz,x,y,z) values('"+l.getWorld().getName().toString()+"',"+c.getX()+","+c.getZ()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ()+");";
				(new SQLQuery(s,msqlc)).excecuteUpdate();
				ResultSet r=null;
				try {
					r = (new SQLQuery("select max(id) from £.locations;",msqlc)).excecuteQuery();
				} catch (MySQLSyntaxErrorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try 
				{
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					while(r.next())
					{
						(new SQLQuery("insert into £.nonnatural(location,dayPlaced) values("+r.getInt(1)+",'"+df.format(b.getDayPlaced().getTime())+"');",msqlc)).excecuteUpdate();
					}
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
		}		
	}
	public static void createDatabase(String string)
	{
		ConfigManager.reloadConfig();
		MySQLConfig msqlc2=ConfigManager.getMySQLConfig();
		(new SQLQuery("CREATE DATABASE IF NOT EXISTS £;",msqlc2)).excecuteUpdate();	
		(new SQLQuery("CREATE TABLE IF NOT EXISTS £.`locations` (`id` int(11) NOT NULL AUTO_INCREMENT, `world` varchar(45) NOT NULL, `chunkx` int(11) NOT NULL, `chunkz` int(11) NOT NULL, `x` int(11) NOT NULL,  `y` int(11) NOT NULL, `z` int(11) NOT NULL, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1",msqlc2)).excecuteUpdate();
		(new SQLQuery("CREATE TABLE IF NOT EXISTS £.`gods` (`idGod` int(11) NOT NULL AUTO_INCREMENT, `champ` varchar(45) DEFAULT NULL, `Descrizione` varchar(45) DEFAULT NULL, `lastActivated` date DEFAULT NULL, PRIMARY KEY (`idGod`)) ENGINE=InnoDB DEFAULT CHARSET=latin1",msqlc2)).excecuteUpdate();
		(new SQLQuery("CREATE TABLE IF NOT EXISTS £.`altar`(`id` int(11) NOT NULL AUTO_INCREMENT, `god` int(11) NOT NULL,`type` varchar(20) NOT NULL, `location` int(11) NOT NULL, `priest` int(11) NOT NULL, PRIMARY KEY (`id`), KEY `FK_god_idx` (`god`), KEY `FK_location_altar_idx` (`location`), KEY `FK_priest_idx`, CONSTRAINT `FK_god` FOREIGN KEY (`god`) REFERENCES £.`gods` (`idGod`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `FK_location_altar` FOREIGN KEY (`location`) REFERENCES £.`locations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE),CONSTRAINT `FK_priest` FOREIGN KEY (`priest`) REFERENCES £.`followers` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE, ENGINE=InnoDB DEFAULT CHARSET=latin1;",msqlc2)).excecuteUpdate();
		(new SQLQuery("CREATE TABLE IF NOT EXISTS £.`followers` (`id` int(11) NOT NULL AUTO_INCREMENT,`follower` varchar(45) NOT NULL,`reputation` int(11) NOT NULL, `points` int(11) NOT NULL,`god` int(11) NOT NULL,`lastpray` date DEFAULT NULL, `lastheal` date DEFAULT NULL, `priest` binary NOT NULL DEFAULT FALSE,PRIMARY KEY (`id`),KEY `FollowerGod_idx` (`god`),CONSTRAINT `FollowerGod` FOREIGN KEY (`god`) REFERENCES £.`gods` (`idGod`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=latin1",msqlc2)).excecuteUpdate();
		(new SQLQuery("CREATE TABLE IF NOT EXISTS £.`nonnatural` (`id` int(11) NOT NULL AUTO_INCREMENT, `location` int(11) NOT NULL,`dayPlaced` date NOT NULL, PRIMARY KEY (`id`),KEY `FK_loc_idx` (`location`),CONSTRAINT `FK_loc` FOREIGN KEY (`location`) REFERENCES £.`locations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=latin1",msqlc2)).excecuteUpdate();
		(new SQLQuery("CREATE TABLE IF NOT EXISTS £.`thrones` (`god` int(11) NOT NULL,`hasThrone` tinyint(1) NOT NULL, `location` int(11) DEFAULT NULL,PRIMARY KEY (`god`), KEY `fkgod_idx` (`god`), KEY `fklocation_idx` (`location`), CONSTRAINT `fkgod` FOREIGN KEY (`god`) REFERENCES £.`gods` (`idGod`) ON DELETE NO ACTION ON UPDATE NO ACTION,  CONSTRAINT `fklocation` FOREIGN KEY (`location`) REFERENCES £.`locations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=latin1",msqlc2)).excecuteUpdate();
		msqlc=msqlc2;
		for(int i=1; i<5; i++)
		{
			(new SQLQuery("insert into £.gods(descrizione) values ('desc')",msqlc)).excecuteUpdate();
			(new SQLQuery("insert into £.thrones(god,hasthrone) values ("+i+",0)",msqlc)).excecuteUpdate();
		}
	}
	public static ArrayList<Entry<String,Integer>> getTopFollowers(God g) 
	{
		ArrayList<Entry<String,Integer>> list=new ArrayList<>();
		ResultSet r=null;
		try {
			r = (new SQLQuery("select follower,reputation from £.followers f where god="+Utility.getDBNumberFromGodType(g.getGodType())+" order by f.reputation limit 10;",msqlc)).excecuteQuery();
		} catch (MySQLSyntaxErrorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			while(r.next())
			{
				list.add(new AbstractMap.SimpleEntry<String, Integer>(r.getString(1), new Integer(r.getInt(2))));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return list;
	}
	public static void updateReputationLevel(OfflinePlayer player, int rep) 
	{
		(new SQLQuery("update table £.followers set reputation=reputation+"+rep+" where £.follower like "+player.getName(),msqlc)).excecuteUpdate();		
	}
	public static void connectToDB() throws SQLException 
	{
		msqlc.getConnection();
	}
}
