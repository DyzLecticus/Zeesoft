Zeesoft Data Modelling Kit
==========================
The Zeesoft Data Modelling Kit (ZDMK) is an open source library for Java application development.
It provides support for versioned data model management.
The ZDMK extends the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK) (ZDK).

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDMK/releases/zdmk-0.1.8.zip) to download the latest ZDMK release (version 0.1.8).
All ZDMK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDMK/releases).
ZDMK releases contain;  
 * the ZDMK jar file.  
 * the corresponding ZDK jar file.  
 * this README file.  
 * Separate zip files containing the generated java documentation for each jar file.  

*All jar files in the release include source code and build scripts.*  

Versioned data modelling
------------------------
A [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java) instance can be used to represent and manipulate object oriented data structures (packages, classes and properties).
Models are created and changed by applying transformations.
All model transformations are logged in model versions.
A model version also contains a set of transformations that can be used to recreate initial state of the model for that version.
Please note that models are not thread safe.

**Transformations**  
Models support the following transformations;  
 * [AddPackage](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/AddPackage.java)  
   Adds a package to the model.  
   * Mandatory parameter **name**: The name of the package.  
 * [SetPackageName](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetPackageName.java)  
   Sets the name property of a package to the specified value.  
   * Mandatory parameter **name**: The current name of the package.  
   * Mandatory parameter **newName**: The new name of the package.  
 * [RemovePackage](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/RemovePackage.java)  
   Removes a package from the model.  
   * Mandatory parameter **name**: The name of the package.  
 * [RemovePackageAll](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/RemovePackageAll.java)  
   Removes all packages from the model.  
 * [AddClass](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/AddClass.java)  
   Adds a class to a package.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
   * Optional parameter **extendsPackageName**: The package name of the class to extend.  
   * Optional parameter **extendsClassName**: The class name of the class to extend (without the package name prefix).  
   * Optional parameter **abstract**: Indicates the class is abstract. Accepts 'true' or 'false'. Defaults to 'false' if omitted.  
 * [SetClassAbstract](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetClassAbstract.java)  
   Sets the abstract property of a class to the specified value.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
   * Mandatory parameter **abstract**: Indicates the class is abstract. Accepts 'true' or 'false'.  
 * [SetClassExtendsClass](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetClassExtendsClass.java)  
   Sets the extendsClass property of a class to the specified value.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
   * Mandatory parameter **extendsPackageName**: The package name of the class to extend.  
   * Mandatory parameter **extendsClassName**: The class name of the class to extend (without the package name prefix).  
 * [SetClassName](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetClassName.java)  
   Sets the name property of a class to the specified value.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The current name of the class (without the package name prefix).  
   * Mandatory parameter **newName**: The new name of the class (without the package name prefix).  
 * [RemoveClass](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/RemoveClass.java)  
   Removes a class from a package.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
 * [AddProperty](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/AddProperty.java)  
   Adds a property to a class.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
   * Optional parameter **type**: The type class name including the package name. Defaults to 'java.lang.String' if omitted.  
   * Optional parameter **list**: Indicates the property can contain multiple values. Accepts 'true' or 'false'. Defaults to 'false' if omitted.  
 * [SetPropertyList](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetPropertyList.java)  
   Sets the list property of a property to the specified value.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
   * Mandatory parameter **list**: Indicates the property can contain multiple values. Accepts 'true' or 'false'.  
 * [SetPropertyName](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetPropertyName.java)  
   Sets the list property of a property to the specified value.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The current name of the property.  
   * Mandatory parameter **newName**: The new name of the property.  
 * [SetPropertyType](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/SetPropertyType.java)  
   Sets the type property of a property to the specified value.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
   * Mandatory parameter **type**: The type class name including the package name.  
 * [RemoveProperty](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/RemoveProperty.java)  
   Removes a property from a class.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
 * [IncrementVersion](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/IncrementVersion.java)  
   Creates a new version of the model containing all transformations needed to revert the model back to the current state.  
 * [RevertVersion](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/RevertVersion.java)  
   Reverts the model state back to the initial state of a certain model version. Removes the specified version and all higher versions from the model.  
   * Mandatory parameter **number**: The number of the model version to revert to. Accepts integer values only.  
 * [RevertVersionCurrent](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/RevertVersionCurrent.java)  
   Reverts all transformations made within the current model version.  
 * [ConvertModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/transformations/impl/ConvertModel.java)  
   Converts the current model to an object model. This means abstract classes are removed, extended class properties are made explicit, and class extensions are set to null.  
   * Optional parameter **idPropertyName**: The name of the ID property to add. Specifying this parameter will make the conversion add a property with the specified name to all remaining classes.  
   * Optional parameter **idPropertyType**: The type of the ID property. Defaults to 'java.lang.String'.  

