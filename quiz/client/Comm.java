package quiz.client;
import java.io.*;
import java.net.*;
import quiz.Common.Data;
import quiz.Common.DataAck;
import quiz.Common.DataEntry;
import quiz.Common.DataErr;
import quiz.Common.Entry;
import quiz.Common.Protocol;



/**
 * Class to handle UDP communication with the server. 
 * @author aashish
 *
 */
//http://systembash.com/content/a-simple-java-udp-server-and-udp-client/
public class Comm implements Runnable {
	//Receive memory buffer for Communication
	public byte[] rx_buff = new byte[300];
	public byte currentData;
	public boolean dataReceived;    
	Protocol protocol;
	DatagramSocket clientReceiver, serverReceiver;
	public Entry receivedEntry;
	InetAddress ServerIPAddress;
	/**
	 * Constructor
	 * Starts a new thread for receiver.
	 * @param serverAddress String name of the server
	 */
	public Comm(String serverAddress)
	{
		try 
		{
			ServerIPAddress = InetAddress.getByName(serverAddress);		
			clientReceiver = new DatagramSocket();
			dataReceived=false;
			t = new Thread(this,"Comunication Thread");
			t.start();
		} 
		catch (SocketException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Unable to setup communication");
		}
		catch (UnknownHostException e)
		{

			e.printStackTrace();
			throw new RuntimeException("Could not find server");
		}
	}
	/**
	 * Send the entry data to the server for adding in the database
	 * @param e Entry data
	 */
	public void send2AddinServer(Entry e)
	{
		Data data = new DataEntry(e.name,e.phoneNum);
		protocol = new Protocol(1,data);
		sendData2Server(protocol.getFrame());
	}
	/**
	 * Send data for entry to be searched in the database
	 * @param e Entry data
	 */
	public void requestSearch2Server(Entry e)
	{
		Data data = new DataEntry(e.name,e.phoneNum);
		protocol = new Protocol(2,data);
		sendData2Server(protocol.getFrame());
	}
	/**
	 * Send a positive or negative acknowledgment to server  
	 * @param posNeg true if positive acknowledgment or false otherwise
	 */
	public void sendAck2Server(boolean posNeg)
	{
		Data data = new DataAck(posNeg);
		protocol = new Protocol(3,data);
		sendData2Server(protocol.getFrame());
	}
	/**
	 * Send an error to server
	 */
	public void sendErr2Server()
	{
		Data data = new DataErr();
		protocol = new Protocol(6,data);
		sendData2Server(protocol.getFrame());
	}   
	/**
	 * Send the frame to server
	 * @param data Data frame or byte array
	 * @param serverAddress IPaddress string of the server
	 */
	private void sendData2Server(byte[] data) 
	{
		{    	
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, ServerIPAddress, 9876);
			try 
			{
				clientReceiver.send(sendPacket);
			} 
			catch (IOException e) 
			{		
				e.printStackTrace();
				throw new RuntimeException("Unable to send data packet");
			}    		
		}

	} 
	/**
	 * Runs in its own thread to listen to the data sent by the server
	 */
	private void receiveDatafromServer()
	{   	
		while(true)
		{
			try {			
				DatagramPacket receivePacket = new DatagramPacket(rx_buff, rx_buff.length);
				clientReceiver.receive(receivePacket);					
				rx_buff = receivePacket.getData();
				//check checksum here

				dataReceived = true;			    	
				currentData = rx_buff[1];
				if(currentData==4||currentData==5)
				{			    		 
					char[] name = new char[rx_buff[2]];
					char[] number = new char[rx_buff[rx_buff[2]+3]];
					for(int ii =0; ii <name.length;ii++)
						name[ii] = (char) rx_buff[ii+3];
					for(int ii =0; ii <number.length;ii++)
						number[ii] = (char) rx_buff[ii+name.length+4];
					receivedEntry = new Entry(new String(name),new String(number));
				}		    
			} 
			catch (IOException e) 
			{			
				e.printStackTrace();
				System.err.println("Unable to receive from server");
			}
		}    	
	}
	/**
	 * method for JUnit tests
	 * @return returns boolean whether client is set up or not
	 */
	public boolean isClientSocketSetup()
	{
		return clientReceiver!=null;		
	}
	Thread t;
	/**
	 * Starts a thread
	 */
	@Override
	public void run() 
	{
		receiveDatafromServer();
	}

}
