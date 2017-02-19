package nl.zeesoft.zdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.ModelClass;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.ModelProperty;
import nl.zeesoft.zdm.model.ModelSelf;
import nl.zeesoft.zdm.model.ModelVersion;

public class TestModelSelf extends TestObject {
	public TestModelSelf(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestModelSelf(new Tester())).test(args);
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
		getTester().describeMock(MockModelSelf.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestModelSelf.class));
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
		List<ModelVersion> versions = modelSelf.getVersions();
		List<ModelPackage> packages = modelSelf.getStructure().getPackages();
		assertEqual(versions.size(),2,"Number of versions does not meet expectation");
		if (versions.size()>=1) {
			assertEqual(versions.get(0).getTransformations().size(),27,"Number of version transformations does not meet expectation");
		}
		if (versions.size()>=2) {
			assertEqual(versions.get(1).getInitialTransformations().size(),29,"Number of version initial transformations does not meet expectation");
		}
		assertEqual(packages.size(),1,"Number of packages does not meet expectation");
		if (packages.size()>0) {
			List<ModelClass> classes = modelSelf.getStructure().getClasses(packages.get(0).getName());
			assertEqual(classes.size(),8,"Number of package classes does not meet expectation");
			if (classes.size()>=8) {
				List<ModelProperty> properties = modelSelf.getStructure().getProperties(packages.get(0).getName(),classes.get(7).getName());
				List<ModelProperty> propertiesExt = modelSelf.getStructure().getPropertiesExtended(packages.get(0).getName(),classes.get(7).getName());
				assertEqual(properties.size(),2,"Number of package class properties does not meet expectation");
				assertEqual(propertiesExt.size(),4,"Number of extended package class properties does not meet expectation");
			}
		}
		ZDM.describeModelVersionLogs(modelSelf);
		ZDM.describeModelPackages(modelSelf,true);
	}
}
