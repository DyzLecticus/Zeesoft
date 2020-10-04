package nl.zeesoft.zdk.test.grid;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.HistoricalGrid;
import nl.zeesoft.zdk.grid.SDR;
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
		SDR sdr = new SDR();
		sdr.initialize(10, 10);
		sdr.setValue(0, 0, true);
		sdr.setValue(9, 9, true);
		
		Str str = sdr.toStr();
		System.out.println("SDR: " + str);

		sdr.flatten();
		System.out.println("SDR flattened: " + sdr.toStr());
		
		sdr.square();
		System.out.println("SDR squared: " + sdr.toStr());
		
		SDR sdr2 = new SDR();
		sdr2.fromStr(str);
		
		Str str2 = sdr2.toStr();
		assertEqual(str2, str, "SDR 2 does not match expectation");
		
		HistoricalGrid history = new HistoricalGrid();
		history.initialize(10,10,10);

		history.update(sdr);

		history.cycle();

		sdr2 = new SDR();
		sdr2.initialize(10, 10);
		sdr2.setValue(50, 50, true);

		history.update(sdr2);
		assertEqual((boolean)history.getColumn(0).getValue(0), false, "Historical grid column value 0 does not match expectation");
		assertEqual((boolean)history.getColumn(0).getValue(1), true, "Historical grid column value 1 does not match expectation");
		assertEqual(history.getColumn(0).getAverageValue(), 0.1F, "Historical grid average column value does not match expectation");
	}
}
