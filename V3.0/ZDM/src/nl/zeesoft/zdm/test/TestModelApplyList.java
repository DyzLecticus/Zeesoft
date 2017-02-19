package nl.zeesoft.zdm.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.transformations.TransformationObject;
import nl.zeesoft.zdm.model.transformations.impl.IncrementVersion;
import nl.zeesoft.zdm.model.transformations.impl.SetPropertyName;

public class TestModelApplyList extends TestObject {
	public TestModelApplyList(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModelApplyList(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to safely apply a list of transformations to a *Model*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create model");
		System.out.println("Model model = new Model();");
		System.out.println("// Create transformation list");
		System.out.println("List<TransformationObject> transformations = new ArrayList<TransformationObject>();");
		System.out.println("// Create and add a transformation to the list");
		System.out.println("transformations.add(new AddPackage(\"new.package.name\"));");
		System.out.println("// Apply transformations");
		System.out.println("model.applyTransformationList(transformations);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockModelTransformations.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestModel.class));
		System.out.println(" * " + getTester().getLinkForClass(MockModelTransformations.class));
		System.out.println(" * " + getTester().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model package structure before and after the list of transformations is applied and the transformation error message that is returned.  ");
	}

	@Override
	protected void test(String[] args) {
		Model model = new Model();

		@SuppressWarnings("unchecked")
		List<TransformationObject> transformations = (List<TransformationObject>) getTester().getMockedObject(MockModelTransformations.class.getName());
		assertEqual(transformations.size(),24,"Number of transformations does not meet expectation");
		String error = model.applyTransformationList(transformations);
		assertEqual(error,"","Unexpected error");

		ZDM.describeModelPackages(model,true);
		
		transformations = new ArrayList<TransformationObject>();
		transformations.add(new SetPropertyName("test.package","TestClassA","testPropertyB","testPropertyZ"));
		transformations.add(new IncrementVersion());
		transformations.add(new SetPropertyName("test.package","TestClassA","testPropertyZ","testPropertyC"));
		error = model.applyTransformationList(transformations);
		assertEqual(error,"Subclass test.package.TestClassA already defines a property named testPropertyC","Property rename error does not meet expectation");

		ZDM.describeModelPackages(model,true);

		System.out.println("Transformation error message: " + error);
	}
}