package nl.zeesoft.zdmk.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zdmk.model.transformations.TransformationObject;
import nl.zeesoft.zdmk.model.transformations.impl.AddClass;
import nl.zeesoft.zdmk.model.transformations.impl.AddPackage;
import nl.zeesoft.zdmk.model.transformations.impl.AddProperty;
import nl.zeesoft.zdmk.model.transformations.impl.IncrementVersion;
import nl.zeesoft.zdmk.model.transformations.impl.RemoveClass;

public class MockModelTransformations extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockModelTransformations*.");
		System.out.println("It provides a reusable set of transformations to build a test model.");
	}

	@Override
	protected Object initialzeMock() {
		List<TransformationObject> transformations = new ArrayList<TransformationObject>();
		transformations.add(new AddPackage("test.package"));
		transformations.add(new AddClass("test.package","TestClassA"));
		transformations.add(new AddProperty("test.package","TestClassA","testPropertyA",Boolean.class.getName()));
		transformations.add(new AddProperty("test.package","TestClassA","testPropertyB",String.class.getName(),true));
		transformations.add(new AddClass("test.package","TestClassB"));
		transformations.add(new AddProperty("test.package","TestClassB","testPropertyA",Integer.class.getName()));
		transformations.add(new AddProperty("test.package","TestClassB","testPropertyB","test.package.TestClassA"));
		transformations.add(new IncrementVersion());
		transformations.add(new AddPackage("another.test.package"));
		transformations.add(new AddClass("another.test.package","TestClassA",false,"test.package","TestClassA"));
		transformations.add(new AddProperty("another.test.package","TestClassA","testPropertyB",String.class.getName(),true)); // Override
		transformations.add(new AddProperty("another.test.package","TestClassA","testPropertyC",Boolean.class.getName()));
		transformations.add(new AddClass("another.test.package","TestClassB"));
		transformations.add(new AddProperty("another.test.package","TestClassB","testPropertyA"));
		transformations.add(new AddProperty("another.test.package","TestClassB","testPropertyB"));
		transformations.add(new IncrementVersion());
		transformations.add(new AddPackage("yet.another.test.package"));
		transformations.add(new AddClass("yet.another.test.package","TestClassA"));
		transformations.add(new AddProperty("yet.another.test.package","TestClassA","testPropertyA"));
		transformations.add(new AddProperty("yet.another.test.package","TestClassA","testPropertyB"));
		transformations.add(new AddClass("yet.another.test.package","TestClassB"));
		transformations.add(new AddProperty("yet.another.test.package","TestClassB","testPropertyA"));
		transformations.add(new AddProperty("yet.another.test.package","TestClassB","testPropertyB"));
		transformations.add(new RemoveClass("yet.another.test.package","TestClassA"));
		return transformations;
	}
}
