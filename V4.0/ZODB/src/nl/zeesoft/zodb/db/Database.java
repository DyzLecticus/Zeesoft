package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class Database {
	private static final String				INDEX_DIR		= "ZODB/Index/";
	private static final String				OBJECT_DIR		= "ZODB/Objects/";
	
	private Config							configuration	= null;
	private Index							index			= null;
	private IndexFileWriteWorker			fileWriter		= null;
	private IndexObjectWriterWorker			objectWriter	= null;
	
	private List<DatabaseStateListener>		listeners		= new ArrayList<DatabaseStateListener>();
	
	public Database(Config config) {
		configuration = config;
		index = new Index(config.getMessenger(),this);
		fileWriter = new IndexFileWriteWorker(config.getMessenger(),config.getUnion(),index);
		objectWriter = new IndexObjectWriterWorker(config.getMessenger(),config.getUnion(),index);
	}
	
	public void addListener(DatabaseStateListener listener) {
		listeners.add(listener);
	}
	
	public String getFullIndexDir() {
		return configuration.getFullDataDir() + INDEX_DIR;
	}
	
	public String getFullObjectDir() {
		return configuration.getFullDataDir() + OBJECT_DIR;
	}
	
	public void install() {
		File dir = new File(getFullIndexDir());
		dir.mkdirs();
		dir = new File(getFullObjectDir());
		dir.mkdirs();
	}

	public void start() {
		configuration.debug(this,"Starting database ...");
		IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index);
		reader.start();
		fileWriter.start();
		objectWriter.start();
		configuration.debug(this,"Started database");
	}

	public void stop() {
		index.setOpen(false);
		configuration.debug(this,"Stopping database ...");
		fileWriter.stop();
		objectWriter.stop();
		for (DatabaseStateListener listener: listeners) {
			listener.databaseStateChanged(false);
		}
		configuration.debug(this,"Stopped database");
	}
	
	public boolean isOpen() {
		return index.isOpen();
	}
	
	public IndexElement addObject(String name,JsFile obj,List<ZStringBuilder> errors) {
		return index.addObject(name, obj, errors);
	}
	
	public IndexElement getObjectById(long id) {
		return index.getObjectById(id);
	}
	
	public IndexElement getObjectByName(String name) {
		return index.getObjectByName(name);
	}
	
	public SortedMap<String,Long> listObjects(int start, int max) {
		return index.listObjects(start,max);
	}
	
	protected SortedMap<String,Long> listObjectsThatStartWith(String startWith,int start, int max) {
		return index.listObjectsThatStartWith(startWith,start,max);
	}

	protected SortedMap<String,Long> listObjectsThatMatch(String contains,int start, int max) {
		return index.listObjectsThatMatch(contains,start,max);
	}
	
	public List<IndexElement> getObjectsByNameStartsWith(String start) {
		return index.getObjectsByNameStartsWith(start);
	}
	
	public List<IndexElement> getObjectsByNameContains(String contains) {
		return index.getObjectsByNameContains(contains);
	}
	
	public void setObject(long id, JsFile obj,List<ZStringBuilder> errors) {
		index.setObject(id,obj,errors);
	}

	public void setObjectName(long id, String name,List<ZStringBuilder> errors) {
		index.setObjectName(id,name,errors);
	}

	public IndexElement removeObject(long id,List<ZStringBuilder> errors) {
		return index.removeObject(id,errors);
	}

	protected void stateChanged(boolean open) {
		if (open) {
			configuration.debug(this,"Database is open for business");
		} else {
			configuration.debug(this,"Database is closed for business");
		}
		for (DatabaseStateListener listener: listeners) {
			listener.databaseStateChanged(open);
		}
	}
}
