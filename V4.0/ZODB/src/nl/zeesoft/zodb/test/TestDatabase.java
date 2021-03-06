package nl.zeesoft.zodb.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.IndexElement;
import nl.zeesoft.zodb.db.idx.IndexConfig;
import nl.zeesoft.zodb.mod.ModZODB;

public class TestDatabase extends TestObject {
	public TestDatabase(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabase(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *Database*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		
		Database db = initializeTestDatabase(config,null,true,10);

		if (db.isOpen()) {
			
			boolean added = false;
			IndexElement element = null;

			element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				addTestObjects(db);
				System.out.println("Adding 250 objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
				added = true;
			}
			
			element = db.getObjectById(1L);
			if (element!=null) {
				assertEqual(element.name.toString(),"testObject001","Name of object found by id does not match expectation");
				assertEqual(element.obj==null,false,"Object found by id does not match expectation");
			} else {
				assertEqual(false,true,"Object with id 1 not found");
			}
			
			element = db.getObjectByName(new ZStringBuilder("testObject002"));
			if (element!=null) {
				assertEqual(element.name.toString(),"testObject002","Name of object found by name does not match expectation");
				assertEqual(element.obj==null,false,"Object found by name does not match expectation");
			} else {
				assertEqual(false,true,"Object with name 'testObject002' not found");
			}
			
			sleep(1000);
			
			element = db.getObjectByName(new ZStringBuilder("testObject125"));
			if (element!=null) {
				assertEqual(element.obj==null,false,"testObject125 does not match expectation");
				element = db.removeObject(element.id,null);
				if (element!=null) {
					assertEqual(element.name.toString(),"testObject125","Removed object name does not match expectation");
					element = addTestObject(db,new ZStringBuilder("testObject125"),125);
					assertEqual(element!=null,true,"Failed to add object with name 'testObject125'");
				}
			} else {
				element = addTestObject(db,new ZStringBuilder("testObject125"),125);
				assertEqual(element!=null,true,"Failed to add object with name 'testObject125'");
			}
			
			Date started = new Date();
			List<IndexElement> elements = db.getObjectsUseIndex(true,IndexConfig.IDX_NAME,false,DatabaseRequest.OP_STARTS_WITH,new ZStringBuilder("testObject"),0L,0L);
			assertEqual(elements.size(),250,"Number of objects does not match expectation");
			for (IndexElement elem: elements) {
				assertEqual(elem.obj!=null,true,"Failed to read object id " + elem.id);
			}
			long ms = ((new Date()).getTime() - started.getTime());
			if (!added) {
				System.out.println("Reading 250 objects took: " + ms + " ms");
			} else {
				System.out.println("Reading 250 cached objects took: " + ms + " ms");
			}

			List<IndexElement> list = db.listObjects(100,100,0L,0L,null);
			assertEqual(list.size(),100,"List size does not match expectation (1)");

			if (list.size()==100) {
				long modAfter = list.get(0).modified - 1;
				list = db.listObjects(0,300,modAfter,0L,null);
				assertEqual(list.size() >= 50,true,"List size does not match expectation (2)");
			}
			
			list = db.listObjects(200,100,0L,0L,null);
			assertEqual(list.size(),50,"List size does not match expectation (3)");
			System.out.println("Obtained list size: " + list.size() + " (start: " + 200 + ", max: " + 100 + ")");
			if (list.size()>0) {
				System.out.println("First list object: " + list.get(0).name);
				System.out.println("Last list object: " + list.get((list.size() - 1)).name);
			}
			
			list = db.listObjectsUseIndex(20,10,false,"testObject:data",false,"",null,0L,0L,null);
			assertEqual(list.size(),10,"List size does not match expectation (4)");
			if (list.size()>0) {
				System.out.println("First index list object: " + list.get(0).name);
				System.out.println("Last index list object: " + list.get((list.size() - 1)).name);
			}

			list = db.listObjectsUseIndex(20,10,false,IndexConfig.IDX_NAME,false,"",null,0L,0L,null);
			assertEqual(list.size(),10,"List size does not match expectation (5)");
			if (list.size()>0) {
				System.out.println("First index list object: " + list.get(0).name);
				System.out.println("Last index list object: " + list.get((list.size() - 1)).name);
			}
			
			list = db.listObjectsUseIndex(0,300,true,"testObject:num",false,DatabaseRequest.OP_GREATER,new ZStringBuilder("99"),0L,0L,null);
			assertEqual(list.size(),206,"List size does not match expectation (6)");
			if (list.size()>0) {
				System.out.println("First index list object: " + list.get(0).name + ", num: " + list.get(0).idxValues.get("num"));
				System.out.println("Last index list object: " + list.get((list.size() - 1)).name + ", num: " + list.get((list.size() - 1)).idxValues.get("num"));
			}
			
			list = db.listObjectsUseIndex(0,300,true,"testObject:num",true,DatabaseRequest.OP_GREATER,new ZStringBuilder("99"),0L,0L,null);
			assertEqual(list.size(),44,"List size does not match expectation (7)");
			if (list.size()>0) {
				System.out.println("First index list object: " + list.get(0).name + ", num: " + list.get(0).idxValues.get("num"));
				System.out.println("Last index list object: " + list.get((list.size() - 1)).name + ", num: " + list.get((list.size() - 1)).idxValues.get("num"));
			}
			
			JsFile obj = new JsFile();
			obj.rootElement = new JsElem();
			obj.rootElement.children.add(new JsElem("data","testObject001",true));
			obj.rootElement.children.add(new JsElem("num","999"));
			List<ZStringBuilder> errors = new ArrayList<ZStringBuilder>();
			db.addObject(new ZStringBuilder("testObject999"), obj, errors);
			assertEqual(errors.size(),1,"Number of errors does not match expectation (1)");
			if (errors.size()==1) {
				assertEqual(errors.get(0).toString(),"Index testObject:data blocks addition of object named 'testObject999'","Add object error does not match expectation");
			}

			errors.clear();
			element = db.addObject(new ZStringBuilder("test999"),obj,errors);
			assertEqual(errors.size(),0,"Number of errors does not match expectation (2)");
			
			if (element!=null) {
				errors.clear();
				db.setObjectName(element.id,new ZStringBuilder("testObject999"),0,errors);
				assertEqual(errors.size(),1,"Number of errors does not match expectation (3)");
				if (errors.size()==1) {
					assertEqual(errors.get(0).toString(),"Index testObject:data blocks update of object named 'test999'","Rename object error does not match expectation");
				}
				
				errors.clear();
				db.removeObject(element.id, errors);
				assertEqual(errors.size(),0,"Number of errors does not match expectation (4)");
				if (errors.size()>0) {
					System.err.println(errors.get(0));
				}
			}
					
			sleep(1000);
			
			config.destroy();
		} else {
			System.exit(1);
		}
	}
	
	public static Database initializeTestDatabase(Config config,StringBuilder newKey,boolean testIndexes,int timeOut) {
		File dir = new File("dist/data/ZODB/Index");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		config.getModule(ModZODB.NAME).selfTest = false;
		config.setZODBKey(new StringBuilder("0123456789012345678901234567890123456789012345678901234567890123"));
		if (newKey!=null && newKey.length()>0) {
			config.getZODB().setNewKey(newKey);
		}
		
		Database db = config.getZODB().getDatabase();
		IndexConfig idxConf = db.getConfiguration().indexConfig;

		if (testIndexes) {
			idxConf.removeIndex(ModZODB.NAME + "/Objects/:testData");
			idxConf.addIndex("testObject","data",false,true);
			idxConf.getIndex("testObject:data").added = false;
			idxConf.addIndex("testObject","num",true,true);
			idxConf.getIndex("testObject:num").added = false;
			idxConf.setRebuild(false);
		} else {
			idxConf.getIndex(ModZODB.NAME + "/Objects/:testData").added = false;
		}

		config.initialize(true,"dist/","",false);
		
		db.install();
		
		waitForDatabaseState(db,true,timeOut);
		return db;
	}
	
	public static final void waitForDatabaseState(Database db,boolean open,int timeOut) {
		int i = 0;
		while(db.isOpen()!=open) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			if (i >= timeOut) {
				break;
			}
		}
		if (db.isOpen()!=open) {
			System.out.println("Waiting for database state timed out after " + i + " seconds");
		}
	}
	
	public static void addTestObjects(Database db) {
		int num = -30;
		for (int i = 1; i <= 250; i++) {
			ZStringBuilder name = new ZStringBuilder("testObject");
			name.append(String.format("%03d",i));
			addTestObject(db,name,num);
			num += 3;
		}
	}

	private static IndexElement addTestObject(Database db,ZStringBuilder name,int num) {
		return db.addObject(name,getTestObject(name,num),null);
	}
	
	private static JsFile getTestObject(ZStringBuilder data,int num) {
		JsFile r = new JsFile();
		r.rootElement = new JsElem();
		r.rootElement.children.add(new JsElem("data",data,true));
		if (data.toString().equals("testObject125")) {
			num = 125;
		}
		r.rootElement.children.add(new JsElem("num","" + num));
		return r;
	}
}
