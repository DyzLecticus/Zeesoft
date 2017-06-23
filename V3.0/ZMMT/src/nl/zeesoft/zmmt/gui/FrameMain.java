package nl.zeesoft.zmmt.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import nl.zeesoft.zmmt.gui.panel.PanelMix;
import nl.zeesoft.zmmt.gui.panel.PanelObject;
import nl.zeesoft.zmmt.gui.panel.PanelPatterns;
import nl.zeesoft.zmmt.gui.panel.PanelSequence;
import nl.zeesoft.zmmt.gui.panel.PanelSettings;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class FrameMain extends FrameObject implements ActionListener, ChangeListener, StateChangeSubscriber {
	private static final String	TITLE				= "ZeeTracker";

	private static final String	QUIT				= "QUIT";
	private static final String	LOAD				= "LOAD";
	private static final String	SAVE				= "SAVE";
	private static final String	SAVE_AS				= "SAVE_AS";
	private static final String	NEW					= "NEW";
	private static final String	DEMO				= "DEMO";
	private static final String	LOAD_FILE_PREFIX	= "LOAD_FILE:";
	
	public static final String	EDIT_UNDO			= "EDIT_UNDO";
	public static final String	EDIT_REDO			= "EDIT_REDO";

	public static final String	PATTERN_PREFIX		= "PATTERN_";
	public static final String	PATTERN_SELECT		= PATTERN_PREFIX + "SELECT";
	public static final String	PATTERN_SELECT_NEXT	= PATTERN_PREFIX + "SELECT_NEXT";
	public static final String	PATTERN_SELECT_PREV	= PATTERN_PREFIX + "SELECT_PREV";
	public static final String	PATTERN_EDIT_MODE	= PATTERN_PREFIX + "EDIT_MODE";
	public static final String	PATTERN_INSERT		= PATTERN_PREFIX + "INSERT";
	public static final String	PATTERN_BARS		= PATTERN_PREFIX + "BARS";
	public static final String	PATTERN_EDIT		= PATTERN_PREFIX + "EDIT";

	public static final String	SOLO				= "SOLO";
	public static final String	UNMUTE				= "UNMUTE";
	
	public static final String	PLAY_PATTERN		= "PLAY_PATTERN";
	public static final String	PLAY_SEQUENCE		= "PLAY_SEQUENCE";
	public static final String	CONTINUE_PLAYING	= "CONTINUE_PLAYING";
	public static final String	STOP_PLAYING		= "STOP_PLAYING";

	public static final String	TAB_COMPOSITION		= "Composition";
	public static final String	TAB_INSTRUMENTS		= "Instruments";
	public static final String	TAB_PATTERNS		= "Patterns";
	public static final String	TAB_SEQUENCE		= "Sequence";
	public static final String	TAB_MIX				= "Mix";
	public static final String	TAB_SETTINGS		= "Settings";
		
	private JTabbedPane			tabs				= null;
	private String				selectedTab			= "";			

	private PanelComposition	compositionPanel	= null;
	private PanelInstruments	instrumentsPanel	= null;
	private PanelPatterns		patternsPanel		= null;
	private PanelSequence		sequencePanel		= null;
	private PanelMix			mixPanel			= null;
	private PanelSettings		settingsPanel		= null;
	
	private String				fileName			= "";
	private boolean				compositionChanged	= false;
	
	private List<String>		recentFilesCopy		= null;
	private JMenu				recentFileMenu		= null;
	private JMenuItem[]			recentFileMenus		= new JMenuItem[8];
	
	public FrameMain(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		selectedTab = controller.getStateManager().getSelectedTab();
		recentFilesCopy = new ArrayList<String>(controller.getStateManager().getSettings().getRecentFiles());
	}

	@Override
	public void initialize() {
		getFrame().setTitle(TITLE);

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

		sequencePanel = new PanelSequence(getController());
		sequencePanel.initialize();
		addPanelToTabs(tabs,"Sequence",sequencePanel);
		
		mixPanel = new PanelMix(getController());
		mixPanel.initialize();
		addPanelToTabs(tabs,"Mix",mixPanel);
		
		settingsPanel = new PanelSettings(getController());
		settingsPanel.initialize();
		addPanelToTabs(tabs,"Settings",settingsPanel);
		
		getFrame().setContentPane(tabs);
		
		getFrame().pack();
		
		switchTo(selectedTab);
		
		tabs.addChangeListener(this);

		getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 100;
		int height = (int) screenSize.getHeight() - 100;
		getFrame().setSize(width,height);
		getFrame().setLocation(50,50);
	
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getSource()!=this && !evt.getSelectedTab().equals(selectedTab)) {
			switchTo(evt.getSelectedTab());
			if (evt.getSource()==getFrame().getJMenuBar()) {
				requestFocus();
			}
		}
		if (compositionChanged!=evt.isCompositionChanged()) {
			compositionChanged = evt.isCompositionChanged();
			resetTitle();
		}
		if (evt.getType().equals(StateChangeEvent.CHANGED_SETTINGS)) {
			String f = (new File(evt.getSettings().getWorkingCompositionFileName())).getName();
			if (!fileName.equals(f)) {
				fileName = f;
				resetTitle();
			}
			recentFilesCopy = new ArrayList<String>(evt.getSettings().getRecentFiles());
			updateRecentFiles();
		}
		tabs.setBackground(Instrument.getColorForInstrument(evt.getSelectedInstrument()));
	}
	
	protected void updateRecentFiles() {
		if (recentFilesCopy!=null) {
			if (recentFilesCopy.size()>0) {
				for (int i = 0; i < recentFileMenus.length; i++) {
					if (i < recentFilesCopy.size()) {
						File file = new File(recentFilesCopy.get(i));
						recentFileMenus[i].setText(file.getName());
						recentFileMenus[i].setActionCommand(LOAD_FILE_PREFIX + recentFilesCopy.get(i));
						recentFileMenus[i].setVisible(true);
					} else {
						recentFileMenus[i].setVisible(false);
					}
				}
				recentFileMenu.setVisible(true);
			} else {
				recentFileMenu.setVisible(false);
			}
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
		if (evt.getActionCommand().equals(DEMO)) {
			getController().newComposition(true);
		} else if (evt.getActionCommand().equals(LOAD)) {
			getController().loadComposition("");
		} else if (evt.getActionCommand().startsWith(LOAD_FILE_PREFIX)) {
			getController().loadComposition(evt.getActionCommand().substring(LOAD_FILE_PREFIX.length()));
		} else if (evt.getActionCommand().equals(SAVE)) {
			getController().saveComposition();
		} else if (evt.getActionCommand().equals(SAVE_AS)) {
			getController().saveCompositionAs();
		} else if (evt.getActionCommand().equals(NEW)) {
			getController().newComposition(false);
		} else if (evt.getActionCommand().equals(QUIT)) {
			getController().closeProgram();
		} else if (evt.getActionCommand().equals(TAB_COMPOSITION)) {
			getController().getStateManager().setSelectedTab(getFrame().getJMenuBar(),TAB_COMPOSITION);
		} else if (evt.getActionCommand().equals(TAB_INSTRUMENTS)) {
			getController().getStateManager().setSelectedTab(getFrame().getJMenuBar(),TAB_INSTRUMENTS);
		} else if (evt.getActionCommand().equals(TAB_PATTERNS)) {
			getController().getStateManager().setSelectedTab(getFrame().getJMenuBar(),TAB_PATTERNS);
		} else if (evt.getActionCommand().equals(TAB_SEQUENCE)) {
			getController().getStateManager().setSelectedTab(getFrame().getJMenuBar(),TAB_SEQUENCE);
		} else if (evt.getActionCommand().equals(TAB_MIX)) {
			getController().getStateManager().setSelectedTab(getFrame().getJMenuBar(),TAB_MIX);
		} else if (evt.getActionCommand().equals(TAB_SETTINGS)) {
			getController().getStateManager().setSelectedTab(getFrame().getJMenuBar(),TAB_SETTINGS);
		} else if (evt.getActionCommand().equals(PLAY_PATTERN)) {
			getController().startSequencer(true);
		} else if (evt.getActionCommand().equals(PLAY_SEQUENCE)) {
			getController().startSequencer(false);
		} else if (evt.getActionCommand().equals(CONTINUE_PLAYING)) {
			getController().startContinueSequencer();
		} else if (evt.getActionCommand().equals(STOP_PLAYING)) {
			getController().stopSequencer();
		} else if (evt.getActionCommand().equals(EDIT_UNDO)) {
			getController().getStateManager().undoCompositionChange(evt.getSource());
		} else if (evt.getActionCommand().equals(EDIT_REDO)) {
			getController().getStateManager().redoCompositionChange(evt.getSource());
		} else if (evt.getActionCommand().startsWith(PATTERN_PREFIX)) {
			if (selectedTab!=TAB_PATTERNS) {
				switchTo(TAB_PATTERNS);
				getController().getStateManager().setSelectedTab(this,selectedTab);
			}
			patternsPanel.actionPerformed(evt);
		} else if (evt.getActionCommand().equals(SOLO) || evt.getActionCommand().equals(UNMUTE)) {
			mixPanel.actionPerformed(evt);
		} else {
			for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
				if (evt.getActionCommand().equals(Instrument.INSTRUMENTS[i])) {
					getController().getStateManager().setSelectedInstrument(this,Instrument.INSTRUMENTS[i]);
					break;
				}
			}
		}
	}

	protected void resetTitle() {
		String title = TITLE;
		if (fileName.length()>0) {
			title = title + " - " + fileName;
		}
		if (compositionChanged) {
			title = title + "*";
		}
		getFrame().setTitle(title);
	}
	
	protected void switchTo(String tab) {
		selectedTab = tab;
		if (tab.equals(TAB_COMPOSITION)) {
			if (tabs.getSelectedIndex()!=0) {
				tabs.setSelectedIndex(0);
			}
		} else if (tab.equals(TAB_INSTRUMENTS)) {
			if (tabs.getSelectedIndex()!=1) {
				tabs.setSelectedIndex(1);
			}
		} else if (tab.equals(TAB_PATTERNS)) {
			if (tabs.getSelectedIndex()!=2) {
				tabs.setSelectedIndex(2);
			}
		} else if (tab.equals(TAB_SEQUENCE)) {
			if (tabs.getSelectedIndex()!=3) {
				tabs.setSelectedIndex(3);
			}
		} else if (tab.equals(TAB_MIX)) {
			if (tabs.getSelectedIndex()!=4) {
				tabs.setSelectedIndex(4);
			}
		} else if (tab.equals(TAB_SETTINGS)) {
			if (tabs.getSelectedIndex()!=5) {
				tabs.setSelectedIndex(5);
			}
		}
	}

	protected void requestFocus() {
		String tab = selectedTab;
		if (tab.equals(TAB_COMPOSITION)) {
			compositionPanel.requestFocus();
		} else if (tab.equals(TAB_INSTRUMENTS)) {
			instrumentsPanel.requestFocus();
		} else if (tab.equals(TAB_PATTERNS)) {
			patternsPanel.requestFocus();
		} else if (tab.equals(TAB_SEQUENCE)) {
			sequencePanel.requestFocus();
		} else if (tab.equals(TAB_MIX)) {
			mixPanel.requestFocus();
		} else if (tab.equals(TAB_SETTINGS)) {
			settingsPanel.requestFocus();
		}
	}

	protected void addPanelToTabs(JTabbedPane tabs,String label,PanelObject panel) {
		tabs.addTab(label,panel.getScroller());
	}
	
	protected String getSelectedTab() {
		String r = "";
		if (tabs.getSelectedIndex()==0) {
			r = TAB_COMPOSITION;
		} else if (tabs.getSelectedIndex()==1) {
			r = TAB_INSTRUMENTS;
		} else if (tabs.getSelectedIndex()==2) {
			r = TAB_PATTERNS;
		} else if (tabs.getSelectedIndex()==3) {
			r = TAB_SEQUENCE;
		} else if (tabs.getSelectedIndex()==4) {
			r = TAB_MIX;
		} else if (tabs.getSelectedIndex()==5) {
			r = TAB_SETTINGS;
		} else {
			r = TAB_COMPOSITION;
		}
		return r;
	}
	
	protected JMenuBar getMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		int evt = ActionEvent.CTRL_MASK;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		bar.add(fileMenu);

		JMenuItem item = new JMenuItem("Quit",KeyEvent.VK_Q);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,evt));
		item.setActionCommand(QUIT);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Load",KeyEvent.VK_L);
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
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,evt));
		item.setActionCommand(SAVE_AS);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("New",KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,evt));
		item.setActionCommand(NEW);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Demo",KeyEvent.VK_D);
		item.setActionCommand(DEMO);
		item.addActionListener(this);
		fileMenu.add(item);

		recentFileMenu = new JMenu("Recent");
		fileMenu.add(recentFileMenu);
		for (int i = 0; i < recentFileMenus.length; i++) {
			recentFileMenus[i] = new JMenuItem("");
			recentFileMenus[i].addActionListener(this);
			recentFileMenu.add(recentFileMenus[i]);
		}

		evt = 0;
		JMenu showMenu = new JMenu("Show");
		showMenu.setMnemonic(KeyEvent.VK_S);
		bar.add(showMenu);

		item = new JMenuItem("Composition",KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,evt));
		item.setActionCommand(TAB_COMPOSITION);
		item.addActionListener(this);
		showMenu.add(item);

		item = new JMenuItem("Instruments",KeyEvent.VK_I);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,evt));
		item.setActionCommand(TAB_INSTRUMENTS);
		item.addActionListener(this);
		showMenu.add(item);
		
		item = new JMenuItem("Patterns",KeyEvent.VK_P);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,evt));
		item.setActionCommand(TAB_PATTERNS);
		item.addActionListener(this);
		showMenu.add(item);
		
		item = new JMenuItem("Sequence",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,evt));
		item.setActionCommand(TAB_SEQUENCE);
		item.addActionListener(this);
		showMenu.add(item);

		item = new JMenuItem("Mix",KeyEvent.VK_M);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,evt));
		item.setActionCommand(TAB_MIX);
		item.addActionListener(this);
		showMenu.add(item);

		item = new JMenuItem("Settings",KeyEvent.VK_E);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12,evt));
		item.setActionCommand(TAB_SETTINGS);
		item.addActionListener(this);
		showMenu.add(item);

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
			item.setAccelerator(KeyStroke.getKeyStroke(ke,evt));
			item.setActionCommand(Instrument.INSTRUMENTS[i]);
			item.addActionListener(this);
			instMenu.add(item);
		}

		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		bar.add(editMenu);

		item = new JMenuItem("Undo",KeyEvent.VK_U);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,evt));
		item.setActionCommand(EDIT_UNDO);
		item.addActionListener(this);
		editMenu.add(item);
		
		item = new JMenuItem("Redo",KeyEvent.VK_R);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,evt));
		item.setActionCommand(EDIT_REDO);
		item.addActionListener(this);
		editMenu.add(item);
		
		JMenu editPatternMenu = new JMenu("Pattern");
		editPatternMenu.setMnemonic(KeyEvent.VK_P);
		editMenu.add(editPatternMenu);

		item = new JMenuItem("Select pattern",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,evt));
		item.setActionCommand(PATTERN_SELECT);
		item.addActionListener(this);
		editPatternMenu.add(item);

		item = new JMenuItem("Next pattern",KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,evt));
		item.setActionCommand(PATTERN_SELECT_NEXT);
		item.addActionListener(this);
		editPatternMenu.add(item);
		
		item = new JMenuItem("Previous pattern",KeyEvent.VK_R);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,evt));
		item.setActionCommand(PATTERN_SELECT_PREV);
		item.addActionListener(this);
		editPatternMenu.add(item);
		
		item = new JMenuItem("Toggle edit mode",KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,evt));
		item.setActionCommand(PATTERN_EDIT_MODE);
		item.addActionListener(this);
		editPatternMenu.add(item);

		item = new JMenuItem("Toggle insert",KeyEvent.VK_I);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,evt));
		item.setActionCommand(PATTERN_INSERT);
		item.addActionListener(this);
		editPatternMenu.add(item);

		item = new JMenuItem("Edit pattern bars",KeyEvent.VK_B);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,evt));
		item.setActionCommand(PATTERN_BARS);
		item.addActionListener(this);
		editPatternMenu.add(item);

		item = new JMenuItem("Edit pattern notes",KeyEvent.VK_E);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,evt));
		item.setActionCommand(PATTERN_EDIT);
		item.addActionListener(this);
		editPatternMenu.add(item);

		evt = 0;
		JMenu sequencerMenu = new JMenu("Sequencer");
		sequencerMenu.setMnemonic(KeyEvent.VK_S);
		bar.add(sequencerMenu);
		
		item = new JMenuItem("Play pattern",KeyEvent.VK_P);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,evt));
		item.setActionCommand(PLAY_PATTERN);
		item.addActionListener(this);
		sequencerMenu.add(item);

		item = new JMenuItem("Play sequence",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,evt));
		item.setActionCommand(PLAY_SEQUENCE);
		item.addActionListener(this);
		sequencerMenu.add(item);

		item = new JMenuItem("Continue",KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,evt));
		item.setActionCommand(CONTINUE_PLAYING);
		item.addActionListener(this);
		sequencerMenu.add(item);

		item = new JMenuItem("Pauze/Stop",KeyEvent.VK_A);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8,evt));
		item.setActionCommand(STOP_PLAYING);
		item.addActionListener(this);
		sequencerMenu.add(item);

		evt = ActionEvent.CTRL_MASK;
		JMenu mixerMenu = new JMenu("Mixer");
		mixerMenu.setMnemonic(KeyEvent.VK_M);
		bar.add(mixerMenu);

		item = new JMenuItem("Solo",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,evt));
		item.setActionCommand(SOLO);
		item.addActionListener(this);
		mixerMenu.add(item);

		item = new JMenuItem("Unmute all",KeyEvent.VK_U);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,evt));
		item.setActionCommand(UNMUTE);
		item.addActionListener(this);
		mixerMenu.add(item);
		
		updateRecentFiles();
		
		return bar;
	}
}
