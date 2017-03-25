package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
import nl.zeesoft.zjmo.orchestra.ProtocolObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class OrchestraController extends Locker implements ActionListener {
	private boolean						working			= false;
	private WorkerUnion					union			= null;
	private Orchestra					orchestra		= null;
	private MemberClient				client			= null;
	private OrchestraControllerWorker	worker			= null;
	
	private JFrame						mainFrame		= null;
	private JLabel						stateLabel		= new JLabel();
	private JTable						grid			= new JTable();
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
			OrchestraMember con = orchestra.getConductor();
			client = con.getNewControlClient(getMessenger(),union);
			client.open();
			if (!client.isOpen()) {
				err = "Failed to connect to: " + con.getId() + " (" + con.getIpAddressOrHostName() + ":" + con.getControlPort() + ")"; 
				stateLabel.setText(err);
			} else {
				stateLabel.setText("Connected to: " + con.getId() + " (" + con.getIpAddressOrHostName() + ":" + con.getControlPort() + ")");
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

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (!client.isOpen()) {
			lockMe(this);
			OrchestraMember con = orchestra.getConductor();
			stateLabel.setText("Lost connection to: " + con.getId() + " (" + con.getIpAddressOrHostName() + ":" + con.getControlPort() + ")");
			unlockMe(this);
		} else {
			List<OrchestraMember> members = gridController.getSelectedMembers(grid);
			for (OrchestraMember member: members) {
				ZStringBuilder response = null;
				if (evt.getActionCommand().equals(ProtocolControl.DRAIN_OFFLINE)) {
					response = client.sendCommand(ProtocolControlConductor.DRAIN_MEMBER_OFFLINE,"id",member.getId());
				} else if (evt.getActionCommand().equals(ProtocolControl.TAKE_OFFLINE)) {
					response = client.sendCommand(ProtocolControlConductor.TAKE_MEMBER_OFFLINE,"id",member.getId());
				} else if (evt.getActionCommand().equals(ProtocolControl.BRING_ONLINE)) {
					response = client.sendCommand(ProtocolControlConductor.BRING_MEMBER_ONLINE,"id",member.getId());
				}
				if (response==null || !ProtocolObject.isResponseJson(response)) {
					// TODO: Handle unexpected response
				}
			}
		}
	}

	protected boolean updateOrchestraState() {
		boolean r = client.isOpen();
		if (!r) {
			r = client.open();
			if (r) {
				lockMe(this);
				OrchestraMember con = orchestra.getConductor();
				stateLabel.setText("Connected to: " + con.getId() + " (" + con.getIpAddressOrHostName() + ":" + con.getControlPort() + ")");
				unlockMe(this);
			} else {
				lockMe(this);
				OrchestraMember con = orchestra.getConductor();
				stateLabel.setText("Failed to connect to: " + con.getId() + " (" + con.getIpAddressOrHostName() + ":" + con.getControlPort() + ")");
				unlockMe(this);
			}
		}
		if (r) {
			ZStringBuilder response = client.sendCommand(ProtocolControlConductor.GET_ORCHESTRA_STATE);
			if (response!=null) {
				lockMe(this);
				ZStringBuilder current = orchestra.toJson(true).toStringBuilder();
				unlockMe(this);
				if (!response.equals(current)) {
					JsFile json = new JsFile();
					json.fromStringBuilder(response);
					lockMe(this);
					orchestra.fromJson(json);
					gridController.updatedOrchestraMembers(getOrchestraMembers());
					gridController.fireTableDataChanged();
					unlockMe(this);
				}
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
		
		frame.setJMenuBar(getMainMenuBar());
		
		grid = new JTable();
		grid.setModel(gridController);
		
		JPanel statePanel = new JPanel();
		statePanel.setLayout(new BoxLayout(statePanel,BoxLayout.LINE_AXIS));
		statePanel.add(stateLabel);
		
		JScrollPane gridScroller = new JScrollPane(grid);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.add(statePanel);
		panel.add(gridScroller);

		frame.add(panel);
		
		return frame;
	}

	public JMenuBar getMainMenuBar() {
		JMenuBar bar = new JMenuBar();
		bar.add(getMainMenu());
		return bar;
	}
	
	public JMenu getMainMenu() {
		JMenu menu = new JMenu("Member");
		JMenuItem item = null;
		
		item = new JMenuItem("Drain offline");
		item.setActionCommand(ProtocolControl.DRAIN_OFFLINE);
		item.addActionListener(this);
		menu.add(item);
		
		item = new JMenuItem("Take offline");
		item.setActionCommand(ProtocolControl.TAKE_OFFLINE);
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem("Bring online");
		item.setActionCommand(ProtocolControl.BRING_ONLINE);
		item.addActionListener(this);
		menu.add(item);
		return menu;
	}
}
