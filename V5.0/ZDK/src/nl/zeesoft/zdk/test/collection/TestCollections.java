package nl.zeesoft.zdk.test.collection;

import java.util.List;

import nl.zeesoft.zdk.FileIO;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.CompleteCollection;
import nl.zeesoft.zdk.collection.CompressedCollection;
import nl.zeesoft.zdk.collection.PartitionedCollection;
import nl.zeesoft.zdk.collection.PersistableCollection;
import nl.zeesoft.zdk.collection.Query;
import nl.zeesoft.zdk.collection.QueryableCollection;
import nl.zeesoft.zdk.test.util.TestObject;
import nl.zeesoft.zdk.test.util.Tester;
import nl.zeesoft.zdk.thread.CodeRunnerManager;

public class TestCollections extends TestObject {
	private static final int	EXPECTED_SIZE	= 4;
	
	public TestCollections(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestCollections(new Tester())).runTest(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to use different collections provided by this library. ");
		System.out.println("These collections are a mix between Java List and LinkedList style collections. ");
		System.out.println("They use Java reflection to provide features like queries and persistence with minimal programming. ");
		System.out.println("The following collections are provided by this library;  ");
		System.out.println(" * *QueryableCollection* provides support for queries.  ");
		System.out.println(" * *CompleteCollection* extends *QueryableCollection* and automatically adds referenced objects. ");
		System.out.println(" * *PersistableCollection* extends *CompleteCollection* and adds persistence. ");
		System.out.println(" * *CompressedCollection* extends *PersistableCollection* and adds compression to persistence. ");
		System.out.println(" * *PartitionedCollection* extends *CompressedCollection* and adds multithreading for saving/loading large collections to/from directories. ");
		System.out.println();
		System.out.println("Persistence for most standard property types is supported including arrays and lists for non primitives. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the PersistableCollection");
		System.out.println("PersistableCollection collection = new PersistableCollection();");
		System.out.println("// Add objects to the collection");
		System.out.println("Str id1 = collection.add(new PersistableParent());");
		System.out.println("Str id2 = collection.add(new PersistableChild());");
		System.out.println("// Write the data to a file");
		System.out.println("collection.toPath(\"data/fileName.txt\");");
		System.out.println("// Load the collection from a file");
		System.out.println("collection.fromPath(\"data/fileName.txt\");");
		System.out.println("// Query the collection");
		System.out.println("SortedMap<Str,Object> results = collection.query(");
		System.out.println("    Query.create(PersistableParent.class)");
		System.out.println("    .equals(\"getTestString\",\"Parent\")");
		System.out.println("    .notContains(\"getTestIntArray\",42)");
		System.out.println(").results;");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestCollections.class));
		System.out.println(" * " + getTester().getLinkForClass(QueryableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(CompleteCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(CompressedCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PartitionedCollection.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the file structure of an example persistable collection.  ");
	}

	@Override
	protected void test(String[] args) {
		CollectionTestParent parent1 = new CollectionTestParent();
		parent1.setTestString("TestParent1");
		parent1.getTestStringList().add("TestString1");
		parent1.getTestStringList().add("TestString2");
		parent1.getTestFloatList().add(0.1F);
		parent1.getTestFloatList().add(0.2F);
		String[] strings = {"TestElement1", "TestElement2"};
		parent1.setTestStringArray(strings);
		int[] ints = {0, 1, 2, 3};
		parent1.setTestIntArray(ints);
		
		CollectionTestParent parent2 = new CollectionTestParent();
		parent2.setTestString("TestParent2");
		
		parent1.setTestPartner(parent2);
		parent2.setTestPartner(parent1);
		
		CollectionTestChild child1 = new CollectionTestChild();
		child1.setTestString("TestChild1");
		child1.setTestBoolean(true);
		child1.setTestInt(111);
		child1.getTestParents()[0] = parent1;
		child1.getTestParents()[1] = parent2;
		child1.getTestStr().sb().append("I like pizza!");
		
		CollectionTestChild child2 = new CollectionTestChild();
		child2.setTestString("TestChild2");
		child2.setTestBoolean(false);
		child2.setTestInt(222);
		child2.getTestParents()[0] = parent2;
		child2.getTestStr().sb().append("I like ice cream!");
		
		parent1.getTestChildren().add(child1);
		parent1.getTestChildren().add(child2);
		parent2.getTestChildren().add(child2);

		testQueries(parent1,parent2);
		
		if (!hasFailures()) {
			PersistableCollection collection = new PersistableCollection();
			Str id = collection.put(parent1);
			collection.put(parent2);
			assertEqual(collection.size(),EXPECTED_SIZE,"PersistableCollectionBase size does not match expectation[1]");
			assertEqual(
				collection.getObjects(CollectionTestParent.class.getName()).size(),2,
				"Number of PersistableParent objects does not match expectation"
				);
			collection.put(parent2);
			assertEqual(collection.size(),EXPECTED_SIZE,"PersistableCollectionBase size does not match expectation[2]");
			
			Str objStr = collection.getObjectAsStr(id);
			Object object = collection.getObjectFromStr(objStr);
			if (assertEqual(object.getClass().getName(),CollectionTestParent.class.getName(),"Class name does not match expectation")) {
				CollectionTestParent parent = (CollectionTestParent) object;
				assertEqual(parent.getTestString(),"TestParent1","Parent testString does not match expectation");
				assertEqual(parent.getTestStringList().size(),0,"Parent testStringList size does not match expectation");
				assertEqual(parent.getTestFloatList().size(),0,"Parent testFloatList size does not match expectation");
				assertEqual(parent.getTestStringArray().length,2,"Parent testStringArray length does not match expectation");
				assertEqual(parent.getTestIntArray().length,4,"Parent testIntArray length does not match expectation");
				if (assertEqual(parent.getTestChildren().size(),2,"Children size does not match expectation")) {
					assertEqual(parent.getTestChildren().get(0).getTestString(),"TestChild1","Child testString does not match expectation");
				}
			}
			
			System.out.println("Regular;");
			Str str = collection.toStr();
			System.out.println(str);
			collection.clear();
			assertEqual(collection.size(),0,"Failed to clear the collection");
			collection.fromStr(str);
			assertEqual(collection.size(),EXPECTED_SIZE,"Failed to parse the collection");
			assertEqual(collection.toStr(),str,"Collection Str does not match expectation");
			
			System.out.println();
			System.out.println("Compressed;");
			collection = new CompressedCollection();
			collection.put(parent1);
			collection.put(parent2);
			Str cstr = collection.toStr();
			collection.fromStr(cstr);
			cstr = collection.toStr();
			System.out.println(cstr);
			
			Str cstrs = cstr.split(PersistableCollection.START_OBJECTS).get(1);
			cstrs.sb().append(PersistableCollection.START_OBJECTS);
			System.out.println();
			System.out.println("Original: " + str.length() + ", compressed: " + cstr.length() + ", compressed excluding header: " + cstrs.length());
		}
		if (!hasFailures()) {
			String dir = "data/";
			FileIO.writeFile(new Str("Remove me"),dir + "2.txt");
			
			PartitionedCollection collection = new PartitionedCollection();
			collection.put(parent1);
			collection.put(parent2);
			collection.setPartitionSize(2);
			Str error = collection.toPath(dir);
			assertEqual(error,new Str(),"An unexpected error was returned");

			sleep(10);
			error = collection.fromPath(dir);
			
			List<Str> actionLog = FileIO.getActionLog();
			assertEqual(actionLog.size(),6,"Number of actions does not match expectation");
			assertEqual(Str.contains(actionLog,FileIO.WRITE + dir + "2.txt"),true,"Expected write 2 not exectuted");
			assertEqual(Str.contains(actionLog,FileIO.WRITE + dir + "0.txt"),true,"Expected write 0 not exectuted");
			assertEqual(Str.contains(actionLog,FileIO.WRITE + dir + "1.txt"),true,"Expected write 1 not exectuted");
			assertEqual(Str.contains(actionLog,FileIO.DELETE + dir + "2.txt"),true,"Expected delete 2 not exectuted");
			assertEqual(Str.contains(actionLog,FileIO.READ + dir + "0.txt"),true,"Expected read 0 not exectuted");
			assertEqual(Str.contains(actionLog,FileIO.READ + dir + "1.txt"),true,"Expected read 1 not exectuted");

			assertEqual(collection.size(),4,"Failed to load the partitioned collection");
			
			assertEqual(CodeRunnerManager.getActiverRunners().size(),0,"Number of active code runners does not match expectation");
		}
	}
	
	private void testQueries(CollectionTestParent parent1, CollectionTestParent parent2) {
		CompleteCollection collection = new CompleteCollection();
		collection.put(parent1);
		collection.put(parent2);
		assertEqual(collection.size(),EXPECTED_SIZE,"CompleteCollection size does not match expectation");
		
		// Test equals
		Query query = collection.query(
			Query.create(CollectionTestParent.class)
			.equals("testString","TestParent1")
		);
		assertResultsIsTestParent1(query,1);
		
		// Test not equals
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.notEquals("testString","TestParent2")
		);
		assertResultsIsTestParent1(query,2);
		
		// Test contains
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.contains("testString","1")
		);
		assertResultsIsTestParent1(query,3);
		
		// Test multiple types
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.addClass(CollectionTestChild.class)
			.contains("testString","1")
		);
		assertEqual(query.results.size(),2,"Query results size does not match expectation[4]");
		
		// Test multiple types and multiple filters
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.addClass(CollectionTestChild.class)
			.contains("testString","1")
			.notContains("testString","Child")
		);
		assertResultsIsTestParent1(query,5);
		
		// Test less
		query = collection.query(
			Query.create(CollectionTestChild.class)
			.lessThan("testInt",222)
		);
		assertEqual(query.results.size(),1,"Query results size does not match expectation[6]");

		// Test less or equal
		query = collection.query(
			Query.create(CollectionTestChild.class)
			.lessOrEquals("testInt",222)
		);
		assertEqual(query.results.size(),2,"Query results size does not match expectation[7]");
		
		// Test list contains
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.contains("testStringList","TestString2")
		);
		assertResultsIsTestParent1(query,8);
		
		// Test array contains
		query = collection.query(
			Query.create(CollectionTestChild.class)
			.contains("testParents",parent2)
		);
		assertEqual(query.results.size(),2,"Query results size does not match expectation[9]");
		
		// Test primitive array contains
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.contains("testIntArray",2)
		);
		assertResultsIsTestParent1(query,10);

		// Test filter error
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.equals("qwerqwer",2)
		);
		assertEqual(query.errors.size(),1,"Query errors size does not match expectation[9]");

		// Test method invocation
		query = collection.query(
			Query.create(CollectionTestParent.class)
			.equals("getTestString","TestParent1")
		);
		assertResultsIsTestParent1(query,10);
	}
	
	private void assertResultsIsTestParent1(Query query,int num) {
		if (assertEqual(query.results.size(),1,"Query results size does not match expectation[" + num + "]")) {
			Object first = query.results.get(query.results.firstKey());
			if (assertEqual(first.getClass().getName(),CollectionTestParent.class.getName(),"Query did not return the expected type[" + num + "]")) {
				CollectionTestParent result = (CollectionTestParent) first;
				assertEqual(result.getTestString(),"TestParent1","Query did not return the expected object[" + num + "]");
			}
		}
	}
}
