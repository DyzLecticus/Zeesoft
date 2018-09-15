package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.zodb.lang.Languages;

public class TestTranslatorRequestResponse extends TestObject {
	public TestTranslatorRequestResponse(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTranslatorRequestResponse(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert *TranslatorRequestResponse* instances to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the TranslatorRequestResponse");
		System.out.println("TranslatorRequestResponse request = new TranslatorRequestResponse();");
		System.out.println("// Convert the TranslatorRequestResponse to JSON");
		System.out.println("JsFile json = request.toJson();");
		System.out.println("// Convert the TranslatorRequestResponse from JSON");
		System.out.println("request.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTranslatorRequestResponse.class));
		System.out.println(" * " + getTester().getLinkForClass(TranslatorRequestResponse.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		TranslatorRequestResponse request = new TranslatorRequestResponse();
		request.languages.add(Languages.ENG);
		request.sequence.append("sequence of symbols");
		JsFile json = request.toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		request = new TranslatorRequestResponse();
		request.fromJson(json);
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!newStr.equals(oriStr)) {
			assertEqual(false,true,"Converted JSON does not match original");
			System.err.println(newStr);
		}
	}
}
