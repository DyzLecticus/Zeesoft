package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.List;

import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class Database {
	public static final String		INDEX_DIR		= "ZODB/Index/";
	public static final String		OBJECT_DIR		= "ZODB/Objects/";
	
	private Config					configuration	= null;
	private Index					index			= null;
	private IndexFileWriteWorker	indexWriter		= null;
	
	public Database(Config config) {
		configuration = config;
		index = new Index(config.getMessenger(),config.getFullDataDir() + INDEX_DIR);
		indexWriter = new IndexFileWriteWorker(config.getMessenger(),config.getUnion(),index);
	}
	
	public void install() {
		File dir = new File(configuration.getFullDataDir() + INDEX_DIR);
		dir.mkdirs();
		dir = new File(configuration.getFullDataDir() + OBJECT_DIR);
		dir.mkdirs();
	}

	public void open() {
		IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index);
		reader.start();
		indexWriter.start();
	}

	public void close() {
		index.setOpen(false);
		indexWriter.stop();
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
}
