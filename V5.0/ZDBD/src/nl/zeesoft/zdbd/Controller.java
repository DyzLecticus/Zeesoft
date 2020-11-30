package nl.zeesoft.zdbd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class Controller implements EventListener {
	public static String			INITIALIZED			= "INITIALIZED";
	public static String			COMPOSITION_SAVED	= "COMPOSITION_SAVED";
	public static String			COMPOSITION_LOADED	= "COMPOSITION_LOADED";
	
	public static EventPublisher	eventPublisher		= new EventPublisher();
	
	private Lock					lock				= new Lock();
	private Settings				settings			= null;
	private Composition				composition			= null;
	
	public Controller() {
		eventPublisher.addListener(this);
	}
	
	public Settings getSettings() {
		lock.lock(this);
		Settings r = settings;
		lock.unlock(this);
		return r;
	}
	
	public CodeRunnerChain initialize(Settings settings) {
		CodeRunnerChain r = null;
		boolean initialize = false;
		lock.lock(this);
		if (this.settings==null) {
			this.settings = settings;
			initialize = true;
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
		}
		return r;
	}
	
	protected CodeRunnerChain initializeState(Settings settings) {
		CodeRunnerChain r = new CodeRunnerChain();
		
		List<RunCode> codes = new ArrayList<RunCode>();
		codes.addAll(getLoadSoundBankRunCodes());
		
		lock.lock(this);
		composition = new Composition();
		composition.directory = FileIO.addSlash(settings.workDir);
		if (settings.workingComposition.length()>0) {
			composition.name = settings.workingComposition;
			codes.add(composition.loadNetwork());
		} else {
			composition.name = "Demo";
			codes.add(composition.initializeNetwork());
		}
		lock.unlock(this);
		
		r.addAll(codes);
		r.add(eventPublisher.getPublishEventRunCode(this, INITIALIZED));
		
		return r;
	}
	
	public CodeRunnerChain destroy() {
		CodeRunnerChain r = new CodeRunnerChain();
		Logger.dbg(this,new Str("Destroying controller ..."));
		MidiSys.closeDevices();
		Logger.dbg(this,new Str("Destroyed controller"));
		return r;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.name.equals(INITIALIZED)) {
			Logger.dbg(this, new Str("Intialized controller"));
		} else if (event.name.equals(COMPOSITION_LOADED)) {
			// TODO Implement
		} else if (event.name.equals(COMPOSITION_SAVED)) {
			// TODO Implement
		}
	}
	
	protected void install(Settings settings) {
		Logger.dbg(this,new Str("Installing ..."));
		settings.toFile();
		Logger.dbg(this,new Str("Installed"));
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
}
