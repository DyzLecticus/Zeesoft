package nl.zeesoft.zdbd.test;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdbd.midi.MidiSequenceUtil;
import nl.zeesoft.zdbd.midi.lfo.ChannelLFO;
import nl.zeesoft.zdbd.midi.lfo.LFO;
import nl.zeesoft.zdbd.pattern.Rythm;
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
		/* TODO: Describe
		System.out.println("This test shows how a *Str* instance can be used to split a comma separated string into a list of *Str* instances. ");
		System.out.println("The *Str* class is designed to add features of the Java String to a Java StringBuilder. ");
		System.out.println("It also contains methods for file writing and reading. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Str");
		System.out.println("Str str = new Str(\"qwer,asdf,zxcv\");");
		System.out.println("// Split the Str");
		System.out.println("List<Str> strs = str.split(\",\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestController.class));
		System.out.println(" * " + getTester().getLinkForClass(Str.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and lists the *Str* objects.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		Rythm rythm = new Rythm();
		List<Float> values = LFO.getTickValuesForCycleSteps(rythm, LFO.SINE, 5);
		printValues(values);
		
		System.out.println();
		values = LFO.getTickValuesForCycleSteps(rythm, LFO.TRIANGLE, 3);
		printValues(values);
		
		System.out.println();
		ChannelLFO clfo = new ChannelLFO();
		values = clfo.getChangesForTicks(2 * MidiSequenceUtil.RESOLUTION);
		printValues(values);
	}
	
	private void printValues(List<Float> values) {
		DecimalFormat df = new DecimalFormat("0.000");
		System.out.println(values.size());
		int i = 0;
		for (Float val: values) {
			System.out.print(df.format(val) + " ");
			i++;
			if (i % 20==0) {
				System.out.println();
			}
		}
	}
}
