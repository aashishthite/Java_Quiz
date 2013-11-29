package quiz.server;
import java.io.*;
import java.net.*;
/**
 * Class that represents the communication protocol between the client and server
 * @author aashish
 *
 */
class Protocol
{
	byte header;//(1010)2=(5)10
	byte frameType;
	//1:request Add 
	//2: request Data
	//3: Acknowledgment
	//4: data
	//5: final data
	//6: error	
	Data data;
	char checkSum;
	byte[] frame;
	/**
	 * Constructor
	 * @param frameType Type of the frame defined by the protocol
	 * @param data data to be added to data field in the Protocol frame
	 */
	public Protocol(int frameType,Data data)
	{
		frame = new byte[300];
		frame[0] = 5;
		this.frameType = (byte) frameType;
		frame[1] = this.frameType;
		byte[] rawData = data.getData();
		for(int ii = 0; ii< rawData.length; ii++)
		{
			frame[ii+2] = rawData[ii];
		}
		//add checkSum to frame[299] = 
	}
	/**
	 * Get the raw byte array for the protocol frame
	 * @return byte array that stores frame data
	 */
	public byte[] getFrame()
	{
		return frame;
	}
}
/**
 * Abstraction of Data 
 * @author aashish
 *
 */
abstract class Data
{
	byte[] data;
	/**
	 * Get the byte array of the data
	 * @return byte array of the data
	 */
	public byte[] getData()
	{
		return data;
	}
}
/**
 * Data for an error frame
 * @author aashish
 *
 */
class DataErr extends Data
{
	/**
	 * 
	 */
	public DataErr()
	{
		data = new byte[1];
	}
}
/**
 * Data for an Acknowledgement frame
 * @author aashish
 *
 */
class DataAck extends Data
{
	/**
	 * Create a data byte array representing a positive or a negetive feedback
	 * @param positive set to true if a positive feedback or false otherwise
	 */
	public DataAck(boolean positive)
	{
		data = new byte[1];
		if(positive) data[0]=1;
		else data[0] = 0;
	}
}
/**
 * Data for a phone-book entry
 * 
 * @author aashish
 *
 */
class DataEntry extends Data
{
	char[] nameData;
	char[] numberData;
	byte nameSize;
	byte numSize;
	/**
	 * Create a data frame using given phone-book entry data
	 * @param name Name data. Cannot exceed 120 bytes
	 * @param number Number data. Cannot exceed 120 bytes
	 */
	public DataEntry(String name, String number)
	{
		 nameData = name.toCharArray();
		 nameSize = (byte) name.length();
		 numberData = number.toCharArray();
		 numSize = (byte) number.length();	
		 data = new byte[nameSize+ numSize +2];
		 data[0]= nameSize;
		 data[nameSize+1] = numSize;
		 for(int ii = 0; ii < nameSize; ii++)
		 {
			 data[ii+1] = (byte) nameData[ii];
		 }
		 for(int ii =0; ii <  numSize; ii++)
		 {
			 data[ii+nameSize+2] = (byte) numberData[ii];
		 }
	}
}
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
    InetAddress IPAddressOfCurrentClient;
    int PortofCurrentClient;
    /**
     * Constructor
     * Starts a new thread for receiver.
     */
    public Comm()
    {
    	try {
			
			serverReceiver = new DatagramSocket(9876);
			dataReceived=false;
			t= new Thread(this,"Comunication Thread");
			t.start();
		} catch (SocketException e) {
			
			e.printStackTrace();
		}
    }
 
    /**
     * Send the entry data to the client for displaying on table in GUI
     * @param e Entry data
     */
    public void sendEntry2Client(Entry e)
    {
    	Data data = new DataEntry(e.name,e.phoneNum);
    	protocol = new Protocol(4,data);
    	sendData2Client(protocol.getFrame());
    }
    /**
     * Send the last entry data to be sent to the client for displaying on table in GUI
     * @param e Entry data
     */
    public void sendLastEntry2Client(Entry e)
    {
    	Data data = new DataEntry(e.name,e.phoneNum);
    	protocol = new Protocol(5,data);
    	sendData2Client(protocol.getFrame());
    }
    /**
     * Send a positive or negative acknowledgment to client 
     * @param posNeg true if positive acknowledgment or false otherwise
     */
    public void sendAck2Client(boolean posNeg)
    {
    	Data data = new DataAck(posNeg);
    	protocol = new Protocol(3,data);
    	sendData2Client(protocol.getFrame());
    }
    /**
     * Send an error to client
     */
    public void sendErr2Client(Entry e)
    {
    	Data data = new DataErr();
    	protocol = new Protocol(6,data);
    	sendData2Client(protocol.getFrame());
    }    
    /**
     * Send the frame to client
     * @param data Data frame or byte array
     */
    private void sendData2Client(byte[] data) 
    {
    	try
    	{
    		DatagramSocket Socket = new DatagramSocket();
    		DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddressOfCurrentClient, PortofCurrentClient);
    	    Socket.send(sendPacket);
    	    Socket.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    /**
     * Runs in its own thread to listen to the data sent by the client
     */
    private void receiveDatafromClient()
    {
    	while(true)
    	{	    	
			try 
			{				
				 DatagramPacket receivePacket = new DatagramPacket(rx_buff, rx_buff.length);
				 serverReceiver.receive(receivePacket);				 
		    	 rx_buff = receivePacket.getData();
		    	 IPAddressOfCurrentClient =receivePacket.getAddress();
		    	 PortofCurrentClient = receivePacket.getPort();
		    	 dataReceived = true;
		    	 currentData = rx_buff[1];		    	 
		    	 //check for checksum here
		    	 
		    	 if(currentData==1||currentData==2)
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
			catch (Exception e) 
			{				
				e.printStackTrace();
			}
    	}
    }
    Thread t;
    /**
     * Starts a thread
     */
	@Override
	public void run() 
	{
		receiveDatafromClient();
	}
    
}
