package nl.zeesoft.zsd.test;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.BaseConfiguration;

public class TestBaseConfigurationToJson extends TestEntityToJson {
	public TestBaseConfigurationToJson(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestBaseConfigurationToJson(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test shows how to convert a *BaseConfiguration* instance to JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the list of dialogs");
		System.out.println("DialogSet ds = new DialogSet();");
		System.out.println("// Initialize the EntityValueTranslator");
		System.out.println("ds.initialize();");
		System.out.println("// Create the DialogToJson instance");
		System.out.println("DialogToJson convertor = new DialogToJson();");
		System.out.println("// Convert the dialogs to JSON");
		System.out.println("JsFile json = convertor.getJsonForDialogs(ds.getDialogs());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestBaseConfigurationToJson.class));
		System.out.println(" * " + getTester().getLinkForClass(BaseConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		BaseConfiguration bc = new BaseConfiguration();
		JsFile json = bc.toJson();
		System.out.println(json.toStringBuilderReadFormat());
	}
}
