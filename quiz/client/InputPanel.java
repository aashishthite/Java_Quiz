package quiz.client;
import java.util.*;
import java.util.List;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class handles the GUI input to the application
 * @author aashish
 *
 */
@SuppressWarnings("serial")
public class InputPanel extends JPanel 
{

    protected JLabel nameLabel;
    protected JLabel phoneLabel;
    protected JTextField nameField;
    protected JTextField phoneField;
    protected JButton addButton;
    protected JButton searchButton;
    protected PhoneBookUIPanel parent;
    /**
     * Constructor
     * @param parent a reference to the PhoneBookUIPanel class which is the parent of this class
     */
    public InputPanel(JPanel parent) 
    {
        init();       
        this.parent = (PhoneBookUIPanel) parent; 
    }
    /**
     * Initialize GUI
     */
    protected void init() {
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        nameLabel = new JLabel("Name");
        phoneLabel = new JLabel("Phone");
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        addButton = new JButton("Add");
        searchButton = new JButton("Search");

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            constraints.ipadx = 10;
            this.add(nameLabel, constraints);
        }

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridwidth = 3;
            constraints.fill = GridBagConstraints.BOTH;
            this.add(nameField, constraints);
        }

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.LINE_END;
            constraints.ipadx = 10;
            this.add(phoneLabel, constraints);
        }

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.gridwidth = 3;
            constraints.fill = GridBagConstraints.BOTH;
            this.add(phoneField, constraints);
        }

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 2;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.LINE_START;
            this.add(addButton, constraints);
        }

        {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 3;
            constraints.gridy = 2;
            constraints.ipadx = 10;
            this.add(searchButton, constraints);
        }
        //Add Listeners to  button events
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
               String name = nameField.getText().trim();
               String number = phoneField.getText().trim();
               
               if(!name.equals("") && !number.equals(""))
               {
            	   if(name.length()<120)
            	   {
            		   if(number.length()<120)
            			   parent.addEntry(new Entry(name,number)); 
            		   else
            			   JOptionPane.showMessageDialog(parent, "Phone number cannot exceed 120 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            	   }
            	   else
            		   JOptionPane.showMessageDialog(parent, "Name cannot exceed 120 characters.", "Error", JOptionPane.ERROR_MESSAGE);
               }
               else
               {
            	   JOptionPane.showMessageDialog(parent, "Missing fields", "Error", JOptionPane.ERROR_MESSAGE);
               }              
           }
        });
       
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
            	 String name = nameField.getText().trim();
                 String number = phoneField.getText().trim();
                 
                 if(!name.equals("") || !number.equals(""))
                 {
                	 
                	 if(name.length()<120)
              	   	 {
                		 if(number.length()<120)
                			 parent.dispEntries(new Entry(name,number));
                		 else
                			 JOptionPane.showMessageDialog(parent, "Phone number cannot exceed 120 characters.", "Error", JOptionPane.ERROR_MESSAGE);
              	   	 }
                	 else
                		 JOptionPane.showMessageDialog(parent, "Name cannot exceed 120 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                 }          	
            }
        });
        //Tool tips for buttons
        addButton.setToolTipText("Add a Phone Book Entry");
        searchButton.setToolTipText("Search for a Phone Book Entry");

    }
}
