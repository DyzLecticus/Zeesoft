package nl.zeesoft.zdk.test.json;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.StrUtil;
import nl.zeesoft.zdk.json.JElem;
import nl.zeesoft.zdk.json.Json;

public class TestJson {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Json json = new Json();
		assert json.toStringBuilder().toString().equals("{}");
		assert json.toStringBuilderReadFormat().toString().equals("{}");
		
		StringBuilder str = new StringBuilder();
		str.append(" { ");
		str.append("\"string\": \"String data\"");
		str.append(",\"int\": 2");
		str.append(",\"double\": 0.1");
		str.append(",\"boolean1\": true");
		str.append(",\"boolean2\": false");
		str.append(",\"object\": { \"objectString\": \"Object string data :,]}\" }");
		str.append(",\"array\": [ \"Array element 1\", \"Array element 2\" ]");
		str.append(" } ");
		
		json.fromStringBuilder(str);
		assert json.root.children.size() == 7;
		assert json.toStringBuilder().toString().equals("{\"string\":\"String data\",\"int\":2,\"double\":0.1,\"boolean1\":true,\"boolean2\":false,\"object\":{\"objectString\":\"Object string data :,]}\"},\"array\":[\"Array element 1\",\"Array element 2\"]}");
		
		JElem child = json.root.put(null, "Pizza");
		assert child == null;
		child = json.root.put("", "Pizza");
		assert child == null;
		child = json.root.put("testPut", "Pizza");
		assert child.value instanceof String;
		assert child.value.equals("Pizza");
		child = json.root.putArray("testPutArray", null);
		assert child.value == null;
		assert json.root.children.size() == 9;
		
		str = json.toStringBuilderReadFormat();
		assert StrUtil.split(str, "\n").size() == 16;
		json.fromStringBuilder(str);
		assert json.root.children.size() == 9;
		assert json.root.children.get(6).children.size() == 2;
		
		str = new StringBuilder("{ \"test:edgeCase\": 12345678901234567890123 }");
		json.fromStringBuilder(str);
		assert json.root.children.size() == 1;
		assert json.toStringBuilder().toString().equals("{\"test:edgeCase\":\"12345678901234567890123\"}");

		str = new StringBuilder("/ \"test\": 123 \\");
		json = new Json(str);
		assert json.root.children.size() == 1;
		assert json.toStringBuilder().toString().equals("{\"/ \"test\":{}}");
		
		str = new StringBuilder("{ \"objectArray\": [ {\"name\": \"object1\"}, {\"name\": \"object2\"} ] }");
		json.fromStringBuilder(str);
		assert json.root.children.size() == 1;
		assert json.root.children.get(0).isArray;
		assert json.root.children.get(0).children.size() == 2;
		assert json.root.children.get(0).children.get(0).children.get(0).value.toString().equals("object1");
		assert json.root.children.get(0).children.get(1).children.get(0).value.toString().equals("object2");
	}
}
