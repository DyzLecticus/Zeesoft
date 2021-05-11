package nl.zeesoft.zdk.test.json;

import java.util.ArrayList;
import java.util.List;

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
		assert json.root.get("pizza") == null;
		
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
		assert child != null;
		json.root.children.remove(child);
		child = json.root.put("", "Pizza");
		assert child == null;
		child = json.root.put("testPut", "Pizza");
		assert child.value instanceof String;
		assert child.value.equals("Pizza");
		
		List<Object> arrayValues = new ArrayList<Object>();
		arrayValues.add(123);
		arrayValues.add(456);
		child = json.root.putArray("testPutArray", arrayValues);
		assert child.value == null;
		assert json.root.children.size() == 9;
		child = json.root.get(8).put(null, null);
		assert json.root.get(8).children.size() == 3;
		json.root.get(8).children.remove(child);
		
		str = json.toStringBuilderReadFormat();
		assert StrUtil.split(str, "\n").size() == 19;
		json.fromStringBuilder(str);
		assert json.root.children.size() == 9;
		assert json.root.get(6).children.size() == 2;
		
		str = new StringBuilder("{ \"test:edgeCase\": 12345678901234567890123 }");
		json.fromStringBuilder(str);
		assert json.root.children.size() == 1;
		assert json.toStringBuilder().toString().equals("{\"test:edgeCase\":\"12345678901234567890123\"}");

		str = new StringBuilder("/ \"test\": 123 \\");
		json = new Json(str);
		assert json.root.children.size() == 1;
		assert json.toStringBuilder().toString().equals("{\"/ \"test\":{}}");
		json.root.putArray("testEmptyArray",null);
		assert json.root.get("testEmptyArray").children.size() == 0;
		
		str = new StringBuilder("{ \"objectArray\": [ {\"name\": \"object1\"}, {\"name\": \"object2\", \"number\": 123} ] }");
		json.fromStringBuilder(str);
		assert json.root.children.size() == 1;
		assert json.root.get(0).isArray;
		assert json.root.get(0).children.size() == 2;
		assert json.root.get(0).get(0).get("name").value.toString().equals("object1");
		assert json.root.get(0).get(1).get("name").value.toString().equals("object2");
		assert (int)json.root.get(0).get(1).get("number").value == 123;
	}
}
