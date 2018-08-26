package nl.zeesoft.zodb.db;

import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class Database {
	public static final String	INDEX_DIR		= "ZODB/Index/";
	public static final String	OBJECT_DIR		= "ZODB/Objects/";
	
	private Config				configuration	= null;
	private Index				index			= null;
	
	public Database(Config config) {
		configuration = config;
		index = new Index(config.getMessenger(),config.getDataDir() + INDEX_DIR);
	}

	public void initialize() {
		IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index);
		reader.start();
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
}
