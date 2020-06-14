package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestLFO extends TestObject {
	public TestLFO(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLFO(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test is not included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		/*
		Logger logger = new Logger(true);
		MidiSys.setLogger(logger);
		
		Inst inst1 = new Inst();

		Inst inst2 = new Inst();
		inst2.velocity = 75;
		inst2.filter = 48;
		inst2.pan = 0;
		inst2.reverb = 24;
		inst2.patchDelaySteps = 3;
		
		Inst inst3 = new Inst();
		inst3.velocity = 60;
		inst3.filter = 40;
		inst3.pan = 127;
		inst2.reverb = 36;
		inst3.patchDelaySteps = 6;
		
		Inst inst4 = new Inst();
		inst4.velocity = 45;
		inst4.filter = 32;
		inst2.reverb = 48;
		inst4.patchDelaySteps = 9;
		
		String patchName = "EchoTest";
		Patch patch = new Patch(patchName);
		patch.instruments.add(inst1);
		patch.instruments.add(inst2);
		patch.instruments.add(inst3);
		patch.instruments.add(inst4);
		
		PatchConfig config = MidiSys.getPatchConfig();
		config.loadPatch(patch);
		assertEqual(config.getAvailableInstrumentChannels().size(),11,"Number of available channels does not match expectation(1)");
		
		patch = config.getPatch(patchName);
		assertNotNull(patch,"Patch not found: " + patchName);
		if (assertEqual(patch.instruments.size(),4,"Number of patch instruments does not match expectation")) {
			assertEqual(patch.instruments.get(3).channel,3,"Assigned instrument channel does not match expectation");
		}
		
		NotePlayer player = MidiSys.getNotePlayer();
		
		System.out.println("Playing notes F-4, A#4 and C-5 ...");
		player.startNotes(patchName,"F-4");
		sleep(100);
		player.startNotes(patchName,"A#4");
		sleep(100);
		player.startNotes(patchName,"C-5");
		sleep(100);
		player.stopNotes(patchName,"F-4","A#4","C-5");
		
		Waiter.waitTillRunnersDone(player.getDelayedPlayers(),3000);
		assertEqual(player.getDelayedPlayers().size(),0,"Number of delayed note players does not match expectation");
		sleep(500);
		System.out.println("Played notes");
		
		if (patch.instruments.size()==4) {
			config.removeInstrumentFromPatch(patchName, patch.instruments.get(3).channel);
			assertEqual(config.getAvailableInstrumentChannels().size(),12,"Number of available channels does not match expectation(2)");
		}
		
		config.unloadPatch(patchName);
		assertEqual(config.getAvailableInstrumentChannels().size(),15,"Number of available channels does not match expectation(3)");
		
		assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
		*/
	}
}