**Self documenting and self testing**  
The tests used to develop the ZDMK are also used to generate this README file.
Run the [ZDMK](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/ZDMK.java) class as a java application to print this documentation to the standard out.
Click [here](#test-results) to scroll down to the test result summary.

nl.zeesoft.zdmk.test.TestModel
------------------------------
This test shows how to create a *Model* instance and then apply some transformations to it.

**Example implementation**  
~~~~
// Create model
Model model = new Model();
// Create and apply transformation
model.applyTransformation(new AddPackage("new.package.name"));
// Clean up model after use to free resources (allow garbage collection)
model.cleanUp();
~~~~

Class references;  
 * [TestModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/TestModel.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-01-24 15:35:28:632 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:671 Applied transformation: AddPackage(name="new.package")
2017-01-24 15:35:28:671 Applied transformation: AddPackage(name="another.new.package")
2017-01-24 15:35:28:672 Failed to apply transformation: AddPackage(name="another.new.package"), error: Package another.new.package already exists
2017-01-24 15:35:28:672 Failed to apply transformation: SetPackageName(name="new.package",newName="another.new.package"), error: Package another.new.package already exists
2017-01-24 15:35:28:673 Applied transformation: SetPackageName(name="new.package",newName="new.package.newName")
2017-01-24 15:35:28:674 Applied transformation: RemovePackage(name="new.package.newName")
2017-01-24 15:35:28:674 Failed to apply transformation: RemovePackage(name="new.package.newName"), error: Package new.package.newName does not exist

Package: another.new.package

~~~~

nl.zeesoft.zdmk.test.TestModelApplyList
---------------------------------------
This test shows how to safely apply a list of transformations to a *Model*.

**Example implementation**  
~~~~
// Create model
Model model = new Model();
// Create transformation list
List<TransformationObject> transformations = new ArrayList<TransformationObject>();
// Create and add a transformation to the list
transformations.add(new AddPackage("new.package.name"));
// Apply transformations
model.applyTransformationList(transformations);
// Clean up model after use to free resources (allow garbage collection)
model.cleanUp();
~~~~

This test uses the *MockModelTransformations*.
It provides a reusable set of transformations to build a test model.

Class references;  
 * [TestModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/TestModel.java)
 * [MockModelTransformations](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/MockModelTransformations.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java)

**Test output**  
The output of this test shows the model package structure before and after the list of transformations is applied and the transformation error message that is returned.  
~~~~
Package: test.package
- Class: TestClassA
  - Property: testPropertyA, type: java.lang.Boolean
  - Property: testPropertyB, type: List<java.lang.String>
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.Integer
  - Property: testPropertyB, type: test.package.TestClassA
Package: another.test.package
- Class: TestClassA, extends: test.package.TestClassA
  - Property: testPropertyB, type: List<java.lang.String> (overrides: test.package.TestClassA:testPropertyB)
  - Property: testPropertyC, type: java.lang.Boolean
  - Property: testPropertyA, type: java.lang.Boolean (extends: test.package.TestClassA:testPropertyA)
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String
Package: yet.another.test.package
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String

Package: test.package
- Class: TestClassA
  - Property: testPropertyA, type: java.lang.Boolean
  - Property: testPropertyB, type: List<java.lang.String>
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.Integer
  - Property: testPropertyB, type: test.package.TestClassA
Package: another.test.package
- Class: TestClassA, extends: test.package.TestClassA
  - Property: testPropertyB, type: List<java.lang.String> (overrides: test.package.TestClassA:testPropertyB)
  - Property: testPropertyC, type: java.lang.Boolean
  - Property: testPropertyA, type: java.lang.Boolean (extends: test.package.TestClassA:testPropertyA)
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String
Package: yet.another.test.package
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String

Transformation error message: Subclass test.package.TestClassA already defines a property named testPropertyC
// Clean up model to free resources for garbage collection
model.cleanUp();
~~~~

nl.zeesoft.zdmk.test.TestModelInitialize
----------------------------------------
This test shows how to create a *Model* instance and then initialize it using an external package structure.

**Example implementation**  
~~~~
// Create package structure
List<ModelPackage> packages = new ArrayList<ModelPackage>();
ModelPackage pack = new ModelPackage("test.package");
ModelClass cls = pack.getNewClass("TestClass");
cls.getNewProperty("TestProperty");
packages.add(pack);
// Create model
Model model = new Model();
// Initialize the model using the package structure
Model model = new Model(packages);
// Clean up model after use to free resources (allow garbage collection)
model.cleanUp();
~~~~

Class references;  
 * [TestModelInitialize](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/TestModelInitialize.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-01-24 15:35:28:763 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:763 Initialized model (package: test.package)

Package: test.package
- Class: TestClass
  - Property: TestProperty, type: java.lang.Boolean

~~~~

nl.zeesoft.zdmk.test.TestModelVersioning
----------------------------------------
This test shows how to use *Model* versioning.

**Example implementation**  
~~~~
// Create model
Model model = new Model();
// Create and apply transformation
model.applyTransformation(new AddPackage("new.package.name"));
// Increment model version
model.applyTransformation(new IncrementVersion());
// Create and apply transformation
model.applyTransformation(new AddPackage("test.package"));
// Revert current version transformations
model.applyTransformation(new RevertVersionCurrent());
// Revert version
model.applyTransformation(new RevertVersion(1));
// Clean up model after use to free resources (allow garbage collection)
model.cleanUp();
~~~~

This test uses the *MockModelTransformations*.
It provides a reusable set of transformations to build a test model.

Class references;  
 * [TestModelVersioning](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/TestModelVersioning.java)
 * [MockModelTransformations](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/MockModelTransformations.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java)

**Test output**  
The output of this test shows two model transformation logs and corresponding package structures;  
 * Once after reverting the current version changes.  
 * Once after reverting back to version one of the model (leaving only the initial version).  
~~~~
Version: 0
2017-01-24 15:35:28:810 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:811 Applied transformation: AddPackage(name="test.package")
2017-01-24 15:35:28:811 Applied transformation: AddClass(packageName="test.package",name="TestClassA")
2017-01-24 15:35:28:812 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyA",type="java.lang.Boolean")
2017-01-24 15:35:28:812 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-01-24 15:35:28:812 Applied transformation: AddClass(packageName="test.package",name="TestClassB")
2017-01-24 15:35:28:813 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyA",type="java.lang.Integer")
2017-01-24 15:35:28:813 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyB",type="test.package.TestClassA")

Version: 1
2017-01-24 15:35:28:814 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:814 Applied transformation: AddPackage(name="another.test.package")
2017-01-24 15:35:28:814 Applied transformation: AddClass(packageName="another.test.package",name="TestClassA",extendsPackageName="test.package",extendsClassName="TestClassA",abstract="false")
2017-01-24 15:35:28:815 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-01-24 15:35:28:815 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyC",type="java.lang.Boolean")
2017-01-24 15:35:28:816 Applied transformation: AddClass(packageName="another.test.package",name="TestClassB")
2017-01-24 15:35:28:816 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyA")
2017-01-24 15:35:28:816 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyB")

Version: 2
2017-01-24 15:35:28:817 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:818 Applied transformation: AddPackage(name="yet.another.test.package")
2017-01-24 15:35:28:818 Applied transformation: AddClass(packageName="yet.another.test.package",name="TestClassA")
2017-01-24 15:35:28:818 Applied transformation: AddProperty(className="TestClassA",packageName="yet.another.test.package",name="testPropertyA")
2017-01-24 15:35:28:819 Applied transformation: AddProperty(className="TestClassA",packageName="yet.another.test.package",name="testPropertyB")
2017-01-24 15:35:28:819 Applied transformation: AddClass(packageName="yet.another.test.package",name="TestClassB")
2017-01-24 15:35:28:820 Applied transformation: AddProperty(className="TestClassB",packageName="yet.another.test.package",name="testPropertyA")
2017-01-24 15:35:28:820 Applied transformation: AddProperty(className="TestClassB",packageName="yet.another.test.package",name="testPropertyB")
2017-01-24 15:35:28:821 Applied transformation: RemoveClass(packageName="yet.another.test.package",name="TestClassA")
2017-01-24 15:35:28:842 Applied transformation: RemovePackageAll()
2017-01-24 15:35:28:843 Applied transformation: AddPackage(name="test.package")
2017-01-24 15:35:28:843 Applied transformation: AddClass(packageName="test.package",name="TestClassA")
2017-01-24 15:35:28:843 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyA",type="java.lang.Boolean")
2017-01-24 15:35:28:844 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-01-24 15:35:28:844 Applied transformation: AddClass(packageName="test.package",name="TestClassB")
2017-01-24 15:35:28:845 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyA",type="java.lang.Integer")
2017-01-24 15:35:28:845 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyB",type="test.package.TestClassA")
2017-01-24 15:35:28:846 Applied transformation: AddPackage(name="another.test.package")
2017-01-24 15:35:28:846 Applied transformation: AddClass(packageName="another.test.package",name="TestClassA")
2017-01-24 15:35:28:847 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-01-24 15:35:28:847 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyC",type="java.lang.Boolean")
2017-01-24 15:35:28:847 Applied transformation: AddClass(packageName="another.test.package",name="TestClassB")
2017-01-24 15:35:28:848 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyA",type="java.lang.String")
2017-01-24 15:35:28:848 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyB",type="java.lang.String")
2017-01-24 15:35:28:849 Applied transformation: SetClassExtendsClass(packageName="another.test.package",name="TestClassA",extendsPackageName="test.package",extendsClassName="TestClassA")
2017-01-24 15:35:28:849 Applied transformation: RevertVersionCurrent()
2017-01-24 15:35:28:849 Failed to apply transformation: RevertVersionCurrent(), error: The current model version does not contain any changes

Package: test.package
- Class: TestClassA
  - Property: testPropertyA, type: java.lang.Boolean
  - Property: testPropertyB, type: List<java.lang.String>
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.Integer
  - Property: testPropertyB, type: test.package.TestClassA
Package: another.test.package
- Class: TestClassA, extends: test.package.TestClassA
  - Property: testPropertyB, type: List<java.lang.String> (overrides: test.package.TestClassA:testPropertyB)
  - Property: testPropertyC, type: java.lang.Boolean
  - Property: testPropertyA, type: java.lang.Boolean (extends: test.package.TestClassA:testPropertyA)
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String

Version: 0
2017-01-24 15:35:28:810 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:811 Applied transformation: AddPackage(name="test.package")
2017-01-24 15:35:28:811 Applied transformation: AddClass(packageName="test.package",name="TestClassA")
2017-01-24 15:35:28:812 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyA",type="java.lang.Boolean")
2017-01-24 15:35:28:812 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-01-24 15:35:28:812 Applied transformation: AddClass(packageName="test.package",name="TestClassB")
2017-01-24 15:35:28:813 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyA",type="java.lang.Integer")
2017-01-24 15:35:28:813 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyB",type="test.package.TestClassA")
2017-01-24 15:35:28:913 Applied transformation: RevertVersion(number="1")

Package: test.package
- Class: TestClassA
  - Property: testPropertyA, type: java.lang.Boolean
  - Property: testPropertyB, type: List<java.lang.String>
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.Integer
  - Property: testPropertyB, type: test.package.TestClassA

~~~~

nl.zeesoft.zdmk.test.TestModelSelf
----------------------------------
This test shows how to create a *ModelSelf* instance and initialize it.

**Example implementation**  
~~~~
// Create model
ModelSelf modelSelf = new ModelSelf();
// Initialize self model
modelSelf.initializeSelf();
~~~~

A *ModelSelf* extends the *Model* class. It contains a method that will generate a model that descibes itself.  

This test uses the *MockModelSelf*.

Class references;  
 * [TestModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/TestModelSelf.java)
 * [MockModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/MockModelSelf.java)
 * [ModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/ModelSelf.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-01-24 15:35:28:921 Applied transformation: IncrementVersion()
2017-01-24 15:35:28:921 Applied transformation: AddPackage(name="nl.zeesoft.zdmk.model")
2017-01-24 15:35:28:921 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelObject",abstract="true")
2017-01-24 15:35:28:922 Applied transformation: AddProperty(className="ModelObject",packageName="nl.zeesoft.zdmk.model",name="id",type="java.lang.Long")
2017-01-24 15:35:28:922 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelSelf",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelObject",abstract="false")
2017-01-24 15:35:28:922 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="versions",type="nl.zeesoft.zdmk.model.ModelVersion",list="true")
2017-01-24 15:35:28:923 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="packages",type="nl.zeesoft.zdmk.model.ModelPackage",list="true")
2017-01-24 15:35:28:923 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelNamedObject",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelObject",abstract="true")
2017-01-24 15:35:28:923 Applied transformation: AddProperty(className="ModelNamedObject",packageName="nl.zeesoft.zdmk.model",name="name")
2017-01-24 15:35:28:924 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelVersion")
2017-01-24 15:35:28:924 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="number",type="java.lang.Integer")
2017-01-24 15:35:28:925 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="log",type="java.lang.StringBuilder")
2017-01-24 15:35:28:925 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="transformations",type="nl.zeesoft.zdmk.model.ModelTransformation",list="true")
2017-01-24 15:35:28:925 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="initialTransformations",type="nl.zeesoft.zdmk.model.ModelTransformation",list="true")
2017-01-24 15:35:28:926 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelTransformation")
2017-01-24 15:35:28:926 Applied transformation: AddProperty(className="ModelTransformation",packageName="nl.zeesoft.zdmk.model",name="parameters",type="nl.zeesoft.zdmk.model.ModelTransformationParameter",list="true")
2017-01-24 15:35:28:927 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelTransformationParameter")
2017-01-24 15:35:28:927 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdmk.model",name="name")
2017-01-24 15:35:28:928 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdmk.model",name="value")
2017-01-24 15:35:28:928 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelPackage",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelNamedObject",abstract="false")
2017-01-24 15:35:28:928 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdmk.model",name="classes",type="nl.zeesoft.zdmk.model.ModelClass",list="true")
2017-01-24 15:35:28:929 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelClass",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelNamedObject",abstract="false")
2017-01-24 15:35:28:930 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="properties",type="nl.zeesoft.zdmk.model.ModelProperty",list="true")
2017-01-24 15:35:28:930 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="extendsClass",type="nl.zeesoft.zdmk.model.ModelClass")
2017-01-24 15:35:28:931 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="abstract",type="java.lang.Boolean")
2017-01-24 15:35:28:931 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelProperty",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelNamedObject",abstract="false")
2017-01-24 15:35:28:931 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="type")
2017-01-24 15:35:28:932 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="list",type="java.lang.Boolean")

