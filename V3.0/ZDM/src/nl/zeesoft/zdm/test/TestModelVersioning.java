package nl.zeesoft.zdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.ModelVersion;
import nl.zeesoft.zdm.model.transformations.TransformationObject;
import nl.zeesoft.zdm.model.transformations.impl.RevertVersion;
import nl.zeesoft.zdm.model.transformations.impl.RevertVersionCurrent;

public class TestModelVersioning extends TestObject {
	public TestModelVersioning(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModelVersioning(new Tester())).test(args);
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
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockModelTransformations.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestModelVersioning.class));
		System.out.println(" * " + getTester().getLinkForClass(MockModelTransformations.class));
		System.out.println(" * " + getTester().getLinkForClass(Model.class));
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
		List<TransformationObject> transformations = (List<TransformationObject>) getTester().getMockedObject(MockModelTransformations.class.getName());
		assertEqual(transformations.size(),24,"Number of transformations does not meet expectation");
		for (TransformationObject transformation: transformations) {
			model.applyTransformation(transformation);
		}
		List<ModelVersion> versions = model.getVersions();
		List<ModelPackage> packages = model.getStructure().getPackages();
		assertEqual(versions.size(),3,"Number of model versions does not meet expectation");
		if (versions.size()>=3) {
			assertEqual(versions.get(0).getInitialTransformations().size(),1,"Number of version 1 initial transformations does not meet expectation");
			assertEqual(versions.get(1).getInitialTransformations().size(),7,"Number of version 2 initial transformations does not meet expectation");
			assertEqual(versions.get(2).getInitialTransformations().size(),15,"Number of version 3 initial transformations does not meet expectation");
			assertEqual(versions.get(2).getTransformations().size(),9,"Number of version 3 transformations does not meet expectation");
		}
		
		assertEqual(packages.size(),3,"Number of model packages does not meet expectation");
		assertEqual(model.getStructure().getClasses(packages.get(2).getName()).size(),1,"Number of package classes does not meet expectation");

		// Revert current version changes
		model.applyTransformation(new RevertVersionCurrent());
		String error = model.applyTransformation(new RevertVersionCurrent());
		versions = model.getVersions();
		packages = model.getStructure().getPackages();
		assertEqual(versions.size(),3,"Number of model versions does not meet expectation");
		assertEqual(packages.size(),2,"Number of model packages does not meet expectation");
		assertEqual(model.getStructure().getClasses(packages.get(1).getName()).size(),2,"Number of package classes does not meet expectation");
		assertEqual(error,"The current model version does not contain any changes","Revert changes error message does not meet expectation");

		ZDM.describeModelVersionLogs(model);
		ZDM.describeModelPackages(model,true);

		// Revert back to first version
		model.applyTransformation(new RevertVersion(1));
		versions = model.getVersions();
		packages = model.getStructure().getPackages();
		assertEqual(versions.size(),1,"Number of model versions does not meet expectation");
		assertEqual(packages.size(),1,"Number of model packages does not meet expectation");
		assertEqual(model.getStructure().getClasses(packages.get(0).getName()).size(),2,"Number of package classes does not meet expectation");
		
		ZDM.describeModelVersionLogs(model);
		ZDM.describeModelPackages(model,true);
	}
}
