package nl.zeesoft.zdk.test.impl;

import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.http.ZHttpRequest;
import nl.zeesoft.zdk.test.MockObject;

public class MockZHttpRequest extends MockObject {

	@Override
	protected void describe() {
		System.out.println("This test uses the *MockZHttpRequest*.");
	}

	@Override
	protected Object initialzeMock() {
		ZHttpRequest http = new ZHttpRequest("GET","http://url.domain") {
			public ZStringBuilder sendRequest(ZStringBuilder body,SortedMap<String,String> headers) {
				return new ZStringBuilder("{\"response\":\"JSON response\"}");
			}
		};
		return http;
	}

}
