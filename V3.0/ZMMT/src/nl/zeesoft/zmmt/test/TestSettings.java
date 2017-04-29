package nl.zeesoft.zmmt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.gui.Settings;

public class TestSettings extends TestObject {
	public TestSettings(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSettings(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *Settings* instance and convert it to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create settings");
		System.out.println("Settings settings = new Settings();");
		System.out.println("// Convert to JSON");
		System.out.println("JsFile json = settings.toJson();");
		System.out.println("// Convert from JSON");
		System.out.println("settings.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSettings.class));
		System.out.println(" * " + getTester().getLinkForClass(Settings.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *Settings*.  ");
	}

	@Override
	protected void test(String[] args) {
		Settings settings = new Settings();
		JsFile json = settings.toJson();
		ZStringBuilder before = json.toStringBuilderReadFormat();
		settings.fromJson(json);
		ZStringBuilder after = settings.toJson().toStringBuilderReadFormat();
		assertEqual(after,before,"The after conversion JSON does not match expectation");
		System.out.println(after);
	}
}
