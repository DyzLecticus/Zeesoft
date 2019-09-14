package nl.zeesoft.zdk.test.impl;

import java.util.List;

import nl.zeesoft.zdk.htm.enc.DateTimeValueEncoder;
import nl.zeesoft.zdk.htm.sdr.SDRSet;
import nl.zeesoft.zdk.test.MockObject;

public class MockRegularSDRSet extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockRegularSDRSet*.");
	}

	@Override
	protected Object initialzeMock() {
		DateTimeValueEncoder enc = new DateTimeValueEncoder();
		SDRSet sdrSet = new SDRSet(enc.size());
		@SuppressWarnings("unchecked")
		List<MockDateTimeValue> mockVals = (List<MockDateTimeValue>) getTester().getMockedObject(MockRegularDateTimeValues.class.getName());
		for (MockDateTimeValue mockVal: mockVals) {
			sdrSet.add(enc.getSDRForValue(mockVal.dateTime,mockVal.value1,mockVal.value2));
		}
		return sdrSet;
	}
}
