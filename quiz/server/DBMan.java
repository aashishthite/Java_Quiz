package quiz.server;
import java.sql.*;
import java.util.*;
public class DBMan {
	Connection dbConn = null;
	
	
    public DBMan()
    {
    	this.getDBConnection();
    }
    
    private void getDBConnection()
    {
            // JDBC connection to the database
      
            
            try 
            {
                    Class.forName("org.h2.Driver");
            } 
            catch (ClassNotFoundException e) 
            {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to load jdbc connector ..");
            }            
            try 
            {
                    dbConn = DriverManager.getConnection("jdbc:h2:mem:","admin", "admin");//http://stackoverflow.com/questions/5225700/can-i-have-h2-autocreate-a-schema-in-an-in-memory-database/5228564#5228564
            } 
            catch (SQLException e) 
            {
                    e.printStackTrace();                        
                    throw new RuntimeException("Connection Failed! Check output console");
            }
            
           
            
            Statement stat;
			try 
			{
				stat = dbConn.createStatement();
				stat.execute("CREATE TABLE PHONEBOOK (NAME VARCHAR(255), NUMBER VARCHAR(255))");
			}
			catch (SQLException e)
			{				
				e.printStackTrace();
				 throw new RuntimeException("Statement Execution failed.");
			}
            
            
    }
    /**
     * Important to close the database connection, so that database resources are not tied up.
     * @param dbConn        JDBC connection to the database
     */
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
    public void addEntry2DB(Entry e)
    {
    	 Statement stmt = null;
    	try 
    	{
			stmt= dbConn.createStatement();
			stmt.executeUpdate("INSERT INTO PHONEBOOK VALUES('" + e.name + "', '" + e.phoneNum + "')");
		}
    	catch (SQLException e1) 
		{
			e1.printStackTrace();
			 throw new RuntimeException("Insertion failed.");
		}
    	
    }
    public List<Entry> SearchEntry(Entry e)
    {
    	List<Entry> resultList = new ArrayList<Entry>();
    	PreparedStatement prep = null;
    	try 
    	{
			prep = dbConn.prepareStatement("SELECT * FROM PHONEBOOK WHERE UPPER(NAME) LIKE UPPER('%"+e.name+"%') AND NUMBER LIKE '%" + e.phoneNum +"%'");
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

}