Version: 1
2017-01-24 15:35:28:933 Applied transformation: IncrementVersion()

Package: nl.zeesoft.zdmk.model
- Class: ModelObject (abstract)
  - Property: id, type: java.lang.Long
- Class: ModelSelf, extends: nl.zeesoft.zdmk.model.ModelObject
  - Property: versions, type: List<nl.zeesoft.zdmk.model.ModelVersion>
  - Property: packages, type: List<nl.zeesoft.zdmk.model.ModelPackage>
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdmk.model.ModelObject:id)
- Class: ModelNamedObject (abstract), extends: nl.zeesoft.zdmk.model.ModelObject
  - Property: name, type: java.lang.String
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdmk.model.ModelObject:id)
- Class: ModelVersion
  - Property: number, type: java.lang.Integer
  - Property: log, type: java.lang.StringBuilder
  - Property: transformations, type: List<nl.zeesoft.zdmk.model.ModelTransformation>
  - Property: initialTransformations, type: List<nl.zeesoft.zdmk.model.ModelTransformation>
- Class: ModelTransformation
  - Property: parameters, type: List<nl.zeesoft.zdmk.model.ModelTransformationParameter>
- Class: ModelTransformationParameter
  - Property: name, type: java.lang.String
  - Property: value, type: java.lang.String
