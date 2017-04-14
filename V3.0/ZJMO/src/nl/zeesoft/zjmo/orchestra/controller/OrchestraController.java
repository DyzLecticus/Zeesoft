package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
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
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;
import nl.zeesoft.zjmo.orchestra.protocol.PublishRequest;

public class OrchestraController extends Locker implements ActionListener {
	public static final String				UPDATE_MEMBER		= "UPDATE_MEMBER";
	public static final String				ADD_MEMBER			= "ADD_MEMBER";
	public static final String				SAVE_MEMBER			= "SAVE_MEMBER";
	
	private boolean							working				= false;
	private boolean							connected			= false;
	private boolean							stopping			= false;
	
	private Orchestra						orchestra			= null;
	private Orchestra						orchestraUpdate		= null;
	private boolean							exitOnClose			= false;
	private WorkerUnion						union				= null;

	private ConductorConnector 				connector			= null;
	private ConductorConnector 				workConnector		= null;
	private ControllerStateWorker			stateWorker			= null;
	private ControllerActionWorker			actionWorker		= null;
	
	private JFrame							mainFrame			= null;
	private JLabel							stateLabel			= null;
	private JTable							grid				= null;
	private GridController					gridController		= new GridController();

	private MemberFrame						memberFrame			= null;
	
	private List<JMenuItem>					menuItems			= new ArrayList<JMenuItem>(); 
	
