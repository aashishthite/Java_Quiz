package quiz.Common;
/**
 * Data for an Acknowledgement frame
 * @author aashish
 *
 */
public class DataAck extends Data {
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
