package nl.zeesoft.zdbd.test;

import java.util.List;

import nl.zeesoft.zdbd.neural.encoders.EncoderFactory;
import nl.zeesoft.zdbd.pattern.InstrumentPattern;
import nl.zeesoft.zdbd.pattern.PatternFactory;
import nl.zeesoft.zdbd.pattern.Rythm;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.network.Network;
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
		System.out.println("This test shows how a *Rythm* and *InstrumentPattern* are converted into SDRs for *Network* training. ");
		System.out.println("The rythm and pattern number are converted into 'context' SDRs. ");
		System.out.println("The pattern instruments are divided into two groups and then converted into SDRs. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the Rythm (default = 4/4)");
		System.out.println("Rythm rythm = new Rythm();");
		System.out.println("// Create the InstrumentPattern");
		System.out.println("InstrumentPattern pattern = PatternFactory.getFourOnFloorInstrumentPattern(0);");
		System.out.println("// Get the context SDRs");
		System.out.println("List<SDR> sdrs = rythm.getSDRsForPattern(pattern.num);");
		System.out.println("// Get the group 1 SDRs");
		System.out.println("sdrs = pattern.getSDRsForGroup(1,rythm.getStepsPerPattern());");
		System.out.println("// Get the group 2 SDRs");
		System.out.println("sdrs = pattern.getSDRsForGroup(2,rythm.getStepsPerPattern());");
		System.out.println("// Get the group 3 SDRs");
		System.out.println("sdrs = pattern.getSDRsForGroup(3,rythm.getStepsPerPattern());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(Rythm.class));
		System.out.println(" * " + getTester().getLinkForClass(InstrumentPattern.class));
		System.out.println(" * " + getTester().getLinkForClass(SDR.class));
		System.out.println(" * " + getTester().getLinkForClass(Network.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows all SDRs for a rythm and instrument pattern.  ");
	}

	@Override
	protected void test(String[] args) {
		Str err = EncoderFactory.testEncoders();
		assertEqual(err, new Str(), "Encoder factory error does not match expectation");
		
		Rythm rythm = new Rythm();
		InstrumentPattern pattern = PatternFactory.getFourOnFloorInstrumentPattern(0);
		
		List<SDR> sdrs = rythm.getSDRsForPattern(pattern.num);
		System.out.println("Context (" + Rythm.sizeX() + "*" + Rythm.sizeY() + ");");
		System.out.println("(Combines pattern number, beat and beat step into a single value)");
		displaySDRList(sdrs);
		
		sdrs = pattern.getSDRsForGroup(1,rythm.getStepsPerPattern());
		System.out.println();
		System.out.println("Group 1 pattern (" + InstrumentPattern.sizeX(1) + "*" + InstrumentPattern.sizeY(1) + ");");
		System.out.println("(Kick, Snare, Hihat, Cymbals)");
		displaySDRList(sdrs);
		
		sdrs = pattern.getSDRsForGroup(2,rythm.getStepsPerPattern());
		System.out.println();
		System.out.println("Group 2 pattern (" + InstrumentPattern.sizeX(2) + "*" + InstrumentPattern.sizeY(2) + ");");
		System.out.println("(Percussion, Bass, Octave, Note)");
		displaySDRList(sdrs);

		sdrs = pattern.getSDRsForGroup(3,rythm.getStepsPerPattern());
		System.out.println();
		System.out.println("Group 3 pattern (" + InstrumentPattern.sizeX(3) + "*" + InstrumentPattern.sizeY(3) + ");");
		System.out.println("(Stab, Shift)");
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
		int i = 0;
		for (SDR sdr: sdrs) {
			i++;
			sdr.flatten();
			System.out.println(String.format("%02d", i) + ": " + sdr.toVisualStr());
		}
	}
}
