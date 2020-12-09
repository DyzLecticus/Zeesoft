package nl.zeesoft.zdbd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.generate.Generator;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;

public class ThemeController implements EventListener, Waitable {
	public static String				INITIALIZING			= "INITIALIZING";
	public static String				INITIALIZED				= "INITIALIZED";
	public static String				INITIALIZED_AND_LOADED	= "INITIALIZED_AND_LOADED";
	
	public static String				INITIALIZING_THEME		= "INITIALIZING_THEME";
	public static String				INITIALIZED_THEME		= "INITIALIZED_THEME";
	public static String				SAVING_THEME			= "SAVING_THEME";
	public static String				SAVED_THEME				= "SAVED_THEME";
	public static String				LOADING_THEME			= "LOADING_THEME";
	public static String				LOADED_THEME			= "LOADED_THEME";
	
	public static String				TRAINING_NETWORK		= "TRAINING_NETWORK";
	public static String				TRAINED_NETWORK			= "TRAINED_NETWORK";
	public static String				GENERATING_SEQUENCE		= "GENERATING_SEQUENCE";
	public static String				GENERATED_SEQUENCE		= "GENERATED_SEQUENCE";
	
	public static String				DESTROYING				= "DESTROYING";
	public static String				DESTROYED				= "DESTROYED";
	
	public EventPublisher				eventPublisher			= new EventPublisher();
	
	protected Lock						lock					= new Lock();
	protected Busy						busy					= new Busy(this);
	protected ThemeControllerSettings	settings				= null;
	
	protected Theme						theme					= null;
	protected long						savedTheme				= 0;
	
	public ThemeController() {
		eventPublisher.addListener(this);
	}
	
