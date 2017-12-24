package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestJson extends TestObject {
	public TestJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *JsFile* instance from a JSON string.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create JSON object");
		System.out.println("JsFile json = new JsFile();");
		System.out.println("// Parse JSON from string");
		System.out.println("json.fromStringBuilder(new ZStringBuilder(\"{\\\"command\\\":\\\"doStuff\\\"}\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestJson.class));
		System.out.println(" * " + getTester().getLinkForClass(JsFile.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the string input and the resulting JSON structure.  ");
	}

	@Override
	protected void test(String[] args) {
		ZStringBuilder testJson = new ZStringBuilder();
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
		System.out.println("Input:");
		System.out.println(testJson);
		JsFile json = new JsFile();
		json.fromStringBuilder(testJson);
		System.out.println("JSON structure:");
		System.out.println(json.toStringBuilderReadFormat());
		assertEqual(json.rootElement.children.size(),3,"Number of root element children does not match expectation");
		if (json.rootElement.children.size()==3) {
			assertEqual(json.rootElement.children.get(2).children.size(),6,"Number of third root element children does not match expectation");
			if (json.rootElement.children.get(2).children.size()==6) {
				assertEqual(json.rootElement.children.get(2).children.get(5).children.size(),2,"Number of object array element children does not match expectation");
			}
		}
	}
}
