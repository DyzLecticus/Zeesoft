package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.test.MockObject;

public class MockStr extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockStr*.");
	}

	@Override
	protected Object initialzeMock() {
		return new Str("\t  qwer,asdf,zxcv \r\n");
	}
}
