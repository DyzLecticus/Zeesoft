package nl.zeesoft.zmmt.test;

import java.util.Date;

import javax.sound.midi.Sequence;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;

public class TestCompositionToSequenceConvertor extends TestObject {
	public TestCompositionToSequenceConvertor(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCompositionToSequenceConvertor(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert a *Composition* to a *Sequence*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create composition");
		System.out.println("Composition comp = new Composition();");
		System.out.println("// Create convertor");
		System.out.println("CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(comp);");
		System.out.println("// Convert to Sequence");
		System.out.println("convertor.convertSequence(false,false);");
		System.out.println("// Get sequence from convertor");
		System.out.println("Sequence seq = convertor.getSequence();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCompositionToSequenceConvertor.class));
		System.out.println(" * " + getTester().getLinkForClass(CompositionToSequenceConvertor.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the time it takes to convert a simple composition, with and without side chain compression.  ");
	}

	@Override
	protected void test(String[] args) {
		long now = 0;
		MockComposition comp = new MockComposition();
		CompositionToSequenceConvertor convertor = new CompositionToSequenceConvertor(comp);
		Sequence seq = null;
		
		convertor.convertSequence(false,false,true);
		convertor.convertSequence(false,false,true);
		convertor.convertSequence(false,false,true);
		convertor.convertSequence(false,false,true);
		convertor.convertSequence(false,false,true);
		
		now = (new Date()).getTime();
		convertor.convertSequence(false,false,true);
		seq = convertor.getSequence();
		assertEqual(seq!=null,true,"Failed to convert composition to sequence");
		if (seq!=null) {
			assertEqual(seq.getTracks().length,11,"Number of sequence tracks does not meet expectation");
			if (seq.getTracks().length>=2) {
				assertEqual(seq.getTracks()[0].size(),5,"Number of track 0 events does not meet expectation");
				assertEqual(seq.getTracks()[1].size(),1604,"Number of track 1 events does not meet expectation");
			}
		}
		System.out.println("Composition to sequence conversion with side chain compression took " + ((new Date()).getTime() - now) + " ms");

		convertor.convertSequence(false,false,false);
		convertor.convertSequence(false,false,false);
		convertor.convertSequence(false,false,false);
		convertor.convertSequence(false,false,false);
		convertor.convertSequence(false,false,false);

		now = (new Date()).getTime();
		convertor.convertSequence(false,false,false);
		seq = convertor.getSequence();
		assertEqual(seq!=null,true,"Failed to convert composition to sequence");
		if (seq!=null) {
			assertEqual(seq.getTracks().length,11,"Number of sequence tracks does not meet expectation");
			if (seq.getTracks().length>=2) {
				assertEqual(seq.getTracks()[0].size(),5,"Number of track 0 events does not meet expectation");
				assertEqual(seq.getTracks()[1].size(),666,"Number of track 1 events does not meet expectation");
			}
		}
		System.out.println("Composition to sequence conversion without side chain compression took " + ((new Date()).getTime() - now) + " ms");
	}
}
