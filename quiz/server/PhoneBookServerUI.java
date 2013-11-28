package quiz.server;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
/**
 * 
 * @author aashish
 *
 */
@SuppressWarnings("serial")
public class PhoneBookServerUI extends JPanel
{
	protected JLabel nameLabel;
	protected JLabel countLabel;
	public List<Entry> entries;
	protected Comm communication;
	Timer timer;
	int index2Send;	
	DBMan dbManager = null;
	/**
	 * 
	 */
	public PhoneBookServerUI()
	{
		init();
		dbManager = new DBMan();
        communication = new Comm();
        timer = new Timer(100, timerEventHandler);
        timer.setRepeats(true);
        timer.start();
        index2Send =0;
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
					case 1:
						dbManager.addEntry2DB(new Entry(communication.receivedEntry.name,communication.receivedEntry.phoneNum));
						//countLabel.setText(((Integer)entries.size()).toString());
						break;					
					case 2:	
						index2Send = 0;
						entries = dbManager.SearchEntry(communication.receivedEntry);
						if(entries.size()>0)
						{	
							communication.sendEntry2Client(entries.get(index2Send));
						}
						else if(entries.size()==1)
						{
							communication.sendLastEntry2Client(entries.get(0));
						}
						else
							communication.sendErr2Client(new Entry(communication.receivedEntry.name,communication.receivedEntry.phoneNum));
						break;
					case 3:
						index2Send++;
						if(index2Send < entries.size()-1)
							communication.sendEntry2Client(entries.get(index2Send));
						else if(entries.size()>1)
							communication.sendLastEntry2Client(entries.get(entries.size()-1));
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
     */
	private void init()
	{
		 GridBagLayout layout = new GridBagLayout();
	     this.setLayout(layout);
	     nameLabel = new JLabel("Number of entries in Database");
	     countLabel = new JLabel("0");     

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
	            constraints.gridx = 0;
	            constraints.gridy = 1;
	            constraints.anchor = GridBagConstraints.LINE_END;
	            constraints.ipadx = 10;
	            this.add(countLabel, constraints);
	        }

	}
}
