package nl.zeesoft.zmmt.gui;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;
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
import nl.zeesoft.zmmt.player.InstrumentPlayer;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;
import nl.zeesoft.zmmt.syntesizer.InstrumentConfiguration;
import nl.zeesoft.zmmt.syntesizer.MidiNote;

public class Controller extends Locker {
	private Settings					settings					= null;
	
	private WorkerUnion					union						= null;
	private ControllerWindowAdapter		adapter						= null;
	private ControllerKeyListener		keyListener					= null;
	private FrameMain					mainFrame					= null;
	private InstrumentPlayer			player						= null;
	private InstrumentPlayerKeyListener	playerKeyListener			= null;
	
	private Composition					composition					= null;
	private Composition					compositionOriginal			= null;
	private boolean						compositionChanged			= false;
	private List<String>				updatedCompositionTabs		= new ArrayList<String>();
	private CompositionUpdateWorker		compositionUpdateWorker		= null;
	
	private ImportExportWorker			importExportWorker			= null;
	private File						compositionFile				= null;
	
	private Synthesizer					synthesizer					= null;

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
		if (settings.getCustomFontName().length()>0) {
			setFont(settings.getCustomFontName(),settings.isCustomFontBold(),settings.getCustomFontSize());
		}
		union = new WorkerUnion(getMessenger());
		adapter = new ControllerWindowAdapter(this);
		keyListener = new ControllerKeyListener(this);
		player = new InstrumentPlayer(getMessenger(),getUnion(),this);
		playerKeyListener = new InstrumentPlayerKeyListener(this,settings.getKeyCodeNoteNumbers());
		mainFrame = new FrameMain(this);
		mainFrame.initialize();
		compositionUpdateWorker = new CompositionUpdateWorker(getMessenger(),getUnion(),this);
		importExportWorker = new ImportExportWorker(getMessenger(),getUnion(),this);
	}

	public void start() {
		start(false);
	}
	
	public void start(boolean debug) {
		String err = "";

		lockMe(this);
		Composition comp = settings.getNewComposition();
		unlockMe(this);
		setComposition(comp);
		
		boolean loading = false;
		if (settings.getWorkingCompositionFileName().length()>0) {
			File file = new File(settings.getWorkingCompositionFileName());
			if (!file.exists()) {
				err = "Working composition file not found: " + settings.getWorkingCompositionFileName();
			} else {
				importExportWorker.loadCompositionAndInitialize(file,settings.getWorkDirName());
				loading = true;
			}
		}

		mainFrame.getFrame().setVisible(true);

		if (!loading) {
			importExportWorker.initialize(settings.getWorkDirName());
		}
		player.start();
		compositionUpdateWorker.start();
		InitializeSynthesizerWorker synthInit = new InitializeSynthesizerWorker(getMessenger(),getUnion(),this);
		synthInit.start();
		getMessenger().setPrintDebugMessages(debug);
		getMessenger().start();
		
		lockMe(this);
		if (settings.getWorkingTab().length()>0) {
			mainFrame.switchTo(settings.getWorkingTab());
		}
		if (settings.getWorkingInstrument().length()>0) {
			selectInstrument(settings.getWorkingInstrument(),this);
		}
		unlockMe(this);
		
		if (err.length()>0) {
			showErrorMessage(this,err);
		}
	}

	public void stop(Worker ignoreWorker) {
		importExportWorker.stop();
		player.stop();
		stopSynthesizer();
		mainFrame.getFrame().setVisible(false);
		compositionUpdateWorker.stop();
		getMessenger().stop();
		union.stopWorkers(ignoreWorker);
		getMessenger().whileWorking();
		lockMe(this);
		if (synthesizer!=null) {
			synthesizer.close();
		}
		unlockMe(this);
	}

	public void windowClosing(WindowEvent e) {
		if (e.getWindow()==mainFrame.getFrame()) {
			boolean confirmed = true;
			if (isCompositionChanged()) {
				confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to quit?");
			}
			if (confirmed) {
				lockMe(this);
				settings.setWorkingTab(mainFrame.getSelectedTab());
				settings.setWorkingInstrument(player.getSelectedInstrument());
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
	
	protected File chooseFile(JFileChooser fileChooser,String title) {
		File file = null;
		int choice = fileChooser.showDialog(mainFrame.getFrame(),title);
		if (choice==JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		return file;
	}

	public void setEnabled(boolean enabled) {
		if (mainFrame!=null) {
			mainFrame.getFrame().setEnabled(enabled);
		}
	}

	public void switchTo(String tab) {
		mainFrame.switchTo(tab);
	}

	public void selectInstrument(String name,Object source) {
		if (player.setSelectedInstrument(name,source)) {
			mainFrame.selectedInstrument(name);
		}
	}
	
	public void selectedPattern(int pattern, Object source) {
		// TODO: Handle sequence changes
	}

	public WorkerUnion getUnion() {
		return union;
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

	protected void setComposition(Composition composition) {
		if (composition!=null) {
			stopSynthesizer();
			lockMe(this);
			this.composition = composition;
			compositionOriginal = composition.copy();
			compositionChanged = false;
			unlockMe(this);
			reconfigureSynthesizer();
			mainFrame.updatedComposition(null,composition);
			mainFrame.setCompositionChanged(false);
		}
	}
	
	public void changedComposition(String tab) {
		lockMe(this);
		if (compositionUpdateWorker.isWorking() && !updatedCompositionTabs.contains(tab)) {
			updatedCompositionTabs.add(tab);
		}
		unlockMe(this);
	}

	protected boolean isCompositionChanged() {
		boolean r = false;
		lockMe(this);
		r = compositionChanged;
		unlockMe(this);
		return r;
	}

	protected void handleCompositionUpdates() {
		boolean reconfigure = false;
		lockMe(this);
		for (String tab: updatedCompositionTabs) {
			mainFrame.getCompositionUpdate(tab,composition);
			mainFrame.updatedComposition(null,composition);
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
		this.synthesizer = synth;
		player.setSynthesizer(synth);
		unlockMe(this);
		reconfigureSynthesizer();
	}
	
	protected void stopSynthesizer() {
		lockMe(this);
		if (synthesizer!=null) {
			List<InstrumentConfiguration> instruments = composition.getSynthesizerConfiguration().getInstruments();
			for (InstrumentConfiguration inst: instruments) {
				player.stopInstrumentNotes(inst.getName());
			}
		}
		unlockMe(this);
	}

	protected void reconfigureSynthesizer() {
		lockMe(this);
		if (synthesizer!=null) {
			composition.getSynthesizerConfiguration().configureMidiSynthesizer(synthesizer);
		}
		unlockMe(this);
	}

	public JComboBox<String> getNewInstrumentSelector() {
		JComboBox<String> r = null;
		lockMe(this);
		r = player.getNewSelector();
		unlockMe(this);
		return r;
	}

	public void playNote(int note,boolean accent) {
		lockMe(this);
		List<MidiNote> notes = composition.getSynthesizerConfiguration().getMidiNotesForNote(player.getSelectedInstrument(),note,accent,composition.getMsPerStep());
		player.playInstrumentNotes(notes);
		unlockMe(this);
	}

	public void stopNote(int note) {
		lockMe(this);
		List<MidiNote> notes = composition.getSynthesizerConfiguration().getMidiNotesForNote(player.getSelectedInstrument(),note,false,composition.getMsPerStep());
		player.stopInstrumentNotes(notes);
		unlockMe(this);
	}

	public void stopNotes() {
		lockMe(this);
		player.stopInstrumentNotes();
		unlockMe(this);
	}

	public void loadComposition() {
		if (importExportWorker.isWorking()) {
			showErrorMessage(this,"Import/export worker is busy");
		} else {
			boolean confirmed = true;
			if (isCompositionChanged()) {
				confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to load a different composition?");
			}
			if (confirmed) {
				importExportWorker.loadComposition();
			}
		}
	}

	public void undoCompositionChanges() {
		if (isCompositionChanged()) {
			boolean confirmed = showConfirmMessage("Are you sure you want to undo the composition changes?");
			if (confirmed) {
				lockMe(this);
				composition = compositionOriginal.copy();
				unlockMe(this);
				setComposition(composition);
			}
		}
	}

	public void saveComposition() {
		if (importExportWorker.isWorking()) {
			showErrorMessage(this,"Import/export worker is busy");
		} else {
			Composition copy = composition.copy();
			File file = compositionFile;
			importExportWorker.saveComposition(copy,file);
		}
	}
	
	public void newComposition() {
		boolean confirmed = true;
		if (isCompositionChanged()) {
			confirmed = showConfirmMessage("Unsaved changes will be lost. Are you sure you want to create a new composition?");
		}
		if (confirmed) {
			lockMe(this);
			composition = settings.getNewComposition();
			settings.setWorkingCompositionFileName("");
			compositionFile = null;
			unlockMe(this);
			setComposition(composition);
			switchTo(FrameMain.COMPOSITION);
		}
	}

	public void loadedComposition(File file,Composition comp) {
		lockMe(this);
		settings.setWorkingCompositionFileName(file.getAbsolutePath());
		compositionFile = file;
		unlockMe(this);
		setComposition(comp);
	}

	public void savedComposition(File file,Composition comp) {
		lockMe(this);
		settings.setWorkingCompositionFileName(file.getAbsolutePath());
		compositionFile = file;
		unlockMe(this);
		setComposition(comp);
	}
	
	protected void setFont(String name, boolean bold, int size) {
		int type = Font.PLAIN;
		if (bold) {
			type = Font.BOLD;
		}
		FontUIResource fr = new FontUIResource(new Font(name,type,size));
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof FontUIResource) {
	            UIManager.put(key, fr);
	        }
	    }
	}
}
