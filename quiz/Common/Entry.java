package quiz.Common;
/**
 * Class to represent an Entry in a PhoneBook
 * @author aashish
 *
 */
public class Entry {
	public String name;
	public String phoneNum;
	/**
	 * Constructor
	 * @param name name field of the entry
	 * @param phoneNum number field of the entry
	 */
	public Entry(String name, String phoneNum)
	{
		this.name = name;
		this.phoneNum = phoneNum;
	}
}
