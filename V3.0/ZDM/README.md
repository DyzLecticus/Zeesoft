Zeesoft Data Modelling
======================
Zeesoft Data Modelling (ZDM) is an open source library for Java application development.
It provides support for versioned data model management.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDM/releases/zdk-0.9.4.zip) to download the latest ZDK release (version 0.9.4).  
All ZDK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDM/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZDM](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/ZDM.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

Versioned data modelling
------------------------
A [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java) instance can be used to represent and manipulate object oriented data structures (packages, classes and properties).
Models are created and changed by applying transformations.
All model transformations are logged in model versions.
A model version also contains a set of transformations that can be used to recreate initial state of the model for that version.
Please note that models are not thread safe.

**Transformations**  
Models support the following transformations;  
 * [AddPackage](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/AddPackage.java)  
   Adds a package to the model.  
   * Mandatory parameter **name**: The name of the package.  
 * [SetPackageName](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetPackageName.java)  
   Sets the name property of a package to the specified value.  
   * Mandatory parameter **name**: The current name of the package.  
   * Mandatory parameter **newName**: The new name of the package.  
 * [RemovePackage](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/RemovePackage.java)  
   Removes a package from the model.  
   * Mandatory parameter **name**: The name of the package.  
 * [RemovePackageAll](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/RemovePackageAll.java)  
   Removes all packages from the model.  
 * [AddClass](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/AddClass.java)  
   Adds a class to a package.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
   * Optional parameter **extendsPackageName**: The package name of the class to extend.  
   * Optional parameter **extendsClassName**: The class name of the class to extend (without the package name prefix).  
   * Optional parameter **abstract**: Indicates the class is abstract. Accepts 'true' or 'false'. Defaults to 'false' if omitted.  
 * [SetClassAbstract](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetClassAbstract.java)  
   Sets the abstract property of a class to the specified value.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
   * Mandatory parameter **abstract**: Indicates the class is abstract. Accepts 'true' or 'false'.  
 * [SetClassExtendsClass](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetClassExtendsClass.java)  
   Sets the extendsClass property of a class to the specified value.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
   * Mandatory parameter **extendsPackageName**: The package name of the class to extend.  
   * Mandatory parameter **extendsClassName**: The class name of the class to extend (without the package name prefix).  
 * [SetClassName](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetClassName.java)  
   Sets the name property of a class to the specified value.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The current name of the class (without the package name prefix).  
   * Mandatory parameter **newName**: The new name of the class (without the package name prefix).  
 * [RemoveClass](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/RemoveClass.java)  
   Removes a class from a package.  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the class (without the package name prefix).  
 * [AddProperty](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/AddProperty.java)  
   Adds a property to a class.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
   * Optional parameter **type**: The type class name including the package name. Defaults to 'java.lang.String' if omitted.  
   * Optional parameter **list**: Indicates the property can contain multiple values. Accepts 'true' or 'false'. Defaults to 'false' if omitted.  
 * [SetPropertyList](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetPropertyList.java)  
   Sets the list property of a property to the specified value.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
   * Mandatory parameter **list**: Indicates the property can contain multiple values. Accepts 'true' or 'false'.  
 * [SetPropertyName](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetPropertyName.java)  
   Sets the list property of a property to the specified value.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The current name of the property.  
   * Mandatory parameter **newName**: The new name of the property.  
 * [SetPropertyType](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/SetPropertyType.java)  
   Sets the type property of a property to the specified value.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
   * Mandatory parameter **type**: The type class name including the package name.  
 * [RemoveProperty](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/RemoveProperty.java)  
   Removes a property from a class.  
   * Mandatory parameter **className**: The name of the class (without the package name prefix).  
   * Mandatory parameter **packageName**: The name of the package.  
   * Mandatory parameter **name**: The name of the property.  
 * [IncrementVersion](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/IncrementVersion.java)  
   Creates a new version of the model containing all transformations needed to revert the model back to the current state.  
 * [RevertVersion](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/RevertVersion.java)  
   Reverts the model state back to the initial state of a certain model version. Removes the specified version and all higher versions from the model.  
   * Mandatory parameter **number**: The number of the model version to revert to. Accepts integer values only.  
 * [RevertVersionCurrent](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/RevertVersionCurrent.java)  
   Reverts all transformations made within the current model version.  
 * [ConvertModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/transformations/impl/ConvertModel.java)  
   Converts the current model to an object model. This means abstract classes are removed, extended class properties are made explicit, and class extensions are set to null.  
   * Optional parameter **idPropertyName**: The name of the ID property to add. Specifying this parameter will make the conversion add a property with the specified name to all remaining classes.  
   * Optional parameter **idPropertyType**: The type of the ID property. Defaults to 'java.lang.String'.  

nl.zeesoft.zdm.test.TestModel
-----------------------------
This test shows how to create a *Model* instance and then apply some transformations to it.

**Example implementation**  
~~~~
// Create model
Model model = new Model();
// Create and apply transformation
model.applyTransformation(new AddPackage("new.package.name"));
~~~~

Class references;  
 * [TestModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/TestModel.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-02-19 19:55:41:711 Applied transformation: IncrementVersion()
2017-02-19 19:55:41:742 Applied transformation: AddPackage(name="new.package")
2017-02-19 19:55:41:743 Applied transformation: AddPackage(name="another.new.package")
2017-02-19 19:55:41:743 Failed to apply transformation: AddPackage(name="another.new.package"), error: Package another.new.package already exists
2017-02-19 19:55:41:744 Failed to apply transformation: SetPackageName(name="new.package",newName="another.new.package"), error: Package another.new.package already exists
2017-02-19 19:55:41:745 Applied transformation: SetPackageName(name="new.package",newName="new.package.newName")
2017-02-19 19:55:41:746 Applied transformation: RemovePackage(name="new.package.newName")
2017-02-19 19:55:41:746 Failed to apply transformation: RemovePackage(name="new.package.newName"), error: Package new.package.newName does not exist

Package: another.new.package

~~~~

nl.zeesoft.zdm.test.TestModelApplyList
--------------------------------------
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
~~~~

This test uses the *MockModelTransformations*.
It provides a reusable set of transformations to build a test model.

Class references;  
 * [TestModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/TestModel.java)
 * [MockModelTransformations](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/MockModelTransformations.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java)

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
  - Property: testPropertyB, type: List<java.lang.String> (overrides: another.test.package.TestClassA:testPropertyB)
  - Property: testPropertyC, type: java.lang.Boolean (overrides: another.test.package.TestClassA:testPropertyC)
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
  - Property: testPropertyB, type: List<java.lang.String> (overrides: another.test.package.TestClassA:testPropertyB)
  - Property: testPropertyC, type: java.lang.Boolean (overrides: another.test.package.TestClassA:testPropertyC)
  - Property: testPropertyA, type: java.lang.Boolean (extends: test.package.TestClassA:testPropertyA)
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String
Package: yet.another.test.package
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String

Transformation error message: Subclass test.package.TestClassA already defines a property named testPropertyC
~~~~

nl.zeesoft.zdm.test.TestModelInitialize
---------------------------------------
This test shows how to create a *ModelStructure* and then use that to initialize a *Model*.

**Example implementation**  
~~~~
// Create package structure
ModelStructure structure = new ModelStructure();
structure.addPackage("test.package");
structure.addClass("test.package","TestClass");
structure.addProperty("test.package","TestClass","testProperty");
// Create model
Model model = new Model(structure);
~~~~

Class references;  
 * [TestModelInitialize](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/TestModelInitialize.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-02-19 19:55:41:860 Applied transformation: IncrementVersion()
2017-02-19 19:55:41:861 Initialized model (package: test.package)

Package: test.package
- Class: TestClass
  - Property: testProperty, type: java.lang.Boolean

~~~~

nl.zeesoft.zdm.test.TestModelVersioning
---------------------------------------
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
~~~~

This test uses the *MockModelTransformations*.
It provides a reusable set of transformations to build a test model.

Class references;  
 * [TestModelVersioning](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/TestModelVersioning.java)
 * [MockModelTransformations](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/MockModelTransformations.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java)

**Test output**  
The output of this test shows two model transformation logs and corresponding package structures;  
 * Once after reverting the current version changes.  
 * Once after reverting back to version one of the model (leaving only the initial version).  
~~~~
Version: 0
2017-02-19 19:55:41:895 Applied transformation: IncrementVersion()
2017-02-19 19:55:41:896 Applied transformation: AddPackage(name="test.package")
2017-02-19 19:55:41:896 Applied transformation: AddClass(packageName="test.package",name="TestClassA")
2017-02-19 19:55:41:897 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyA",type="java.lang.Boolean")
2017-02-19 19:55:41:897 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-02-19 19:55:41:898 Applied transformation: AddClass(packageName="test.package",name="TestClassB")
2017-02-19 19:55:41:898 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyA",type="java.lang.Integer")
2017-02-19 19:55:41:898 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyB",type="test.package.TestClassA")

Version: 1
2017-02-19 19:55:41:898 Applied transformation: IncrementVersion()
2017-02-19 19:55:41:899 Applied transformation: AddPackage(name="another.test.package")
2017-02-19 19:55:41:899 Applied transformation: AddClass(packageName="another.test.package",name="TestClassA",extendsPackageName="test.package",extendsClassName="TestClassA",abstract="false")
2017-02-19 19:55:41:900 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-02-19 19:55:41:900 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyC",type="java.lang.Boolean")
2017-02-19 19:55:41:900 Applied transformation: AddClass(packageName="another.test.package",name="TestClassB")
2017-02-19 19:55:41:901 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyA")
2017-02-19 19:55:41:901 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyB")

Version: 2
2017-02-19 19:55:41:902 Applied transformation: IncrementVersion()
2017-02-19 19:55:41:902 Applied transformation: AddPackage(name="yet.another.test.package")
2017-02-19 19:55:41:902 Applied transformation: AddClass(packageName="yet.another.test.package",name="TestClassA")
2017-02-19 19:55:41:902 Applied transformation: AddProperty(className="TestClassA",packageName="yet.another.test.package",name="testPropertyA")
2017-02-19 19:55:41:903 Applied transformation: AddProperty(className="TestClassA",packageName="yet.another.test.package",name="testPropertyB")
2017-02-19 19:55:41:903 Applied transformation: AddClass(packageName="yet.another.test.package",name="TestClassB")
2017-02-19 19:55:41:903 Applied transformation: AddProperty(className="TestClassB",packageName="yet.another.test.package",name="testPropertyA")
2017-02-19 19:55:41:904 Applied transformation: AddProperty(className="TestClassB",packageName="yet.another.test.package",name="testPropertyB")
2017-02-19 19:55:41:904 Applied transformation: RemoveClass(packageName="yet.another.test.package",name="TestClassA")
2017-02-19 19:55:41:905 Applied transformation: RemovePackageAll()
2017-02-19 19:55:41:905 Applied transformation: AddPackage(name="test.package")
2017-02-19 19:55:41:905 Applied transformation: AddClass(packageName="test.package",name="TestClassA")
2017-02-19 19:55:41:906 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyA",type="java.lang.Boolean")
2017-02-19 19:55:41:906 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-02-19 19:55:41:906 Applied transformation: AddClass(packageName="test.package",name="TestClassB")
2017-02-19 19:55:41:907 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyA",type="java.lang.Integer")
2017-02-19 19:55:41:907 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyB",type="test.package.TestClassA")
2017-02-19 19:55:41:908 Applied transformation: AddPackage(name="another.test.package")
2017-02-19 19:55:41:909 Applied transformation: AddClass(packageName="another.test.package",name="TestClassA")
2017-02-19 19:55:41:909 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-02-19 19:55:41:910 Applied transformation: AddProperty(className="TestClassA",packageName="another.test.package",name="testPropertyC",type="java.lang.Boolean")
2017-02-19 19:55:41:910 Applied transformation: AddClass(packageName="another.test.package",name="TestClassB")
2017-02-19 19:55:41:910 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyA",type="java.lang.String")
2017-02-19 19:55:41:911 Applied transformation: AddProperty(className="TestClassB",packageName="another.test.package",name="testPropertyB",type="java.lang.String")
2017-02-19 19:55:41:911 Applied transformation: SetClassExtendsClass(packageName="another.test.package",name="TestClassA",extendsPackageName="test.package",extendsClassName="TestClassA")
2017-02-19 19:55:41:911 Applied transformation: RevertVersionCurrent()
2017-02-19 19:55:41:912 Failed to apply transformation: RevertVersionCurrent(), error: The current model version does not contain any changes

Package: test.package
- Class: TestClassA
  - Property: testPropertyA, type: java.lang.Boolean
  - Property: testPropertyB, type: List<java.lang.String>
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.Integer
  - Property: testPropertyB, type: test.package.TestClassA
Package: another.test.package
- Class: TestClassA, extends: test.package.TestClassA
  - Property: testPropertyB, type: List<java.lang.String> (overrides: another.test.package.TestClassA:testPropertyB)
  - Property: testPropertyC, type: java.lang.Boolean (overrides: another.test.package.TestClassA:testPropertyC)
  - Property: testPropertyA, type: java.lang.Boolean (extends: test.package.TestClassA:testPropertyA)
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.String
  - Property: testPropertyB, type: java.lang.String

Version: 0
2017-02-19 19:55:41:895 Applied transformation: IncrementVersion()
2017-02-19 19:55:41:896 Applied transformation: AddPackage(name="test.package")
2017-02-19 19:55:41:896 Applied transformation: AddClass(packageName="test.package",name="TestClassA")
2017-02-19 19:55:41:897 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyA",type="java.lang.Boolean")
2017-02-19 19:55:41:897 Applied transformation: AddProperty(className="TestClassA",packageName="test.package",name="testPropertyB",type="java.lang.String",list="true")
2017-02-19 19:55:41:898 Applied transformation: AddClass(packageName="test.package",name="TestClassB")
2017-02-19 19:55:41:898 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyA",type="java.lang.Integer")
2017-02-19 19:55:41:898 Applied transformation: AddProperty(className="TestClassB",packageName="test.package",name="testPropertyB",type="test.package.TestClassA")
2017-02-19 19:55:41:999 Applied transformation: RevertVersion(number="1")

Package: test.package
- Class: TestClassA
  - Property: testPropertyA, type: java.lang.Boolean
  - Property: testPropertyB, type: List<java.lang.String>
- Class: TestClassB
  - Property: testPropertyA, type: java.lang.Integer
  - Property: testPropertyB, type: test.package.TestClassA

~~~~

nl.zeesoft.zdm.test.TestModelSelf
---------------------------------
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
 * [TestModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/TestModelSelf.java)
 * [MockModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/MockModelSelf.java)
 * [ModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/ModelSelf.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-02-19 19:55:42:021 Applied transformation: IncrementVersion()
2017-02-19 19:55:42:022 Applied transformation: AddPackage(name="nl.zeesoft.zdm.model")
2017-02-19 19:55:42:022 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelSelf",abstract="false")
2017-02-19 19:55:42:023 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdm.model",name="versions",type="nl.zeesoft.zdm.model.ModelVersion",list="true")
2017-02-19 19:55:42:023 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdm.model",name="packages",type="nl.zeesoft.zdm.model.ModelPackage",list="true")
2017-02-19 19:55:42:023 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelVersion")
2017-02-19 19:55:42:024 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="number",type="java.lang.Integer")
2017-02-19 19:55:42:024 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="log",type="java.lang.StringBuilder")
2017-02-19 19:55:42:024 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="transformations",type="nl.zeesoft.zdm.model.ModelTransformation",list="true")
2017-02-19 19:55:42:025 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="initialTransformations",type="nl.zeesoft.zdm.model.ModelTransformation",list="true")
2017-02-19 19:55:42:025 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelTransformation")
2017-02-19 19:55:42:025 Applied transformation: AddProperty(className="ModelTransformation",packageName="nl.zeesoft.zdm.model",name="parameters",type="nl.zeesoft.zdm.model.ModelTransformationParameter",list="true")
2017-02-19 19:55:42:026 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelTransformationParameter")
2017-02-19 19:55:42:026 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdm.model",name="name")
2017-02-19 19:55:42:026 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdm.model",name="value")
2017-02-19 19:55:42:027 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelNamedObject",abstract="true")
2017-02-19 19:55:42:027 Applied transformation: AddProperty(className="ModelNamedObject",packageName="nl.zeesoft.zdm.model",name="id",type="java.lang.Long")
2017-02-19 19:55:42:027 Applied transformation: AddProperty(className="ModelNamedObject",packageName="nl.zeesoft.zdm.model",name="name")
2017-02-19 19:55:42:028 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelPackage",extendsPackageName="nl.zeesoft.zdm.model",extendsClassName="ModelNamedObject",abstract="false")
2017-02-19 19:55:42:028 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdm.model",name="classes",type="nl.zeesoft.zdm.model.ModelClass",list="true")
2017-02-19 19:55:42:028 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelClass",extendsPackageName="nl.zeesoft.zdm.model",extendsClassName="ModelNamedObject",abstract="false")
2017-02-19 19:55:42:029 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="properties",type="nl.zeesoft.zdm.model.ModelProperty",list="true")
2017-02-19 19:55:42:029 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="extendsClass",type="nl.zeesoft.zdm.model.ModelClass")
2017-02-19 19:55:42:030 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="abstract",type="java.lang.Boolean")
2017-02-19 19:55:42:030 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelProperty",extendsPackageName="nl.zeesoft.zdm.model",extendsClassName="ModelNamedObject",abstract="false")
2017-02-19 19:55:42:030 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="type")
2017-02-19 19:55:42:031 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="list",type="java.lang.Boolean")

