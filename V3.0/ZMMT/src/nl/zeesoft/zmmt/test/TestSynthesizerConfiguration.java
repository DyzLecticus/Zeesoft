package nl.zeesoft.zmmt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.syntesizer.SynthesizerConfiguration;

public class TestSynthesizerConfiguration extends TestObject {
	public TestSynthesizerConfiguration(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestSynthesizerConfiguration(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *SynthesizerConfiguration* instance and convert it to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create configuration");
		System.out.println("SynthesizerConfiguration conf = new SynthesizerConfiguration();");
		System.out.println("// Convert to JSON");
		System.out.println("JsFile json = conf.toJson();");
		System.out.println("// Convert from JSON");
		System.out.println("conf.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestSynthesizerConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(SynthesizerConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *SynthesizerConfiguration*.  ");
	}

	@Override
	protected void test(String[] args) {
		SynthesizerConfiguration conf = new SynthesizerConfiguration();
		JsFile json = conf.toJson();
		ZStringBuilder before = json.toStringBuilderReadFormat();
		conf.fromJson(json);
		ZStringBuilder after = conf.toJson().toStringBuilderReadFormat();
		assertEqual(after,before,"The after conversion JSON does not match expectation");
		System.out.println(after);
	}
}
