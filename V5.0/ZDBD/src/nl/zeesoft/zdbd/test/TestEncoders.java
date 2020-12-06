package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
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
		
		Rythm rythm = new Rythm();
		InstrumentPattern pattern = PatternFactory.getFourOnFloorInstrumentPattern(0);
		
		List<SDR> sdrs = rythm.getSDRsForPattern(pattern.num);
		System.out.println("Context (" + Rythm.sizeX() + "*" + Rythm.sizeY() + ");");
		displaySDRList(sdrs);
		
		sdrs = pattern.getSDRsForGroup(1,rythm.getStepsPerPattern());
		System.out.println();
		System.out.println("Group 1 pattern (" + InstrumentPattern.sizeX(1) + "*" + InstrumentPattern.sizeY(1) + ");");
		displaySDRList(sdrs);
		
		sdrs = pattern.getSDRsForGroup(2,rythm.getStepsPerPattern());
		System.out.println();
		System.out.println("Group 2 pattern (" + InstrumentPattern.sizeX(2) + "*" + InstrumentPattern.sizeY(2) + ");");
		displaySDRList(sdrs);
		
		assertEqual(InstrumentPattern.getValueForDuration(0,false),0,"Note does not match expectation (1)");
		assertEqual(InstrumentPattern.getValueForDuration(1,false),1,"Note does not match expectation (2)");
		assertEqual(InstrumentPattern.getValueForDuration(1,true),2,"Note does not match expectation (3)");
		assertEqual(InstrumentPattern.getValueForDuration(2,false),3,"Note does not match expectation (4)");
		assertEqual(InstrumentPattern.getValueForDuration(2,true),4,"Note does not match expectation (5)");
		
		assertEqual(InstrumentPattern.getDurationForValue(0),0,"Duration does not match expectation (1)");
		assertEqual(InstrumentPattern.getDurationForValue(1),1,"Duration does not match expectation (2)");
		assertEqual(InstrumentPattern.getDurationForValue(2),1,"Duration does not match expectation (3)");
		assertEqual(InstrumentPattern.getDurationForValue(3),2,"Duration does not match expectation (4)");
		assertEqual(InstrumentPattern.getDurationForValue(4),2,"Duration does not match expectation (5)");
		
		assertEqual(InstrumentPattern.isAccent(0),false,"Accent does not match expectation (1)");
		assertEqual(InstrumentPattern.isAccent(3),false,"Accent does not match expectation (2)");
		assertEqual(InstrumentPattern.isAccent(4),true,"Accent does not match expectation (3)");
	}
	
	private void displaySDRList(List<SDR> sdrs) {
		for (SDR sdr: sdrs) {
			sdr.flatten();
			System.out.println(sdr.toVisualStr());
		}
	}
}
