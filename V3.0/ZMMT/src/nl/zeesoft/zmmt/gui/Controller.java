package nl.zeesoft.zmmt.gui;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.gui.panel.PanelInstruments;
import nl.zeesoft.zmmt.gui.panel.PatternGridKeyListener;
import nl.zeesoft.zmmt.gui.panel.SequenceGridKeyListener;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.gui.state.StateManager;
import nl.zeesoft.zmmt.player.InstrumentPlayer;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;
import nl.zeesoft.zmmt.sequencer.SequencePlayer;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;
import nl.zeesoft.zmmt.sequencer.SequencePlayerUpdateWorker;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.synthesizer.MidiNote;

public class Controller extends Locker implements StateChangeSubscriber {
	private Settings					settings					= null;
	
	private WorkerUnion					union						= null;
	private StateManager				stateManager				= null;

	private ControllerWindowAdapter		adapter						= null;
	private FrameMain					mainFrame					= null;
	private WindowBusy					busyWindow					= null;
	
	private InstrumentPlayer			player						= null;
	private InstrumentPlayerKeyListener	playerKeyListener			= null;
	private PatternGridKeyListener		patternKeyListener			= null;
	private SequenceGridKeyListener		sequenceKeyListener			= null;
	
	private File						compositionFile				= null;
	
	private ImportExportWorker			importExportWorker			= null;
	
	private Synthesizer					synthesizer					= null;

	private SequencePlayer				sequencePlayer				= null;
	private SequencePlayerUpdateWorker	sequencePlayerWorker 		= null;
	private Sequencer					sequencer					= null;
	private List<MetaEventListener>		sequencerMetaListeners		= new ArrayList<MetaEventListener>();
	
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
		settings.fromFile();
		if (settings.getCustomFontName().length()>0 || settings.getCustomFontSize()>0) {
			setFont(settings.getCustomFontName(),settings.getCustomFontSize());
		}

		union = new WorkerUnion(getMessenger());
		stateManager = new StateManager(getMessenger(),getUnion(),settings);
		if (settings.getWorkingTab().length()>0) {
			stateManager.setSelectedTab(this,settings.getWorkingTab());
		}
		if (settings.getWorkingInstrument().length()>0) {
			stateManager.setSelectedInstrument(this,settings.getWorkingInstrument());
		}
		if (settings.getWorkingCompositionPattern()>0) {
			stateManager.setSelectedPattern(this,settings.getWorkingCompositionPattern());
		}
		stateManager.addSubscriber(this);

		adapter = new ControllerWindowAdapter(this);
		player = new InstrumentPlayer(getMessenger(),getUnion());
		playerKeyListener = new InstrumentPlayerKeyListener(this,settings.getKeyCodeNoteNumbers());
		patternKeyListener = new PatternGridKeyListener(this,settings.getKeyCodeNoteNumbers());
		sequenceKeyListener = new SequenceGridKeyListener(this,settings.getKeyCodeNoteNumbers());

		sequencePlayer = new SequencePlayer(getMessenger(),getUnion());
		stateManager.addSubscriber(sequencePlayer);
		sequencePlayerWorker = new SequencePlayerUpdateWorker(getMessenger(),getUnion(),sequencePlayer);
		
		mainFrame = new FrameMain(this);
		mainFrame.initialize();
		busyWindow = new WindowBusy(getMessenger(),mainFrame.getFrame());
		
