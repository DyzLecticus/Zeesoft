package nl.zeesoft.zeetracker.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;

public class WindowBusy extends Locker {
	private JFrame					parent	= null;
	private JWindow					window	= null;
	private JLabel					busy	= null;
	private JLabel					details	= null;
	
	private List<WindowBusyClient>	clients	= new ArrayList<WindowBusyClient>(); 

	public WindowBusy(Messenger msgr,JFrame parent) {
		super(msgr);
		this.parent = parent;
		window = new JWindow();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		String z = "0000000000";
		z = z + z + z + z;
		busy = new JLabel(z);
		busy.setAlignmentX(Component.CENTER_ALIGNMENT);
		busy.setFocusable(false);
		details = new JLabel(z + z);
		details.setAlignmentX(Component.CENTER_ALIGNMENT);
		details.setFocusable(false);
		
		panel.add(busy);
		panel.add(details);

		JPanel border = new JPanel();
		border.setLayout(new BorderLayout());
		border.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		border.add(panel,BorderLayout.CENTER);

		window.setContentPane(border);
		window.pack();
	}
	
	public void setBusy(Object source, String busy, String details) {
		lockMe(source);
		WindowBusyClient client = getClient(source);
		if (client==null) {
			client = new WindowBusyClient();
			client.client = source;
			clients.add(client);
		}
		client.busy = busy;
		client.details = details;
		changedClients();
		unlockMe(source);
	}
	
	public int setDone(Object source) {
		int remaining = 0;
		lockMe(source);
		WindowBusyClient client = getClient(source);
		if (client!=null) {
			clients.remove(client);
			changedClients();
		}
		remaining = clients.size();
		unlockMe(source);
		return remaining;
	}

	protected WindowBusyClient getClient(Object source) {
		WindowBusyClient r = null;
		for (WindowBusyClient client: clients) {
			if (client.client==source) {
				r = client;
				break;
			}
		}
		return r;
	}
	
	protected void changedClients() {
		if (clients.size()>0) {
			busy.setText(maxLen(clients.get(0).busy,80));
			details.setText(maxLen(clients.get(0).details,80));
			FrameObject.positionFrameOverFrame(window,parent);
			if (parent.isVisible()) {
				window.setVisible(true);
			}
		} else {
			window.setVisible(false);
		}
	}
	
	private String maxLen(String str,int len) {
		if (str.length()>len) {
			str = str.substring((str.length() - len));
		}
		return str;
	}
}
