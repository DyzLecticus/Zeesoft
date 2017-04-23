package nl.zeesoft.zjmo.orchestra.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import nl.zeesoft.zjmo.Orchestrator;
import nl.zeesoft.zjmo.orchestra.MemberState;
import nl.zeesoft.zjmo.orchestra.OrchestraMember;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolControl;

public class MainFrame extends FrameObject {
	private JLabel							stateLabel			= null;
	private JTable							grid				= null;
	private GridController					gridController		= new GridController();
	private List<JMenuItem>					controlMenuItems	= new ArrayList<JMenuItem>();
	private JMenu							orchestraMenu		= null;
	private JMenu							changesMenu			= null;
	private JMenuItem 						publishMenuItem		= null;
	private JMenuItem 						revertMenuItem		= null;
	
	private int								notConnected		= 0;
	
	public MainFrame(OrchestraController controller) {
		gridController.updatedOrchestraMembers(controller.getOrchestraMembers());
		
		setFrame(new JFrame());
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().addWindowListener(controller.getNewAdapter());
		getFrame().setTitle("Zeesoft Orchestra Controller");
		getFrame().setSize(800,400);
		getFrame().setIconImage(new ImageIconLabel("z",32,Color.WHITE).getBufferedImage());
		getFrame().setJMenuBar(controller.getMainMenuBar(this));
		for (JMenuItem item: controlMenuItems) {
			item.setEnabled(false);
		}

		grid = new JTable();
		grid.setModel(gridController);
		grid.setComponentPopupMenu(getMemberPopupMenu(controller));
		grid.addMouseListener(controller.getNewGridMouseListener());
		grid.getColumnModel().getColumn(0).setPreferredWidth(100);
		grid.getColumnModel().getColumn(1).setPreferredWidth(40);
		grid.getColumnModel().getColumn(2).setPreferredWidth(200);
		grid.getColumnModel().getColumn(3).setPreferredWidth(30);
		grid.getColumnModel().getColumn(5).setPreferredWidth(30);
		grid.setEnabled(false);
		
		stateLabel = new JLabel("Connecting ");
		JPanel statePanel = new JPanel();
		statePanel.setLayout(new BoxLayout(statePanel,BoxLayout.LINE_AXIS));
		statePanel.add(stateLabel);
		
		JScrollPane gridScroller = new JScrollPane(grid);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		panel.add(statePanel);
		panel.add(gridScroller);

		getFrame().add(panel);
	}

	protected List<OrchestraMember> getSelectedMembers() {
		return gridController.getSelectedMembers(grid);
	}

	protected void clearSelection() {
		grid.clearSelection();
	}

	protected void refreshGrid(List<OrchestraMember> members) {
		List<OrchestraMember> selected = gridController.getSelectedMembers(grid);
		gridController.updatedOrchestraMembers(members);
		gridController.fireTableDataChanged();
		gridController.selectMembers(grid,selected);
	}

	protected void setOrchestraChanged(boolean orchestraChanged) {
		changesMenu.setEnabled(orchestraChanged);
		publishMenuItem.setEnabled(orchestraChanged);
		revertMenuItem.setEnabled(orchestraChanged);
		if (orchestraChanged) {
			orchestraMenu.setText("Orchestra*");
			changesMenu.setText("Changes*");
			publishMenuItem.setText("Publish*");
		} else {
			orchestraMenu.setText("Orchestra");
			changesMenu.setText("Changes");
			publishMenuItem.setText("Publish");
		}
	}

	protected void setConnected(boolean connected,ActiveClient client,List<OrchestraMember> members) {
		grid.setEnabled(connected);
		for (JMenuItem item: controlMenuItems) {
			item.setEnabled(connected);
		}
		if (connected) {
			OrchestraMember conductor = client.getMember();
			stateLabel.setText("Connected to: " + conductor.getId() + " (" + conductor.getIpAddressOrHostName() + ":" + conductor.getControlPort() + ")");
		} else {
			String text = "Connecting ";
			for (int i = 0; i<notConnected; i++) {
				text += ".";
			}
			stateLabel.setText(text);
			notConnected++;
			if (notConnected>3) {
				notConnected = 0;
			}
			for (OrchestraMember member: members) {
				member.setState(MemberState.getState(MemberState.UNKNOWN));
			}
			refreshGrid(members);
		}
	}

	protected JMenu getMemberMenu(ActionListener listener) {
		JMenu menu = new JMenu("Members");
		addMemberOptionsToMenu(menu,listener);
		return menu;
	}

	protected JMenu getOrchestraMenu(ActionListener listener) {
		orchestraMenu = new JMenu("Orchestra");
		addOptionToMenu(orchestraMenu,"Import",ControllerImportExportWorker.IMPORT,listener);
		addOptionToMenu(orchestraMenu,"Export",ControllerImportExportWorker.EXPORT,listener);
		changesMenu = new JMenu("Changes");
		publishMenuItem = addOptionToMenu(changesMenu,"Publish",ProtocolControl.UPDATE_ORCHESTRA,listener);
		revertMenuItem = addOptionToMenu(changesMenu,"Revert",OrchestraController.REVERT_CHANGES,listener);
		changesMenu.setEnabled(false);
		publishMenuItem.setEnabled(false);
		revertMenuItem.setEnabled(false);
		orchestraMenu.add(changesMenu);
		addOptionToMenu(orchestraMenu,"Generate",Orchestrator.GENERATE,listener);
		return orchestraMenu;
	}

	protected JPopupMenu getMemberPopupMenu(ActionListener listener) {
		JPopupMenu menu = new JPopupMenu("Members");
		addMemberOptionsToMenu(menu,listener);
		return menu;
	}
	
	protected JMenuItem addOptionToMenu(Component menu,String label,String action,ActionListener listener) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(action);
		item.addActionListener(listener);
		if (menu instanceof JMenu) {
			((JMenu) menu).add(item);
		} else if (menu instanceof JPopupMenu) {
			((JPopupMenu) menu).add(item);
		}
		return item;
	}

	protected void addMemberOptionsToMenu(Component menu,ActionListener listener) {
		controlMenuItems.add(addOptionToMenu(menu,"Refresh",ProtocolControl.GET_STATE,listener));
		addOptionToMenu(menu,"Update",OrchestraController.UPDATE_MEMBER,listener);
		addOptionToMenu(menu,"Add",OrchestraController.ADD_MEMBER,listener);
		addOptionToMenu(menu,"Remove",OrchestraController.REMOVE_MEMBERS,listener);
		JMenu subMenu = new JMenu("Control");
		controlMenuItems.add(addOptionToMenu(subMenu,"Drain offline",ProtocolControl.DRAIN_OFFLINE,listener));
		controlMenuItems.add(addOptionToMenu(subMenu,"Take offline",ProtocolControl.TAKE_OFFLINE,listener));
		controlMenuItems.add(addOptionToMenu(subMenu,"Bring online",ProtocolControl.BRING_ONLINE,listener));
		controlMenuItems.add(addOptionToMenu(subMenu,"Restart",ProtocolControl.RESTART_PROGRAM,listener));
		if (menu instanceof JMenu) {
			((JMenu) menu).add(subMenu);
		} else if (menu instanceof JPopupMenu) {
			((JPopupMenu) menu).add(subMenu);
		}
	}

	public GridController getGridController() {
		return gridController;
	}
}
