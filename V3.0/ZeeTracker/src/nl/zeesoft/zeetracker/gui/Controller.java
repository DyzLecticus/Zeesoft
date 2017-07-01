package nl.zeesoft.zeetracker.gui;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
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
import nl.zeesoft.zeetracker.gui.panel.PanelInstruments;
import nl.zeesoft.zeetracker.gui.panel.PanelMix;
import nl.zeesoft.zeetracker.gui.state.StateChangeEvent;
import nl.zeesoft.zeetracker.gui.state.StateChangeSubscriber;
import nl.zeesoft.zeetracker.gui.state.StateManager;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.player.InstrumentPlayer;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;
import nl.zeesoft.zmmt.synthesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.synthesizer.MidiNote;

public class Controller extends Locker implements StateChangeSubscriber {
	private Settings					settings					= null;
	private boolean						firstTime					= false;
	private boolean						displayedAbout				= false;
	
	private WorkerUnion					union						= null;
	private StateManager				stateManager				= null;

	private ControllerWindowAdapter		adapter						= null;
	private FrameMain					mainFrame					= null;
	private WindowBusy					busyWindow					= null;
	private DialogAbout					aboutDialog					= null;
	
	private InstrumentPlayer			player						= null;
	private InstrumentPlayerKeyListener	playerKeyListener			= null;
	
	private File						compositionFile				= null;
	
	private ImportExportWorker			importExportWorker			= null;
	
	private Synthesizer					synthesizer					= null;
	private Soundbank					baseSoundFont				= null;
	private Soundbank					internalDrumKit				= null;
	private Soundbank					internalSynthesizers		= null;
	private boolean						initializedSoundFont		= false;

	private SequencePlayerImpl			sequencePlayer				= null;
	private Sequencer					sequencer					= null;
	private List<MetaEventListener>		sequencerMetaListeners		= new ArrayList<MetaEventListener>();
	
	private boolean						useInternalDrumKit			= true;
	private boolean						useInternalSynthesizers		= true;
	
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
		File file = new File(settings.getFileName());
		if (file.exists()) {
			settings.fromFile();
		} else {
			firstTime = true;
		}
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
		stateManager.setShowInstrumentFX(this,settings.isWorkingShowInstrumentFX());
		if (settings.getWorkingPatternEditMode().length()>0) {
			stateManager.setPatternEditMode(this,settings.getWorkingPatternEditMode());
		}
		if (settings.getWorkingCompositionPattern()>0) {
			stateManager.setSelectedPattern(this,settings.getWorkingCompositionPattern());
		}
		useInternalDrumKit = settings.getSynthesizerConfiguration().isUseInternalDrumKit();
		useInternalSynthesizers = settings.getSynthesizerConfiguration().isUseInternalSynthesizers();
		stateManager.addSubscriber(this);

		adapter = new ControllerWindowAdapter(this);
		player = new InstrumentPlayer(getMessenger(),getUnion());
		playerKeyListener = new InstrumentPlayerKeyListener(this,settings.getKeyCodeNoteNumbers());

		sequencePlayer = new SequencePlayerImpl(getMessenger(),getUnion());
		stateManager.addSubscriber(sequencePlayer);
		
		mainFrame = new FrameMain(this);
		mainFrame.initialize();
		busyWindow = new WindowBusy(getMessenger(),mainFrame.getFrame());
		
		importExportWorker = new ImportExportWorker(getMessenger(),getUnion(),this);
		
