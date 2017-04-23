package nl.zeesoft.zso.orchestra.controller;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import nl.zeesoft.zjmo.orchestra.Orchestra;
import nl.zeesoft.zjmo.orchestra.client.ActiveClient;
import nl.zeesoft.zjmo.orchestra.controller.MainFrame;
import nl.zeesoft.zjmo.orchestra.controller.OrchestraController;
import nl.zeesoft.zso.composition.Composition;
import nl.zeesoft.zso.composition.sequencer.Sequencer;

public class SampleOrchestraController extends OrchestraController {
	private Sequencer		sequencer		= null;
	private JMenuItem		startMenuItem	= null;
	private JMenuItem		stopMenuItem	= null;
	
	public SampleOrchestraController(Orchestra orchestra, boolean exitOnClose) {
		super(orchestra, exitOnClose);
		sequencer = new Sequencer(getMessenger(),getUnion(),orchestra.getCopy(false),this);
	}

	@Override
	public void stop() {
		sequencer.stop();
		super.stop();
	}

	@Override
	protected JMenuBar getMainMenuBar(MainFrame mainFrame) {
		JMenuBar bar = super.getMainMenuBar(mainFrame);
		JMenu sequencerMenu = new JMenu("Sequencer");

		JMenuItem item = null;
		
		item = new JMenuItem("Load composition");
		item.setActionCommand(Sequencer.LOAD_COMPOSITION);
		item.addActionListener(sequencer);
		sequencerMenu.add(item);

		startMenuItem = new JMenuItem("Start");
		startMenuItem.setActionCommand(Sequencer.START);
		startMenuItem.addActionListener(sequencer);
		startMenuItem.setEnabled(false);
		sequencerMenu.add(startMenuItem);

		stopMenuItem = new JMenuItem("Stop");
		stopMenuItem.setActionCommand(Sequencer.STOP);
		stopMenuItem.addActionListener(sequencer);
		stopMenuItem.setEnabled(false);
		sequencerMenu.add(stopMenuItem);
		
		bar.add(sequencerMenu);
		return bar;
	}
	
	public void setComposition(Composition composition) {
		sequencer.setComposition(composition);
		resetStartStopEnabled();
	}

	@Override
	protected void setConnected(boolean connected, ActiveClient client) {
		super.setConnected(connected, client);
		resetStartStopEnabled();
	}
	
	protected void resetStartStopEnabled() {
		boolean enabled = sequencer.getComposition()!=null && isConnected();
		stopMenuItem.setEnabled(enabled);
		startMenuItem.setEnabled(enabled);
	}
}
