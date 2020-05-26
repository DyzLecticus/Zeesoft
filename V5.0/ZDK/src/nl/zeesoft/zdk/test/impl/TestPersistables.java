package nl.zeesoft.zdk.test.impl;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.persist.PersistableCollection;
import nl.zeesoft.zdk.persist.PersistableCollectionBase;
import nl.zeesoft.zdk.persist.PersistableObject;
import nl.zeesoft.zdk.persist.PersistableProperty;
import nl.zeesoft.zdk.persist.Query;
import nl.zeesoft.zdk.persist.QueryFilter;
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
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestPersistables.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableCollectionBase.class));
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
		int[] ints = {0, 1, 2, 3};
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
		
		PersistableCollectionBase collection = new PersistableCollectionBase();
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
		
		
		System.out.println();
		System.out.println("Compressed;");
		collection = new PersistableCollection();
		collection.put(parent1);
		collection.put(parent2);
		Str cstr = collection.toStr();
		collection.fromStr(cstr);
		cstr = collection.toStr();
		System.out.println(cstr);
		
		Str cstrs = cstr.split(PersistableCollectionBase.START_OBJECTS).get(1);
		cstrs.sb().append(PersistableCollectionBase.START_OBJECTS);
		System.out.println();
		System.out.println("Original: " + str.length() + ", compressed: " + cstr.length() + ", compressed excluding header: " + cstrs.length());
		
		// Test queries
		collection = new PersistableCollection();
		collection.put(parent1);
		collection.put(parent2);
		
		// Test equals
		Query query = collection.query(
			Query.create(PersistableParent.class)
			.filter("testString","TestParent1")
		);
		assertResultsIsTestParent1(query,1);
		
		// Test not equals
		query = collection.query(
			Query.create(PersistableParent.class)
			.filter("testString",true,"TestParent2")
		);
		assertResultsIsTestParent1(query,2);
		
		// Test contains
		query = collection.query(
			Query.create(PersistableParent.class)
			.filter("testString",QueryFilter.CONTAINS,"1")
		);
		assertResultsIsTestParent1(query,3);
		
		// Test multiple types
		query = collection.query(
			Query.create(PersistableParent.class)
			.addClass(PersistableChild.class)
			.filter("testString",QueryFilter.CONTAINS,"1")
		);
		assertEqual(query.results.size(),2,"Query results size does not match expectation[4]");
		
		// Test multiple types and multiple filters
		query = collection.query(
			Query.create(PersistableParent.class)
			.addClass(PersistableChild.class)
			.filter("testString",QueryFilter.CONTAINS,"1")
			.filter("testString",true,QueryFilter.CONTAINS,"Child")
		);
		assertResultsIsTestParent1(query,5);
		
		// Test less
		query = collection.query(
			Query.create(PersistableChild.class)
			.filter("testInt",QueryFilter.LESS,222)
		);
		assertEqual(query.results.size(),1,"Query results size does not match expectation[6]");

		// Test less or equal (invert greater)
		query = collection.query(
			Query.create(PersistableChild.class)
			.filter("testInt",true,QueryFilter.GREATER,222)
		);
		assertEqual(query.results.size(),2,"Query results size does not match expectation[7]");
		
		// Test list contains
		query = collection.query(
			Query.create(PersistableParent.class)
			.filter("testStringList",QueryFilter.CONTAINS,"TestString2")
		);
		assertResultsIsTestParent1(query,8);
		
		// Test array contains
		query = collection.query(
			Query.create(PersistableChild.class)
			.filter("testParents",QueryFilter.CONTAINS,parent2)
		);
		assertEqual(query.results.size(),2,"Query results size does not match expectation[9]");
		
		// Test primitive array contains
		query = collection.query(
			Query.create(PersistableParent.class)
			.filter("testIntArray",QueryFilter.CONTAINS,2)
		);
		assertResultsIsTestParent1(query,10);

		// Test filter error
		query = collection.query(
			Query.create(PersistableParent.class)
			.filter("qwerqwer",QueryFilter.CONTAINS,2)
		);
		assertEqual(query.errors.size(),1,"Query errors size does not match expectation[9]");
	}
	
	private void assertResultsIsTestParent1(Query query,int num) {
		if (assertEqual(query.results.size(),1,"Query results size does not match expectation[" + num + "]")) {
			Object first = query.results.get(query.results.firstKey());
			if (assertEqual(first.getClass().getName(),PersistableParent.class.getName(),"Query did not return the expected type[" + num + "]")) {
				PersistableParent result = (PersistableParent) first;
				assertEqual(result.getTestString(),"TestParent1","Query did not return the expected object[" + num + "]");
			}
		}
	}
}
