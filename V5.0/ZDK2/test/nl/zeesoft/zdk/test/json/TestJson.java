package nl.zeesoft.zdk.test.json;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.StrUtil;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonExceptionHandler;

public class TestJson {
	private static Exception exception = null;
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Json json = new Json();
		assert json.toStringBuilder().toString().equals("");
		assert json.toStringBuilderReadFormat().toString().equals("");
		
		StringBuilder str = new StringBuilder();
		str.append(" { ");
		str.append("\"string\": \"String data\"");
		str.append(",\"int\": 2");
		str.append(",\"double\": 0.1");
		str.append(",\"boolean1\": true");
		str.append(",\"boolean2\": false");
		str.append(",\"object\": { \"objectString\": \"Object string data\" }");
		str.append(",\"array\": [ \"Array element 1\", \"Array element 2\" ]");
		str.append(" } ");
		
		json.fromStringBuilder(str);
		
		assert json.root.children.size() == 7;
		assert json.toStringBuilder().toString().equals("{\"string\":\"String data\",\"int\":2,\"double\":0.1,\"boolean1\":true,\"boolean2\":false,\"object\":{\"objectString\":\"Object string data\"},\"array\":[\"Array element 1\",\"Array element 2\"]}");
		
		JElem child = json.root.put(null, "Pizza");
		assert child == null;
		child = json.root.put("", "Pizza");
		assert child == null;
		child = json.root.put("testPut", 123);
		assert child.value instanceof Integer;
		assert (int)child.value == 123;
		child = json.root.putArray("testPutArray", null);
		assert child.value == null;
		assert json.root.children.size() == 9;
		
		str = json.toStringBuilderReadFormat();
		assert StrUtil.split(str, "\n").size() == 16;
		json.fromStringBuilder(str);
		assert json.root.children.size() == 9;
		assert json.root.children.get(6).children.size() == 2;
		
		// Test error handling
		str.append("qwer");
		json.fromStringBuilder(str);
		assert json.root.children.size() == 0;
		assert exception == null;
		
		// Test default exception handler
		str.append("qwer");
		json.exceptionHandler = new JsonExceptionHandler();
		json.fromStringBuilder(str);
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
		json.fromStringBuilder(str);
		assert json.root.children.size() == 0;
		assert exception != null;
	}
}
