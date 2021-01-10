package nl.zeesoft.zdbd.theme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdbd.midi.MidiSequenceUtil;
import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.Generator;
import nl.zeesoft.zdbd.neural.NetworkTrainer;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.PatternSequence;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.model.ModelStatistics;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;

public class ThemeController implements EventListener, Waitable {
	public static String				INITIALIZING				= "INITIALIZING";
	public static String				INITIALIZED					= "INITIALIZED";
	public static String				INITIALIZED_AND_LOADED		= "INITIALIZED_AND_LOADED";
	
	public static String				INITIALIZING_THEME			= "INITIALIZING_THEME";
	public static String				INITIALIZED_THEME			= "INITIALIZED_THEME";
	public static String				SAVING_THEME				= "SAVING_THEME";
	public static String				SAVED_THEME					= "SAVED_THEME";
	public static String				LOADING_THEME				= "LOADING_THEME";
	public static String				LOADED_THEME				= "LOADED_THEME";
	public static String				DELETING_THEME				= "DELETING_THEME";
	public static String				DELETED_THEME				= "DELETED_THEME";
	
	public static String				CHANGED_TRAINING_SEQUENCE	= "CHAINGED_TRAINING_SEQUENCE";
	public static String				TRAINING_NETWORK			= "TRAINING_NETWORK";
	public static String				TRAINED_NETWORK				= "TRAINED_NETWORK";
	public static String				GENERATING_SEQUENCES		= "GENERATING_SEQUENCES";
	public static String				GENERATED_SEQUENCES			= "GENERATED_SEQUENCES";
	public static String				GENERATING_SEQUENCE			= "GENERATING_SEQUENCE";
	public static String				GENERATED_SEQUENCE			= "GENERATED_SEQUENCE";
	
	public static String				EXPORTING_RECORDING			= "EXPORTING_RECORDING";
	public static String				EXPORTED_RECORDING			= "EXPORTED_RECORDING";

	public static String				DONE						= "DONE";

	public static String				DESTROYING					= "DESTROYING";
	public static String				DESTROYED					= "DESTROYED";
	
	public EventPublisher				eventPublisher				= new EventPublisher();
	
	protected Lock						lock						= new Lock();
	protected Busy						busy						= new Busy(this);
	protected ThemeControllerSettings	settings					= null;
	