		importExportWorker = new ImportExportWorker(getMessenger(),getUnion(),this);
	}

	public void start() {
		start(false);
	}
	
	public void start(boolean debug) {
		String err = "";

		lockMe(this);
		Composition comp = settings.getNewComposition(false);
		unlockMe(this);
		setComposition(comp);
		
		boolean loading = false;
		String workDirName = "";
		
		lockMe(this);
		workDirName = settings.getWorkDirName();
		if (settings.getWorkingCompositionFileName().length()>0) {
			File file = new File(settings.getWorkingCompositionFileName());
			if (!file.exists()) {
				err = "Working composition file not found: " + settings.getWorkingCompositionFileName();
			} else {
				importExportWorker.loadCompositionAndInitialize(file,settings.getWorkDirName());
				loading = true;
			}
		}
		unlockMe(this);

		mainFrame.getFrame().setVisible(true);

		if (!loading) {
			importExportWorker.initialize(workDirName);
		}
		
		player.start();
		sequencePlayerWorker.start();
		
		stateManager.start();
		
		InitializeMidiDevicesWorker midi = new InitializeMidiDevicesWorker(getMessenger(),getUnion(),this,settings.getCustomSoundFontFileName());
		midi.start();
		getMessenger().setPrintDebugMessages(debug);
		getMessenger().start();
		
		if (err.length()>0) {
			showErrorMessage(this,err);
		}
	}

	public void stop(Worker ignoreWorker) {
		importExportWorker.stop();
		player.stop();
		sequencePlayerWorker.stop();
		stopSequencer();
		stopSynthesizer(stateManager.getComposition());
		mainFrame.getFrame().setVisible(false);
		
		stateManager.stop();
		
		getMessenger().stop();
		union.stopWorkers(ignoreWorker);
		getMessenger().whileWorking();
		lockMe(this);
		if (synthesizer!=null) {
			synthesizer.close();
		}
		if (sequencer!=null) {
			sequencer.close();
		}
		unlockMe(this);
	}

	public void windowClosing(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			boolean confirmed = true;
			if (stateManager.isCompositionChanged()) {
				confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to quit?");
			}
			if (confirmed) {
				lockMe(this);
				settings.setWorkingTab(stateManager.getSelectedTab());
				settings.setWorkingInstrument(stateManager.getSelectedInstrument());
				if (settings.getWorkingCompositionFileName().length()>0) {
					settings.setWorkingCompositionPattern(stateManager.getSelectedPattern());
				} else {
					settings.setWorkingCompositionPattern(0);
				}
				String err = settings.toFile();
				if (err.length()>0) {
					showErrorMessage(settings,err);
				}
				unlockMe(this);
				stop(null);
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
			stopSynthesizer(stateManager.getComposition());
		}
	}
	
	public void windowStateChanged(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame() && e.getID()==WindowEvent.WINDOW_LOST_FOCUS) {
			stopSynthesizer(stateManager.getComposition());
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
	
	protected File chooseFile(JFileChooser fileChooser,String title) {
		File file = null;
		int choice = fileChooser.showDialog(mainFrame.getFrame(),title);
		if (choice==JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		return file;
	}

	public void setBusy(Object source,String busy,String details) {
		busyWindow.setBusy(source,busy,details);
		if (mainFrame!=null) {
			mainFrame.getFrame().setEnabled(false);
		}
	}

	public void setDone(Object source) {
		if (busyWindow.setDone(source)==0) {
			if (mainFrame!=null) {
				mainFrame.getFrame().setEnabled(true);
				mainFrame.switchTo(stateManager.getSelectedTab());
			}
		}
	}

	public WorkerUnion getUnion() {
		return union;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}

	public ControllerWindowAdapter getAdapter() {
		return adapter;
	}

	public InstrumentPlayerKeyListener getPlayerKeyListener() {
		return playerKeyListener;
	}

	public PatternGridKeyListener getPatternKeyListener() {
		return patternKeyListener;
	}

	public SequenceGridKeyListener getSequenceKeyListener() {
		return sequenceKeyListener;
	}

	protected void setComposition(Composition composition) {
		if (composition!=null) {
			stopSynthesizer(stateManager.getComposition());
			stateManager.setCompositionChanged(this,false);
			stateManager.setComposition(this,composition);
			reconfigureSynthesizer(composition);
		}
	}
	
	protected void setSynthesizer(Synthesizer synth) {
		lockMe(this);
		this.synthesizer = synth;
		player.setSynthesizer(synth);
		unlockMe(this);
		reconfigureSynthesizer(stateManager.getComposition());
	}

	protected void stopSynthesizer(Composition composition) {
		lockMe(this);
		if (synthesizer!=null) {
			List<InstrumentConfiguration> instruments = composition.getSynthesizerConfiguration().getInstruments();
			for (InstrumentConfiguration inst: instruments) {
				player.stopInstrumentNotes(inst.getName());
			}
		}
		unlockMe(this);
	}

	protected void reconfigureSynthesizer(Composition composition) {
		lockMe(this);
		if (synthesizer!=null) {
			composition.getSynthesizerConfiguration().configureMidiSynthesizer(synthesizer);
		}
		unlockMe(this);
	}

	public void playNote(int note,boolean accent) {
		List<MidiNote> notes = getNotes(note,accent);
		lockMe(this);
		player.playInstrumentNotes(notes);
		unlockMe(this);
	}

	public void stopNote(int note) {
		List<MidiNote> notes = getNotes(note,false);
		lockMe(this);
		player.stopInstrumentNotes(notes);
		unlockMe(this);
	}

	public void stopNotes() {
		lockMe(this);
		player.stopInstrumentNotes(stateManager.getSelectedInstrument());
		unlockMe(this);
	}

	public void addSequencerMetaListener(MetaEventListener listener) {
		sequencerMetaListeners.add(listener);
	}
	
	public void addSequencerSubscriber(SequencePlayerSubscriber sub) {
		sequencePlayer.addSequencerSubscriber(sub);
	}
	
	protected void setSequencer(Sequencer seq) {
		lockMe(this);
		this.sequencer = seq;
		for (MetaEventListener listener: sequencerMetaListeners) {
			sequencer.addMetaEventListener(listener);
		}
		sequencePlayer.setSequencer(sequencer);
		unlockMe(this);
		reconfigureSynthesizer(stateManager.getComposition());
	}

	public void startSequencer(boolean patternMode) {
		sequencePlayer.setPatternMode(patternMode);
		sequencePlayer.start();
	}

	public void stopSequencer() {
		sequencePlayer.stop();
	}

	public void startContinueSequencer() {
		sequencePlayer.startContinue();
	}

	protected void loadComposition() {
		if (importExportWorker.isWorking()) {
			showErrorMessage(this,"Import/export worker is busy");
		} else {
			boolean confirmed = true;
			if (stateManager.isCompositionChanged()) {
				confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to load a different composition?");
			}
			if (confirmed) {
				importExportWorker.loadComposition();
			}
		}
	}

	protected void saveComposition() {
		if (stateManager.isCompositionChanged()) {
			if (importExportWorker.isWorking()) {
				showErrorMessage(this,"Import/export worker is busy");
			} else {
				Composition copy = stateManager.getComposition().copy();
				File file = compositionFile;
				importExportWorker.saveComposition(copy,file);
			}
		}
	}

	protected void saveCompositionAs() {
		if (importExportWorker.isWorking()) {
			showErrorMessage(this,"Import/export worker is busy");
		} else {
			Composition copy = stateManager.getComposition().copy();
			importExportWorker.saveComposition(copy,null);
		}
	}
	
	protected void newComposition(boolean demo) {
		boolean confirmed = true;
		if (stateManager.isCompositionChanged()) {
			confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to create a new composition?");
		}
		if (confirmed) {
			Settings settingsCopy = null;
			lockMe(this);
			settings.setWorkingCompositionFileName("");
			settings.setWorkingCompositionPattern(0);
			settingsCopy = settings.copy();
			compositionFile = null;
			unlockMe(this);
			stopSequencer();
			if (demo) {
				if (importExportWorker.isWorking()) {
					showErrorMessage(this,"Import/export worker is busy");
				} else {
					importExportWorker.loadDemoComposition(settingsCopy);
				}
			} else {
				stateManager.setSelectedTab(this,FrameMain.TAB_COMPOSITION);
				stateManager.setSettings(this,settingsCopy.copy());
				setComposition(settingsCopy.getNewComposition(demo));
			}
		}
	}

	protected void loadedComposition(File file,Composition comp) {
		Settings settingsCopy = null;
		lockMe(this);
		if (file!=null) {
			settings.setWorkingCompositionFileName(file.getAbsolutePath());
			compositionFile = file;
		} else {
			settings.setWorkingCompositionFileName("");
			compositionFile = null;
		}
		settingsCopy = settings.copy();
		unlockMe(this);
		stateManager.setSettings(this,settingsCopy);
		stopSequencer();
		setComposition(comp);
	}

	protected void savedComposition(File file,Composition comp) {
		Settings settingsCopy = null;
		lockMe(this);
		settings.setWorkingCompositionFileName(file.getAbsolutePath());
		compositionFile = file;
		settingsCopy = settings.copy();
		unlockMe(this);
		stateManager.setSettings(this,settingsCopy);
		setComposition(comp);
	}
	
	protected void setFont(String name, int size) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				FontUIResource cfr = (FontUIResource) value;
				String fn = name;
				int s = size;
				int type = Font.PLAIN;

				if (fn.length()==0) {
					fn = cfr.getName();
				}
				if (cfr.isBold()) {
					type = Font.BOLD;
				} else if (cfr.isItalic()) {
					type = Font.ITALIC;
				}
				if (s<=0) {
					s = cfr.getSize();
				}
				FontUIResource fr = new FontUIResource(new Font(fn,type,s));
				UIManager.put(key, fr);
			}
		}
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getSource()!=this) {
			if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
				if (evt.getSource() instanceof PanelInstruments) {
					stopSynthesizer(evt.getComposition());
					reconfigureSynthesizer(evt.getComposition());
				}
			} else if (evt.getType().equals(StateChangeEvent.CHANGED_SETTINGS)) {
				lockMe(this);
				settings = evt.getSettings().copy();
				unlockMe(this);
			} else if (evt.getType().equals(StateChangeEvent.SELECTED_INSTRUMENT)) {
				stopSynthesizer(evt.getComposition());
				reconfigureSynthesizer(evt.getComposition());
			}
		}
	}
	
	private List<MidiNote> getNotes(int note,boolean accent) {
		return stateManager.getComposition().getSynthesizerConfiguration().getMidiNotesForNote(
			stateManager.getSelectedInstrument(),note,accent,stateManager.getComposition().getMsPerStep()
			);
	}
}
