package nl.zeesoft.zdk.test.impl.htm;

import java.util.List;

import nl.zeesoft.zdk.htm.enc.DateTimeValuesEncoder;
import nl.zeesoft.zdk.htm.sdr.SDRMap;
import nl.zeesoft.zdk.test.MockObject;

public class MockRegularSDRMap extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockRegularSDRMap*.");
	}

	@Override
	protected Object initialzeMock() {
		DateTimeValuesEncoder enc = new DateTimeValuesEncoder();
		SDRMap sdrMap = new SDRMap(enc.length());
		@SuppressWarnings("unchecked")
		List<MockDateTimeValue> mockVals = (List<MockDateTimeValue>) getTester().getMockedObject(MockRegularDateTimeValues.class.getName());
		for (MockDateTimeValue mockVal: mockVals) {
			sdrMap.add(enc.getSDRForValue(mockVal.dateTime,mockVal.value1,mockVal.value2));
		}
		return sdrMap;
	}
}
