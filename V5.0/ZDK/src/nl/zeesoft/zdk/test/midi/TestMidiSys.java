package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.DrumInst;
import nl.zeesoft.zdk.midi.DrumPatch;
import nl.zeesoft.zdk.midi.Inst;
import nl.zeesoft.zdk.midi.Lfo;
import nl.zeesoft.zdk.midi.LfoManager;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.NotePlayer;
import nl.zeesoft.zdk.midi.Patch;
import nl.zeesoft.zdk.midi.PatchConfig;
import nl.zeesoft.zdk.midi.State;
import nl.zeesoft.zdk.midi.StateManager;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.Waiter;

public class TestMidiSys extends TestObject {
	public TestMidiSys(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestMidiSys(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		Logger logger = new Logger(true);
		MidiSys.setLogger(logger);

		// Load soundbanks
		CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(
			"../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2",
			"../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2"
		);
		chain.addProgressListener(new ProgressBar("Loading soundbanks"));
		Waiter.startAndWaitFor(chain,3000);
		
		State state = new State();
		state.beatsPerMinute = 100;
		state.stepDelayPercentages[1] = 0.3F;
		state.stepDelayPercentages[3] = 0.3F;
		
		StateManager stateManager = MidiSys.getStateManager();
		stateManager.setState(state);

		// Create some LFOs
		LfoManager lfoManager = MidiSys.getLfoManager();		
		Lfo lfo1 = new Lfo();
		Lfo lfo2 = new Lfo(Lfo.BINARY);
		lfoManager.setLfo(0,lfo1);
		lfoManager.setLfo(1,lfo2);
		lfoManager.start();
		
		// Create some instruments
		Inst inst1 = new Inst();

		Inst inst2 = new Inst();
		inst2.velocity = 60;
		inst2.filter = 48;
		inst2.pan = 0;
		inst2.reverb = 24;
		inst2.patchDelaySteps = 3;
		
		Inst inst3 = new Inst();
		inst3.velocity = 40;
		inst3.filter = 40;
		inst3.pan = 127;
		inst3.reverb = 36;
		inst3.patchDelaySteps = 6;
		
		Inst inst4 = new Inst();
		inst4.velocity = 20;
		inst4.filter = 32;
		inst4.reverb = 48;
		inst4.patchDelaySteps = 9;
		
		// Connect instrument properties to LFOs
		inst1.setLfoSource(Inst.FILTER,0,0.3F,true);
		inst2.setLfoSource(Inst.FILTER,0,0.25F,true);
		inst3.setLfoSource(Inst.FILTER,0,0.20F,true);
		inst4.setLfoSource(Inst.FILTER,0,0.15F,true);

		// Create instrument patch
		String patchName = "EchoTest";
		Patch patch = new Patch(patchName);
		patch.instruments.add(inst1);
		patch.instruments.add(inst2);
		patch.instruments.add(inst3);
		patch.instruments.add(inst4);
		
		// Load instrument patch
		PatchConfig config = MidiSys.getPatchConfig();
		config.loadPatch(patch);
		assertEqual(config.getAvailableInstrumentChannels().size(),11,"Number of available channels does not match expectation(1)");
		
		// Get instrument patch (instrument channels have been assigned)
		patch = config.getPatch(patchName);
		assertNotNull(patch,"Patch not found: " + patchName);
		if (assertEqual(patch.instruments.size(),4,"Number of patch instruments does not match expectation")) {
			assertEqual(patch.instruments.get(3).channel,3,"Assigned instrument channel does not match expectation");
		}
		
		// Play some notes
		NotePlayer player = MidiSys.getNotePlayer();
		System.out.println();
		System.out.println("Playing notes F-4, A#4 and C-5 ...");
		player.startNotes(patchName,"F-4");
		sleep(100);
		player.startNotes(patchName,"A#4");
		sleep(100);
		player.startNotes(patchName,"C-5");
		sleep(100);
		player.stopNotes(patchName,"F-4","A#4","C-5");
		
		// Wait until the delayed notes have been played
		Waiter.waitForRunners(player.getDelayedPlayers(),5000);
		assertEqual(player.getDelayedPlayers().size(),0,"Number of delayed note players does not match expectation");
		sleep(500);
		System.out.println("Played notes");
		
		if (patch.instruments.size()==4) {
			config.removeInstrumentFromPatch(patchName, patch.instruments.get(3).channel);
			assertEqual(config.getAvailableInstrumentChannels().size(),12,"Number of available channels does not match expectation(2)");
		}
		
		config.unloadPatch(patchName);
		assertEqual(config.getAvailableInstrumentChannels().size(),15,"Number of available channels does not match expectation(3)");

		String drumPatchName = "DrumKit";
		DrumPatch dp = new DrumPatch(drumPatchName);
		config.loadPatch(dp);
		
		dp = (DrumPatch) config.getPatch(drumPatchName);
		DrumInst di = dp.getDrumInstrument();
		assertEqual(di.instrument,118,"Drum kit instrument number does not match expectation");
		assertEqual(di.channel,9,"Drum kit channel number does not matche expectation");
		
		// Play some drum notes
		System.out.println();
		System.out.println("Playing drum notes C-3 and C#3 ...");
		player.startNotes(drumPatchName,"C-3");
		sleep(500);
		player.stopNotes(drumPatchName,"C-3");
		player.startNotes(drumPatchName,"C#3");
		sleep(500);
		player.stopNotes(drumPatchName,"C#3");
		player.startNotes(drumPatchName,"C-3");
		sleep(500);
		player.stopNotes(drumPatchName,"C-3");
		player.startNotes(drumPatchName,"C#3");
		sleep(500);
		player.stopNotes(drumPatchName,"C#3");
		System.out.println("Played drum notes");
		
		lfoManager.stop();
		Waiter.waitForRunners(lfoManager.getRunners(),100);
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
