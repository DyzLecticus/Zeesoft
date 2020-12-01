package nl.zeesoft.zdbd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Busy;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waitable;

public class Controller implements EventListener, Waitable {
	public static String			INITIALIZED				= "INITIALIZED";
	
	public static String			SAVING_COMPOSITION		= "SAVING_COMPOSITION";
	public static String			COMPOSITION_SAVED		= "COMPOSITION_SAVED";
	
	public static String			LOADING_COMPOSISTION	= "LOADING_COMPOSITION";
	public static String			COMPOSITION_LOADED		= "COMPOSITION_LOADED";
	
	public static EventPublisher	eventPublisher			= new EventPublisher();
	
	private Lock					lock					= new Lock();
	private Busy					busy					= new Busy(this);
	private Settings				settings				= null;
	private Composition				composition				= null;
	
	public Controller() {
		eventPublisher.addListener(this);
	}
	
	public Settings getSettings() {
		lock.lock(this);
		Settings r = settings.copy();
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain initialize(Settings settings) {
		CodeRunnerChain r = null;
		boolean initialize = false;
		lock.lock(this);
		if (this.settings==null) {
			this.settings = settings.copy();
			initialize = true;
			busy.setBusy(true);
		}
		lock.unlock(this);
		if (initialize) {
			if (settings.fileExists()) {
				settings = settings.fromFile();
			} else {
				install(settings);
			}
			Logger.dbg(this, new Str("Intializing controller ..."));
			r = initializeState(settings);
			r.start();
		} else {
			Logger.dbg(this, new Str("Controller has already been initialized"));
		}
		return r;
	}
	
	public CodeRunnerChain saveState() {
		return getSaveStateRunnerChain();
	}
	
	public CodeRunnerChain loadState(String name) {
		return getLoadStateRunnerChain(name);
	}
	
	public CodeRunnerChain destroy() {
		CodeRunnerChain r = new CodeRunnerChain();
		
		lock.lock(this);
		if (!busy.isBusy()) {
			Logger.dbg(this,new Str("Destroying controller ..."));
			MidiSys.closeDevices();
			settings = null;
			composition = null;
			Logger.dbg(this,new Str("Destroyed controller"));
		}
		r.start();
		lock.unlock(this);
		
		return r;
	}

	@Override
	public boolean isBusy() {
		return busy.isBusy();
	}
	
	@Override
	public void handleEvent(Event event) {
		if (event.name.equals(INITIALIZED)) {
			Logger.dbg(this, new Str("Intialized controller"));
			busy.setBusy(false);
		} else if (event.name.equals(COMPOSITION_LOADED)) {
			Logger.dbg(this, new Str("Loaded composition"));
			lock.lock(this);
			settings.workingComposition = composition.name;
			settings.toFile();
			lock.unlock(this);
			busy.setBusy(false);
		} else if (event.name.equals(COMPOSITION_SAVED)) {
			Logger.dbg(this, new Str("Saved composition"));
			lock.lock(this);
			settings.workingComposition = composition.name;
			settings.toFile();
			lock.unlock(this);
			busy.setBusy(false);
		}
	}
	
	protected void install(Settings settings) {
		Logger.dbg(this,new Str("Installing ..."));
		// TODO: Mkdirs
		settings.toFile();
		Logger.dbg(this,new Str("Installed"));
	}
	
	protected CodeRunnerChain initializeState(Settings settings) {
		CodeRunnerChain r = new CodeRunnerChain();
		
		List<RunCode> codes = new ArrayList<RunCode>();
		codes.addAll(getLoadSoundBankRunCodes());
		
		// TODO: Check dirs
		
		lock.lock(this);
		composition = new Composition();
		composition.workDir = FileIO.addSlash(settings.workDir);
		if (settings.workingComposition.length()>0) {
			composition.name = settings.workingComposition;
			codes.add(composition.loadNetworkTrainer());
			codes.add(composition.loadNetwork());
			codes.add(composition.loadGenerators());
		} else {
			composition.name = "Demo";
			codes.add(composition.initializeNetwork(false));
		}
		lock.unlock(this);
		
		r.addAll(codes);
		r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZED));
		
		return r;
	}

	protected List<RunCode> getLoadSoundBankRunCodes() {
		List<RunCode> r = new ArrayList<RunCode>();
		Settings settings = getSettings();
		String soundBankDir = FileIO.addSlash(settings.soundBankDir);
		if (settings.useInternalSyntesizers) {
			r.add(MidiSys.getLoadSoundbankRunCode(soundBankDir + "ZeeTrackerSynthesizers.sf2"));
		}
		if (settings.useInternalDrumKit) {
			r.add(MidiSys.getLoadSoundbankRunCode(soundBankDir + "ZeeTrackerDrumKit.sf2"));
		}
		return r;
	}
	
	protected CodeRunnerChain getSaveStateRunnerChain() {
		CodeRunnerChain r = new CodeRunnerChain();
		
		lock.lock(this);
		if (composition!=null && !busy.isBusy()) {
			busy.setBusy(true);
			
			List<RunCode> codes = new ArrayList<RunCode>();
			codes.add(composition.saveNetworkTrainer());
			codes.add(composition.saveNetwork());
			codes.add(composition.saveGenerators());
			
			r.add(composition.getMkdirsRunCode());
			r.addAll(codes);
			r.add(eventPublisher.getPublishEventRunCode(this, COMPOSITION_SAVED));
			r.start();
		}
		lock.unlock(this);
		
		return r;
	}
	
	protected CodeRunnerChain getLoadStateRunnerChain(String name) {
		CodeRunnerChain r = new CodeRunnerChain();
		
		// TODO: Check dirs

		lock.lock(this);
		if (!busy.isBusy()) {
			busy.setBusy(true);
			
			List<RunCode> codes = new ArrayList<RunCode>();
			composition = new Composition();
			composition.workDir = FileIO.addSlash(settings.workDir);
			composition.name = name;
			codes.add(composition.loadNetworkTrainer());
			codes.add(composition.loadNetwork());
			codes.add(composition.loadGenerators());
			
			r.addAll(codes);
			r.add(eventPublisher.getPublishEventRunCode(this, COMPOSITION_LOADED));
			r.start();
		}
		lock.unlock(this);
		
		return r;
	}
}
