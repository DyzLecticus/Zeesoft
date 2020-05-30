package nl.zeesoft.zdk.test.impl;

import java.util.SortedMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.collection.Query;
import nl.zeesoft.zdk.database.DatabaseConfiguration;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.Waiter;

public class TestDatabaseCollection extends TestObject {
	public TestDatabaseCollection(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseCollection(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: Describe
		System.out.println("This test shows how to use different collections provided by this library. ");
		System.out.println("These collections are a mix between Java List and LinkedList style collections. ");
		System.out.println("They use Java reflection to provide features like queries and persistence with minimal programming. ");
		System.out.println("The following collections are provided by this library;  ");
		System.out.println(" * *QueryableCollection* provides support for queries.  ");
		System.out.println(" * *CompleteCollection* extends *QueryableCollection* and automatically adds referenced objects. ");
		System.out.println(" * *PersistableCollectionBase* extends *CompleteCollection* and adds persistence. ");
		System.out.println(" * *PersistableCollection* extends *PersistableCollectionBase* and adds compression to persistence. ");
		System.out.println();
		System.out.println("Persisted object classes must be annotated with *PersistableObject*. ");
		System.out.println("The object properties that are to be persisted must be annotated with *PersistableProperty*. ");
		System.out.println("Most standard property types are supported including array lists for non primitives. ");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the PersistableCollection");
		System.out.println("PersistableCollection collection = new PersistableCollection();");
		System.out.println("// Add objects to the collection");
		System.out.println("Str id1 = collection.add(new PersistableParent());");
		System.out.println("Str id2 = collection.add(new PersistableChild());");
		System.out.println("// Write the data to a file");
		System.out.println("collection.toFile(\"fileName.txt\");");
		System.out.println("// Load the collection from a file");
		System.out.println("collection.fromFile(\"fileName.txt\");");
		System.out.println("// Query the collection");
		System.out.println("SortedMap<Str,Object> results = collection.query(");
		System.out.println("    Query.create(PersistableParent.class)");
		System.out.println("    .equals(\"getTestString\",\"Parent\")");
		System.out.println("    .notContains(\"getTestIntArray\",\"Parent\")");
		System.out.println(").results;");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestDatabaseCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(QueryableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(CompleteCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableCollectionBase.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableCollection.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableObject.class));
		System.out.println(" * " + getTester().getLinkForClass(PersistableProperty.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the file structure of an example persistable collection.  ");
		*/
	}

	@Override
	protected void test(String[] args) {
		DatabaseConfiguration config = new DatabaseConfiguration();
		Logger logger = new Logger(true);
		config.setLogger(logger);
		config.setBaseDir("dist/");
		config.rmDirs();
		config.mkDirs();
		
		System.out.println();
		DCTest collection = new DCTest(config);		
		config.debug(collection,new Str("Adding objects ..."));
		for (int i = 0; i < 10000; i++) {
			DCTestUser user = new DCTestUser();
			user.setName("TestUser" + i);
			user.setPassword(new Str("TestPassword" + i));
			collection.put(user);
		}
		Str msg = new Str("Added objects: ");
		msg.sb().append(collection.getObjectIds().size());
		config.debug(collection,msg);
		
		sleep(2000);
		System.out.println();
		config.debug(collection,new Str("Saving index ..."));
		collection.saveIndex(false);
		config.debug(collection,new Str("Saved index"));
		
		sleep(2000);
		System.out.println();
		collection.saveAllBlocks(false);
		collection.clear();
		assertEqual(collection.getObjectIds().size(),0,"Collection ids size does not match expectation");
		assertEqual(collection.size(),0,"Collection size does not match expectation");
		assertEqual(collection.getAddedClassNames().size(),0,"Collection added class names size does not match expectation");
		
		sleep(2000);
		System.out.println();
		collection = new DCTest(config);
		collection.triggerLoadIndex().waitTillDone(10000);
		assertEqual(collection.getObjectIds().size(),10000,"Collection ids size does not match expectation");
		assertEqual(collection.size(),0,"Collection size does not match expectation");
		assertEqual(collection.getAddedClassNames().size(),1,"Collection added class names size does not match expectation");
		
		sleep(2000);
		System.out.println();
		config.debug(collection,new Str("Loading objects ..."));
		Waiter.waitTillDone(collection.triggerLoadAllBlocks(), 10000);
		config.debug(collection,new Str("Loaded objects: " + collection.size()));
		
		sleep(2000);
		System.out.println();
		collection = new DCTest(config);
		collection.triggerLoadIndex().waitTillDone(10000);
		
		Object object = collection.get(new Str(DCTestUser.class.getName() + "@1000"));
		config.debug(collection,new Str("Loaded object: " + object));
		config.debug(collection,new Str("Loaded objects: " + collection.size()));

		
		System.out.println();
		config.debug(collection,new Str("Loading objects ..."));
		SortedMap<Str,Object> results = collection.query(Query.create(DCTestUser.class)).results;
		config.debug(collection,new Str("Loaded objects: " + collection.size()));
		assertEqual(results.size(),10000,"Results size does not match expectation");
		
		System.out.println();
		config.rmDirs();
	}
}
