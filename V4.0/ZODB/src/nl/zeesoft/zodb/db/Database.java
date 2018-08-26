package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class Database {
	public static final String				INDEX_DIR		= "ZODB/Index/";
	public static final String				OBJECT_DIR		= "ZODB/Objects/";
	
	private Config							configuration	= null;
	private Index							index			= null;
	private IndexFileWriteWorker			indexWriter		= null;
	
	private List<DatabaseStateListener>		listeners		= new ArrayList<DatabaseStateListener>();
	
	public Database(Config config) {
		configuration = config;
		index = new Index(config.getMessenger(),this,config.getFullDataDir() + INDEX_DIR);
		indexWriter = new IndexFileWriteWorker(config.getMessenger(),config.getUnion(),index);
	}
	
	public void addListener(DatabaseStateListener listener) {
		listeners.add(listener);
	}
	
	public void install() {
		File dir = new File(configuration.getFullDataDir() + INDEX_DIR);
		dir.mkdirs();
		dir = new File(configuration.getFullDataDir() + OBJECT_DIR);
		dir.mkdirs();
	}

	public void start() {
		configuration.debug(this,"Starting database ...");
		IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index);
		reader.start();
		indexWriter.start();
		configuration.debug(this,"Started database");
	}

	public void stop() {
		index.setOpen(false);
		configuration.debug(this,"Stopping database ...");
		indexWriter.stop();
		for (DatabaseStateListener listener: listeners) {
			listener.databaseStateChanged(false);
		}
		configuration.debug(this,"Stopped database");
	}
	
	public boolean isOpen() {
		return index.isOpen();
	}
	
	public IndexElement addObject(String name,JsFile obj) {
		return index.addObject(name, obj);
	}
	
	public IndexElement getObjectById(long id) {
		return index.getObjectById(id);
	}
	
	public IndexElement getObjectByName(String name) {
		return index.getObjectByName(name);
	}

	public List<IndexElement> getObjectsByNameStartsWith(String start) {
		return index.getObjectsByNameStartsWith(start);
	}
	
	public List<IndexElement> getObjectsByNameMatches(String match) {
		return index.getObjectsByNameMatches(match);
	}
	
	public void setObject(long id, JsFile obj) {
		index.setObject(id, obj);
	}

	public void setObjectName(long id, String name) {
		index.setObjectName(id, name);
	}

	public IndexElement removeObject(long id) {
		return index.removeObject(id);
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