- Class: ModelPackage, extends: nl.zeesoft.zdmk.model.ModelNamedObject
  - Property: classes, type: List<nl.zeesoft.zdmk.model.ModelClass>
  - Property: name, type: java.lang.String (extends: nl.zeesoft.zdmk.model.ModelNamedObject:name)
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdmk.model.ModelObject:id)
- Class: ModelClass, extends: nl.zeesoft.zdmk.model.ModelNamedObject
  - Property: properties, type: List<nl.zeesoft.zdmk.model.ModelProperty>
  - Property: extendsClass, type: nl.zeesoft.zdmk.model.ModelClass
  - Property: abstract, type: java.lang.Boolean
  - Property: name, type: java.lang.String (extends: nl.zeesoft.zdmk.model.ModelNamedObject:name)
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdmk.model.ModelObject:id)
- Class: ModelProperty, extends: nl.zeesoft.zdmk.model.ModelNamedObject
  - Property: type, type: java.lang.String
  - Property: list, type: java.lang.Boolean
  - Property: name, type: java.lang.String (extends: nl.zeesoft.zdmk.model.ModelNamedObject:name)
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdmk.model.ModelObject:id)

~~~~

nl.zeesoft.zdmk.test.TestModelSelfConvertModel
----------------------------------------------
This test shows how to create a *ModelSelf* instance, initialize it and then convert it to an object model.

