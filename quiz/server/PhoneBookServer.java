package quiz.server;

import javax.swing.JFrame;
/**
 * 
 * @author aashish
 *
 */
public class PhoneBookServer 
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{		
		 JFrame jFrame = new JFrame("Phone Book Server App");
	     jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     PhoneBookServerUI phoneBookUIPanel = new PhoneBookServerUI();
	     jFrame.add(phoneBookUIPanel);
	     jFrame.setSize(500, 600);
	     jFrame.setVisible(true);
	}
}