		aboutDialog = new DialogAbout(this,mainFrame.getFrame());
	}

	public void start() {
		start(false);
	}
	
	public void start(boolean debug) {
		String err = "";

		lockMe(this);
		Composition comp = settings.getNewComposition(firstTime);
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
				settings.getRecentFiles().remove(settings.getWorkingCompositionFileName());
				settings.setWorkingCompositionFileName("");
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
		sequencePlayer.startWorkers();
		
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
		sequencePlayer.stopWorkers();
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

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		if (evt.getSource()!=this) {
			if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
				if (evt.getSource() instanceof PanelInstruments ||
					evt.getSource() instanceof PanelMix ||
					useInternalDrumKit!=evt.getComposition().getSynthesizerConfiguration().isUseInternalDrumKit() ||
					useInternalSynthesizers!=evt.getComposition().getSynthesizerConfiguration().isUseInternalSynthesizers()
					) {
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

	public void windowClosing(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			closeProgram();
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
		return showConfirmMessage("Are you sure?",message);
	}
	
	public boolean showConfirmMessage(String title,String message) {
		boolean r = false;
		if (mainFrame!=null) {
			r = (JOptionPane.showConfirmDialog(mainFrame.getFrame(),message,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
		}
		return r;
	}

	protected void closeProgram() {
		boolean confirmed = true;
		if (stateManager.isCompositionChanged()) {
			confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to quit?");
		}
		if (confirmed) {
			lockMe(this);
			settings.setWorkingTab(stateManager.getSelectedTab());
			settings.setWorkingInstrument(stateManager.getSelectedInstrument());
			settings.setWorkingShowInstrumentFX(stateManager.isShowInstrumentFX());
			settings.setWorkingPatternEditMode(stateManager.getPatternEditMode());
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

	protected File chooseFile(JFileChooser fileChooser,String title) {
		File file = null;
		int choice = fileChooser.showDialog(mainFrame.getFrame(),title);
		if (choice==JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		return file;
	}

	public void showAbout() {
		aboutDialog.repositionAndShow();
	}

	public void setBusy(Object source,String busy,String details) {
		busyWindow.setBusy(source,busy,details);
	}

	public void setDone(Object source, boolean refocus) {
		if (busyWindow.setDone(source)==0) {
			if (mainFrame!=null) {
				if (refocus) {
					mainFrame.requestFocus();
				}
				if (firstTime && !displayedAbout) {
					showAbout();
					displayedAbout = true;
				}
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

	protected void setComposition(Composition composition) {
		if (composition!=null) {
			stopSynthesizer(stateManager.getComposition());
			stateManager.setCompositionChanged(this,false);
			stateManager.setComposition(this,composition);
			reconfigureSynthesizer(composition);
		}
	}
	
	protected void setSynthesizer(Synthesizer synth,Soundbank baseSF,Soundbank internalDK,Soundbank internalSynths) {
		lockMe(this);
		this.synthesizer = synth;
		this.baseSoundFont = baseSF;
		this.internalDrumKit = internalDK;
		this.internalSynthesizers = internalSynths;
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
			if (!initializedSoundFont || useInternalDrumKit!=composition.getSynthesizerConfiguration().isUseInternalDrumKit()) {
				useInternalDrumKit = composition.getSynthesizerConfiguration().isUseInternalDrumKit();
				if (useInternalDrumKit) {
					replaceSoundBankInstruments(synthesizer,internalDrumKit);
				} else {
					restoreSoundBankInstruments(synthesizer,internalDrumKit);
				}
			}
			if (!initializedSoundFont || useInternalSynthesizers!=composition.getSynthesizerConfiguration().isUseInternalSynthesizers()) {
				useInternalSynthesizers = composition.getSynthesizerConfiguration().isUseInternalSynthesizers();
				if (useInternalSynthesizers) {
					replaceSoundBankInstruments(synthesizer,internalSynthesizers);
				} else {
					restoreSoundBankInstruments(synthesizer,internalSynthesizers);
				}
			}
			composition.getSynthesizerConfiguration().configureMidiSynthesizer(synthesizer,!sequencePlayer.isPlaying());
			initializedSoundFont = true;
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
		reconfigureSynthesizer(stateManager.getComposition());
	}

	public void startContinueSequencer() {
		sequencePlayer.startContinue();
	}

	protected void loadComposition(String fileName) {
		File file = null;
		if (fileName.length()>0) {
			file = new File(fileName);
			if (!file.exists()) {
				lockMe(this);
				settings.getRecentFiles().remove(fileName);
				Settings settingsCopy = settings.copy();
				unlockMe(this);
				stateManager.setSettings(this,settingsCopy);
				showErrorMessage(this,"Composition file not found: " + file.getAbsolutePath());
				file = null;
			}
		}
		if (importExportWorker.isWorking()) {
			showErrorMessage(this,"Import/export worker is busy");
		} else {
			boolean confirmed = true;
			if (stateManager.isCompositionChanged()) {
				confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to load a different composition?");
			}
			if (confirmed) {
				importExportWorker.loadComposition(file);
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
				stateManager.setSettings(this,settingsCopy);
				setComposition(settingsCopy.getNewComposition(demo));
			}
		}
	}

	protected void loadedComposition(File file,Composition comp) {
		Settings settingsCopy = null;
		lockMe(this);
		if (file!=null) {
			settings.setWorkingCompositionFileName(file.getAbsolutePath());
			settings.updateRecentFiles(file.getAbsolutePath());
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
		settings.updateRecentFiles(file.getAbsolutePath());
		compositionFile = file;
		settingsCopy = settings.copy();
		unlockMe(this);
		stateManager.setSettings(this,settingsCopy);
		stateManager.setCompositionChanged(this,false);
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

	protected void replaceSoundBankInstruments(Synthesizer synth,Soundbank sb) {
		if (sb!=null) {
			for (Instrument inst: sb.getInstruments()) {
				for (Instrument synthInst: synth.getLoadedInstruments()) {
					/**
					 * The sound bank number is not loaded correctly and an empty piano is always loaded.
					 */
					if (!synthInst.getName().startsWith("Piano 1") &&
						synthInst.getPatch().getProgram()==inst.getPatch().getProgram() &&
						synthInst.getPatch().getBank()==inst.getPatch().getBank()
						) {
						synth.unloadInstrument(synthInst);
						synth.loadInstrument(inst);
					}
				}
			}
		}
	}

	protected void restoreSoundBankInstruments(Synthesizer synth,Soundbank sb) {
		if (sb!=null) {
			for (Instrument inst: sb.getInstruments()) {
				for (Instrument synthInst: synth.getLoadedInstruments()) {
					/**
					 * The sound bank number is not loaded correctly and an empty piano is always loaded.
					 */
					if (!synthInst.getName().startsWith("Piano 1") && 
						synthInst.getPatch().getProgram()==inst.getPatch().getProgram() &&
						synthInst.getPatch().getBank()==inst.getPatch().getBank()
						) {
						synth.unloadInstrument(synthInst);
						synth.loadInstrument(baseSoundFont.getInstrument(inst.getPatch()));
					}
				}
			}
		}
	}

	private List<MidiNote> getNotes(int note,boolean accent) {
		return stateManager.getComposition().getSynthesizerConfiguration().getMidiNotesForNote(
			stateManager.getSelectedInstrument(),note,accent,stateManager.getComposition().getMsPerStep()
			);
	}
}
