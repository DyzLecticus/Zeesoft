package nl.zeesoft.zmmt.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class FrameMain extends FrameObject {
	private PanelComposition	compositionPanel	= null;
	private PanelInstruments	instrumentsPanel	= null;
	
	public FrameMain(ControllerWindowAdapter adapter) {
		super(adapter);
	}

	@Override
	public void initialize() {
		getFrame().setTitle("Zeesoft MIDI Mod Tracker");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 100;
		int height = (int) screenSize.getHeight() - 100;
		getFrame().setSize(width,height);
		getFrame().setLocation(50,50);
		getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
	
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().addWindowListener(getAdapter());

		JTabbedPane tabbedPane = new JTabbedPane();

		compositionPanel = new PanelComposition();
		compositionPanel.initialize();
		tabbedPane.addTab("Composition",new JScrollPane(compositionPanel.getPanel()));

		instrumentsPanel = new PanelInstruments();
		instrumentsPanel.initialize();
		tabbedPane.addTab("Instruments",instrumentsPanel.getPanel());
		
		getFrame().setContentPane(tabbedPane);
	}
}