Version: 1
2017-02-19 19:55:42:032 Applied transformation: IncrementVersion()

Package: nl.zeesoft.zdm.model
- Class: ModelSelf
  - Property: versions, type: List<nl.zeesoft.zdm.model.ModelVersion>
  - Property: packages, type: List<nl.zeesoft.zdm.model.ModelPackage>
- Class: ModelVersion
  - Property: number, type: java.lang.Integer
  - Property: log, type: java.lang.StringBuilder
  - Property: transformations, type: List<nl.zeesoft.zdm.model.ModelTransformation>
  - Property: initialTransformations, type: List<nl.zeesoft.zdm.model.ModelTransformation>
- Class: ModelTransformation
  - Property: parameters, type: List<nl.zeesoft.zdm.model.ModelTransformationParameter>
- Class: ModelTransformationParameter
  - Property: name, type: java.lang.String
  - Property: value, type: java.lang.String
- Class: ModelNamedObject (abstract)
  - Property: id, type: java.lang.Long
  - Property: name, type: java.lang.String
- Class: ModelPackage, extends: nl.zeesoft.zdm.model.ModelNamedObject
  - Property: classes, type: List<nl.zeesoft.zdm.model.ModelClass> (overrides: nl.zeesoft.zdm.model.ModelPackage:classes)
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdm.model.ModelNamedObject:id)
  - Property: name, type: java.lang.String (extends: nl.zeesoft.zdm.model.ModelNamedObject:name)
