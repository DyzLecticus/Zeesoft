package nl.zeesoft.zspp.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.lang.Languages;
import nl.zeesoft.zspp.prepro.PreprocessorRequestResponse;

public class TestPreprocessorRequestResponse extends TestObject {
	public TestPreprocessorRequestResponse(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPreprocessorRequestResponse(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert *PreprocessorRequestResponse* instances to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the PreprocessorRequestResponse");
		System.out.println("PreprocessorRequestResponse request = new PreprocessorRequestResponse();");
		System.out.println("// Convert the PreprocessorRequestResponse to JSON");
		System.out.println("JsFile json = request.toJson();");
		System.out.println("// Convert the PreprocessorRequestResponse from JSON");
		System.out.println("request.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPreprocessorRequestResponse.class));
		System.out.println(" * " + getTester().getLinkForClass(PreprocessorRequestResponse.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		PreprocessorRequestResponse request = new PreprocessorRequestResponse();
		request.languages.add(Languages.ENG);
		request.sequence.append("sequence of symbols");
		JsFile json = request.toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		request = new PreprocessorRequestResponse();
		request.fromJson(json);
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!newStr.equals(oriStr)) {
			assertEqual(false,true,"Converted JSON does not match original");
			System.err.println(newStr);
		}
	}
}
