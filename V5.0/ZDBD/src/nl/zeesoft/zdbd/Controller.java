package nl.zeesoft.zdbd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.zeesoft.zdbd.midi.MidiSys;
import nl.zeesoft.zdbd.neural.NetworkConfigFactory;
import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.NetworkConfig;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.CodeRunnerChain;

public class Controller implements StateListener {
	public static String		BPM					= "BPM";
	public static String		SETTINGS			= "SETTINGS";
	public static String		LOAD_COMPOSITION	= "LOAD_COMPOSITION";
	public static String		COMPOSITION			= "COMPOSITION";
	
	public static StateManager	stateManager		= new StateManager();
	
	public Controller() {
		stateManager.addListener(this);
	}
	
	public Settings getSettings() {
		return (Settings) stateManager.getObject(SETTINGS);
	}
	
	public Composition getComposition() {
		return (Composition) stateManager.getObject(COMPOSITION);
	}
	
	public String getCompositionDir() {
		return FileIO.addSlash(getSettings().workDir) + FileIO.addSlash(getComposition().name);
	}
	
	public CodeRunnerChain initialize(Settings settings) {
		if (settings.fileExists()) {
			settings = settings.fromFile();
		} else {
			settings.toFile();
			install();
		}
		return initializeState(settings);
	}
	
	protected CodeRunnerChain initializeState(Settings settings) {
		stateManager.putObject(SETTINGS, settings);
		stateManager.putObject(BPM, 120);
		
		Logger.dbg(this,new Str("Initializing ..."));
		CodeRunnerChain soundBankChain = getLoadSoundBankChain();
		soundBankChain.start();

		String name = "Demo";
		boolean load = false;
		if (settings.workingComposition.length()>0) {
			name = settings.workingComposition;
			load = true;
		}
		stateManager.putObject(LOAD_COMPOSITION, load);
		newComposition(name);

		return soundBankChain;
	}
	
	protected void install() {
		Logger.dbg(this,new Str("Installing ..."));
	}

	protected CodeRunnerChain getLoadSoundBankChain() {
		Settings settings = getSettings();
		List<String> filePaths = new ArrayList<String>();
		if (settings.useInternalSyntesizers) {
			filePaths.add(settings.soundBankDir + "ZeeTrackerSynthesizers.sf2");
		}
		if (settings.useInternalDrumKit) {
			filePaths.add(settings.soundBankDir + "ZeeTrackerDrumKit.sf2");
		}
		CodeRunnerChain r = MidiSys.getCodeRunnerChainForSoundbankFiles(filePaths);
		return r;
	}
	
	public List<CodeRunner> destroy() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		Logger.dbg(this,new Str("Destroying ..."));
		
		MidiSys.closeDevices();

		Logger.dbg(this,new Str("Destroyed"));
		return r;
	}
	
	protected void newComposition(String name) {
		Composition composition = new Composition();
		composition.name = name;
		stateManager.putAndPublishObject(COMPOSITION,composition);
	}
	
	protected void loadState(String name) {
		Composition composition = new Composition();
		composition.name = name;
		stateManager.putAndPublishObject(COMPOSITION,composition);
		
		NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
		config.directory = getCompositionDir();
		Network network = new Network();
		network.initializeAndLoad(config);
	}

	@Override
	public void changedState(String name, HashMap<String, Object> params) {
		if (name.equals(SETTINGS)) {
			// TODO Implement
		} else if (name.equals(BPM)) {
			int bpm = (int) stateManager.getObject(BPM);
			MidiSys.sequencePlayer.setBeatsPerMinute(bpm);
		} else if (name.equals(COMPOSITION)) {
			boolean load = (boolean) stateManager.getObject(LOAD_COMPOSITION);
			if (load) {
				getComposition().loadNetwork(getCompositionDir());
			} else {
				getComposition().initializeNetwork(getCompositionDir());
			}
		}
	}
}
