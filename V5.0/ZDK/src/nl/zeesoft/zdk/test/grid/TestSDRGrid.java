package nl.zeesoft.zdk.test.grid;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.grid.SDRGrid;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;

public class TestSDRGrid extends TestObject {
	public TestSDRGrid(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSDRGrid(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		// TODO: Describe
		//System.out.println("This test is not yet included in the ZDK test set");
	}

	@Override
	protected void test(String[] args) {
		SDRGrid sdr = new SDRGrid();
		sdr.initialize(10, 10);
		sdr.setValue(0, 0, true);
		sdr.setValue(9, 9, true);
		
		Str str = sdr.toStr();
		System.out.println("SDR: " + str);

		sdr.flatten();
		System.out.println("SDR flattened: " + sdr.toStr());
		
		sdr.square();
		System.out.println("SDR squared: " + sdr.toStr());
		
		SDRGrid sdr2 = new SDRGrid();
		sdr2.fromStr(str);
		
		Str str2 = sdr2.toStr();
		assertEqual(str2, str, "SDR 2 does not match expectation");
	}
}
