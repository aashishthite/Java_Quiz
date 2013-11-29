package Common;


/**
 * Class that represents the communication protocol between the client and server
 * @author aashish
 *
 */
public class Protocol {


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
	byte[] frame;//300 byte frame
	
	
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
		//add checkSum frame[299] = 
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
