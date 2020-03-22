package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.grid.enc.ZGridEncoderValue;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.test.MockObject;

public class MockRegularSDRMap2 extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockRegularSDRMap2*.");
	}

	@Override
	protected Object initialzeMock() {
		ZGridEncoderValue enc = new ZGridEncoderValue();
		SDRMap sdrMap = new SDRMap(enc.length());
		for (int c = 1; c <= 300; c++) {
			for (int r = 1; r <= 10; r++) {
				sdrMap.add(enc.getSDRForIntegerValue(r));
			}
		}
		return sdrMap;
	}
}
