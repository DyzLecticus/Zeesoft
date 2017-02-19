package nl.zeesoft.zdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.transformations.impl.AddPackage;
import nl.zeesoft.zdm.model.transformations.impl.RemovePackage;
import nl.zeesoft.zdm.model.transformations.impl.SetPackageName;

public class TestModel extends TestObject {
	public TestModel(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModel(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *Model* instance and then apply some transformations to it.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create model");
		System.out.println("Model model = new Model();");
		System.out.println("// Create and apply transformation");
		System.out.println("model.applyTransformation(new AddPackage(\"new.package.name\"));");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestModel.class));
		System.out.println(" * " + getTester().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  ");
	}

	@Override
	protected void test(String[] args) {
		Model model = new Model();
		model.applyTransformation(new AddPackage("new.package"));
		model.applyTransformation(new AddPackage("another.new.package"));
		String error = model.applyTransformation(new AddPackage("another.new.package"));
		List<ModelPackage> packages = model.getStructure().getPackages();
		assertEqual(packages.size(),2,"Number of model packages does not meet expectation");
		if (packages.size()>0) {
			assertEqual(packages.get(0).getName(),"new.package","Package name does not match expectation");
		}
		assertEqual(error,"Package another.new.package already exists","Package add error does not meet expectation");
		if (packages.size()>0) {
			error = model.applyTransformation(new SetPackageName("new.package","another.new.package"));
			assertEqual(packages.get(0).getName(),"new.package","Package name does not match expectation");
			assertEqual(error,"Package another.new.package already exists","Package rename error does not meet expectation");
			error = model.applyTransformation(new SetPackageName("new.package","new.package.newName"));
			assertEqual(packages.get(0).getName(),"new.package.newName","Renamed package name does not match expectation");
			error = model.applyTransformation(new RemovePackage("new.package.newName"));
			error = model.applyTransformation(new RemovePackage("new.package.newName"));
			assertEqual(error,"Package new.package.newName does not exist","Package remove error does not meet expectation");
			packages = model.getStructure().getPackages();
			assertEqual(packages.size(),1,"Number of model packages after removal does not meet expectation");
		}
		ZDM.describeModelVersionLogs(model);
		ZDM.describeModelPackages(model,true);
	}
}
