package quiz.serverTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.*;

import quiz.server.Comm;
/**
 * Class to test communication for server.
 * @author aashish
 *
 */
public class serverCommTest {

	@Test
	public void testServerReceiver() {
		Comm communication = new Comm();
		DatagramSocket clientReceiver = null;
		DatagramPacket sendPacket = null;
		try 
		{
			clientReceiver = new DatagramSocket();
			sendPacket = new DatagramPacket(new byte[300], 300 , InetAddress.getByName("localhost"), 9876);
			clientReceiver.send(sendPacket);
			Thread.sleep(500);
			assertTrue(communication.dataReceived);
		} 
		catch (SocketException e1) 
		{			
			e1.printStackTrace();
		}
		catch (UnknownHostException e1) 
		{			
			e1.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (InterruptedException e) 
		{			
			e.printStackTrace();
		}
	}

}
