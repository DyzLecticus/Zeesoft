package nl.zeesoft.zdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.ModelSelf;
import nl.zeesoft.zdm.model.transformations.impl.ConvertModel;

public class TestModelSelfConvertModel extends TestObject {
	public TestModelSelfConvertModel(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModelSelfConvertModel(new Tester())).test(args);
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
		System.out.println(" * " + getTester().getLinkForClass(TestModelSelfConvertModel.class));
		System.out.println(" * " + getTester().getLinkForClass(MockModelSelf.class));
		System.out.println(" * " + getTester().getLinkForClass(ModelSelf.class));
		System.out.println(" * " + getTester().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  ");
	}

	@Override
	protected void test(String[] args) {
		ModelSelf modelSelf = (ModelSelf) getTester().getMockedObject(MockModelSelf.class.getName());
		String error = modelSelf.applyTransformation(new ConvertModel());
		assertEqual(error,"","Unexpected error");
		List<ModelPackage> packages = modelSelf.getPackagesCopy();
		assertEqual(packages.size(),1,"Number of packages does not meet expectation");
		if (packages.size()>0) {
			assertEqual(packages.get(0).getClasses().size(),7,"Number of package classes does not meet expectation");
			if (packages.get(0).getClasses().size()>=7) {
				assertEqual(packages.get(0).getClasses().get(6).getProperties().size(),4,"Number of package class properties does not meet expectation");
			}
		}
		ZDM.describeModelVersionLogs(modelSelf);
		ZDM.describeModelPackages(modelSelf,true);
		modelSelf.revertVersion(1);
	}
}
