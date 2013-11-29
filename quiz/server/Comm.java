package quiz.server;
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
			throw new RuntimeException("Unable to setup communication");
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
			catch (IOException e) 
			{				
				e.printStackTrace();
				System.err.println("Unable to receive from client.");
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
