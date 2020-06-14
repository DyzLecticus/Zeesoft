package nl.zeesoft.zdk.test.midi;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.midi.Inst;
import nl.zeesoft.zdk.midi.MidiSys;
import nl.zeesoft.zdk.midi.NotePlayer;
import nl.zeesoft.zdk.midi.Patch;
import nl.zeesoft.zdk.midi.PatchConfig;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerManager;
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
		/*
		System.out.println("This test shows how to use different collections provided by this library. ");
		System.out.println("These collections are a mix between Java List and LinkedList style collections. ");
		System.out.println("They use Java reflection to provide features like queries and persistence with minimal programming. ");
		System.out.println("The following collections are provided by this library;  ");
		System.out.println(" * *QueryableCollection* provides support for queries.  ");
		System.out.println(" * *CompleteCollection* extends *QueryableCollection* and automatically adds referenced objects. ");
		System.out.println(" * *PersistableCollection* extends *CompleteCollection* and adds persistence. ");
		System.out.println(" * *CompressedCollection* extends *PersistableCollection* and adds compression to persistence. ");
		System.out.println(" * *PartitionedCollection* extends *CompressedCollection* and adds multithreading for saving/loading large collections to/from directories. ");
		System.out.println();
		System.out.println("Persistence for most standard property types is supported including arrays and lists for non primitives. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the PersistableCollection");
		System.out.println("PersistableCollection collection = new PersistableCollection();");
		System.out.println("// Add objects to the collection");
		System.out.println("Str id1 = collection.add(new PersistableParent());");
		System.out.println("Str id2 = collection.add(new PersistableChild());");
		System.out.println("// Write the data to a file");
		System.out.println("collection.toPath(\"data/fileName.txt\");");
		System.out.println("// Load the collection from a file");
		System.out.println("collection.fromPath(\"data/fileName.txt\");");
		System.out.println("// Query the collection");
		System.out.println("SortedMap<Str,Object> results = collection.query(");
		System.out.println("    Query.create(PersistableParent.class)");
		System.out.println("    .equals(\"getTestString\",\"Parent\")");
		System.out.println("    .notContains(\"getTestIntArray\",42)");
		System.out.println(").results;");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestMidiSystem.class));
		System.out.println(" * " + getTester().getLinkForClass(QueryableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(CompleteCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(CompressedCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PartitionedCollection.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the file structure of an example persistable collection.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
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
		
		PatchConfig config = MidiSys.getInstrumentConfig();
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
	}
}
