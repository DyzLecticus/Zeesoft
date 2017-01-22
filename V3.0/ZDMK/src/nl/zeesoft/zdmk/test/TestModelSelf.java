package nl.zeesoft.zdmk.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdmk.model.Model;
import nl.zeesoft.zdmk.model.ModelSelf;

public class TestModelSelf extends TestObject {
	public static void main(String[] args) {
		(new TestModelSelf()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *ModelSelf* instance and initialize it.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create model");
		System.out.println("ModelSelf modelSelf = new ModelSelf();");
		System.out.println("// Initialize self model");
		System.out.println("modelSelf.initializeSelf();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("A *ModelSelf* extends the *Model* class. It contains a method that will generate a model that descibes itself.  ");
		System.out.println();
		Tester.getInstance().describeMock(MockModelSelf.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestModelSelf.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(MockModelSelf.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(ModelSelf.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  ");
	}

	@Override
	protected void test(String[] args) {
		ModelSelf modelSelf = (ModelSelf) Tester.getInstance().getMockedObject(MockModelSelf.class.getName());
		assertEqual(modelSelf.getVersions().size(),2,"Number of versions does not meet expectation");
		if (modelSelf.getVersions().size()>=1) {
			assertEqual(modelSelf.getVersions().get(0).getTransformations().size(),28,"Number of version transformations does not meet expectation");
		}
		if (modelSelf.getVersions().size()>=2) {
			assertEqual(modelSelf.getVersions().get(1).getInitialTransformations().size(),32,"Number of version initial transformations does not meet expectation");
		}
		assertEqual(modelSelf.getPackages().size(),1,"Number of packages does not meet expectation");
		if (modelSelf.getPackages().size()>0) {
			assertEqual(modelSelf.getPackages().get(0).getClasses().size(),9,"Number of package classes does not meet expectation");
			if (modelSelf.getPackages().get(0).getClasses().size()>=9) {
				assertEqual(modelSelf.getPackages().get(0).getClasses().get(8).getProperties().size(),2,"Number of package class properties does not meet expectation");
				assertEqual(modelSelf.getPackages().get(0).getClasses().get(8).getExtendedProperties().size(),4,"Number of extended package class properties does not meet expectation");
			}
		}
		ZDMK.describeModelVersionLogs(modelSelf);
		ZDMK.describeModelPackages(modelSelf,true);
	}
}
