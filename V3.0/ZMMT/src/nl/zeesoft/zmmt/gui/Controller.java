package nl.zeesoft.zmmt.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.UIManager;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.syntesizer.InstrumentConfiguration;

public class Controller extends Locker implements ActionListener {
	private Settings				settings		= null;
	
	private boolean					exitOnClose		= true;
	private WorkerUnion				union			= null;
	private Composition				composition		= null;
	private ControllerWindowAdapter	adapter			= null;
	private ControllerKeyListener	keyListener		= null;
	private FrameMain				mainFrame		= null;
	
	private Synthesizer				synth			= null;
	private InstrumentPlayer		player			= null;
	
	public Controller(Settings settings) {
		super(new Messenger(null));
		this.settings = settings;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
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
		mainFrame = new FrameMain(adapter,keyListener);
		mainFrame.initialize(this);
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
		initializeSynthesizer();
		getMessenger().setPrintDebugMessages(debug);
		getMessenger().start();
		mainFrame.getFrame().setVisible(true);
	}

	public void stop(Worker ignoreWorker) {
		stopSynthesizer();
		mainFrame.getFrame().setVisible(false);
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
		// Ignore
	}
	
	public void windowStateChanged(WindowEvent e) {
		// Ignore
	}

	public WorkerUnion getUnion() {
		return union;
	}

	protected Settings getSettings() {
		return settings;
	}

	protected Composition getComposition() {
		return composition;
	}

	protected void setComposition(Composition composition) {
		if (composition!=null) {
			stopSynthesizer();
			this.composition = composition;
			reconfigureSynthesizer();
		}
	}
	
	protected void initializeSynthesizer() {
		lockMe(this);
		synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
			if (synth!=null) {
				synth.open();
				if (synth.isOpen()) {
					player.setSynthesizer(synth);
				}
			}
		} catch (MidiUnavailableException e) {
			getMessenger().error(this,"Failed to initialize MIDI syntesizer",e);
		}
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
		r.addKeyListener(keyListener);
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
