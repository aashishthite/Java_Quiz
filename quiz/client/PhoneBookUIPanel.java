package quiz.client;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
/**
 * 
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
    Timer timer;
    boolean dataRequested;
    /**
     * 
     */
    public PhoneBookUIPanel() 
    {
        init();        
        communication = new Comm();
        timer = new Timer(100, timerEventHandler);
        timer.setRepeats(true);
        timer.start();
        dataRequested=false;
        
    }
    /**
     * 
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
					case 3:
						break;
					case 4:						
						if(dataRequested)
						{
							tableModel.addRow(new Object[]{communication.receivedEntry.name,communication.receivedEntry.phoneNum});				
							communication.sendAck2Server(true);							
						}						
						break;
					case 5:
						if(dataRequested)
						{
							tableModel.addRow(new Object[]{communication.receivedEntry.name,communication.receivedEntry.phoneNum});
							resultsTable.setModel(tableModel);
							dataRequested = false;
						}
						break;
					case 6:
						break;
					default:
						break;							
				}			
				communication.dataReceived=false;
			}			
		}
    };
    /**
     * 
     * @param e
     */
    public void addEntry(Entry e)
    {    	
    	communication.send2AddinServer(e); 
    }
    /**
     * 
     * @param e
     */
    public void dispEntries(Entry e)
    {      	 	
    	communication.requestSearch2Server(e);
    	dataRequested=true;    	
    	tableModel = new DefaultTableModel(null,new Object[] {"Name", "Phone"});    	
    }
    /**
     * 
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
