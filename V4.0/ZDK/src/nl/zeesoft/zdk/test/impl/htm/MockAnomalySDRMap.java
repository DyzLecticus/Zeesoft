package nl.zeesoft.zdk.test.impl.htm;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.htm.sdr.SDR;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.htm.stream.StreamEncoder;
import nl.zeesoft.zdk.test.MockObject;

public class MockAnomalySDRMap extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockAnomalySDRMap*.");
	}

	@Override
	protected Object initialzeMock() {
		StreamEncoder enc = new StreamEncoder();
		enc.setValueMinMax(0,150);
		ZStringBuilder err = enc.testScalarOverlap(true);
		if (err.length()>0) {
			System.err.println(err);
		}
		SDRMap sdrMap = new SDRMap(enc.length());
		int maxValue = 0;
		@SuppressWarnings("unchecked")
		List<MockDateTimeValue> mockVals = (List<MockDateTimeValue>) getTester().getMockedObject(MockAnomalyDateTimeValues.class.getName());
		for (MockDateTimeValue mockVal: mockVals) {
			SDR sdr = enc.getSDRForValue(mockVal.value2);
			DateTimeSDR dts = new DateTimeSDR(sdr);
			dts.dateTime = mockVal.dateTime;
			dts.keyValues.put("value",mockVal.value2);
			sdrMap.add(dts);
			if (mockVal.value2>maxValue) {
				maxValue = mockVal.value2;
			}
		}
		return sdrMap;
	}
}
