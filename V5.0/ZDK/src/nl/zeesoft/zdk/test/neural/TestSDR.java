package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.neural.SDR;
import nl.zeesoft.zdk.neural.SDRHistory;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestSDR extends TestObject {
	public TestSDR(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSDR(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		//System.out.println("This test is not yet included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		SDR sdr = new SDR(4, 4);
		sdr.setBit(0, true);
		sdr.setBit(7, true);
		sdr.setBit(8, true);
		sdr.setBit(15, true);
		assertEqual(sdr.onBits(), 4, "Number of on bits does not match expectation");
		assertEqual(sdr.getBit(3, 1), true, "Bit state does not match expectation");
		
		Str str = sdr.toStr();
		
		System.out.println("SDR: " + str);
		
		System.out.println();
		System.out.println("Visualized;");
		System.out.println(sdr.toVisualStr());
		
		sdr.flatten();
		assertEqual(sdr.sizeX(), 16, "Flattened sizeX does not match expectation");
		assertEqual(sdr.sizeY(), 1, "Flattened sizeY does not match expectation");
		
		sdr.square();
		assertEqual(sdr.sizeX(), 4, "Squared sizeX does not match expectation");
		assertEqual(sdr.sizeY(), 4, "Squared sizeX does not match expectation");
		
		SDR sdr2 = new SDR();
		sdr2.fromStr(str);
		
		assertEqual(sdr2.toStr(),str,"SDR 2 Str does not match expectation");
		
		SDRHistory hist = new SDRHistory(4, 4, 4);
		
		hist.addSDR(sdr);
		
		sdr2 = new SDR(4, 4);
		sdr2.setBit(0, true);
		
		hist.addSDR(sdr2);
		
		assertEqual(hist.getAverage(0),1F,"Average for bit 0 does not match expectation");
		assertEqual(hist.getAverage(15),0.5F,"Average for bit 15 does not match expectation");
		assertEqual(hist.getTotalAverage(),0.15625F,"Total average does not match expectation");
	}
}
