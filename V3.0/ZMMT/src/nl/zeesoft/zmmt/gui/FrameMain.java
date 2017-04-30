package nl.zeesoft.zmmt.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import nl.zeesoft.zmmt.composition.Composition;

public class FrameMain extends FrameObject implements CompositionUpdater {
	private static final String	TITLE				= "ZeeTracker";
	
	public static final String	COMPOSITION			= "Composition";
	public static final String	INSTRUMENTS			= "Instruments";
	
	private JTabbedPane			tabs				= null;

	private PanelComposition	compositionPanel	= null;
	private PanelInstruments	instrumentsPanel	= null;
	
	public FrameMain(Controller controller) {
		super(controller);
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
		getFrame().addKeyListener(getController().getPlayerKeyListener());

		tabs = new JTabbedPane();
		tabs.addKeyListener(getController().getPlayerKeyListener());

		compositionPanel = new PanelComposition(getController());
		compositionPanel.initialize();
		addPanelToTabs(tabs,"Composition",compositionPanel.getPanel());
		//tabs.setMnemonicAt(0,KeyEvent.VK_F1);

		instrumentsPanel = new PanelInstruments(getController());
		instrumentsPanel.initialize();
		addPanelToTabs(tabs,"Instruments",instrumentsPanel.getPanel());
		//tabs.setMnemonicAt(1,KeyEvent.VK_F2);
		
		getFrame().setContentPane(tabs);
	}
	
	@Override
	public void updatedComposition(String tab,Composition comp) {
		if (tab==null || tab.length()==0 || !tab.equals(COMPOSITION)) {
			compositionPanel.updatedComposition(tab,comp);
		} 
		if (tab==null || tab.length()==0 || !tab.equals(INSTRUMENTS)) {
			instrumentsPanel.updatedComposition(tab,comp);
		} 
	}

	@Override
	public void getCompositionUpdate(String tab, Composition comp) {
		if (tab.equals(COMPOSITION)) {
			compositionPanel.getCompositionUpdate(tab, comp);
		} else if (tab.equals(INSTRUMENTS)) {
			instrumentsPanel.getCompositionUpdate(tab, comp);
		}
	}
	
	protected JScrollPane addPanelToTabs(JTabbedPane tabs,String label,JPanel panel) {
		JScrollPane scroller = new JScrollPane(panel);
		scroller.getVerticalScrollBar().setUnitIncrement(20);
		tabs.addTab(label,scroller);
		return scroller;
	}
	
	protected void switchTo(String tab) {
		if (tab.equals(COMPOSITION) && tabs.getSelectedIndex()!=0) {
			tabs.setSelectedIndex(0);
			compositionPanel.requestFocus();
		} else if (tab.equals(INSTRUMENTS) && tabs.getSelectedIndex()!=1) {
			tabs.setSelectedIndex(1);
			instrumentsPanel.requestFocus();
		}
	}
	
	protected void setCompositionChanged(boolean changed) {
		if (changed) {
			getFrame().setTitle(TITLE + "*");
		} else {
			getFrame().setTitle(TITLE);
		}
	}
}