**Example implementation**  
~~~~
// Create model
ModelSelf modelSelf = new ModelSelf();
// Initialize self model
modelSelf.initializeSelf();
// Convert the model
modelSelf.applyTransformation(new ConvertModel());
~~~~

Class references;  
 * [TestModelSelfConvertModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/TestModelSelfConvertModel.java)
 * [MockModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/test/MockModelSelf.java)
 * [ModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/ModelSelf.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDMK/src/nl/zeesoft/zdmk/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-01-24 15:35:29:018 Applied transformation: IncrementVersion()
2017-01-24 15:35:29:018 Applied transformation: AddPackage(name="nl.zeesoft.zdmk.model")
2017-01-24 15:35:29:018 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelObject",abstract="true")
2017-01-24 15:35:29:018 Applied transformation: AddProperty(className="ModelObject",packageName="nl.zeesoft.zdmk.model",name="id",type="java.lang.Long")
2017-01-24 15:35:29:019 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelSelf",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelObject",abstract="false")
2017-01-24 15:35:29:019 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="versions",type="nl.zeesoft.zdmk.model.ModelVersion",list="true")
2017-01-24 15:35:29:019 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="packages",type="nl.zeesoft.zdmk.model.ModelPackage",list="true")
2017-01-24 15:35:29:019 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelNamedObject",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelObject",abstract="true")
2017-01-24 15:35:29:020 Applied transformation: AddProperty(className="ModelNamedObject",packageName="nl.zeesoft.zdmk.model",name="name")
2017-01-24 15:35:29:020 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelVersion")
2017-01-24 15:35:29:020 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="number",type="java.lang.Integer")
2017-01-24 15:35:29:020 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="log",type="java.lang.StringBuilder")
2017-01-24 15:35:29:021 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="transformations",type="nl.zeesoft.zdmk.model.ModelTransformation",list="true")
2017-01-24 15:35:29:021 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="initialTransformations",type="nl.zeesoft.zdmk.model.ModelTransformation",list="true")
2017-01-24 15:35:29:021 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelTransformation")
2017-01-24 15:35:29:021 Applied transformation: AddProperty(className="ModelTransformation",packageName="nl.zeesoft.zdmk.model",name="parameters",type="nl.zeesoft.zdmk.model.ModelTransformationParameter",list="true")
2017-01-24 15:35:29:022 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelTransformationParameter")
2017-01-24 15:35:29:022 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdmk.model",name="name")
2017-01-24 15:35:29:022 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdmk.model",name="value")
2017-01-24 15:35:29:022 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelPackage",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelNamedObject",abstract="false")
2017-01-24 15:35:29:023 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdmk.model",name="classes",type="nl.zeesoft.zdmk.model.ModelClass",list="true")
2017-01-24 15:35:29:023 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelClass",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelNamedObject",abstract="false")
2017-01-24 15:35:29:024 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="properties",type="nl.zeesoft.zdmk.model.ModelProperty",list="true")
2017-01-24 15:35:29:024 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="extendsClass",type="nl.zeesoft.zdmk.model.ModelClass")
2017-01-24 15:35:29:024 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="abstract",type="java.lang.Boolean")
2017-01-24 15:35:29:025 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelProperty",extendsPackageName="nl.zeesoft.zdmk.model",extendsClassName="ModelNamedObject",abstract="false")
2017-01-24 15:35:29:025 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="type")
2017-01-24 15:35:29:026 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="list",type="java.lang.Boolean")

