package nl.zeesoft.zmmt.test;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestSequencePlayer extends TestObject {
	public TestSequencePlayer(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSequencePlayer(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* Test is not included in set
		System.out.println("This test shows how to create a *Composition* instance and convert it to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create composition");
		System.out.println("Composition comp = new Composition();");
		System.out.println("// Convert to JSON");
		System.out.println("JsFile json = comp.toJson();");
		System.out.println("// Convert from JSON");
		System.out.println("comp.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSequencePlayer.class));
		System.out.println(" * " + getTester().getLinkForClass(Composition.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *Composition*.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		MockComposition comp = new MockComposition();
		
		Synthesizer synth = null; 
		Sequencer seq = null;
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			seq = MidiSystem.getSequencer(true);
			seq.open();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (seq!=null && synth!=null) {
			ZDKFactory factory = new ZDKFactory();
			
			Messenger msgr = factory.getMessenger();
			WorkerUnion uni = factory.getWorkerUnion(msgr);
			
			MockSequencePlayer player = new MockSequencePlayer(msgr,uni);
			player.setSequencer(seq);
			
			player.startWorkers();
			
			for (int i = 0; i < 3; i++) {
				player.setComposition(comp);
				sleep(1000);
				player.start();
				System.out.println("Started: " + player.isPlaying());
				
				sleep(1000);
				
				player.stop();
				System.out.println("Stopped");
			}
			
			player.stopWorkers();
						
			synth.close();
			seq.close();
		}
	}
}
