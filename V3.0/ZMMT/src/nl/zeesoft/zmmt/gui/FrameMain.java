package nl.zeesoft.zmmt.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class FrameMain extends FrameObject {
	private PanelComposition	compositionPanel	= null;
	private PanelInstruments	instrumentsPanel	= null;
	
	public FrameMain(ControllerWindowAdapter adapter,ControllerKeyListener keyListener) {
		super(adapter,keyListener);
	}

	@Override
	public void initialize(Controller controller) {
		getFrame().setTitle("Zeesoft MIDI Mod Tracker");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 100;
		int height = (int) screenSize.getHeight() - 100;
		getFrame().setSize(width,height);
		getFrame().setLocation(50,50);
		getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
	
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().addWindowListener(getAdapter());
		getFrame().addKeyListener(getKeyListener());

		JTabbedPane tabs = new JTabbedPane();
		tabs.addKeyListener(getKeyListener());

		compositionPanel = new PanelComposition();
		compositionPanel.initialize(controller,getKeyListener());
		addPanelToTabs(tabs,"Composition",compositionPanel.getPanel());

		instrumentsPanel = new PanelInstruments();
		instrumentsPanel.initialize(controller,getKeyListener());
		addPanelToTabs(tabs,"Instruments",instrumentsPanel.getPanel());
		
		getFrame().setContentPane(tabs);
	}
	
	protected void addPanelToTabs(JTabbedPane tabs,String label,JPanel panel) {
		JScrollPane scroller = new JScrollPane(panel);
		scroller.getVerticalScrollBar().setUnitIncrement(20);
		tabs.addTab(label,scroller);
	}
}
