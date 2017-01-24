package nl.zeesoft.zdmk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdmk.model.Model;
import nl.zeesoft.zdmk.model.ModelClass;
import nl.zeesoft.zdmk.model.ModelPackage;
import nl.zeesoft.zdmk.model.ModelProperty;

public class TestModelInitialize extends TestObject {
	public static void main(String[] args) {
		(new TestModelInitialize()).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *Model* instance and then initialize it using an external package structure.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create package structure");
		System.out.println("List<ModelPackage> packages = new ArrayList<ModelPackage>();");
		System.out.println("ModelPackage pack = new ModelPackage(\"test.package\");");
		System.out.println("ModelClass cls = pack.getNewClass(\"TestClass\");");
		System.out.println("cls.getNewProperty(\"TestProperty\");");
		System.out.println("packages.add(pack);");
		System.out.println("// Create model");
		System.out.println("Model model = new Model();");
		System.out.println("// Initialize the model using the package structure");
		System.out.println("Model model = new Model(packages);");
		System.out.println("// Clean up model after use to free resources (allow garbage collection)");
		System.out.println("model.cleanUp();");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + Tester.getInstance().getLinkForClass(TestModelInitialize.class));
		System.out.println(" * " + Tester.getInstance().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  ");
	}

	@Override
	protected void test(String[] args) {
		ModelPackage pack = new ModelPackage("test.package");
		ModelClass cls = pack.getNewClass("TestClass");		
		ModelProperty prop = cls.getNewProperty("TestProperty");
		prop.setType(Boolean.class.getName());
		List<ModelPackage> packages = new ArrayList<ModelPackage>();
		packages.add(pack);
		Model model = new Model(packages);
		packages = model.getPackagesCopy();
		assertEqual(packages.size(),1,"Number of model packages does not meet expectation");
		if (packages.size()>0) {
			assertEqual(packages.get(0).getName(),"test.package","Package name does not match expectation");
			assertEqual(packages.get(0).getClasses().size(),1,"Number of package classes does not meet expectation");
			if (packages.get(0).getClasses().size()>0) {
				assertEqual(packages.get(0).getClasses().get(0).getName(),"TestClass","Class name does not match expectation");
				assertEqual(packages.get(0).getClasses().get(0).getProperties().size(),1,"Number of class properties does not meet expectation");
			}
		}
		ZDMK.describeModelVersionLogs(model);
		ZDMK.describeModelPackages(model,true);
		model.cleanUp();
	}
}
