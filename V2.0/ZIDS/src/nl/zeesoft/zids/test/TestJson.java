package nl.zeesoft.zids.test;

import nl.zeesoft.zids.json.JsFile;

public class TestJson {

	public static void main(String[] args) {
		StringBuilder testJson = new StringBuilder();
		testJson.append("{\n");
		testJson.append("    \"qwerValue\" : \"qwerqwer\",\n");
		testJson.append("    \"qwerObject1\" : {\"qwerName\":\"name1\" ,\"qwerNumber\": 1234},\n");
		testJson.append("    \"qwerObject2\" : {\n");
		testJson.append("        \"qwerName\": \"name2\",\n");
		testJson.append("        \"qwerNumber\": 12345,\n");
		testJson.append("        \"qwerArray\": [],\n");
		testJson.append("        \"qwerSubObject1\": {qwerqwer:\"qwer qwer1\",qwertqwert:\"qwert qwert1\"},\n");
		testJson.append("        \"qwerSubObject2\": {qwerqwer:\"qwer qwer2\",qwertqwert:\"qwert qwert2\"},\n");
		testJson.append("        \"qwerObjectArray\": [{asdfasdf:\"asdf\",\"qwer\":[\"qwerqwer\",\"qwerqwerqwer\",\"qwerqwerqwerqwer\"]},{asdf:\"asdfasdf\"}]\n");
		testJson.append("    }\n");
		testJson.append("}\n");

		System.out.print("input: \n" + testJson);
		
		JsFile json = new JsFile();
		
		json.fromStringBuilder(testJson);
		
		System.out.print("From and to string builder: \n" + json.toStringBuilderReadFormat());
	}

}
