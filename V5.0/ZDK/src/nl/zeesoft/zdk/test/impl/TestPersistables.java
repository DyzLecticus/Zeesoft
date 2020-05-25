package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.persist.PersistableCollection;
import nl.zeesoft.zdk.persist.PersistableObject;
import nl.zeesoft.zdk.persist.PersistableProperty;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;

public class TestPersistables extends TestObject {
	private static final int	EXPECTED_SIZE	= 4;
	
	public TestPersistables(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestPersistables(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how a *PersistableCollection* instance can be used persist objects. ");
		System.out.println("Persisted object classes must be annotated with *PersistableObject*. ");
		System.out.println("The object properties that are to be persisted must be annotated with *PersistableProperty*. ");
		System.out.println("Most standard property types are supported including array lists for non primitives. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the PersistableCollection");
		System.out.println("PersistableCollection collection = new PersistableCollection();");
		System.out.println("// Add objects to the collection");
		System.out.println("collection.add(new Object());");
		System.out.println("collection.add(new Object());");
		System.out.println("// Write the data to a file");
		System.out.println("collection.toFile(\"fileName.txt\");");
		System.out.println("// Load the collection from a file");
		System.out.println("collection.fromFile(\"fileName.txt\");");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockStr.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPersistables.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableObject.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableProperty.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the file structure of an example persistable collection.  ");
	}

	@Override
	protected void test(String[] args) {
		PersistableParent parent1 = new PersistableParent();
		parent1.setTestString("TestParent1");
		parent1.getTestStringList().add("TestString1");
		parent1.getTestStringList().add("TestString2");
		parent1.getTestFloatList().add(0.1F);
		parent1.getTestFloatList().add(0.2F);
		String[] strings = {"TestElement1", "TestElement2"};
		parent1.setTestStringArray(strings);
		Integer[] ints = {0, 1, 2, 3};
		parent1.setTestIntArray(ints);
		
		PersistableParent parent2 = new PersistableParent();
		parent2.setTestString("TestParent2");
		
		parent1.setTestPartner(parent2);
		parent2.setTestPartner(parent1);
		
		PersistableChild child1 = new PersistableChild();
		child1.setTestString("TestChild1");
		child1.setTestBoolean(true);
		child1.setTestInt(111);
		child1.getTestParents()[0] = parent1;
		child1.getTestParents()[1] = parent2;
		child1.getTestStr().sb().append("I like pizza!");
		
		PersistableChild child2 = new PersistableChild();
		child2.setTestString("TestChild2");
		child2.setTestBoolean(false);
		child2.setTestInt(222);
		child2.getTestParents()[0] = parent2;
		child2.getTestStr().sb().append("I like ice cream!");
		
		parent1.getTestChildren().add(child1);
		parent1.getTestChildren().add(child2);
		parent2.getTestChildren().add(child2);
		
		PersistableCollection collection = new PersistableCollection();
		collection.put(parent1);
		collection.put(parent2);
		assertEqual(collection.size(),EXPECTED_SIZE,"Collection size does not match expectation");
		assertEqual(
			collection.getObjects(PersistableParent.class.getName()).size(),2,
			"Number of PersistableParent objects does not match expectation"
			);
		collection.put(parent2);
		assertEqual(collection.size(),EXPECTED_SIZE,"Collection size does not match expectation");
		
		Str objStr = collection.getObjectAsStr(parent1);
		Object object = collection.getObjectFromStr(objStr);
		if (assertEqual(object.getClass().getName(),PersistableParent.class.getName(),"Class name does not match expectation")) {
			PersistableParent parent = (PersistableParent) object;
			assertEqual(parent.getTestString(),"TestParent1","Parent testString does not match expectation");
			assertEqual(parent.getTestStringList().size(),0,"Parent testStringList size does not match expectation");
			assertEqual(parent.getTestFloatList().size(),0,"Parent testFloatList size does not match expectation");
			assertEqual(parent.getTestStringArray().length,2,"Parent testStringArray length does not match expectation");
			assertEqual(parent.getTestIntArray().length,4,"Parent testIntArray length does not match expectation");
			if (assertEqual(parent.getTestChildren().size(),2,"Children size does not match expectation")) {
				assertEqual(parent.getTestChildren().get(0).getTestString(),"TestChild1","Child testString does not match expectation");
			}
		}
		
		Str str = collection.toStr();
		System.out.println(str);
		collection.clear();
		assertEqual(collection.size(),0,"Failed to clear the collection");
		collection.fromStr(str);
		assertEqual(collection.size(),EXPECTED_SIZE,"Failed to parse the collection");
		assertEqual(collection.toStr(),str,"Collection Str does not match expectation");
	}
}
