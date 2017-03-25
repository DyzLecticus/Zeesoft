package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class OrchestraController extends Locker implements ActionListener {
	private boolean							working			= false;
	private boolean							connected		= false;
	
	private Orchestra						orchestra		= null;
	private OrchestraMember					conductor		= null;
	private boolean							exitOnClose		= false;
	private WorkerUnion						union			= null;

	private MemberClient					client			= null;
	private OrchestraControllerStateWorker	stateWorker		= null;
	private OrchestraControllerActionWorker	actionWorker	= null;
	
	private JFrame							mainFrame		= null;
	private JLabel							stateLabel		= null;
	private JTable							grid			= null;
	private GridController					gridController	= new GridController();
	
	private List<JMenuItem>					menuItems		= new ArrayList<JMenuItem>(); 
	
	public OrchestraController(Orchestra orchestra,boolean exitOnClose) {
		super(new Messenger(null));
		this.orchestra = orchestra;
		this.exitOnClose = exitOnClose;
		union = new WorkerUnion(getMessenger());
		gridController.updatedOrchestraMembers(getOrchestraMembers());
		stateWorker = getNewStateWorker();
	}

	public boolean isConnected() {
		boolean r = false;
		lockMe(this);
		r = connected;
		unlockMe(this);
		return r;
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
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JFrame.setDefaultLookAndFeelDecorated(true);
			} catch (Exception e) {
				// Ignore
			}
			conductor = orchestra.getConductor();
			client = conductor.getNewControlClient(getMessenger(),union);
			actionWorker = getNewActionWorker(client);
			getMessenger().start();
			stateWorker.start();
			mainFrame = getMainFrame();
			mainFrame.setVisible(true);
			working = true;
		}
		unlockMe(this);
		return err;
	}

	public void stop() {
		lockMe(this);
		if (client!=null && client.isOpen()) {
			client.sendCloseSessionCommand();
			client.close();
			client = null;
		}
		if (mainFrame!=null) {
			mainFrame.setVisible(false);
			mainFrame = null;
		}
		stateWorker.stop();
		if (actionWorker!=null) {
			actionWorker.stop();
		}
		getMessenger().stop();
		union.stopWorkers();
		getMessenger().whileWorking();
		working = false;
		unlockMe(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (!client.isOpen()) {
			setConnected(false);
		} else {
			String err = "";
			List<OrchestraMember> members = gridController.getSelectedMembers(grid);
			if (evt.getActionCommand().equals(ProtocolControl.GET_STATE) && members.size()==0) {
				members = getOrchestraMembers();
			}
			if (members.size()==0) {
				err = "Select one or more members";
			}
			if (err.length()==0) {
				String confirmMessage = "";
				boolean confirmed = true;
				if (evt.getActionCommand().equals(ProtocolControl.DRAIN_OFFLINE)) {
					confirmMessage = "Are you sure you want to drain the selected members offline?";
				} else if (evt.getActionCommand().equals(ProtocolControl.TAKE_OFFLINE)) {
					confirmMessage = "Are you sure you want to take the selected members offline?";
				}
				if (confirmMessage.length()>0) {
					confirmed = showConfirmMessage(confirmMessage,"Are you sure?");
				}
				if (confirmed) {
					if (!actionWorker.isWorking()) {
						actionWorker.handleAction(evt.getActionCommand(), members);
					} else {
						err = "Action worker is busy";
					}
				}
			}
			if (err.length()>0) {
				showErrorMessage(err,"Error");
			}
		}
	}

	protected boolean updateOrchestraState() {
		boolean r = client.isOpen();
		if (!r) {
			r = client.open();
			setConnected(r);
		}
		if (r) {
			ZStringBuilder response = client.sendCommand(ProtocolControlConductor.GET_ORCHESTRA_STATE);
			if (response!=null && client.isOpen()) {
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
		if (exitOnClose) {
			int status = 0;
			if (getMessenger().isError()) {
				status = 1;
			}
			System.exit(status);
		}
	}
	
	protected void setConnected(boolean connected) {
		lockMe(this);
		this.connected = connected;
		if (mainFrame!=null) {
			grid.setEnabled(connected);
			for (JMenuItem item: menuItems) {
				item.setEnabled(connected);
			}
			if (connected) {
				stateLabel.setText("Connected to: " + conductor.getId() + " (" + conductor.getIpAddressOrHostName() + ":" + conductor.getControlPort() + ")");
			} else {
				stateLabel.setText("Failed to connect to: " + conductor.getId() + " (" + conductor.getIpAddressOrHostName() + ":" + conductor.getControlPort() + ")");
				for (OrchestraMember member: orchestra.getMembers()) {
					member.setState(MemberState.getState(MemberState.UNKNOWN));
				}
				gridController.updatedOrchestraMembers(getOrchestraMembers());
			}
		}
		unlockMe(this);
	}

	protected List<OrchestraMember> getOrchestraMembers() {
		List<OrchestraMember> members = new ArrayList<OrchestraMember>();
		for (OrchestraMember member: orchestra.getMembers()) {
			members.add(member.getCopy());
		}
		return members;
	}

	protected OrchestraControllerStateWorker getNewStateWorker() {
		return new OrchestraControllerStateWorker(getMessenger(),union,this);
	}

	protected OrchestraControllerActionWorker getNewActionWorker(MemberClient client) {
		return new OrchestraControllerActionWorker(getMessenger(),union,this,client);
	}
	
	protected ControllerWindowAdapter getNewAdapter() {
		return new ControllerWindowAdapter(this);
	}

	protected JFrame getMainFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(getNewAdapter());
		frame.setTitle("Zeesoft Orchestra Controller");
		frame.setSize(800,400);
		frame.setIconImage(new ImageIconLabel("z",32,Color.WHITE).getBufferedImage());
		frame.setJMenuBar(getMainMenuBar());

		grid = new JTable();
		grid.setModel(gridController);
		grid.setComponentPopupMenu(getMemberPopupMenu());
		
		grid.setEnabled(connected);
		for (JMenuItem item: menuItems) {
			item.setEnabled(connected);
		}
		
		stateLabel = new JLabel();
		JPanel statePanel = new JPanel();
		statePanel.setLayout(new BoxLayout(statePanel,BoxLayout.LINE_AXIS));
		statePanel.add(stateLabel);
		
		JScrollPane gridScroller = new JScrollPane(grid);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.add(statePanel);
		panel.add(gridScroller);

		frame.add(panel);
		
		return frame;
	}

	protected JMenuBar getMainMenuBar() {
		JMenuBar bar = new JMenuBar();
		bar.add(getMemberMenu());
		return bar;
	}
	
	protected JMenu getMemberMenu() {
		JMenu menu = new JMenu("Members");
		addMemberOptionsToMenu(menu);
		return menu;
	}

	protected JPopupMenu getMemberPopupMenu() {
		JPopupMenu menu = new JPopupMenu("Members");
		addMemberOptionsToMenu(menu);
		return menu;
	}
	
	protected void addMemberOptionToMenu(Component menu,String label,String action) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(action);
		item.addActionListener(this);
		menuItems.add(item);
		if (menu instanceof JMenu) {
			((JMenu) menu).add(item);
		} else if (menu instanceof JPopupMenu) {
			((JPopupMenu) menu).add(item);
		}
	}

	protected void addMemberOptionsToMenu(Component menu) {
		addMemberOptionToMenu(menu,"Refresh",ProtocolControl.GET_STATE);
		addMemberOptionToMenu(menu,"Drain offline",ProtocolControl.DRAIN_OFFLINE);
		addMemberOptionToMenu(menu,"Take offline",ProtocolControl.TAKE_OFFLINE);
		addMemberOptionToMenu(menu,"Bring online",ProtocolControl.BRING_ONLINE);
	}
	
	protected void showErrorMessage(String message,String title) {
		if (mainFrame!=null) {
			JOptionPane.showMessageDialog(mainFrame, message, title,JOptionPane.ERROR_MESSAGE);
		}
	}

	protected boolean showConfirmMessage(String message,String title) {
		boolean r = false;
		if (mainFrame!=null) {
			r = (JOptionPane.showConfirmDialog(mainFrame,message,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
		}
		return r;
	}
}
