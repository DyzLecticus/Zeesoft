package nl.zeesoft.zdk.test.impl.htm;

import nl.zeesoft.zdk.htm.stream.StreamEncoder;
import nl.zeesoft.zdk.htm.util.DateTimeValue;
import nl.zeesoft.zdk.htm.util.DateTimeValueGenerator;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.test.MockObject;

public class MockRegularSDRMap extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockRegularSDRMap*.");
	}

	@Override
	protected Object initialzeMock() {
		StreamEncoder enc = new StreamEncoder();
		enc.setEncodeProperties(true,true,true,false,false,true);
		SDRMap sdrMap = new SDRMap(enc.length());
		DateTimeValueGenerator generator = new DateTimeValueGenerator(7200000,0,42,1);
		int num = (12 * 7 * 365) / 2;
		for (int i = 0; i < num; i++) {
			DateTimeValue dtv = generator.getNextDateTimeValue();
			sdrMap.add(enc.getSDRForValue(dtv.dateTime,dtv.value,dtv.label));
		}
		return sdrMap;
	}
}
