package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.sdr.DateTimeValue;
import nl.zeesoft.zdk.htm.sdr.DateTimeValueGenerator;
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
		SDRMap sdrMap = new SDRMap(enc.length());
		DateTimeValueGenerator generator = new DateTimeValueGenerator(7200000,0,42,1);
		int num = (12 * 7 * 365) / 2;
		int numAnomaly = num / 2;
		for (int i = 0; i < num; i++) {
			float addValue = 0;
			if (i>=numAnomaly) {
				addValue = 40;
			}
			DateTimeValue dtv = generator.getNextDateTimeValue(addValue);
			sdrMap.add(enc.getSDRForValue(dtv.dateTime,dtv.value,dtv.label));
		}
		return sdrMap;
	}
}