Version: 1
2017-01-24 15:35:29:028 Applied transformation: IncrementVersion()
2017-01-24 15:35:29:029 Applied transformation: RemovePackageAll()
2017-01-24 15:35:29:029 Applied transformation: AddPackage(name="nl.zeesoft.zdmk.model")
2017-01-24 15:35:29:030 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelSelf")
2017-01-24 15:35:29:030 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="versions",type="nl.zeesoft.zdmk.model.ModelVersion",list="true")
2017-01-24 15:35:29:031 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="packages",type="nl.zeesoft.zdmk.model.ModelPackage",list="true")
2017-01-24 15:35:29:031 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdmk.model",name="id",type="java.lang.Long",list="false")
2017-01-24 15:35:29:031 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelVersion")
2017-01-24 15:35:29:032 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="number",type="java.lang.Integer",list="false")
2017-01-24 15:35:29:032 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="log",type="java.lang.StringBuilder",list="false")
2017-01-24 15:35:29:033 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="transformations",type="nl.zeesoft.zdmk.model.ModelTransformation",list="true")
2017-01-24 15:35:29:034 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdmk.model",name="initialTransformations",type="nl.zeesoft.zdmk.model.ModelTransformation",list="true")
2017-01-24 15:35:29:034 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelTransformation")
2017-01-24 15:35:29:035 Applied transformation: AddProperty(className="ModelTransformation",packageName="nl.zeesoft.zdmk.model",name="parameters",type="nl.zeesoft.zdmk.model.ModelTransformationParameter",list="true")
2017-01-24 15:35:29:035 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelTransformationParameter")
2017-01-24 15:35:29:035 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdmk.model",name="name",type="java.lang.String",list="false")
2017-01-24 15:35:29:036 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdmk.model",name="value",type="java.lang.String",list="false")
2017-01-24 15:35:29:036 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelPackage")
2017-01-24 15:35:29:037 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdmk.model",name="classes",type="nl.zeesoft.zdmk.model.ModelClass",list="true")
2017-01-24 15:35:29:037 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdmk.model",name="name",type="java.lang.String",list="false")
2017-01-24 15:35:29:037 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdmk.model",name="id",type="java.lang.Long",list="false")
2017-01-24 15:35:29:037 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelClass")
2017-01-24 15:35:29:038 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="properties",type="nl.zeesoft.zdmk.model.ModelProperty",list="true")
2017-01-24 15:35:29:039 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="extendsClass",type="nl.zeesoft.zdmk.model.ModelClass",list="false")
2017-01-24 15:35:29:039 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="abstract",type="java.lang.Boolean",list="false")
2017-01-24 15:35:29:040 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="name",type="java.lang.String",list="false")
2017-01-24 15:35:29:040 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdmk.model",name="id",type="java.lang.Long",list="false")
2017-01-24 15:35:29:041 Applied transformation: AddClass(packageName="nl.zeesoft.zdmk.model",name="ModelProperty")
2017-01-24 15:35:29:041 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="type",type="java.lang.String",list="false")
2017-01-24 15:35:29:041 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="list",type="java.lang.Boolean",list="false")
2017-01-24 15:35:29:042 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="name",type="java.lang.String",list="false")
2017-01-24 15:35:29:042 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdmk.model",name="id",type="java.lang.Long",list="false")
2017-01-24 15:35:29:043 Applied transformation: ConvertModel()

