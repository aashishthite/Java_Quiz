package quiz.server;
import java.sql.*;
import java.util.*;

import quiz.Common.Entry;

/**
 * Database manager class
 * Uses H2 in-memory database
 * @author aashish
 *
 */
public class DBMan 
{
	Connection dbConn = null;	
	public static final String TABLE_NAME = "phonebook";


	/**
	 * Constructor
	 */
	public DBMan()
	{
		dbConn = this.getDBConnection();
	}
	/**
	 * Gets connection to the database
	 * Initializes Tables
	 */
	private Connection getDBConnection()
	{
		// JDBC connection to the database 
		Connection tempConn;
		try 
		{
			Class.forName("org.h2.Driver");
			tempConn = DriverManager.getConnection("jdbc:h2:mem:","admin", "admin");//http://stackoverflow.com/questions/5225700/can-i-have-h2-autocreate-a-schema-in-an-in-memory-database/5228564#5228564
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			throw new RuntimeException("Failed to load jdbc connector ..");
		}            
		catch (SQLException e) 
		{
			e.printStackTrace();                        
			throw new RuntimeException("Connection Failed! Check output console");
		}            
		Statement stat;
		try 
		{
			stat = tempConn.createStatement();
			stat.execute("CREATE TABLE " + TABLE_NAME +" (name VARCHAR(255), number VARCHAR(255), PRIMARY KEY (name, number))");
		}
		catch (SQLException e)
		{				
			e.printStackTrace();
			throw new RuntimeException("Statement Execution failed.");
		}
		return tempConn;

	}
	/**
	 * Important to close the database connection, so that database resources are not tied up.
	 * H2 is an inMemory database so the connection is closed when application is exit.     
	 */
	@SuppressWarnings("unused")
	private void closeDBConnection()
	{
		try
		{
			dbConn.close();
			System.out.println("Closed db connection ..");
		} 
		catch (SQLException e) 
		{
			System.err.println("Failed to close connection to the H2 database ..");
			e.printStackTrace();
		}
	}
	/**
	 * Adds entry to the database
	 * @param e Entry data to be added to the database
	 * @throws SQLException 
	 */
	public void addEntry2DB(Entry e) throws SQLException
	{
		PreparedStatement stmt = null;
		stmt= dbConn.prepareStatement("INSERT INTO phonebook VALUES(?, ?)");
		stmt.setString(1, e.name);
		stmt.setString(2, e.phoneNum);
		stmt.execute();//executeUpdate("INSERT INTO phonebook VALUES('" + e.name + "', '" + e.phoneNum + "')");
				

	}
	/**
	 * Search for a similar entry in the database
	 * @param e Entries similar to this parameter are searched in database
	 * @return returns a list of entries that are similar to the entry searched for
	 */
	public List<Entry> SearchEntry(Entry e)
	{
		List<Entry> resultList = new ArrayList<Entry>();
		PreparedStatement prep = null;
		try 
		{
			//Ensure search to be case insensitive
			prep = dbConn.prepareStatement("SELECT * FROM phonebook WHERE UPPER(name) LIKE UPPER(CONCAT('%', ?, '%') ) AND NUMBER LIKE CONCAT('%', ?, '%')");
			prep.setString(1, e.name);
			prep.setString(2, e.phoneNum);
			ResultSet rs = prep.executeQuery();

			while(rs.next()) 
			{
				String name = rs.getString("NAME");
				String number = rs.getString("NUMBER");
				resultList.add(new Entry(name,number));

			}
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
			throw new RuntimeException("Result retreival failed.");
		}
		return resultList;

	}
	/**
	 * Method for helping test Insert
	 * @return ResultSet set of results retrieved from database
	 */
	public ResultSet getAllEntries()
	{
		PreparedStatement prep = null;
		try 
		{
			prep = dbConn.prepareStatement("SELECT * FROM " + TABLE_NAME);
			ResultSet rs = prep.executeQuery();
			return rs;
			
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();			
		}
		return null;		
		
	}

}
