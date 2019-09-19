package nl.zeesoft.zdk.test.impl.htm;

import java.util.List;

import nl.zeesoft.zdk.htm.enc.ScalarEncoder;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.MockObject;

public class MockAnomalySDRSet extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockAnomalySDRSet*.");
	}

	@Override
	protected Object initialzeMock() {
		ScalarEncoder enc = new ScalarEncoder(256,16,0,200);
		SDRSet sdrSet = new SDRSet(enc.size());
		@SuppressWarnings("unchecked")
		List<MockDateTimeValue> mockVals = (List<MockDateTimeValue>) getTester().getMockedObject(MockAnomalyDateTimeValues.class.getName());
		for (MockDateTimeValue mockVal: mockVals) {
			SDR sdr = enc.getSDRForValue(mockVal.value2);
			DateTimeSDR dts = new DateTimeSDR(sdr);
			dts.dateTime = mockVal.dateTime;
			dts.keyValues.put("value",mockVal.value2);
			sdrSet.add(dts);
		}
		return sdrSet;
	}
}
