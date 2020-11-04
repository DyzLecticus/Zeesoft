package nl.zeesoft.zdbd;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.midi.LfoManager;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.ProgressBar;

public class Controller {
	private Settings	settings	= null;
	
	public Controller(Settings settings) {
		this.settings = settings;
	}
	
	public CodeRunnerChain initialize() {
		Logger.dbg(this,new Str("Initializing ..."));
		
		List<String> filePaths = new ArrayList<String>();
		if (settings.useInternalSyntesizers) {
			filePaths.add(settings.soundBankDir + "ZeeTrackerSynthesizers.sf2");
		}
		if (settings.useInternalDrumKit) {
			filePaths.add(settings.soundBankDir + "ZeeTrackerDrumKit.sf2");
		}
		CodeRunnerChain r = MidiSys.getCodeRunnerChainForSoundbankFiles(filePaths);
		r.addProgressListener(new ProgressBar("Loading soundbanks"));
		r.start();
		
		MidiSys.getLfoManager().start();
		
		Logger.dbg(this,new Str("Initialized"));
		return r;
	}
	
	public List<CodeRunner> destroy() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		Logger.dbg(this,new Str("Destroying ..."));

		LfoManager lfoManager = MidiSys.getLfoManager();
		lfoManager.stop();
		r = lfoManager.getRunners();
		
		MidiSys.closeDevices();

		Logger.dbg(this,new Str("Destroyed"));
		return r;
	}
}
