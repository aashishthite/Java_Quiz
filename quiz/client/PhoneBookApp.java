package quiz.client;
import javax.swing.*;
/**
 * Main class for the Phone-book client application
 * @author Engineer at Schrodinger *
 */
public class PhoneBookApp {

	/**
	 * Entry point to the client app
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Phone Book App");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PhoneBookUIPanel phoneBookUIPanel = new PhoneBookUIPanel();
        jFrame.add(phoneBookUIPanel);
        jFrame.setSize(500, 600);
        jFrame.setVisible(true);
    }
}
