package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	
	private List<StateListener>				listeners		= new ArrayList<StateListener>();
	
	public Database(Config config) {
		configuration = config;
		index = new Index(config.getMessenger(),this);
		fileWriter = new IndexFileWriteWorker(config.getMessenger(),config.getUnion(),index);
		objectWriter = new IndexObjectWriterWorker(config.getMessenger(),config.getUnion(),index);
	}
	
	public void addListener(StateListener listener) {
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
	
	public List<IndexElement> listObjects(int start,int max,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjects(start,max,modAfter,modBefore,data);
	}
	
	public List<IndexElement> listObjectsThatStartWith(String startWith,int start,int max,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjectsThatStartWith(startWith,start,max,modAfter,modBefore,data);
	}

	public List<IndexElement> listObjectsThatContain(String contains,int start,int max,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjectsThatContain(contains,start,max,modAfter,modBefore,data);
	}
	
	public List<IndexElement> getObjectsByNameStartsWith(String start,long modAfter,long modBefore) {
		return index.getObjectsByNameStartsWith(start,modAfter,modBefore);
	}
	
	public List<IndexElement> getObjectsByNameContains(String contains,long modAfter,long modBefore) {
		return index.getObjectsByNameContains(contains,modAfter,modBefore);
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
	
	public List<IndexElement> removeObjectsThatStartWith(String startsWith,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		return index.removeObjectsThatStartWith(startsWith,modAfter,modBefore,errors);
	}
	
	public List<IndexElement> removeObjectsThatContain(String contains,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		return index.removeObjectsThatContain(contains,modAfter,modBefore,errors);
	}

	protected void stateChanged(boolean open) {
		if (open) {
			configuration.debug(this,"Database is open for business");
		} else {
			configuration.debug(this,"Database is closed for business");
		}
		for (StateListener listener: listeners) {
			listener.stateChanged(this,open);
		}
	}
}