	protected Theme						theme						= null;
	protected long						savedTheme					= 0;
	
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
		lock.lock(this);
		List<String> r = listThemesNoLock();
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
			MidiSys.sequencer.setTempoInBPM(beatsPerMinute);
		}
		lock.unlock(this);
	}
	
	public void setTrainingSequence(PatternSequence sequence) {
		boolean changed = false;
		lock.lock(this);
		if (theme!=null) {
			sequence.rythm.copyFrom(theme.rythm);
			theme.networkTrainer.setSequence(sequence);
			changed = true;
		}
		lock.unlock(this);
		if (changed) {
			eventPublisher.publishEvent(this, CHANGED_TRAINING_SEQUENCE, NetworkTrainer.TRAINING_SEQUENCE);
		}
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
	
	public boolean changedTrainingSequenceSinceTraining() {
		boolean r = false;
		lock.lock(this);
		if (theme!=null) {
			r = theme.networkTrainer.changedSequenceSinceTraining();
		}
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain trainNetwork() {
		return getTrainNetworkRunnerChain();
	}
	
	public ModelStatistics getNetworkStatistics() {
		ModelStatistics r = null;
		lock.lock(this);
		if (theme!=null) {
			r = theme.network.getStatistics();
		}
		lock.unlock(this);
		return r;
	}
	
	public NetworkIO getLastIO() {
		NetworkIO r = null;
		lock.lock(this);
		if (theme!=null) {
			r = theme.networkTrainer.getLastIO();
		}
		lock.unlock(this);
		return r;
	}
	
	public void putGenerator(Generator generator) {
		if (!generator.name.equals(NetworkTrainer.TRAINING_SEQUENCE)) {
			lock.lock(this);
			if (theme!=null) {
				theme.generators.put(generator);
			}
			lock.unlock(this);
		}
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
	
	public List<Generator> listGenerators() {
		List<Generator> r = new ArrayList<Generator>();
		lock.lock(this);
		if (theme!=null) {
			r = theme.generators.list();
		}
		lock.unlock(this);
		return r;
	}

	public void moveGenerator(String name, boolean up) {
		lock.lock(this);
		if (theme!=null) {
			int index = theme.generators.indexOf(name);
			if (up && index>0) {
				index--;
				theme.generators.set(name, index);
			} else if (!up && index<(theme.generators.size() - 1)) {
				index++;
				theme.generators.set(name, index);
			}
		}
		lock.unlock(this);
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
	
	public Str renameGenerator(String name,String newName) {
		Str r = new Str();
		lock.lock(this);
		if (theme!=null) {
			if (theme.generators.get(newName)!=null) {
				r.sb().append("Generator already exists with name: ");
				r.sb().append(newName);
			} else {
				theme.generators.rename(name, newName);
			}
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

	public List<String> getSequenceNames() {
		List<String> r = new ArrayList<String>();
		lock.lock(this);
		if (theme!=null) {
			List<Generator> generators = theme.generators.list();
			for (Generator gen: generators) {
				if (gen.generatedPatternSequence!=null) {
					r.add(gen.name);
				}
			}
			if (theme.networkTrainer.getSequence().getSequencedPatterns().size()>0) {
				r.add(NetworkTrainer.TRAINING_SEQUENCE);
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public SortedMap<String,PatternSequence> getSequences() {
		SortedMap<String,PatternSequence> r = new TreeMap<String,PatternSequence>();
		lock.lock(this);
		if (theme!=null) {
			r.putAll(theme.generators.getSequences());
			PatternSequence seq = theme.networkTrainer.getSequence();
			if (seq.getSequencedPatterns().size()>0) {
				r.put(NetworkTrainer.TRAINING_SEQUENCE,seq);
			}
		}
		lock.unlock(this);
		return r;
	}
	
	public Sequence generateMidiSequence(PatternSequence sequence) {
		Sequence r = null;
		lock.lock(this);
		if (theme!=null) {
			r = theme.soundPatch.generateMidiSequence(sequence);
		}
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain generateSequence(String name) {
		return getGenerateSequenceRunnerChain(name);
	}
	
	public CodeRunnerChain generateSequences() {
		return getGenerateSequenceRunnerChain("*");
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
		return getSaveThemeRunnerChain(name);
	}
	
	public CodeRunnerChain exportRecordingTo(String path, boolean midi) {
		return getExportRecordingRunnerChain(path, midi);
	}
	
	public CodeRunnerChain loadTheme(String name) {
		return getLoadThemeRunnerChain(name);
	}
	
	public CodeRunnerChain deleteTheme(String name) {
		return getDeleteThemeRunnerChain(name);
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
		Str msg = new Str(event.name);
		if (event.param!=null) {
			msg.sb().append("(");
			msg.sb().append(event.param.toString());
			msg.sb().append(")");
		}
		Logger.dbg(this, msg);
		if (event.name.equals(INITIALIZING)) {
			// Ignore
		} else if (event.name.equals(INITIALIZED)) {
			lock.lock(this);
			updateSequencerAndSynthesizerNoLock();
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(INITIALIZED_AND_LOADED)) {
			lock.lock(this);
			savedTheme = System.currentTimeMillis();
			updateSequencerAndSynthesizerNoLock();
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(LOADING_THEME)) {
			MidiSys.sequencer.stop();
		} else if (event.name.equals(LOADED_THEME)) {
			lock.lock(this);
			savedTheme = System.currentTimeMillis();
			updateSequencerAndSynthesizerNoLock();
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(SAVING_THEME)) {
			// Ignore
		} else if (event.name.equals(SAVED_THEME)) {
			lock.lock(this);
			savedTheme = System.currentTimeMillis();
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(DELETING_THEME)) {
			// Ignore
		} else if (event.name.equals(DELETED_THEME)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(TRAINING_NETWORK)) {
			MidiSys.sequencer.pause();
		} else if (event.name.equals(TRAINED_NETWORK)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(INITIALIZING_THEME)) {
			MidiSys.sequencer.stop();
		} else if (event.name.equals(INITIALIZED_THEME)) {
			lock.lock(this);
			updateSequencerAndSynthesizerNoLock();
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(EXPORTING_RECORDING)) {
			MidiSys.sequencer.stop();
		} else if (event.name.equals(EXPORTED_RECORDING)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(GENERATING_SEQUENCES)) {
			// Ignore
		} else if (event.name.equals(GENERATED_SEQUENCES)) {
			// Ignore
		} else if (event.name.equals(GENERATING_SEQUENCE)) {
			// Ignore
		} else if (event.name.equals(GENERATED_SEQUENCE)) {
			// Ignore
		} else if (event.name.equals(DESTROYING)) {
			if (MidiSys.isInitialized()) {
				MidiSys.sequencer.stop();
			}
		} else if (event.name.equals(DESTROYED)) {
			lock.lock(this);
			eventPublisher.removeListener(this);
			busy.setBusy(false);
			lock.unlock(this);
		} else if (event.name.equals(DONE)) {
			lock.lock(this);
			busy.setBusy(false);
			lock.unlock(this);
		}
	}
	
	protected void updateSequencerAndSynthesizerNoLock() {
		if (theme!=null) {
			if (MidiSys.sequencer!=null) {
				MidiSys.sequencer.setTempoInBPM(theme.rythm.beatsPerMinute);
				MidiSys.sequencer.setSynthConfig(theme.soundPatch.synthConfig);
			}
			theme.soundPatch.synthConfig.setRythm(theme.rythm);
			if (MidiSys.synthesizer!=null) {
				theme.soundPatch.synthConfig.configureSynthesizer(MidiSys.synthesizer);
			}
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
		if (settings!=null) {
			lock.lock(this);
			if (theme!=null) {
				settings.workingTheme = theme.name;
			}
			settings.toFile();
			settings = null;
			theme = null;
			lock.unlock(this);
		}
		MidiSys.closeDevices();
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
				codes.add(theme.loadSoundPatch());
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
				PatternSequence sequence = theme.networkTrainer.getSequence();
				sequence.rythm.copyFrom(rythm);
				theme.networkTrainer.setSequence(sequence);
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
				codes.add(theme.saveSoundPatch());
				
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
			Theme thm = new Theme();
			thm.themeDir = settings.getThemeDir();
			thm.name = name;
			if (thm.directoryExists()) {
				theme = thm;
				codes.add(theme.loadRythm());
				codes.add(theme.loadNetworkTrainer());
				codes.add(theme.loadNetwork());
				codes.add(theme.loadGenerators());
				codes.add(theme.loadSoundPatch());
				
				r = new CodeRunnerChain();
				r.add(eventPublisher.getPublishEventRunCode(this, LOADING_THEME));
				r.addAll(codes);
				r.add(eventPublisher.getPublishEventRunCode(this, LOADED_THEME));
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getDeleteThemeRunnerChain(String name) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (!busy.isBusy() && listThemesNoLock().contains(name)) {
			busy.setBusy(true);
			Theme thm = null;
			if (theme!=null && theme.name.equals(name)) {
				thm = theme;
				savedTheme = 0;
			} else {
				thm = new Theme();
				thm.themeDir = settings.getThemeDir();
				thm.name = name;
			}
			if (thm.directoryExists()) {
				r.add(eventPublisher.getPublishEventRunCode(this, DELETING_THEME));
				r.add(thm.getDeleteDirRunCode());
				r.add(eventPublisher.getPublishEventRunCode(this, DELETED_THEME));
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getTrainNetworkRunnerChain() {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (theme!=null && !busy.isBusy()) {
			List<RunCode> codes = theme.trainNetwork().getCodes();
			if (codes.size()>0) {
				busy.setBusy(true);
				r.add(eventPublisher.getPublishEventRunCode(this, TRAINING_NETWORK));
				for (RunCode code: codes) {
					r.add(code);
				}
				r.add(eventPublisher.getPublishEventRunCode(this, TRAINED_NETWORK));
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getGenerateSequenceRunnerChain(String name) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (theme!=null && !busy.isBusy()) {
			busy.setBusy(true);
			if (name.equals("*")) {
				List<Generator> generators = theme.generators.list();
				for (Generator generator: generators) {
					r.add(eventPublisher.getPublishEventRunCode(this, GENERATING_SEQUENCES));
					r.add(theme.generateSequence(generator.name));
					r.add(eventPublisher.getPublishEventRunCode(this, GENERATED_SEQUENCE, generator.name));
				}
				r.add(eventPublisher.getPublishEventRunCode(this, GENERATED_SEQUENCES));
				r.add(eventPublisher.getPublishEventRunCode(this, DONE, GENERATED_SEQUENCES));
			} else {
				r.add(eventPublisher.getPublishEventRunCode(this, GENERATING_SEQUENCE));
				r.add(theme.generateSequence(name));
				r.add(eventPublisher.getPublishEventRunCode(this, GENERATED_SEQUENCE, name));
				r.add(eventPublisher.getPublishEventRunCode(this, DONE, GENERATED_SEQUENCE));
			}
		}
		lock.unlock(this);
		return r;
	}
	
	protected CodeRunnerChain getExportRecordingRunnerChain(String path, boolean midi) {
		CodeRunnerChain r = new CodeRunnerChain();
		lock.lock(this);
		if (theme!=null && !busy.isBusy()) {
			Sequence midiSequence = MidiSys.sequencer.getRecordedSequence();
			if (midiSequence!=null) {
				busy.setBusy(true);
				r.add(eventPublisher.getPublishEventRunCode(this, EXPORTING_RECORDING));
				if (midi) {
					r.add(MidiSequenceUtil.getRenderSequenceToMidiFileRunCode(midiSequence, path));
				} else {
					r.add(MidiSequenceUtil.getRenderSequenceToAudioFileRunCode(midiSequence, path));
				}
				r.add(eventPublisher.getPublishEventRunCode(this, EXPORTED_RECORDING, path));
			}
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
	
	protected List<String> listThemesNoLock() {
		List<String> r = new ArrayList<String>();
		if (settings!=null) {
			List<File> directories = FileIO.listDirectories(settings.getThemeDir());
			for (File dir: directories) {
				r.add(dir.getName());
			}
		}
		return r;
	}
}
