package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zjmo.orchestra.MemberClient;
import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.client.ConductorConnector;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControlConductor;

public class OrchestraController extends Locker implements ActionListener {
	public static final String				UPDATE_MEMBER		= "UPDATE_MEMBER";
	public static final String				ADD_MEMBER			= "ADD_MEMBER";
	public static final String				SAVE_MEMBER			= "SAVE_MEMBER";
	public static final String				REMOVE_MEMBERS		= "REMOVE_MEMBERS";
	public static final String				REVERT_CHANGES		= "REVERT_CHANGES";

	private boolean							working				= false;
	private boolean							connected			= false;
	private boolean							stopping			= false;
	
	private Orchestra						orchestra			= null;
	private boolean							exitOnClose			= false;
	private WorkerUnion						union				= null;

	private Orchestra						orchestraUpdate		= null;
	private boolean							orchestraChanged	= false;
	
	private ConductorConnector 				connector			= null;
	private ControllerStateWorker			stateWorker			= null;
	private ControllerActionWorker			actionWorker		= null;
	private ControllerImportExportWorker	importExportWorker	= null;
	
	private MainFrame						mainFrame			= null;
	private MemberFrame						memberFrame			= null;
	private JFileChooser					fileChooser			= null;

	public OrchestraController(Orchestra orchestra,boolean exitOnClose) {
		super(new Messenger(null));
		this.orchestra = orchestra;
		this.orchestraUpdate = orchestra.getCopy(false);
		this.exitOnClose = exitOnClose;
		union = new WorkerUnion(getMessenger());
		stateWorker = getNewStateWorker(union);
		connector = new ConductorConnector(getMessenger(),union,true);
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
			if (memberFrame==null) {
				memberFrame = getNewMemberFrame(orchestraUpdate);
			}
			if (mainFrame==null) {
				mainFrame = getNewMainFrame();
				mainFrame.getFrame().setVisible(true);
			}
			getMessenger().start();
			connector.initialize(orchestraUpdate,null);
			connector.open();
			stateWorker.start();
			if (importExportWorker==null) {
				importExportWorker = this.getNewImportExportWorker(union);
				importExportWorker.initialize();
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
		if (mainFrame!=null) {
			mainFrame.getFrame().setVisible(false);
		}
		if (actionWorker!=null) {
			actionWorker.stop();
			actionWorker = null;
		}
		if (importExportWorker!=null) {
			importExportWorker.stop();
		}
		connector.close();
		getMessenger().stop();
		union.stopWorkers();
		getMessenger().whileWorking();
		unlockMe(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (
			evt.getActionCommand().equals(ControllerImportExportWorker.IMPORT) || 
			evt.getActionCommand().equals(ControllerImportExportWorker.EXPORT) || 
			evt.getActionCommand().equals(Orchestrator.GENERATE)
			) {
			boolean confirmed = true;
			if (evt.getActionCommand().equals(ControllerImportExportWorker.IMPORT) && isOrchestraChanged()) {
				confirmed = showConfirmMessage("Unpublished orchestra changes will be lost. Are you sure you want to continue?");
			}
			if (confirmed) {
				if (importExportWorker.isWorking()) {
					showErrorMessage("Import/export worker is busy");
				} else {
					lockMe(this);
					importExportWorker.handleAction(evt.getActionCommand(),orchestraUpdate);
					unlockMe(this);
				}
			}
		} else if (evt.getActionCommand().equals(UPDATE_MEMBER)) {
			List<OrchestraMember> members = mainFrame.getSelectedMembers();
			if (members.size()==0) {
				showErrorMessage("Select a member");
			} else {
				updateMemberIfSelected();
			}
		} else if (evt.getActionCommand().equals(ADD_MEMBER)) {
			memberFrame.setMember(null);
			showMemberFrame();
		} else if (evt.getActionCommand().equals(SAVE_MEMBER)) {
			lockMe(this);
			OrchestraMember saveMember = memberFrame.getSaveMember(orchestraUpdate);
			unlockMe(this);
			if (saveMember!=null) {
				memberFrame.getFrame().setVisible(false);
				lockMe(this);
				if (memberFrame.getMember()==null) {
					orchestraUpdate.addMember(saveMember.getPosition().getName(),saveMember.getPositionBackupNumber(),saveMember.getIpAddressOrHostName(),saveMember.getControlPort(),saveMember.getWorkPort());
				} else {
					OrchestraMember updateMember = orchestraUpdate.getMemberById(saveMember.getId());
					updateMember.setControlPort(saveMember.getControlPort());
					updateMember.setWorkPort(saveMember.getWorkPort());
					updateMember.setWorkRequestTimeout(saveMember.getWorkRequestTimeout());
					updateMember.setWorkRequestTimeoutDrain(saveMember.isWorkRequestTimeoutDrain());
				}
				unlockMe(this);
				setOrchestraChanged(true);
			}
		} else if (evt.getActionCommand().equals(REMOVE_MEMBERS)) {
			String err = "";
			List<OrchestraMember> members = mainFrame.getSelectedMembers();
			if (members.size()==0) {
				err = "Select one or more members";
			} else {
				for (OrchestraMember member: members) {
					if (member.getPosition().getName().equals(Orchestra.CONDUCTOR) && member.getPositionBackupNumber()==0) {
						err = "Primary orchestra conductor is mandatory";
						break;
					}
				}
			}
			if (err.length()>0) {
				showErrorMessage(err);
			} else {
				boolean confirmed = this.showConfirmMessage("Are you sure you want to remove the selected members?");
				if (confirmed) {
					lockMe(this);
					for (OrchestraMember member: members) {
						orchestraUpdate.removeMember(member.getId());
					}
					mainFrame.clearSelection();
					mainFrame.refreshGrid(getOrchestraMembers());
					unlockMe(this);
					setOrchestraChanged(true);
				}
			}
		} else if (evt.getActionCommand().equals(REVERT_CHANGES)) {
			if (!isOrchestraChanged()) {
				showErrorMessage("No changes to revert");
			} else {
				boolean confirmed = this.showConfirmMessage("Are you sure you want to revert the orchestra changes?");
				if (confirmed) {
					lockMe(this);
					this.orchestraUpdate = orchestra.getCopy(false);
					unlockMe(this);
					setOrchestraChanged(false);
				}
			}
		} else if (
			evt.getActionCommand().equals(ProtocolControl.GET_STATE) || 
			evt.getActionCommand().equals(ProtocolControl.DRAIN_OFFLINE) || 
			evt.getActionCommand().equals(ProtocolControl.TAKE_OFFLINE) || 
			evt.getActionCommand().equals(ProtocolControl.BRING_ONLINE) || 
			evt.getActionCommand().equals(ProtocolControl.RESTART_PROGRAM) ||
			evt.getActionCommand().equals(ProtocolControl.UPDATE_ORCHESTRA)
			) {
			String err = "";
			MemberClient client = getClient();
			if (client==null) {
				err = "Not connected to a conductor";
			} else if (!isStopping()) {
				List<OrchestraMember> members = mainFrame.getSelectedMembers();
				if (
					evt.getActionCommand().equals(ProtocolControl.UPDATE_ORCHESTRA) ||
					(evt.getActionCommand().equals(ProtocolControl.GET_STATE) && members.size()==0)
					) {
					lockMe(this);
					members = getOrchestraMembers();
					unlockMe(this);
				}
				if (members.size()==0) {
					err = "Select one or more members";
				} else {
					if (evt.getActionCommand().equals(ProtocolControl.RESTART_PROGRAM)) {
						int conductors = 0;
						for (OrchestraMember member: members) {
							if (member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
								conductors++;
							}
						}
						if (conductors>1) {
							err = "Multiple conductors cannot be restarted simultaniously";
						}
					}
				}
				if (err.length()==0) {
					String confirmMessage = "";
					boolean confirmed = true;
					if (evt.getActionCommand().equals(ProtocolControl.DRAIN_OFFLINE)) {
						confirmMessage = "Are you sure you want to drain the selected members offline?";
					} else if (evt.getActionCommand().equals(ProtocolControl.TAKE_OFFLINE)) {
						confirmMessage = "Are you sure you want to take the selected members offline?";
					} else if (evt.getActionCommand().equals(ProtocolControl.RESTART_PROGRAM)) {
						confirmMessage = "Are you sure you want to restart the selected members?";
					} else if (evt.getActionCommand().equals(ProtocolControl.UPDATE_ORCHESTRA)) {
						confirmMessage = "Are you sure you want to publish the orchestra changes?";
					}
					if (confirmMessage.length()>0) {
						confirmed = showConfirmMessage(confirmMessage);
					}
					if (confirmed && client.isOpen() && !isStopping()) {
						lockMe(this);
						if (actionWorker==null || !actionWorker.isWorking()) {
							actionWorker = getNewActionWorker(union,client);
							if (evt.getActionCommand().equals(ProtocolControl.UPDATE_ORCHESTRA)) {
								actionWorker.publishOrchestraUpdate(orchestraUpdate);
							} else {
								actionWorker.handleAction(evt.getActionCommand(), members);
							}
						} else {
							err = "Action worker is busy";
						}
						unlockMe(this);
					}
				}
			}
			if (err.length()>0) {
				showErrorMessage(err);
			}
		}
	}

	public JFileChooser getFileChooser() {
		JFileChooser r = null;
		lockMe(this);
		r = fileChooser;
		unlockMe(this);
		return r;
	}

	public boolean isOrchestraChanged() {
		boolean r = false;
		lockMe(this);
		r = orchestraChanged;
		unlockMe(this);
		return r;
	}
	
	protected ControllerStateWorker getNewStateWorker(WorkerUnion union) {
		return new ControllerStateWorker(getMessenger(),union,this);
	}

	protected ControllerActionWorker getNewActionWorker(WorkerUnion union,MemberClient client) {
		return new ControllerActionWorker(getMessenger(),union,this,client);
	}
	
	protected ControllerImportExportWorker getNewImportExportWorker(WorkerUnion union) {
		return new ControllerImportExportWorker(getMessenger(),union,this);
	}
	
	protected MainFrame getNewMainFrame() {
		return new MainFrame(this);
	}

	protected MemberFrame getNewMemberFrame(Orchestra orchestra) {
		return new MemberFrame(this,orchestra);
	}
		
	protected ControllerWindowAdapter getNewAdapter() {
		return new ControllerWindowAdapter(this);
	}

	protected GridMouseListener getNewGridMouseListener() {
		return new GridMouseListener(this);
	}
	
	protected JMenuBar getMainMenuBar(MainFrame mainFrame) {
		JMenuBar bar = new JMenuBar();
		bar.add(mainFrame.getOrchestraMenu(this));
		bar.add(mainFrame.getMemberMenu(this));
		return bar;
	}

	protected void setFileChooser(JFileChooser fileChooser) {
		lockMe(this);
		this.fileChooser = fileChooser;
		unlockMe(this);
	}
	
	protected File chooseFile(boolean selectFile,String title) {
		File file = null;
		lockMe(this);
		if (fileChooser!=null) {
			if (selectFile) {
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			} else {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}
			int choice = fileChooser.showDialog(mainFrame.getFrame(),title);
			if (choice==JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
		}
		unlockMe(this);
		return file;
	}

	protected void completedImportExportAction(Orchestra orchestraUpdate, String action) {
		// Override to extend
	}

	protected List<OrchestraMember> publishedOrchestraUpdate() {
		List<OrchestraMember> restartMembers = new ArrayList<OrchestraMember>();
		setOrchestraChanged(false);
		lockMe(this);
		for (OrchestraMember member: orchestra.getMembers()) {
			OrchestraMember updateMember = orchestraUpdate.getMemberById(member.getId());
			if (updateMember==null) {
				restartMembers.add(member.getCopy());
			}
		}
		unlockMe(this);
		return restartMembers;
	}
	
	protected void importedOrchestraUpdate(Orchestra orchestraUpdate) {
		lockMe(this);
		this.orchestraUpdate = orchestraUpdate;
		unlockMe(this);
		setOrchestraChanged(true);
	}

	protected void setOrchestraChanged(boolean orchestraChanged) {
		lockMe(this);
		this.orchestraChanged = orchestraChanged;
		mainFrame.setOrchestraChanged(orchestraChanged);
		unlockMe(this);
	}
	
	protected void restartedMembers(List<OrchestraMember> members) {
		boolean reconnect = false;
		for (OrchestraMember member: members) {
			if (member.getPosition().getName().equals(Orchestra.CONDUCTOR)) {
				reconnect = true;
				break;
			}
		}
		if (reconnect) {
			lockMe(this);
			stateWorker.stop();
			connector.close();
			connector.initialize(orchestraUpdate,null);
			connector.open();
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
					mainFrame.refreshGrid(getOrchestraMembers());
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
		if (e.getWindow()==mainFrame.getFrame()) {
			boolean confirmed = true;
			if (isOrchestraChanged()) {
				confirmed = showConfirmMessage("Unpublished orchestra changes will be lost. Are you sure you want to quit?");
			}
			if (confirmed) {
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
			}
		} else {
			e.getWindow().setVisible(false);
		}
	}

	protected void windowIconified(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			if (memberFrame!=null && memberFrame.getFrame().isVisible()) {
				memberFrame.getFrame().setVisible(false);
			}
		} else {
			e.getWindow().setVisible(false);
		}
	}

	protected void updateMemberIfSelected() {
		List<OrchestraMember> members = mainFrame.getSelectedMembers();
		if (members.size()>0) {
			OrchestraMember updateMember = orchestraUpdate.getMemberById(members.get(0).getId());
			memberFrame.setMember(updateMember);
			showMemberFrame();
		}
	}
	
	protected void setConnected(boolean connected,ActiveClient client) {
		lockMe(this);
		this.connected = connected;
		if (mainFrame!=null) {
			mainFrame.setConnected(connected, client, getOrchestraMembers());
		}
		unlockMe(this);
	}

	protected List<OrchestraMember> getOrchestraMembers() {
		List<OrchestraMember> members = new ArrayList<OrchestraMember>();
		for (OrchestraMember member: orchestraUpdate.getMembers()) {
			OrchestraMember stateMember = orchestra.getMemberById(member.getId());
			if (stateMember==null) {
				stateMember = member;
			}
			members.add(stateMember.getCopy());
		}
		return members;
	}

	protected void showErrorMessage(String message) {
		if (mainFrame!=null) {
			JOptionPane.showMessageDialog(mainFrame.getFrame(),message,"Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	protected boolean showConfirmMessage(String message) {
		return showConfirmMessage(message,"Are you sure?");
	}
	
	protected boolean showConfirmMessage(String message,String title) {
		boolean r = false;
		if (mainFrame!=null) {
			r = (JOptionPane.showConfirmDialog(mainFrame.getFrame(),message,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
		}
		return r;
	}
	
	protected void showMemberFrame() {
		if (!memberFrame.getFrame().isVisible() && mainFrame.getFrame().isVisible()) {
			positionFrameOverFrame(memberFrame.getFrame(),mainFrame.getFrame());
		}
		memberFrame.getFrame().setVisible(true);
	}
	
	protected void positionFrameOverFrame(Window top,Window bottom) {
		int x = (bottom.getX() + (bottom.getWidth() / 2)) - (top.getWidth() / 2);
		int y = (bottom.getY() + (bottom.getHeight() / 2)) - (top.getHeight() / 2);
		top.setLocation(x, y);
	}
}
