package nl.zeesoft.zdmk.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdmk.model.Model;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.IncrementVersion;
import nl.zeesoft.zdmk.model.transformations.impl.SetPropertyName;

public class TestModelApplyList extends TestObject {
	public static void main(String[] args) {
		(new TestModelApplyList()).test(args);
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
		Tester.getInstance().describeMock(MockModelTransformations.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestModel.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(MockModelTransformations.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model package structure before and after the list of transformations is applied and the transformation error message that is returned.  ");
	}

	@Override
	protected void test(String[] args) {
		Model model = new Model();

		@SuppressWarnings("unchecked")
		List<TransformationObject> transformations = (List<TransformationObject>) Tester.getInstance().getMockedObject(MockModelTransformations.class.getName());
		String error = model.applyTransformationList(transformations);
		assertEqual(error,"","Unexpected error");

		ZDMK.describeModelPackages(model,true);
		
		transformations.clear();
		transformations.add(new SetPropertyName("test.package","TestClassA","testPropertyB","testPropertyZ"));
		transformations.add(new IncrementVersion());
		transformations.add(new SetPropertyName("test.package","TestClassA","testPropertyZ","testPropertyC"));
		error = model.applyTransformationList(transformations);
		assertEqual(error,"Subclass test.package.TestClassA already defines a property named testPropertyC","Property rename error does not meet expectation");

		ZDMK.describeModelPackages(model,true);

		System.out.println("Transformation error message: " + error);
	}
}