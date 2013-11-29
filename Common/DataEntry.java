package Common;
/**
 * Data for a phone-book entry
 * 
 * @author aashish
 *
 */
public class DataEntry extends Data {
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
		if(name.length()>127 || number.length() >127)
		{

			throw new IllegalArgumentException("Invalid Lengths");
		}
		else
		{

			nameData = name.toCharArray();
			nameSize = (byte) name.length();
			numberData = number.toCharArray();
			numSize = (byte) number.length();	

			data = new byte[nameSize+ numSize +2];//nameSize, numSize should be less than 128
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
}
