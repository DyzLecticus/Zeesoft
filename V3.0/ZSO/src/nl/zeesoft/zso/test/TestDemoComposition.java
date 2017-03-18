package nl.zeesoft.zso.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zso.composition.DemoComposition;

public class TestDemoComposition extends TestObject {
	public TestDemoComposition(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDemoComposition(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *DemoComposition* instance and convert it to JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create composition");
		System.out.println("DemoComposition comp = new DemoComposition();");
		System.out.println("// Convert to JSON");
		System.out.println("comp.toJson();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDemoComposition.class));
		System.out.println(" * " + getTester().getLinkForClass(DemoComposition.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the JSON structure of the *DemoComposition*.  ");
	}

	@Override
	protected void test(String[] args) {
		DemoComposition comp = new DemoComposition();
		JsFile json = comp.toJson();
		ZStringBuilder before = json.toStringBuilderReadFormat();
		comp.fromJson(json);
		ZStringBuilder after = comp.toJson().toStringBuilderReadFormat();
		assertEqual(after,before,"The after conversion composition does not match expectation");
		System.out.println(after);
	}
}
