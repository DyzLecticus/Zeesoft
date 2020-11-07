package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdbd.pattern.DrumPattern;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestEncoders extends TestObject {
	public TestEncoders(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEncoders(new Tester())).runTest(args);
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
		Str err = EncoderFactory.testEncoders();
		assertEqual(err, new Str(), "Encoder factory error does not match expectation");
		
		DrumPattern pattern = PatternFactory.getFourOnFloor(0);
		
		List<SDR> sdrs = pattern.rythm.getSDRsForPattern(pattern.num);
		System.out.println("Context (" + Rythm.sizeX() + "*" + Rythm.sizeY() + ");");
		displaySDRList(sdrs);
		
		sdrs = pattern.getSDRsForPattern();
		System.out.println();
		System.out.println("Drum pattern (" + DrumPattern.sizeX() + "*" + DrumPattern.sizeY() + ");");
		displaySDRList(sdrs);
	}
	
	private void displaySDRList(List<SDR> sdrs) {
		for (SDR sdr: sdrs) {
			sdr.flatten();
			System.out.println(sdr.toVisualStr());
		}
	}
}
