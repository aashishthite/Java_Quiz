package quiz.client;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import quiz.Common.Entry;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class is used to create GUI and handle the GUI events for the
 * Phone-book client Application. 
 * @author aashish
 *
 */
@SuppressWarnings("serial")
public class PhoneBookUIPanel extends JPanel 
{
	
    protected InputPanel inputPanel;
    protected JTable resultsTable;
    protected DefaultTableModel tableModel;
    protected Comm communication;
    protected Timer timer;
    boolean dataRequested;
    /**
     * Constructor
     * Initializes GUI, Communication thread and timers
     */
    public PhoneBookUIPanel() 
    {
        init();        
        communication = new Comm("localhost");
        timer = new Timer(100, timerEventHandler);
        timer.setRepeats(true);
        timer.start();
        dataRequested=false;
        
    }
    /**
     * Event-handler for timer event.
     * At every timer event, it checks if data is received and takes actions accordingly
     */
    ActionListener timerEventHandler = new ActionListener()
    {    
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{				
			if(communication.dataReceived)
			{				
				switch(communication.currentData)
				{
					case 3://Acknowledgement
						
						break;
					case 4://Received entry						
						if(dataRequested)
						{
							tableModel.addRow(new Object[]{communication.receivedEntry.name,communication.receivedEntry.phoneNum});				
							communication.sendAck2Server(true);							
						}						
						break;
					case 5://Received last entry
						if(dataRequested)
						{
							tableModel.addRow(new Object[]{communication.receivedEntry.name,communication.receivedEntry.phoneNum});
							resultsTable.setModel(tableModel);
							dataRequested = false;
						}
						break;
					case 6://error
						if(dataRequested)
						{							
							JOptionPane.showMessageDialog(inputPanel, "Contact not found.");
							dataRequested = false;
						}
						break;
					default:						
						break;							
				}			
				communication.dataReceived=false;
			}			
		}
    };
    /**
     * Request the server to add an entry to the database
     * @param e Entry data that should be added to database 
     */
    public void addEntry(Entry e)
    {    	
    	communication.send2AddinServer(e); 
    	
    }
    /**
     * Request the server for entries similar to the provided entry
     * @param e partial or complete entry that should be searched for in database
     */
    public void dispEntries(Entry e)
    {      	 	
    	communication.requestSearch2Server(e);
    	dataRequested=true;    	
    	tableModel = new DefaultTableModel(null,new Object[] {"Name", "Phone"});    
    	resultsTable.setModel(tableModel);
    }
    /**
     * Initialize GUI
     */
    protected void init() 
    {
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        inputPanel = new InputPanel((JPanel)this);
        inputPanel.setBorder(new EtchedBorder());
        this.add(inputPanel, BorderLayout.NORTH);
        resultsTable = new JTable();
        Object[] columnNames = new Object[] {"Name", "Phone"};
        tableModel = new DefaultTableModel(null, columnNames);
        resultsTable.setModel(tableModel);
        this.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
    }
}
