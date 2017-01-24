package nl.zeesoft.zdmk.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdmk.model.Model;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.RevertVersion;
import nl.zeesoft.zdmk.model.transformations.impl.RevertVersionCurrent;

public class TestModelVersioning extends TestObject {
	public static void main(String[] args) {
		(new TestModelVersioning()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use *Model* versioning.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create model");
		System.out.println("Model model = new Model();");
		System.out.println("// Create and apply transformation");
		System.out.println("model.applyTransformation(new AddPackage(\"new.package.name\"));");
		System.out.println("// Increment model version");
		System.out.println("model.applyTransformation(new IncrementVersion());");
		System.out.println("// Create and apply transformation");
		System.out.println("model.applyTransformation(new AddPackage(\"test.package\"));");
		System.out.println("// Revert current version transformations");
		System.out.println("model.applyTransformation(new RevertVersionCurrent());");
		System.out.println("// Revert version");
		System.out.println("model.applyTransformation(new RevertVersion(1));");
		System.out.println("// Clean up model after use to free resources (allow garbage collection)");
		System.out.println("model.cleanUp();");
		System.out.println("~~~~");
		System.out.println();
		Tester.getInstance().describeMock(MockModelTransformations.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestModelVersioning.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(MockModelTransformations.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows two model transformation logs and corresponding package structures;  ");
		System.out.println(" * Once after reverting the current version changes.  ");
		System.out.println(" * Once after reverting back to version one of the model (leaving only the initial version).  ");
	}

	@Override
	protected void test(String[] args) {
		Model model = new Model();

		// Build model
		@SuppressWarnings("unchecked")
		List<TransformationObject> transformations = (List<TransformationObject>) Tester.getInstance().getMockedObject(MockModelTransformations.class.getName());
		for (TransformationObject transformation: transformations) {
			model.applyTransformation(transformation);
		}		
		assertEqual(model.getVersionsCopy().size(),3,"Number of model versions does not meet expectation");
		if (model.getVersionsCopy().size()>=3) {
			assertEqual(model.getVersionsCopy().get(0).getInitialTransformations().size(),1,"Number of version 1 initial transformations does not meet expectation");
			assertEqual(model.getVersionsCopy().get(1).getInitialTransformations().size(),7,"Number of version 2 initial transformations does not meet expectation");
			assertEqual(model.getVersionsCopy().get(2).getInitialTransformations().size(),15,"Number of version 3 initial transformations does not meet expectation");
			assertEqual(model.getVersionsCopy().get(2).getTransformations().size(),9,"Number of version 3 transformations does not meet expectation");
		}
		assertEqual(model.getPackagesCopy().size(),3,"Number of model packages does not meet expectation");
		assertEqual(model.getPackagesCopy().get(2).getClasses().size(),1,"Number of package classes does not meet expectation");

		// Revert current version changes
		model.applyTransformation(new RevertVersionCurrent());
		String error = model.applyTransformation(new RevertVersionCurrent());
		assertEqual(model.getVersionsCopy().size(),3,"Number of model versions does not meet expectation");
		assertEqual(model.getPackagesCopy().size(),2,"Number of model packages does not meet expectation");
		assertEqual(model.getPackagesCopy().get(1).getClasses().size(),2,"Number of package classes does not meet expectation");
		assertEqual(error,"The current model version does not contain any changes","Revert changes error message does not meet expectation");

		ZDMK.describeModelVersionLogs(model);
		ZDMK.describeModelPackages(model,true);

		// Revert back to first version
		model.applyTransformation(new RevertVersion(1));
		assertEqual(model.getVersionsCopy().size(),1,"Number of model versions does not meet expectation");
		assertEqual(model.getPackagesCopy().size(),1,"Number of model packages does not meet expectation");
		assertEqual(model.getPackagesCopy().get(0).getClasses().size(),2,"Number of package classes does not meet expectation");
		
		ZDMK.describeModelVersionLogs(model);
		ZDMK.describeModelPackages(model,true);
		
		model.cleanUp();
	}
}
