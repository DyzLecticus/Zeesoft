package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class OrchestraController extends Locker {
	private boolean						working			= false;
	private WorkerUnion					union			= null;
	private Orchestra					orchestra		= null;
	private MemberClient				client			= null;
	private OrchestraControllerWorker	worker			= null;
	
	private JFrame						mainFrame		= new JFrame();
	private JLabel						stateLabel		= new JLabel();
	private GridController				gridController	= new GridController();
	
	public OrchestraController(Orchestra orchestra) {
		super(new Messenger(null));
		union = new WorkerUnion(getMessenger());
		this.orchestra = orchestra;
		gridController.updatedOrchestraMembers(getOrchestraMembers());
		worker = new OrchestraControllerWorker(getMessenger(),union,this);
	}

	public boolean isWorking() {
		boolean r = false;
		lockMe(this);
		r = working;
		unlockMe(this);
		return r;
	}

	public String start() {
		String err = "";
		lockMe(this);
		if (GraphicsEnvironment.isHeadless()) {
			err = "Envrironment is headless";
		}
		if (err.length()==0) {
			// TODO: Create reconnector
			OrchestraMember con = orchestra.getConductor();
			client = con.getNewControlClient(getMessenger(),union);
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
			working = true;
	        getMessenger().start();
	        worker.start();
			mainFrame = getMainFrame();
			mainFrame.setVisible(true);
		}
		unlockMe(this);
		return err;
	}

	public void stop() {
		lockMe(this);
		if (client!=null && client.isOpen()) {
			client.sendCloseSessionCommand();
			client.close();
		}
		if (mainFrame!=null) {
			mainFrame.setVisible(false);
			mainFrame = null;
		}
        worker.stop();
        getMessenger().stop();
        union.stopWorkers();
        getMessenger().whileWorking();
		working = false;
		unlockMe(this);
	}

	protected boolean updateOrchestraState() {
		boolean r = client.isOpen();
		if (!r) {
			r = client.open();
		}
		if (r) {
			ZStringBuilder response = client.sendCommand(ProtocolControlConductor.GET_ORCHESTRA_STATE);
			if (response!=null) {
				JsFile json = new JsFile();
				json.fromStringBuilder(response);
				lockMe(this);
				orchestra.fromJson(json);
				gridController.updatedOrchestraMembers(getOrchestraMembers());
				unlockMe(this);
			} else {
				r = false;
			}
		}
		return r;
	}
	
	protected void windowClosing(WindowEvent e) {
		stop();
    }
	
	protected ControllerWindowAdapter getNewAdapter() {
		return new ControllerWindowAdapter(this);
	}
	
	private List<OrchestraMember> getOrchestraMembers() {
		List<OrchestraMember> members = new ArrayList<OrchestraMember>();
		for (OrchestraMember member: orchestra.getMembers()) {
			members.add(member.getCopy());
		}
		return members;
	}
	
	private JFrame getMainFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(getNewAdapter());
		frame.setTitle("Zeesoft Orchestra Controller");
		frame.setSize(800,600);
		
		JTable grid = new JTable();
		grid.setModel(gridController);
		
		JScrollPane scroller = new JScrollPane(grid);
		
		frame.add(stateLabel);
		frame.add(scroller);
		
		return frame;
	}
}