- Class: ModelClass, extends: nl.zeesoft.zdm.model.ModelNamedObject
  - Property: properties, type: List<nl.zeesoft.zdm.model.ModelProperty> (overrides: nl.zeesoft.zdm.model.ModelClass:properties)
  - Property: extendsClass, type: nl.zeesoft.zdm.model.ModelClass (overrides: nl.zeesoft.zdm.model.ModelClass:extendsClass)
  - Property: abstract, type: java.lang.Boolean (overrides: nl.zeesoft.zdm.model.ModelClass:abstract)
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdm.model.ModelNamedObject:id)
  - Property: name, type: java.lang.String (extends: nl.zeesoft.zdm.model.ModelNamedObject:name)
- Class: ModelProperty, extends: nl.zeesoft.zdm.model.ModelNamedObject
  - Property: type, type: java.lang.String (overrides: nl.zeesoft.zdm.model.ModelProperty:type)
  - Property: list, type: java.lang.Boolean (overrides: nl.zeesoft.zdm.model.ModelProperty:list)
  - Property: id, type: java.lang.Long (extends: nl.zeesoft.zdm.model.ModelNamedObject:id)
  - Property: name, type: java.lang.String (extends: nl.zeesoft.zdm.model.ModelNamedObject:name)

~~~~

nl.zeesoft.zdm.test.TestModelSelfConvertModel
---------------------------------------------
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
 * [TestModelSelfConvertModel](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/TestModelSelfConvertModel.java)
 * [MockModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/test/MockModelSelf.java)
 * [ModelSelf](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/ModelSelf.java)
 * [Model](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDM/src/nl/zeesoft/zdm/model/Model.java)

