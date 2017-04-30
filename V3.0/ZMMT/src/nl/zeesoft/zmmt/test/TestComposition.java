package nl.zeesoft.zmmt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.DemoComposition;

public class TestComposition extends TestObject {
	public TestComposition(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestComposition(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *Composition* instance and convert it to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create composition");
		System.out.println("Composition comp = new Composition();");
		System.out.println("// Convert to JSON");
		System.out.println("JsFile json = comp.toJson();");
		System.out.println("// Convert from JSON");
		System.out.println("comp.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestComposition.class));
		System.out.println(" * " + getTester().getLinkForClass(Composition.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *Composition*.  ");
	}

	@Override
	protected void test(String[] args) {
		DemoComposition comp = new DemoComposition();
		JsFile json = comp.toJson();
		ZStringBuilder before = json.toStringBuilderReadFormat();
		comp.fromJson(json);
		ZStringBuilder after = comp.toJson().toStringBuilderReadFormat();
		assertEqual(after,before,"The after conversion JSON does not match expectation");
		System.out.println(after);
	}
}