Package: nl.zeesoft.zdmk.model
- Class: ModelSelf
  - Property: versions, type: List<nl.zeesoft.zdmk.model.ModelVersion>
  - Property: packages, type: List<nl.zeesoft.zdmk.model.ModelPackage>
  - Property: id, type: java.lang.Long
- Class: ModelVersion
  - Property: number, type: java.lang.Integer
  - Property: log, type: java.lang.StringBuilder
  - Property: transformations, type: List<nl.zeesoft.zdmk.model.ModelTransformation>
  - Property: initialTransformations, type: List<nl.zeesoft.zdmk.model.ModelTransformation>
- Class: ModelTransformation
  - Property: parameters, type: List<nl.zeesoft.zdmk.model.ModelTransformationParameter>
- Class: ModelTransformationParameter
  - Property: name, type: java.lang.String
  - Property: value, type: java.lang.String
- Class: ModelPackage
  - Property: classes, type: List<nl.zeesoft.zdmk.model.ModelClass>
  - Property: name, type: java.lang.String
  - Property: id, type: java.lang.Long
- Class: ModelClass
  - Property: properties, type: List<nl.zeesoft.zdmk.model.ModelProperty>
  - Property: extendsClass, type: nl.zeesoft.zdmk.model.ModelClass
  - Property: abstract, type: java.lang.Boolean
  - Property: name, type: java.lang.String
  - Property: id, type: java.lang.Long
- Class: ModelProperty
  - Property: type, type: java.lang.String
  - Property: list, type: java.lang.Boolean
  - Property: name, type: java.lang.String
  - Property: id, type: java.lang.Long

~~~~

Test results
------------
All 6 tests have been executed successfully (40 assertions).  
Total test duration: 500 ms (total sleep duration: 0 ms).  
