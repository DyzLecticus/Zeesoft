package nl.zeesoft.zdk.test.midi;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.midi.DrumInst;
import nl.zeesoft.zdk.midi.DrumPatch;
import nl.zeesoft.zdk.midi.LfoManager;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.NotePlayer;
import nl.zeesoft.zdk.midi.Patch;
import nl.zeesoft.zdk.midi.PatchFactory;
import nl.zeesoft.zdk.midi.PatternToSequenceConvertor;
import nl.zeesoft.zdk.midi.SequencePlayer;
import nl.zeesoft.zdk.midi.State;
import nl.zeesoft.zdk.midi.StateManager;
import nl.zeesoft.zdk.midi.SynthManager;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
import nl.zeesoft.zdk.thread.ProgressBar;
import nl.zeesoft.zdk.thread.Waiter;

public class TestMidiSys extends TestObject {
	private static final boolean	PLAY_NOTES		= false;
	private static final boolean	PLAY_SEQUENCES	= true;
	
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
		Logger.setLoggerDebug(true);
		
		// Load soundbanks
		CodeRunnerChain chain = MidiSys.getCodeRunnerChainForSoundbankFiles(
			"../../V3.0/ZeeTracker/resources/ZeeTrackerSynthesizers.sf2",
			"../../V3.0/ZeeTracker/resources/ZeeTrackerDrumKit.sf2"
		);
		chain.addProgressListener(new ProgressBar("Loading soundbanks"));
		Waiter.startAndWaitFor(chain,3000);
		
		State state = new State();
		state.beatsPerMinute = 100;
		state.stepDelayPercentages[1] = 0.2F;
		state.stepDelayPercentages[3] = 0.2F;
		
		StateManager stateManager = MidiSys.getStateManager();
		stateManager.setState(state);

		// Initialize default LFOs
		LfoManager lfoManager = MidiSys.getLfoManager();
		lfoManager.initializeDefaultLfos();
		lfoManager.start();
		
		// Create patch
		String patchName = "EchoTest";
		Patch patch = PatchFactory.getEchoSaw(patchName,3);
		
		// Load patch
		SynthManager synthManager = MidiSys.getSynthManager();
		synthManager.addPatch(patch);
		Str error = synthManager.loadPatch(patch.name);
		assertEqual(error,new Str(),"Error message does not match expectation");
		assertEqual(synthManager.getAvailableInstrumentChannels().size(),11,"Number of available channels does not match expectation(1)");
		
		// Get patch (instrument channels have been assigned)
		patch = synthManager.getPatch(patchName);
		assertNotNull(patch,"Patch not found: " + patchName);
		if (assertEqual(patch.instruments.size(),4,"Number of patch instruments does not match expectation")) {
			assertEqual(patch.instruments.get(3).channel,3,"Assigned instrument channel does not match expectation");
		}
		
		NotePlayer player = MidiSys.getNotePlayer();
		
		if (PLAY_NOTES) {
			// Play some notes
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
		}
		
		if (patch.instruments.size()==4) {
			synthManager.removeInstrumentFromPatch(patchName, patch.instruments.get(3).channel);
			assertEqual(synthManager.getAvailableInstrumentChannels().size(),12,"Number of available channels does not match expectation(2)");
		}
		
		synthManager.unloadPatch(patchName);
		assertEqual(synthManager.getAvailableInstrumentChannels().size(),15,"Number of available channels does not match expectation(3)");

		String drumPatchName = "DrumKit";
		DrumPatch dp = PatchFactory.getDefaultDrumPatch(drumPatchName);
		synthManager.addPatch(dp);
		synthManager.loadPatch(dp.name);
		
		dp = (DrumPatch) synthManager.getPatch(drumPatchName);
		DrumInst di = dp.getDrumInstrument();
		assertEqual(di.instrument,118,"Drum kit instrument number does not match expectation");
		assertEqual(di.channel,9,"Drum kit channel number does not matche expectation");
		assertEqual(synthManager.listPatches().size(),2,"Number of patches does not match expectation");
		assertEqual(synthManager.listLoadedPatches().size(),1,"Number of loaded patches does not match expectation");
		
		if (PLAY_NOTES) {
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
		}
		
		PatternToSequenceConvertor convertor = new PatternToSequenceConvertor(stateManager, synthManager);
		Sequence sequence = convertor.generateSequenceForPattern(drumPatchName, dp.patterns[0]);
		
		SequencePlayer sequencePlayer = MidiSys.getSequencPlayer();
		sequencePlayer.setSequence(sequence);
	
		if (PLAY_SEQUENCES) {
			// Play the drum sequence
			System.out.println();
			System.out.println("Playing drum pattern ...");
			sequencePlayer.start();
			sleep(10000);
			sequencePlayer.stop();
			System.out.println("Played drum pattern");
		}
		
		MidiSys.closeDevices();
		
		lfoManager.stop();
		Waiter.waitForRunners(lfoManager.getRunners(),100);
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
	}
}
