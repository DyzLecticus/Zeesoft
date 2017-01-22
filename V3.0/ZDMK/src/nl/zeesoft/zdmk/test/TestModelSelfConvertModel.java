package nl.zeesoft.zdmk.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdmk.model.Model;
import nl.zeesoft.zdmk.model.ModelSelf;
import nl.zeesoft.zdmk.model.transformations.impl.ConvertModel;

public class TestModelSelfConvertModel extends TestObject {
	public static void main(String[] args) {
		(new TestModelSelfConvertModel()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *ModelSelf* instance, initialize it and then convert it to an object model.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create model");
		System.out.println("ModelSelf modelSelf = new ModelSelf();");
		System.out.println("// Initialize self model");
		System.out.println("modelSelf.initializeSelf();");
		System.out.println("// Convert the model");
		System.out.println("modelSelf.applyTransformation(new ConvertModel());");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestModelSelfConvertModel.class));
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
		String error = modelSelf.applyTransformation(new ConvertModel());
		assertEqual(error,"","Unexpected error");
		assertEqual(modelSelf.getPackages().size(),1,"Number of packages does not meet expectation");
		if (modelSelf.getPackages().size()>0) {
			assertEqual(modelSelf.getPackages().get(0).getClasses().size(),7,"Number of package classes does not meet expectation");
			if (modelSelf.getPackages().get(0).getClasses().size()>=7) {
				assertEqual(modelSelf.getPackages().get(0).getClasses().get(6).getProperties().size(),4,"Number of package class properties does not meet expectation");
			}
		}
		ZDMK.describeModelVersionLogs(modelSelf);
		ZDMK.describeModelPackages(modelSelf,true);
	}
}