**Test output**  
The output of this test shows the model transformation log for all versions of the model and the resulting package structure.  
~~~~
Version: 0
2017-02-19 19:55:42:021 Applied transformation: IncrementVersion()
2017-02-19 19:55:42:022 Applied transformation: AddPackage(name="nl.zeesoft.zdm.model")
2017-02-19 19:55:42:022 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelSelf",abstract="false")
2017-02-19 19:55:42:023 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdm.model",name="versions",type="nl.zeesoft.zdm.model.ModelVersion",list="true")
2017-02-19 19:55:42:023 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdm.model",name="packages",type="nl.zeesoft.zdm.model.ModelPackage",list="true")
2017-02-19 19:55:42:023 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelVersion")
2017-02-19 19:55:42:024 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="number",type="java.lang.Integer")
2017-02-19 19:55:42:024 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="log",type="java.lang.StringBuilder")
2017-02-19 19:55:42:024 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="transformations",type="nl.zeesoft.zdm.model.ModelTransformation",list="true")
2017-02-19 19:55:42:025 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="initialTransformations",type="nl.zeesoft.zdm.model.ModelTransformation",list="true")
2017-02-19 19:55:42:025 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelTransformation")
2017-02-19 19:55:42:025 Applied transformation: AddProperty(className="ModelTransformation",packageName="nl.zeesoft.zdm.model",name="parameters",type="nl.zeesoft.zdm.model.ModelTransformationParameter",list="true")
2017-02-19 19:55:42:026 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelTransformationParameter")
2017-02-19 19:55:42:026 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdm.model",name="name")
2017-02-19 19:55:42:026 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdm.model",name="value")
2017-02-19 19:55:42:027 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelNamedObject",abstract="true")
2017-02-19 19:55:42:027 Applied transformation: AddProperty(className="ModelNamedObject",packageName="nl.zeesoft.zdm.model",name="id",type="java.lang.Long")
2017-02-19 19:55:42:027 Applied transformation: AddProperty(className="ModelNamedObject",packageName="nl.zeesoft.zdm.model",name="name")
2017-02-19 19:55:42:028 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelPackage",extendsPackageName="nl.zeesoft.zdm.model",extendsClassName="ModelNamedObject",abstract="false")
2017-02-19 19:55:42:028 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdm.model",name="classes",type="nl.zeesoft.zdm.model.ModelClass",list="true")
2017-02-19 19:55:42:028 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelClass",extendsPackageName="nl.zeesoft.zdm.model",extendsClassName="ModelNamedObject",abstract="false")
2017-02-19 19:55:42:029 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="properties",type="nl.zeesoft.zdm.model.ModelProperty",list="true")
2017-02-19 19:55:42:029 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="extendsClass",type="nl.zeesoft.zdm.model.ModelClass")
2017-02-19 19:55:42:030 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="abstract",type="java.lang.Boolean")
2017-02-19 19:55:42:030 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelProperty",extendsPackageName="nl.zeesoft.zdm.model",extendsClassName="ModelNamedObject",abstract="false")
2017-02-19 19:55:42:030 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="type")
2017-02-19 19:55:42:031 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="list",type="java.lang.Boolean")

