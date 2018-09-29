package nl.zeesoft.znlb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.znlb.ZNLBConfig;
import nl.zeesoft.znlb.context.ContextConfig;

public class TestContextConfig extends TestObject {
	public TestContextConfig(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestContextConfig(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to convert *ContextConfig* instance languages to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the context configuration");
		System.out.println("ContextConfig contextConfig = new ContextConfig(new ZNLBConfig());");
		System.out.println("// Initialize the context configuration");
		System.out.println("contextConfig.initializeDatabaseObjects();");
		System.out.println("// Convert a context configuration language to JSON");
		System.out.println("JsFile json = contextConfig.getLanguages().get(0).toJson();");
		System.out.println("// Convert the context configuration language from JSON");
		System.out.println("contextConfig.getLanguages().get(0).fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestContextConfig.class));
		System.out.println(" * " + getTester().getLinkForClass(ContextConfig.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
	}
	
	@Override
	protected void test(String[] args) {
		ContextConfig contextConfig = new ContextConfig(new ZNLBConfig());
		contextConfig.initializeDatabaseObjects();
		
		JsFile json = contextConfig.getLanguages().get(0).toJson();
		ZStringBuilder oriStr = json.toStringBuilderReadFormat();
		System.out.println(oriStr);
		
		contextConfig = new ContextConfig(new ZNLBConfig());
		contextConfig.initializeDatabaseObjects();
		contextConfig.getLanguages().get(0).fromJson(json);
		json = contextConfig.getLanguages().get(0).toJson();
		ZStringBuilder newStr = json.toStringBuilderReadFormat();
		if (!newStr.equals(oriStr)) {
			assertEqual(false,true,"Converted JSON does not match original");
			System.err.println(newStr);
		}
	}
}