	public ThemeControllerSettings getSettings() {
		lock.lock(this);
		ThemeControllerSettings r = (ThemeControllerSettings) settings.copy();
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain initialize(ThemeControllerSettings settings) {
		CodeRunnerChain r = null;
		boolean initialize = false;
		lock.lock(this);
		if (this.settings==null && !busy.isBusy()) {
			this.settings = (ThemeControllerSettings) settings.copy();
			initialize = true;
			busy.setBusy(true);
		}
		lock.unlock(this);
		if (initialize) {
			List<RunCode> codes = new ArrayList<RunCode>();
			codes.add(MidiSys.getInitializeRunCode());
			if (settings.fileExists()) {
				settings = (ThemeControllerSettings) settings.fromFile();
			} else {
				codes.add(getInstallRunCode(settings));
			}
			r = getInitializeRunnerChain(settings,codes);
		} else {
			Logger.err(this, new Str("Controller has already been initialized"));
		}
		return r;
	}
	
	public List<String> listThemes() {
		List<String> r = new ArrayList<String>();
		lock.lock(this);
		if (settings!=null) {
			List<File> directories = FileIO.listDirectories(settings.getThemeDir());
			for (File dir: directories) {
				r.add(dir.getName());
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public String getName() {
		String r = "";
		lock.lock(this);
		if (theme!=null) {
			r = theme.name;
		}
		lock.unlock(this);
		return r;
	}
	
	public Rythm getRythm() {
		Rythm r = null;
		lock.lock(this);
		if (theme!=null) {
			r = new Rythm();
			r.copyFrom(theme.rythm);
		}
		lock.unlock(this);
		return r;
	}
	
	public void setBeatsPerMinute(float beatsPerMinute) {
		lock.lock(this);
		if (theme!=null) {
			theme.rythm.beatsPerMinute = beatsPerMinute;
			MidiSys.midiSequencer.setTempoInBPM(beatsPerMinute);
		}
		lock.unlock(this);
	}
	
	public void setTrainingSequence(PatternSequence sequence) {
		lock.lock(this);
		if (theme!=null) {
			sequence.rythm.copyFrom(theme.rythm);
			theme.networkTrainer.setSequence(sequence);
		}
		lock.unlock(this);
	}
	
	public PatternSequence getTrainingSequence() {
		PatternSequence r = null;
		lock.lock(this);
		if (theme!=null) {
			r = theme.networkTrainer.getSequence();
		}
		lock.unlock(this);
		return r;
	}
	
	public void putGenerator(Generator generator) {
		lock.lock(this);
		if (theme!=null) {
			theme.generators.put(generator);
		}
		lock.unlock(this);
	}
	
	public Generator getGenerator(String name) {
		Generator r = null;
		lock.lock(this);
		if (theme!=null) {
			r = theme.generators.get(name);
		}
		lock.unlock(this);
		return r;
	}
	
	public Generator removeGenerator(String name) {
		Generator r = null;
		lock.lock(this);
		if (theme!=null) {
			r = theme.generators.remove(name);
		}
		lock.unlock(this);
		return r;
	}
	
	public List<Generator> getGenerators() {
		List<Generator> r = new ArrayList<Generator>();
		lock.lock(this);
		if (theme!=null) {
			r = theme.generators.list();
		}
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain trainNetwork() {
		return getTrainNetworkRunnerChain();
	}
	
	public CodeRunnerChain generateSequence(String name) {
		return getGenerateSequenceRunnerChain(name);
	}
	
	public boolean themeHasChanges() {
		boolean r = false;
		lock.lock(this);
		if (theme!=null && (
			savedTheme<theme.networkTrainer.getChangedSequence() || 
			savedTheme<theme.networkTrainer.getTrainedNetwork() || 
			savedTheme<theme.generators.getChanged() 
			)) {
			r = true;
		}
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain newTheme(String name) {
		return getNewThemeRunnerChain(name,null);
	}
	
	public CodeRunnerChain newTheme(String name, Rythm rythm) {
		return getNewThemeRunnerChain(name,rythm);
	}
	
	public CodeRunnerChain saveTheme() {
		return getSaveThemeRunnerChain();
	}
	
	public CodeRunnerChain saveThemeAs(String name) {
		return getSaveThemeRunnerChain();
	}
	
	public CodeRunnerChain loadTheme(String name) {
		return getLoadThemeRunnerChain(name);
	}
	
	public CodeRunnerChain destroy() {
		return getDestroyRunnerChain();
	}

	@Override
	public boolean isBusy() {
		return busy.isBusy();
	}
	
	@Override
	public void handleEvent(Event event) {
		Logger.dbg(this, new Str(event.name));
		if (event.name.equals(INITIALIZING)) {
			// Ignore
		} else if (event.name.equals(INITIALIZED)) {
			lock.lock(this);
			MidiSys.midiSequencer.setTempoInBPM(theme.rythm.beatsPerMinute);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(INITIALIZED_AND_LOADED)) {
			lock.lock(this);
			savedTheme = System.currentTimeMillis();
			MidiSys.midiSequencer.setTempoInBPM(theme.rythm.beatsPerMinute);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(LOADING_THEME)) {
			MidiSys.midiSequencer.stop();
		} else if (event.name.equals(LOADED_THEME)) {
			lock.lock(this);
			savedTheme = System.currentTimeMillis();
			MidiSys.midiSequencer.setTempoInBPM(theme.rythm.beatsPerMinute);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(SAVING_THEME)) {
			// Ignore
		} else if (event.name.equals(SAVED_THEME)) {
			lock.lock(this);
			savedTheme = System.currentTimeMillis();
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(TRAINING_NETWORK)) {
			MidiSys.midiSequencer.pause();
		} else if (event.name.equals(TRAINED_NETWORK)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(INITIALIZING_THEME)) {
			MidiSys.midiSequencer.stop();
		} else if (event.name.equals(INITIALIZED_THEME)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(GENERATING_SEQUENCE)) {
			// Ignore
		} else if (event.name.equals(GENERATED_SEQUENCE)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(DESTROYING)) {
			MidiSys.midiSequencer.stop();
		} else if (event.name.equals(DESTROYED)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		}
	}
	
	protected RunCode getInstallRunCode(ThemeControllerSettings settings) {
		return new RunCode() {
			@Override
			protected boolean run() {
				install(settings);
				return true;
			}
		};
	}
	
	protected void install(ThemeControllerSettings settings) {
		Logger.dbg(this,new Str("Installing ..."));
		settings.toFile();
		if (FileIO.checkDirectory(settings.getThemeDir()).length()>0) {
			FileIO.mkDirs(settings.getThemeDir());
		}
		Logger.dbg(this,new Str("Installed"));
	}
	
	protected void destroyMe() {
		Logger.dbg(this,new Str("Destroying ..."));
		MidiSys.closeDevices();
		lock.lock(this);
		if (theme!=null) {
			settings.workingTheme = theme.name;
		}
		settings.toFile();
		settings = null;
		theme = null;
		lock.unlock(this);
		eventPublisher.removeListener(this);
		Logger.dbg(this,new Str("Destroyed"));
	}
	
	protected CodeRunnerChain getInitializeRunnerChain(ThemeControllerSettings settings, List<RunCode> beforeCodes) {
		CodeRunnerChain r = new CodeRunnerChain();
		
		List<RunCode> codes = new ArrayList<RunCode>();
		codes.addAll(getLoadSoundBankRunCodes());
		
		boolean load = false;
		
		lock.lock(this);
		this.settings = settings;
		if (settings.workingTheme.length()>0) {
			theme = new Theme();
			theme.themeDir = settings.getThemeDir();
			theme.name = settings.workingTheme;
			if (theme.directoryExists()) {
				codes.add(theme.loadRythm());
				codes.add(theme.loadNetworkTrainer());
				codes.add(theme.loadNetwork());
				codes.add(theme.loadGenerators());
				load = true;
			} else {
				theme = null;
			}
		}
		if (theme==null) {
			theme = new Theme();
			theme.themeDir = settings.getThemeDir();
			theme.name = "Demo";
			theme.networkTrainer.setSequence(PatternFactory.getFourOnFloorInstrumentPatternSequence());
			codes.add(theme.initializeNetwork());
		}
		r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZING));
		r.addAll(beforeCodes);
		r.addAll(codes);
		if (load) {
			r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZED_AND_LOADED));
		} else {
			r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZED));
		}
		lock.unlock(this);
		
		return r;
	}

