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
import nl.zeesoft.zmmt.syntesizer.SynthesizerInstrument;

public class Controller extends Locker implements ActionListener {
	private boolean					exitOnClose		= true;
	private WorkerUnion				union			= null;
	private Composition				composition		= null;
	private ControllerWindowAdapter	adapter			= null;
	private ControllerKeyListener	keyListener		= null;
	private FrameMain				mainFrame		= null;
	
	private Synthesizer				synth			= null;
	private InstrumentPlayer		player			= null;
	
	public Controller() {
		super(new Messenger(null));
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

		initializeSynthesizer();
		initializePlayer();
		
		setComposition(new Composition());

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
		getMessenger().setPrintDebugMessages(debug);
		getMessenger().start();
		mainFrame.getFrame().setVisible(true);
	}

	public void stop(Worker ignoreWorker) {
		mainFrame.getFrame().setVisible(false);
		getMessenger().stop();
		union.stopWorkers(ignoreWorker);
		getMessenger().whileWorking();
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

	protected Composition getComposition() {
		return composition;
	}

	protected void setComposition(Composition composition) {
		this.composition = composition;
		updatedCompositionSynthesizer();
	}
	
	protected void initializeSynthesizer() {
		lockMe(this);
		synth = null;
		try {
			synth = MidiSystem.getSynthesizer();
			if (synth!=null) {
				synth.open();
			}
		} catch (MidiUnavailableException e) {
			getMessenger().error(this,"Failed to initialize MIDI syntesizer",e);
		}
		unlockMe(this);
	}

	protected void updatedCompositionSynthesizer() {
		lockMe(this);
		if (synth!=null) {
			List<SynthesizerInstrument> instruments = composition.getSynthesizer().getInstruments();
			for (SynthesizerInstrument inst: instruments) {
				if (player!=null) {
					player.stopInstrumentNotes(inst.getInstrument());
				}
			}
			composition.getSynthesizer().configureMidiSynthesizer(synth);
		}
		unlockMe(this);
	}

	protected void initializePlayer() {
		lockMe(this);
		player = null;
		if (synth!=null) {
			player = new InstrumentPlayer(synth);
		}
		unlockMe(this);
	}

	protected JComboBox<String> getNewInstrumentSelector() {
		JComboBox<String> r = null;
		lockMe(this);
		if (player!=null) {
			r = player.getNewSelector();
			r.addKeyListener(keyListener);
		}
		unlockMe(this);
		return r;
	}

	protected void playNote(int note,boolean accent) {
		lockMe(this);
		if (player!=null) {
			int playNote = composition.getSynthesizer().getMidiNoteNumberForNote(player.getSelectedInstrument(),note);
			if (playNote>=0) {
				int velocity = composition.getSynthesizer().getVelocityForNote(player.getSelectedInstrument(),note,accent);
				if (velocity>=0) {
					player.playInstrumentNote(playNote,velocity);
				}
			}
		}
		unlockMe(this);
	}

	protected void stopNote(int note) {
		lockMe(this);
		if (player!=null) {
			int playNote = composition.getSynthesizer().getMidiNoteNumberForNote(player.getSelectedInstrument(),note);
			if (playNote>=0) {
				player.stopInstrumentNote(playNote);
			}
		}
		unlockMe(this);
	}
}
