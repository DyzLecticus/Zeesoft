package nl.zeesoft.zdm.test;

import java.util.List;

import nl.zeesoft.zdk.test.LibraryObject;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.test.impl.ZDK;
import nl.zeesoft.zdm.model.Model;
import nl.zeesoft.zdm.model.ModelClass;
import nl.zeesoft.zdm.model.ModelPackage;
import nl.zeesoft.zdm.model.ModelProperty;
import nl.zeesoft.zdm.model.ModelVersion;
import nl.zeesoft.zdm.model.transformations.TransformationObject;
import nl.zeesoft.zdm.model.transformations.TransformationParameter;
import nl.zeesoft.zdm.model.transformations.Transformations;

public class ZDM extends LibraryObject {
	public ZDM(Tester tester) {
		super(tester);
		setNameAbbreviated("ZDK");
		setNameFull("Zeesoft Data Modelling");
		setBaseProjectUrl("https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDM/");
		setBaseReleaseUrl("https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDM/releases/");
		setBaseSrcUrl("https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/");
		getDependencies().add(new ZDK(null));
	}

	public static void main(String[] args) {
		(new ZDM(new Tester())).describeAndTest(args);
	}

	@Override
	public void describe() {
		System.out.println("Zeesoft Data Modelling");
		System.out.println("======================");
		System.out.println("Zeesoft Data Modelling (ZDM) is an open source library for Java application development.");
		System.out.println("It provides support for versioned data model management.");
		describeDependencies();
		System.out.println();
		describeRelease();
		System.out.println();
		describeTesting(ZDM.class);
		System.out.println();
		System.out.println("Versioned data modelling");
		System.out.println("------------------------");
		System.out.println("A " + getTester().getLinkForClass(Model.class) + " instance can be used to represent and manipulate object oriented data structures (packages, classes and properties).");
		System.out.println("Models are created and changed by applying transformations.");
		System.out.println("All model transformations are logged in model versions.");
		System.out.println("A model version also contains a set of transformations that can be used to recreate initial state of the model for that version.");
		System.out.println("Please note that models are not thread safe.");
		System.out.println();
		System.out.println("**Transformations**  ");
		System.out.println("Models support the following transformations;  ");
		describeTransformations(false);
		System.out.println();
	}

	@Override
	public void addTests(List<TestObject> tests) {
		tests.add(new TestModel(getTester()));
		tests.add(new TestModelApplyList(getTester()));
		tests.add(new TestModelInitialize(getTester()));
		tests.add(new TestModelVersioning(getTester()));
		tests.add(new TestModelSelf(getTester()));
		tests.add(new TestModelSelfConvertModel(getTester()));
	}

	public void describeTransformations(boolean summary) {
		for (TransformationObject transformation: Transformations.getAllTransformations()) {
			System.out.print(" * " + getTester().getLinkForClass(transformation.getClass()));
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
		for (ModelVersion version: model.getVersions()) {
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
		for (ModelPackage pack: model.getStructure().getPackages()) {
			desc.append("Package: ");
			desc.append(pack.getName());
			desc.append("\n");
			for (ModelClass cls: model.getStructure().getClasses(pack.getName())) {
				desc.append("- Class: ");
				desc.append(cls.getName());
				if (cls.isAbstr()) {
					desc.append(" (abstract)");
				}
				if (cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
					desc.append(", extends: ");
					desc.append(cls.getExtendsPackageName() + "." + cls.getExtendsClassName());
				}
				desc.append("\n");
				List<ModelProperty> props = null;
				if (includeExtendedProperties) {
					props = model.getStructure().getPropertiesExtended(cls);
				} else {
					props = model.getStructure().getProperties(cls);
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
					if (cls.getExtendsPackageName().length()>0 && cls.getExtendsClassName().length()>0) {
						eProp = model.getStructure().getProperty(cls, prop.getName(), true);
						if (eProp!=null) {
							if (!model.getStructure().getProperties(cls).contains(prop)) {
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