	protected List<RunCode> getLoadSoundBankRunCodes() {
		List<RunCode> r = new ArrayList<RunCode>();
		ThemeControllerSettings settings = getSettings();
		String soundBankDir = FileIO.addSlash(settings.soundBankDir);
		if (settings.useInternalSyntesizers) {
			r.add(MidiSys.getLoadSoundbankRunCode(soundBankDir + "ZeeTrackerSynthesizers.sf2"));
		}
		if (settings.useInternalDrumKit) {
			r.add(MidiSys.getLoadSoundbankRunCode(soundBankDir + "ZeeTrackerDrumKit.sf2"));
		}
		return r;
	}
	
	protected CodeRunnerChain getNewThemeRunnerChain(String name, Rythm rythm) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (!busy.isBusy()) {
			busy.setBusy(true);
			
			List<RunCode> codes = new ArrayList<RunCode>();
			theme = new Theme();
			theme.themeDir = settings.getThemeDir();
			theme.name = name;
			if (rythm!=null) {
				theme.rythm.copyFrom(rythm);
			}
			codes.add(theme.initializeNetwork());
			
			r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZING_THEME));
			r.addAll(codes);
			r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZED_THEME));
		}
		lock.unlock(this);
		return r;
	}

	protected CodeRunnerChain getSaveThemeRunnerChain() {
		return getSaveThemeRunnerChain(null);
	}
	
	protected CodeRunnerChain getSaveThemeRunnerChain(String name) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (theme!=null && !busy.isBusy()) {
			busy.setBusy(true);
			
			if (name!=null && name.length()>0 && !theme.name.equals(name)) {
				theme.name = name;
			}
			
			if (theme.name.length()>0) {
				List<RunCode> codes = new ArrayList<RunCode>();
				codes.add(theme.saveRythm());
				codes.add(theme.saveNetworkTrainer());
				codes.add(theme.saveNetwork());
				codes.add(theme.saveGenerators());
				
				r.add(eventPublisher.getPublishEventRunCode(this, SAVING_THEME));
				r.add(theme.getMkdirsRunCode());
				r.addAll(codes);
				r.add(eventPublisher.getPublishEventRunCode(this, SAVED_THEME));
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getLoadThemeRunnerChain(String name) {
		CodeRunnerChain r = null;
		lock.lock(this);
		if (!busy.isBusy()) {
			busy.setBusy(true);
			
			List<RunCode> codes = new ArrayList<RunCode>();
			theme = new Theme();
			theme.themeDir = settings.getThemeDir();
			theme.name = name;
			if (theme.directoryExists()) {
				codes.add(theme.loadRythm());
				codes.add(theme.loadNetworkTrainer());
				codes.add(theme.loadNetwork());
				codes.add(theme.loadGenerators());
				
				r = new CodeRunnerChain();
				r.add(eventPublisher.getPublishEventRunCode(this, LOADING_THEME));
				r.addAll(codes);
				r.add(eventPublisher.getPublishEventRunCode(this, LOADED_THEME));
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getTrainNetworkRunnerChain() {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (theme!=null && !busy.isBusy() &&
			theme.networkTrainer.changedSequenceSinceTraining()) {
			busy.setBusy(true);
			r.add(eventPublisher.getPublishEventRunCode(this, TRAINING_NETWORK));
			r.add(theme.trainNetwork());
			r.add(eventPublisher.getPublishEventRunCode(this, TRAINED_NETWORK));
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getGenerateSequenceRunnerChain(String name) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (theme!=null && !busy.isBusy()) {
			busy.setBusy(true);
			r.add(eventPublisher.getPublishEventRunCode(this, GENERATING_SEQUENCE));
			r.add(theme.generateSequence(name));
			r.add(eventPublisher.getPublishEventRunCode(this, GENERATED_SEQUENCE, name));
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getDestroyRunnerChain() {
		CodeRunnerChain r = new CodeRunnerChain();
		
		lock.lock(this);
		if (!busy.isBusy()) {
			busy.setBusy(true);
			r.add(eventPublisher.getPublishEventRunCode(this, DESTROYING));
			r.add(new RunCode() {
				@Override
				protected boolean run() {
					destroyMe();
					return true;
				}
			});
			r.add(eventPublisher.getPublishEventRunCode(this, DESTROYED));
		}
		lock.unlock(this);
		
		return r;
	}
}
