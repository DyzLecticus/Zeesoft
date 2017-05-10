package nl.zeesoft.zmmt.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.zeesoft.zmmt.gui.panel.PanelComposition;
import nl.zeesoft.zmmt.gui.panel.PanelInstruments;
import nl.zeesoft.zmmt.gui.panel.PanelObject;
import nl.zeesoft.zmmt.gui.panel.PanelPatterns;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.syntesizer.Instrument;

public class FrameMain extends FrameObject implements ChangeListener, StateChangeSubscriber {
	private static final String	TITLE				= "ZeeTracker";
	
	public static final String	COMPOSITION			= "Composition";
	public static final String	INSTRUMENTS			= "Instruments";
	public static final String	PATTERNS			= "Patterns";
	
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
		if (evt.getSource()!=this) {
			if (evt.getType().equals(StateChangeEvent.SELECTED_TAB)) {
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
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (!getSelectedTab().equals(selectedTab)) {
			selectedTab = getSelectedTab();
			getController().getStateManager().setSelectedTab(this,selectedTab);
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
}
