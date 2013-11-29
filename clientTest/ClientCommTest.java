package clientTest;
import static org.junit.Assert.*;

import org.junit.*;

import Common.Entry;

import quiz.client.Comm;

/**
 * Class to test communication for client
 * @author aashish
 *
 */
public class ClientCommTest {

	@Test
	public void testSend2AddinServer() {
		Comm communication = new Comm("localhost");
		assertTrue(communication.isClientSocketSetup());
		try
		{
			communication.requestSearch2Server(new Entry("name",new String(new char[129])));
			fail("Expected an exception");
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Invalid Lengths", e.getMessage());
		}		
	}

	@Test 
	public void testRequestSearch2Server() 
	{
		Comm communication = new Comm("localhost");
		assertTrue(communication.isClientSocketSetup());
		try
		{
			communication.requestSearch2Server(new Entry(new String(new char[129]),"num"));
			fail("Expected an exception");
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Invalid Lengths", e.getMessage());
		}
	}
}
