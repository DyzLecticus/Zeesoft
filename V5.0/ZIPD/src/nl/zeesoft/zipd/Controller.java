package nl.zeesoft.zipd;

import java.util.ArrayList;
import java.util.List;

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
		MidiSys.setLogger(settings.logger);
		settings.logger.debug(this,new Str("Initializing ..."));
		
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
		
		settings.logger.debug(this,new Str("Initialized"));
		return r;
	}
	
	public List<CodeRunner> destroy() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		settings.logger.debug(this,new Str("Destroying ..."));

		LfoManager lfoManager = MidiSys.getLfoManager();
		lfoManager.stop();
		r = lfoManager.getRunners();
		
		MidiSys.closeDevices();

		settings.logger.debug(this,new Str("Destroyed"));
		return r;
	}
}
