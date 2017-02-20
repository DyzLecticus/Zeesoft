package nl.zeesoft.zdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.ModelClass;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.ModelProperty;
import nl.zeesoft.zdm.model.ModelStructure;

public class TestModelInitialize extends TestObject {
	public TestModelInitialize(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModelInitialize(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to create a *ModelStructure* and then use that to initialize a *Model*.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create package structure");
		System.out.println("ModelStructure structure = new ModelStructure();");
		System.out.println("ModelPackage pck = structure.addPackage(\"test.package\");");
		System.out.println("ModelClass cls = structure.addClass(\"test.package\",\"TestClass\");");
		System.out.println("ModelProperty prp = structure.addProperty(\"test.package\",\"TestClass\",\"testProperty\");");
		System.out.println("// Create model");
		System.out.println("Model model = new Model(structure);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestModelInitialize.class));
		System.out.println(" * " + getTester().getLinkForClass(ModelStructure.class));
		System.out.println(" * " + getTester().getLinkForClass(Model.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  ");
	}

	@Override
	protected void test(String[] args) {
		ModelStructure structure = new ModelStructure();
		structure.addPackage("test.package");
		structure.addClass("test.package","TestClass");		
		ModelProperty prop = structure.addProperty("test.package","TestClass","testProperty");		
		prop.setType(Boolean.class.getName());

		Model model = new Model(structure);
		
		List<ModelPackage> packages = model.getStructure().getPackages();
		List<ModelClass> classes = model.getStructure().getClasses();
		List<ModelProperty> properties = model.getStructure().getProperties();

		assertEqual(packages.size(),1,"Number of model packages does not meet expectation");
		if (packages.size()>0) {
			assertEqual(packages.get(0).getName(),"test.package","Package name does not match expectation");
			assertEqual(classes.size(),1,"Number of package classes does not meet expectation");
			if (classes.size()>0) {
				assertEqual(classes.get(0).getName(),"TestClass","Class name does not match expectation");
				assertEqual(properties.size(),1,"Number of class properties does not meet expectation");
			}
		}
		ZDM.describeModelVersionLogs(model);
		ZDM.describeModelPackages(model,true);
	}
}