Version: 1
2017-02-19 19:55:42:032 Applied transformation: IncrementVersion()
2017-02-19 19:55:42:117 Applied transformation: RemovePackageAll()
2017-02-19 19:55:42:117 Applied transformation: AddPackage(name="nl.zeesoft.zdm.model")
2017-02-19 19:55:42:117 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelSelf")
2017-02-19 19:55:42:117 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdm.model",name="versions",type="nl.zeesoft.zdm.model.ModelVersion",list="true")
2017-02-19 19:55:42:118 Applied transformation: AddProperty(className="ModelSelf",packageName="nl.zeesoft.zdm.model",name="packages",type="nl.zeesoft.zdm.model.ModelPackage",list="true")
2017-02-19 19:55:42:118 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelVersion")
2017-02-19 19:55:42:118 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="number",type="java.lang.Integer",list="false")
2017-02-19 19:55:42:119 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="log",type="java.lang.StringBuilder",list="false")
2017-02-19 19:55:42:119 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="transformations",type="nl.zeesoft.zdm.model.ModelTransformation",list="true")
2017-02-19 19:55:42:119 Applied transformation: AddProperty(className="ModelVersion",packageName="nl.zeesoft.zdm.model",name="initialTransformations",type="nl.zeesoft.zdm.model.ModelTransformation",list="true")
2017-02-19 19:55:42:119 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelTransformation")
2017-02-19 19:55:42:120 Applied transformation: AddProperty(className="ModelTransformation",packageName="nl.zeesoft.zdm.model",name="parameters",type="nl.zeesoft.zdm.model.ModelTransformationParameter",list="true")
2017-02-19 19:55:42:120 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelTransformationParameter")
2017-02-19 19:55:42:120 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdm.model",name="name",type="java.lang.String",list="false")
2017-02-19 19:55:42:121 Applied transformation: AddProperty(className="ModelTransformationParameter",packageName="nl.zeesoft.zdm.model",name="value",type="java.lang.String",list="false")
2017-02-19 19:55:42:121 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelPackage")
2017-02-19 19:55:42:121 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdm.model",name="classes",type="nl.zeesoft.zdm.model.ModelClass",list="true")
2017-02-19 19:55:42:122 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdm.model",name="id",type="java.lang.Long",list="false")
2017-02-19 19:55:42:122 Applied transformation: AddProperty(className="ModelPackage",packageName="nl.zeesoft.zdm.model",name="name",type="java.lang.String",list="false")
2017-02-19 19:55:42:122 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelClass")
2017-02-19 19:55:42:123 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="properties",type="nl.zeesoft.zdm.model.ModelProperty",list="true")
2017-02-19 19:55:42:123 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="extendsClass",type="nl.zeesoft.zdm.model.ModelClass",list="false")
2017-02-19 19:55:42:123 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="abstract",type="java.lang.Boolean",list="false")
2017-02-19 19:55:42:124 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="id",type="java.lang.Long",list="false")
2017-02-19 19:55:42:124 Applied transformation: AddProperty(className="ModelClass",packageName="nl.zeesoft.zdm.model",name="name",type="java.lang.String",list="false")
2017-02-19 19:55:42:124 Applied transformation: AddClass(packageName="nl.zeesoft.zdm.model",name="ModelProperty")
2017-02-19 19:55:42:125 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="type",type="java.lang.String",list="false")
2017-02-19 19:55:42:125 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="list",type="java.lang.Boolean",list="false")
2017-02-19 19:55:42:125 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="id",type="java.lang.Long",list="false")
2017-02-19 19:55:42:125 Applied transformation: AddProperty(className="ModelProperty",packageName="nl.zeesoft.zdm.model",name="name",type="java.lang.String",list="false")
2017-02-19 19:55:42:126 Applied transformation: ConvertModel()

