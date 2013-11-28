package quiz.client;

import javax.swing.*;
/**
 * 
 * @author Engineer at Schrodinger
 *
 */
public class PhoneBookApp {

	/**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Phone Book App");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PhoneBookUIPanel phoneBookUIPanel = new PhoneBookUIPanel();
       // JOptionPane.showMessageDialog(jFrame, "Eggs are not supposed to be green.");
        jFrame.add(phoneBookUIPanel);
        jFrame.setSize(500, 600);
        jFrame.setVisible(true);
    }
}
