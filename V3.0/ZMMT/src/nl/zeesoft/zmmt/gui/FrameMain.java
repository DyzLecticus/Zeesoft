package nl.zeesoft.zmmt.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.zeesoft.zmmt.gui.panel.PanelComposition;
import nl.zeesoft.zmmt.gui.panel.PanelInstruments;
import nl.zeesoft.zmmt.gui.panel.PanelObject;
import nl.zeesoft.zmmt.gui.panel.PanelPatterns;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.syntesizer.Instrument;

public class FrameMain extends FrameObject implements ActionListener, ChangeListener, StateChangeSubscriber {
	private static final String	TITLE				= "ZeeTracker";
	
	public static final String	COMPOSITION			= "Composition";
	public static final String	INSTRUMENTS			= "Instruments";
	public static final String	PATTERNS			= "Patterns";

	private static final String	LOAD				= "LOAD";
	private static final String	UNDO				= "UNDO";
	private static final String	SAVE				= "SAVE";
	private static final String	SAVE_AS				= "SAVE_AS";
	private static final String	NEW					= "NEW";
		
	private JTabbedPane			tabs				= null;
	private String				selectedTab			= "";			

	private PanelComposition	compositionPanel	= null;
	private PanelInstruments	instrumentsPanel	= null;
	private PanelPatterns		patternsPanel		= null;
	
	public FrameMain(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		selectedTab = controller.getStateManager().getSelectedTab();
	}

	@Override
	public void initialize() {
		getFrame().setTitle(TITLE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 100;
		int height = (int) screenSize.getHeight() - 100;
		getFrame().setSize(width,height);
		getFrame().setLocation(50,50);
		getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
	
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().addWindowListener(getController().getAdapter());
		getFrame().addWindowFocusListener(getController().getAdapter());
		getFrame().addKeyListener(getController().getPlayerKeyListener());
		
		getFrame().setJMenuBar(getMenuBar());
		
		tabs = new JTabbedPane();
		tabs.addKeyListener(getController().getPlayerKeyListener());
		tabs.setOpaque(true);
		tabs.setBackground(Instrument.getColorForInstrument(getController().getStateManager().getSelectedInstrument()));

		compositionPanel = new PanelComposition(getController());
		compositionPanel.initialize();
		addPanelToTabs(tabs,"Composition",compositionPanel);

		instrumentsPanel = new PanelInstruments(getController());
		instrumentsPanel.initialize();
		addPanelToTabs(tabs,"Instruments",instrumentsPanel);

		patternsPanel = new PanelPatterns(getController());
		patternsPanel.initialize();
		addPanelToTabs(tabs,"Patterns",patternsPanel);

		getFrame().setContentPane(tabs);
		
		switchTo(selectedTab);
		tabs.addChangeListener(this);
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.SELECTED_TAB)) {
			switchTo(evt.getSelectedTab());
		} else {
			if (evt.isCompositionChanged()) {
				getFrame().setTitle(TITLE + "*");
			} else {
				getFrame().setTitle(TITLE);
			}
			tabs.setBackground(Instrument.getColorForInstrument(evt.getSelectedInstrument()));
		}
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (!getSelectedTab().equals(selectedTab)) {
			selectedTab = getSelectedTab();
			getController().getStateManager().setSelectedTab(this,selectedTab);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(LOAD)) {
			getController().loadComposition();
		} else if (evt.getActionCommand().equals(SAVE)) {
			getController().saveComposition();
		} else if (evt.getActionCommand().equals(SAVE_AS)) {
			getController().saveCompositionAs();
		} else if (evt.getActionCommand().equals(UNDO)) {
			getController().undoCompositionChanges();
		} else if (evt.getActionCommand().equals(NEW)) {
			getController().newComposition();
		} else if (evt.getActionCommand().equals(COMPOSITION)) {
			switchTo(COMPOSITION);
		} else if (evt.getActionCommand().equals(INSTRUMENTS)) {
			switchTo(INSTRUMENTS);
		} else if (evt.getActionCommand().equals(PATTERNS)) {
			switchTo(PATTERNS);
		} else {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (evt.getActionCommand().equals(Instrument.INSTRUMENTS[i])) {
					getController().getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[i]);
					break;
				}
			}
		}
	}
	
	protected void switchTo(String tab) {
		selectedTab = tab;
		if (tab.equals(COMPOSITION) && tabs.getSelectedIndex()!=0) {
			tabs.setSelectedIndex(0);
			compositionPanel.requestFocus();
		} else if (tab.equals(INSTRUMENTS) && tabs.getSelectedIndex()!=1) {
			tabs.setSelectedIndex(1);
			instrumentsPanel.requestFocus();
		} else if (tab.equals(PATTERNS) && tabs.getSelectedIndex()!=2) {
			tabs.setSelectedIndex(2);
			patternsPanel.requestFocus();
		}
	}

	protected void addPanelToTabs(JTabbedPane tabs,String label,PanelObject panel) {
		tabs.addTab(label,panel.getScroller());
	}
	
	protected String getSelectedTab() {
		String r = "";
		if (tabs.getSelectedIndex()==0) {
			r = COMPOSITION;
		} else if (tabs.getSelectedIndex()==1) {
			r = INSTRUMENTS;
		} else if (tabs.getSelectedIndex()==2) {
			r = PATTERNS;
		} else {
			r = COMPOSITION;
		}
		return r;
	}
	
	protected JMenuBar getMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		bar.add(fileMenu);
		
		int evt = ActionEvent.CTRL_MASK;
		JMenuItem item = new JMenuItem("Load",KeyEvent.VK_L);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,evt));
		item.setActionCommand(LOAD);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Save",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,evt));
		item.setActionCommand(SAVE);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Save as",KeyEvent.VK_A);
		item.setActionCommand(SAVE_AS);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Undo changes",KeyEvent.VK_U);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,evt));
		item.setActionCommand(UNDO);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("New",KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,evt));
		item.setActionCommand(NEW);
		item.addActionListener(this);
		fileMenu.add(item);

		evt = ActionEvent.SHIFT_MASK;
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		bar.add(editMenu);

		item = new JMenuItem("Composition",KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,evt));
		item.setActionCommand(COMPOSITION);
		item.addActionListener(this);
		editMenu.add(item);

		item = new JMenuItem("Instruments",KeyEvent.VK_I);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,evt));
		item.setActionCommand(INSTRUMENTS);
		item.addActionListener(this);
		editMenu.add(item);
		
		item = new JMenuItem("Patterns",KeyEvent.VK_P);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,evt));
		item.setActionCommand(PATTERNS);
		item.addActionListener(this);
		editMenu.add(item);
		
		evt = ActionEvent.CTRL_MASK;
		JMenu instMenu = new JMenu("Instrument");
		instMenu.setMnemonic(KeyEvent.VK_I);
		bar.add(instMenu);
		
		for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
			item = new JMenuItem(Instrument.INSTRUMENTS[i]);
			int ke = (KeyEvent.VK_1 + i);
			if (i == 9) {
				ke = KeyEvent.VK_0;
			}
			item.setAccelerator(KeyStroke.getKeyStroke(ke,ActionEvent.CTRL_MASK));
			item.setActionCommand(Instrument.INSTRUMENTS[i]);
			item.addActionListener(this);
			instMenu.add(item);
		}
		
		return bar;
	}
}
