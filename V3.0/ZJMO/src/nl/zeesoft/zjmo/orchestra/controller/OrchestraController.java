package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;

public class OrchestraController {
	private Messenger		messenger	= null;
	private WorkerUnion		union		= null;
	private Orchestra		orchestra	= null;
	private JFrame			mainFrame	= new JFrame();
	private MemberClient	client		= null;
	
	public OrchestraController(Orchestra orchestra) {
		messenger = new Messenger(null);
		union = new WorkerUnion(messenger);
		this.orchestra = orchestra;
	}

	public String start() {
		String err = "";
		if (!GraphicsEnvironment.isHeadless()) {
			err = "Envrironment is headless";
		}
		if (err.length()==0) {
			OrchestraMember con = orchestra.getConductor();
			client = con.getNewControlClient(messenger,union);
			client.open();
			if (!client.isOpen()) {
				err = "Failed to connect to conductor: " + con.getIpAddressOrHostName() + ":" + con.getControlPort(); 
			}
		}
		if (err.length()==0) {
	        try {
	        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        	JFrame.setDefaultLookAndFeelDecorated(true);
	        } catch (Exception e) {
	        	// Ignore
	        }
	        messenger.start();
			mainFrame = getMainFrame();
			mainFrame.setVisible(true);
		}
		return err;
	}

	public void stop() {
		if (client!=null && client.isOpen()) {
			client.close();
		}
		if (mainFrame!=null) {
			mainFrame.setVisible(false);
		}
        messenger.stop();
        union.stopWorkers();
	}

	protected void setOrchestra(Orchestra orchestra) {
		this.orchestra = orchestra;
	}
	
	protected void windowClosing(WindowEvent e) {
		stop();
    }
	
	protected ControllerWindowAdapter getNewAdapter() {
		return new ControllerWindowAdapter(this);
	}
	
	private JFrame getMainFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(getNewAdapter());
		frame.setTitle("Zeesoft Orchestra Controller");
		frame.setSize(800,600);
		return frame;
	}
}
