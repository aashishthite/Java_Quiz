package quiz.serverTest;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import quiz.Common.Entry;
import quiz.server.DBMan;
/**
 * Unit tests for DBMan class
 * @author aashish
 *
 */
public class DBTest {


	@Test
	public void testAddEntry2DB() {
		DBMan dbManager = new DBMan();
		dbManager.addEntry2DB(new Entry("MyName","MyNumber"));
		try 
		{	
			ResultSet rs = dbManager.getAllEntries();
			if(rs.next())
			{
				assertEquals(rs.getString("NAME"),"MyName");
				assertEquals(rs.getString("NUMBER"),"MyNumber");
			}

		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}

	@Test
	public void testSearchEntry() {
		DBMan dbManager = new DBMan();
		dbManager.addEntry2DB(new Entry("John Doe","1242354235"));
		dbManager.addEntry2DB(new Entry("Johnathan Jones","1543512356"));
		dbManager.addEntry2DB(new Entry("Robert Johnathan Smith","1242354235"));
		dbManager.addEntry2DB(new Entry("Aashish Thite","981750198575"));
		List<Entry> returnedList= dbManager.SearchEntry(new Entry("JoHn",""));
		assertEquals(returnedList.size(),3);
		returnedList= dbManager.SearchEntry(new Entry("jOhN",""));
		assertEquals(returnedList.size(),3);
		returnedList= dbManager.SearchEntry(new Entry("","2"));
		assertEquals(returnedList.size(),3);
		returnedList= dbManager.SearchEntry(new Entry("","985"));
		assertEquals(returnedList.size(),1);
	}

}