	public OrchestraController(Orchestra orchestra,boolean exitOnClose) {
		super(new Messenger(null));
		this.orchestra = orchestra;
		this.orchestraUpdate = orchestra.getCopy(false);
		this.exitOnClose = exitOnClose;
		union = new WorkerUnion(getMessenger());
		gridController.updatedOrchestraMembers(getOrchestraMembers());
		stateWorker = getNewStateWorker();
		connector = new ConductorConnector(getMessenger(),union,true);
		workConnector = new ConductorConnector(getMessenger(),union,false);
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

	public boolean isStopping() {
		boolean r = false;
		lockMe(this);
		r = stopping;
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
			stopping = false;
			connected = false;
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JFrame.setDefaultLookAndFeelDecorated(true);
			} catch (Exception e) {
				// Ignore
			}
			connector.initialize(orchestraUpdate,null);
			connector.open();
			workConnector.initialize(orchestraUpdate,null);
			workConnector.open();
			getMessenger().start();
			stateWorker.start();
			if (memberFrame==null) {
				memberFrame = getMemberFrame(orchestraUpdate);
			}
			if (mainFrame==null) {
				mainFrame = getMainFrame();
				mainFrame.setVisible(true);
			}
			working = true;
		}
		unlockMe(this);
		return err;
	}

	public void stop() {
		lockMe(this);
		stopping = true;
		unlockMe(this);
		stateWorker.stop();
		lockMe(this);
		if (memberFrame!=null && memberFrame.getFrame().isVisible()) {
			memberFrame.getFrame().setVisible(false);
		}
		if (actionWorker!=null) {
			actionWorker.stop();
			actionWorker = null;
		}
		connector.close();
		workConnector.close();
		getMessenger().stop();
		union.stopWorkers();
		getMessenger().whileWorking();
		unlockMe(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		MemberClient client = getClient();
		if (client!=null && !isStopping()) {
			if (evt.getActionCommand().equals(ADD_MEMBER)) {
				memberFrame.setMember(null);
				memberFrame.getFrame().setVisible(true);
			} else if (evt.getActionCommand().equals(SAVE_MEMBER)) {
				if (memberFrame.checkSave()) {
					memberFrame.getFrame().setVisible(false);
					OrchestraMember saveMember = memberFrame.getSaveMember();
					if (memberFrame.getMember()==null) {
						orchestraUpdate.addMember(saveMember.getPosition().getName(),saveMember.getPositionBackupNumber(),saveMember.getIpAddressOrHostName(),saveMember.getControlPort(),saveMember.getWorkPort());
					} else {
						OrchestraMember updateMember = orchestraUpdate.getMemberById(saveMember.getId());
						updateMember.setControlPort(saveMember.getControlPort());
						updateMember.setWorkPort(saveMember.getWorkPort());
						updateMember.setWorkRequestTimeout(saveMember.getWorkRequestTimeout());
						updateMember.setWorkRequestTimeoutDrain(saveMember.isWorkRequestTimeoutDrain());
					}
					JsFile json = orchestraUpdate.toJson(false);
					json.rootElement.children.add(new JsElem("requestType",ProtocolWork.UPDATE_ORCHESTRA,true));
					PublishRequest pr = new PublishRequest();
					pr.setChannelName(Orchestra.ORCHESTRA_CRITICAL);
					pr.setRequest(json);
					pr = workConnector.publishRequest(pr);
					if (pr==null) {
						showErrorMessage("Failed to publish request","Error");
					} else if (pr.getError().length()>0) {
						showErrorMessage(pr.getError(),"Error");
					} else {
						File f = new File("orchestra.json");
						if (f.exists()) {
							orchestraUpdate.toJson(false).toFile("orchestra.json",true);
						}
					}
				}
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
					if (evt.getActionCommand().equals(UPDATE_MEMBER)) {
						updateMemberIfSelected();
					} else {
						String confirmMessage = "";
						boolean confirmed = true;
						if (evt.getActionCommand().equals(ProtocolControl.DRAIN_OFFLINE)) {
							confirmMessage = "Are you sure you want to drain the selected members offline?";
						} else if (evt.getActionCommand().equals(ProtocolControl.TAKE_OFFLINE)) {
							confirmMessage = "Are you sure you want to take the selected members offline?";
						} else if (evt.getActionCommand().equals(ProtocolControl.RESTART_PROGRAM)) {
							confirmMessage = "Are you sure you want to restart the selected members?";
						}
						if (confirmMessage.length()>0) {
							confirmed = showConfirmMessage(confirmMessage,"Are you sure?");
						}
						if (confirmed && client.isOpen() && !isStopping()) {
							lockMe(this);
							if (actionWorker==null || !actionWorker.isWorking()) {
								actionWorker = getNewActionWorker(client);
								actionWorker.handleAction(evt.getActionCommand(), members);
							} else {
								err = "Action worker is busy";
							}
							unlockMe(this);
						}
					}
				}
				if (err.length()>0) {
					showErrorMessage(err,"Error");
				}
			}
		}
	}

	protected void restartedMember(OrchestraMember member) {
		if (member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
			lockMe(this);
			stateWorker.stop();
			connector.close();
			connector.initialize(orchestraUpdate,null);
			connector.open();
			workConnector.initialize(orchestraUpdate,null);
			workConnector.open();
			workConnector.close();
			stateWorker.start();
			unlockMe(this);
		}
	}
	
	protected void updateOrchestraState() {
		MemberClient client = getClient();
		if (client!=null && !isStopping()) {
			ZStringBuilder response = client.sendCommand(ProtocolControlConductor.GET_ORCHESTRA_STATE);
			if (response!=null && client.isOpen() && !isStopping()) {
				lockMe(this);
				ZStringBuilder current = orchestra.toJson(true).toStringBuilder();
				unlockMe(this);
				if (!response.equals(current)) {
					JsFile json = new JsFile();
					json.fromStringBuilder(response);
					lockMe(this);
					orchestra.fromJson(json);
					List<OrchestraMember> selected = gridController.getSelectedMembers(grid);
					gridController.updatedOrchestraMembers(getOrchestraMembers());
					gridController.fireTableDataChanged();
					gridController.selectMembers(grid,selected);
					unlockMe(this);
				}
			}
		}
	}
	
	protected MemberClient getClient() {
		MemberClient r = null;
		ActiveClient ac = null;
		boolean stop = false;
		lockMe(this);
		List<ActiveClient> clients = connector.getOpenClients();
		if (clients.size()>0) {
			ac = clients.get(0);
			r = ac.getClient();
		}
		stop = stopping;
		unlockMe(this);
		if (!stop) {
			setConnected(r!=null,ac);
		}
		return r;
	}
	
	protected void windowClosing(WindowEvent e) {
		if (e.getWindow()==mainFrame) {
			if (mainFrame!=null) {
				mainFrame.setVisible(false);
			}
			stop();
			lockMe(this);
			working = false;
			unlockMe(this);
			if (exitOnClose) {
				int status = 0;
				if (getMessenger().isError()) {
					status = 1;
				}
				System.exit(status);
			}
		} else {
			e.getWindow().setVisible(false);
		}
	}

	protected void updateMemberIfSelected() {
		List<OrchestraMember> members = gridController.getSelectedMembers(grid);
		if (members.size()>0) {
			OrchestraMember updateMember = orchestraUpdate.getMemberById(members.get(0).getId());
			memberFrame.setMember(updateMember);
			memberFrame.getFrame().setVisible(true);
		}
	}
	
	protected void setConnected(boolean connected,ActiveClient client) {
		lockMe(this);
		this.connected = connected;
		if (mainFrame!=null) {
			grid.setEnabled(connected);
			for (JMenuItem item: menuItems) {
				item.setEnabled(connected);
			}
			if (connected) {
				OrchestraMember conductor = client.getMember();
				stateLabel.setText("Connected to: " + conductor.getId() + " (" + conductor.getIpAddressOrHostName() + ":" + conductor.getControlPort() + ")");
			} else {
				stateLabel.setText("Failed to connect to a conductor");
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

	protected ControllerStateWorker getNewStateWorker() {
		return new ControllerStateWorker(getMessenger(),union,this);
	}

	protected ControllerActionWorker getNewActionWorker(MemberClient client) {
		return new ControllerActionWorker(getMessenger(),union,this,client);
	}
	
	protected ControllerWindowAdapter getNewAdapter() {
		return new ControllerWindowAdapter(this);
	}

	protected GridMouseListener getNewGridMouseListener() {
		return new GridMouseListener(this);
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
		grid.addMouseListener(getNewGridMouseListener());
		
		grid.setEnabled(connected);
		for (JMenuItem item: menuItems) {
			item.setEnabled(connected);
		}
		
		stateLabel = new JLabel("Connecting ...");
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

	protected MemberFrame getMemberFrame(Orchestra orchestra) {
		return new MemberFrame(this,orchestra);
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
		addMemberOptionToMenu(menu,"Update",UPDATE_MEMBER);
		addMemberOptionToMenu(menu,"Refresh",ProtocolControl.GET_STATE);
		addMemberOptionToMenu(menu,"Drain offline",ProtocolControl.DRAIN_OFFLINE);
		addMemberOptionToMenu(menu,"Take offline",ProtocolControl.TAKE_OFFLINE);
		addMemberOptionToMenu(menu,"Bring online",ProtocolControl.BRING_ONLINE);
		addMemberOptionToMenu(menu,"Restart",ProtocolControl.RESTART_PROGRAM);
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
