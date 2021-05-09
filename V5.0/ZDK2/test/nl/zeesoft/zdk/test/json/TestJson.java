package nl.zeesoft.zdk.test.json;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonExceptionHandler;

public class TestJson {
	private static Exception exception = null;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Json json = new Json();
		assert json.toStr().toString().equals("");
		
		StringBuilder str = new StringBuilder();
		str.append(" { ");
		str.append("\"string\": \"String data\"");
		str.append(",\"int\": 2");
		str.append(",\"float\": 0.1");
		str.append(",\"boolean1\": true");
		str.append(",\"boolean2\": false");
		str.append(",\"object\": { \"objectString\": \"Object string data\" }");
		str.append(",\"array\": [ \"Array element 1\", \"Array element 2\" ]");
		str.append(" } ");
		
		json.fromStr(str);
		
		assert json.root.children.size() == 7;
		assert json.toStr().toString().equals("{\"string\":\"String data\",\"int\":2,\"float\":0.1,\"boolean1\":true,\"boolean2\":false,\"object\":{\"objectString\":\"Object string data\"},\"array\":[\"Array element 1\",\"Array element 2\"]}");
		
		JElem child = json.root.put(null, "Pizza");
		assert child == null;
		child = json.root.put("", "Pizza");
		assert child == null;
		child = json.root.put("testAddChild", 123);
		assert child.value instanceof Integer;
		assert (int)child.value == 123;
		child = json.root.putArray("testAddChildArray", null);
		assert child.value == null;
		assert json.root.children.size() == 9;
		
		// Test error handling
		str.append("qwer");
		json.fromStr(str);
		assert json.root.children.size() == 0;
		assert exception == null;
		
		// Test default exception handler
		str.append("qwer");
		json.exceptionHandler = new JsonExceptionHandler();
		json.fromStr(str);
		assert json.root.children.size() == 0;
		assert exception == null;
		
		// Test custom exception handler
		JsonExceptionHandler handler = new JsonExceptionHandler() {
			@Override
			protected void handleException(Object source, Exception ex) {
				exception = ex;
			}
			
		};
		json.exceptionHandler = handler;
		json.fromStr(str);
		assert json.root.children.size() == 0;
		assert exception != null;
	}
}
