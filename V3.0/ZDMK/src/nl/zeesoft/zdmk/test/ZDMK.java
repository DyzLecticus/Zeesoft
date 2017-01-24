package nl.zeesoft.zdmk.test;

import java.util.List;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdmk.model.Model;
import nl.zeesoft.zdmk.model.ModelClass;
import nl.zeesoft.zdmk.model.ModelPackage;
import nl.zeesoft.zdmk.model.ModelProperty;
import nl.zeesoft.zdmk.model.ModelVersion;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.TransformationParameter;
import nl.zeesoft.zdmk.model.transformations.Transformations;

public class ZDMK {
	public static void main(String[] args) {
		Tester.getInstance().setBaseUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK");
	
		String version = "";
		if (args!=null && args.length>=1 && args[0]!=null && args[0].length()>0) {
			version = args[0];
		} else {
			version = "[???]";
		}
		
		System.out.println("Zeesoft Data Modelling Kit");
		System.out.println("==========================");
		System.out.println("The Zeesoft Data Modelling Kit (ZDMK) is an open source library for Java application development.");
		System.out.println("It provides support for versioned data model management.");
		System.out.println("The ZDMK extends the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK) (ZDK).");
		System.out.println();
		System.out.println("**Release downloads**  ");
		System.out.println("Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDMK/releases/zdmk-" + version + ".zip) to download the latest ZDMK release (version " + version + ").");
		System.out.println("All ZDMK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDMK/releases).");
		System.out.println("ZDMK releases contain;  ");
		System.out.println(" * the ZDMK jar file.  ");
		System.out.println(" * the corresponding ZDK jar file.  ");
		System.out.println(" * this README file.  ");
		System.out.println(" * Separate zip files containing the generated java documentation for each jar file.  ");
		System.out.println();
		System.out.println("*All jar files in the release include source code and build scripts.*  ");
		System.out.println();
		System.out.println("Versioned data modelling");
		System.out.println("------------------------");
		System.out.println("A " + Tester.getInstance().getLinkForClass(Model.class) + " instance can be used to represent and manipulate object oriented data structures (packages, classes and properties).");
		System.out.println("Models are created and changed by applying transformations.");
		System.out.println("All model transformations are logged in model versions.");
		System.out.println("A model version also contains a set of transformations that can be used to recreate initial state of the model for that version.");
		System.out.println("Please note that models are not thread safe.");
		System.out.println();
		System.out.println("**Transformations**  ");
		System.out.println("Models support the following transformations;  ");
		describeTransformations(false);
		System.out.println();
		System.out.println("**Self documenting and self testing**  ");
		System.out.println("The tests used to develop the ZDMK are also used to generate this README file.");
		System.out.println("Run the " + Tester.getInstance().getLinkForClass(ZDMK.class) + " class as a java application to print this documentation to the standard out.");
		System.out.println("Click [here](#test-results) to scroll down to the test result summary.");
		System.out.println();
		
		List<TestObject> tests = Tester.getInstance().getTests();
		tests.add(new TestModel());
		tests.add(new TestModelApplyList());
		tests.add(new TestModelInitialize());
		tests.add(new TestModelVersioning());
		tests.add(new TestModelSelf());
		tests.add(new TestModelSelfConvertModel());
		boolean success = Tester.getInstance().test(args);
		if (success) {
			System.exit(0);
		} else {
			System.exit(1);
		}
	}
	
	public static void describeTransformations(boolean summary) {
		for (TransformationObject transformation: Transformations.getAllTransformations()) {
			System.out.print(" * " + Tester.getInstance().getLinkForClass(transformation.getClass()));
			System.out.println("  ");
			System.out.println("   " + transformation.getDescription() + "  ");
			if (!summary && transformation.getParameters().size()>0) {
				for (TransformationParameter param: transformation.getParameters()) {
					String par = "   * ";
					if (param.isMandatory()) {
						par += "Mandatory parameter ";
					} else {
						par += "Optional parameter ";
					}
					par += "**" + param.getName() + "**: " + param.getDescription();
					System.out.print(par);
					System.out.println("  ");
				}
			}
		}
	}

	public static void describeModelVersionLogs(Model model) {
		StringBuilder desc = new StringBuilder();
		for (ModelVersion version: model.getVersionsCopy()) {
			if (version.getLog().length()>0) {
				if (desc.length()>0) {
					desc.append("\n");
				}
				desc.append("Version: ");
				desc.append(version.getNumber());
				desc.append("\n");
				desc.append(version.getLog());
			}
		}
		System.out.println(desc);
	}

	public static void describeModelPackages(Model model, boolean includeExtendedProperties) {
		System.out.println(getModelPackageDescription(model,includeExtendedProperties));
	}
	
	public static final StringBuilder getModelPackageDescription(Model model, boolean includeExtendedProperties) {
		StringBuilder desc = new StringBuilder();
		for (ModelPackage pack: model.getPackagesCopy()) {
			desc.append("Package: ");
			desc.append(pack.getName());
			desc.append("\n");
			for (ModelClass cls: pack.getClasses()) {
				desc.append("- Class: ");
				desc.append(cls.getName());
				if (cls.isAbstr()) {
					desc.append(" (abstract)");
				}
				if (cls.getExtendsClass()!=null) {
					desc.append(", extends: ");
					desc.append(cls.getExtendsClass().getFullName());
				}
				desc.append("\n");
				List<ModelProperty> props = null;
				if (includeExtendedProperties) {
					props = cls.getExtendedProperties();
				} else {
					props = cls.getProperties();
				}
				for (ModelProperty prop: props) {
					desc.append("  - Property: ");
					desc.append(prop.getName());
					desc.append(", type: ");
					if (prop.isList()) {
						desc.append("List<");
						desc.append(prop.getType());
						desc.append(">");
					} else {
						desc.append(prop.getType());
					}
					ModelProperty eProp = null;
					if (cls.getExtendsClass()!=null) {
						eProp = cls.getExtendsClass().getProperty(prop.getName());
						if (eProp!=null) {
							if (!cls.getProperties().contains(prop)) {
								desc.append(" (extends: " + eProp.getFullName() + ")");
							} else {
								desc.append(" (overrides: " + eProp.getFullName() + ")");
							}
						}
					}
					desc.append("\n");
				}
			}
		}
		return desc;
	}
}