Package: nl.zeesoft.zdm.model
- Class: ModelSelf
  - Property: versions, type: List<nl.zeesoft.zdm.model.ModelVersion>
  - Property: packages, type: List<nl.zeesoft.zdm.model.ModelPackage>
- Class: ModelVersion
  - Property: number, type: java.lang.Integer
  - Property: log, type: java.lang.StringBuilder
  - Property: transformations, type: List<nl.zeesoft.zdm.model.ModelTransformation>
  - Property: initialTransformations, type: List<nl.zeesoft.zdm.model.ModelTransformation>
- Class: ModelTransformation
  - Property: parameters, type: List<nl.zeesoft.zdm.model.ModelTransformationParameter>
- Class: ModelTransformationParameter
  - Property: name, type: java.lang.String
  - Property: value, type: java.lang.String
- Class: ModelPackage
  - Property: classes, type: List<nl.zeesoft.zdm.model.ModelClass>
  - Property: id, type: java.lang.Long
  - Property: name, type: java.lang.String
- Class: ModelClass
  - Property: properties, type: List<nl.zeesoft.zdm.model.ModelProperty>
  - Property: extendsClass, type: nl.zeesoft.zdm.model.ModelClass
  - Property: abstract, type: java.lang.Boolean
  - Property: id, type: java.lang.Long
  - Property: name, type: java.lang.String
- Class: ModelProperty
  - Property: type, type: java.lang.String
  - Property: list, type: java.lang.Boolean
  - Property: id, type: java.lang.Long
  - Property: name, type: java.lang.String

~~~~

Test results
------------
All 6 tests have been executed successfully (42 assertions).  
Total test duration: 532 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zdm.test.TestModel: 412 Kb / 0 Mb
 * nl.zeesoft.zdm.test.TestModelApplyList: 440 Kb / 0 Mb
 * nl.zeesoft.zdm.test.TestModelInitialize: 441 Kb / 0 Mb
 * nl.zeesoft.zdm.test.TestModelVersioning: 244 Kb / 0 Mb
 * nl.zeesoft.zdm.test.TestModelSelf: 284 Kb / 0 Mb
 * nl.zeesoft.zdm.test.TestModelSelfConvertModel: 288 Kb / 0 Mb
