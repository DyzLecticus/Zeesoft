package nl.zeesoft.zmmt.gui;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.syntesizer.InstrumentConfiguration;

public class Controller extends Locker {
	private Settings					settings			= null;
	
	private boolean						exitOnClose					= true;
	private WorkerUnion					union						= null;
	private ControllerWindowAdapter		adapter						= null;
	private ControllerKeyListener		keyListener					= null;
	private FrameMain					mainFrame					= null;
	private InstrumentPlayer			player						= null;
	private InstrumentPlayerKeyListener	playerKeyListener			= null;
	
	private Composition					composition					= null;
	private List<String>				updatedCompositionTabs		= new ArrayList<String>();
	private CompositionUpdateWorker		compositionUpdateWorker		= null;
	private boolean						compositionChanged			= false;
	
	private Synthesizer					synth						= null;

	public Controller(Settings settings) {
		super(new Messenger(null));
		this.settings = settings;
	}

	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			// Ignore
		}
		union = new WorkerUnion(getMessenger());
		adapter = new ControllerWindowAdapter(this);
		keyListener = new ControllerKeyListener(this);
		player = new InstrumentPlayer();
		playerKeyListener = new InstrumentPlayerKeyListener(this);
		mainFrame = new FrameMain(this);
		mainFrame.initialize();
		compositionUpdateWorker = new CompositionUpdateWorker(getMessenger(),getUnion(),this);
	}
	
	public void setExitOnClose(boolean exitOnClose) {
		this.exitOnClose = exitOnClose;
	}

	public void start() {
		start(false);
	}
	
	public void start(boolean debug) {
		setComposition(settings.getNewComposition());
		mainFrame.getFrame().setVisible(true);
		compositionUpdateWorker.start();
		InitializeSynthesizerWorker synthInit = new InitializeSynthesizerWorker(getMessenger(),getUnion(),this);
		synthInit.start();
		getMessenger().setPrintDebugMessages(debug);
		getMessenger().start();
		mainFrame.getFrame().setVisible(true);
	}

	public void stop(Worker ignoreWorker) {
		stopSynthesizer();
		mainFrame.getFrame().setVisible(false);
		compositionUpdateWorker.stop();
		getMessenger().stop();
		union.stopWorkers(ignoreWorker);
		getMessenger().whileWorking();
		lockMe(this);
		if (synth!=null) {
			synth.close();
		}
		lockMe(this);
	}

	public void windowClosing(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			stop(null);
			if (exitOnClose) {
				int status = 0;
				if (getMessenger().isError()) {
					status = 1;
				}
				System.exit(status);
			}
		}
	}
	
	public void windowIconified(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			stopSynthesizer();
		}
	}
	
	public void windowStateChanged(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame() && e.getID()==WindowEvent.WINDOW_LOST_FOCUS) {
			stopSynthesizer();
		}
	}

	public void showErrorMessage(Object source,String message) {
		showErrorMessage(source,message,null);
	}
	
	public void showErrorMessage(Object source,String message,Exception e) {
		if (mainFrame!=null) {
			JOptionPane.showMessageDialog(mainFrame.getFrame(),message,"Error",JOptionPane.ERROR_MESSAGE);
			if (e!=null) {
				getMessenger().error(source,message,e);
			} else {
				getMessenger().error(source,message);
			}
		}
	}
	
	public boolean showConfirmMessage(String message) {
		boolean r = false;
		if (mainFrame!=null) {
			r = (JOptionPane.showConfirmDialog(mainFrame.getFrame(),message,"Are you sure?",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
		}
		return r;
	}
	
	public void switchTo(String tab) {
		mainFrame.switchTo(tab);
	}
	
	public WorkerUnion getUnion() {
		return union;
	}
	
	public Settings getSettings() {
		return settings;
	}

	public ControllerWindowAdapter getAdapter() {
		return adapter;
	}

	public ControllerKeyListener getKeyListener() {
		return keyListener;
	}

	public InstrumentPlayerKeyListener getPlayerKeyListener() {
		return playerKeyListener;
	}
	
	protected Composition getComposition() {
		return composition;
	}

	protected void setComposition(Composition composition) {
		if (composition!=null) {
			stopSynthesizer();
			this.composition = composition;
			reconfigureSynthesizer();
			mainFrame.updatedComposition(null,composition);
		}
	}
	
	protected void changedComposition(String tab) {
		lockMe(this);
		if (compositionUpdateWorker.isWorking() && !updatedCompositionTabs.contains(tab)) {
			updatedCompositionTabs.add(tab);
		}
		unlockMe(this);
	}

	protected void handleCompositionUpdates() {
		boolean reconfigure = false;
		lockMe(this);
		for (String tab: updatedCompositionTabs) {
			mainFrame.getCompositionUpdate(tab,composition);
			mainFrame.updatedComposition(tab,composition);
			if (tab.equals(FrameMain.INSTRUMENTS)) {
				reconfigure = true;
			}
			compositionChanged = true;
			mainFrame.setCompositionChanged(compositionChanged);
		}
		updatedCompositionTabs.clear();
		unlockMe(this);
		if (reconfigure) {
			stopSynthesizer();
			reconfigureSynthesizer();
		}
	}

	protected void setSynthesizer(Synthesizer synth) {
		lockMe(this);
		this.synth = synth;
		player.setSynthesizer(synth);
		unlockMe(this);
		reconfigureSynthesizer();
	}
	
	protected void stopSynthesizer() {
		lockMe(this);
		if (synth!=null) {
			List<InstrumentConfiguration> instruments = composition.getSynthesizerConfiguration().getInstruments();
			for (InstrumentConfiguration inst: instruments) {
				player.stopInstrumentNotes(inst.getInstrument());
			}
		}
		unlockMe(this);
	}

	protected void reconfigureSynthesizer() {
		lockMe(this);
		if (synth!=null) {
			composition.getSynthesizerConfiguration().configureMidiSynthesizer(synth);
		}
		unlockMe(this);
	}

	protected JComboBox<String> getNewInstrumentSelector() {
		JComboBox<String> r = null;
		lockMe(this);
		r = player.getNewSelector();
		r.addKeyListener(playerKeyListener);
		unlockMe(this);
		return r;
	}

	protected void playNote(int note,boolean accent) {
		lockMe(this);
		int playNote = composition.getSynthesizerConfiguration().getMidiNoteNumberForNote(player.getSelectedInstrument(),note);
		if (playNote>=0) {
			int velocity = composition.getSynthesizerConfiguration().getVelocityForNote(player.getSelectedInstrument(),note,accent);
			if (velocity>=0) {
				player.playInstrumentNote(playNote,velocity);
			}
		}
		unlockMe(this);
	}

	protected void stopNote(int note) {
		lockMe(this);
		int playNote = composition.getSynthesizerConfiguration().getMidiNoteNumberForNote(player.getSelectedInstrument(),note);
		if (playNote>=0) {
			player.stopInstrumentNote(playNote);
		}
		unlockMe(this);
	}
}
