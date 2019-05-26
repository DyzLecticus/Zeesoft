package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.idx.IndexConfig;

public class Database {
	private static final String				INDEX_DIR		= "ZODB/Index/";
	private static final String				OBJECT_DIR		= "ZODB/Objects/";
	
	private Config							configuration	= null;
	private IndexConfig						indexConfig		= null;
	private Index							index			= null;
	private IndexFileWriteWorker			fileWriter		= null;
	private IndexObjectWriterWorker			objectWriter	= null;
	
	private List<StateListener>				listeners		= new ArrayList<StateListener>();
	
	public Database(Config config) {
		configuration = config;
		indexConfig = new IndexConfig(config.getMessenger());
		index = new Index(config.getMessenger(),this,indexConfig);
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
	
	public IndexConfig getIndexConfig() {
		return indexConfig;
	}
	
	public void install() {
		File dir = new File(getFullIndexDir());
		dir.mkdirs();
		dir = new File(getFullObjectDir());
		dir.mkdirs();
	}

	public void start() {
		configuration.debug(this,"Starting database ...");
		readConfig();
		StringBuilder newKey = configuration.getZODB().getNewKey();
		if (newKey!=null && newKey.length()>0) {
			configuration.debug(this,"Changing database key ...");
		}
		if (indexConfig.isRebuild()) {
			configuration.debug(this,"Rebuilding indexes ...");
		}
		IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index,newKey);
		reader.start();
		fileWriter.start();
		objectWriter.start();
		configuration.debug(this,"Started database");
	}

	public void stop() {
		if (index.isOpen()) {
			index.setOpen(false);
			configuration.debug(this,"Stopping database ...");
			fileWriter.stop();
			objectWriter.stop();
			if (indexConfig.isRebuild()) {
				writeConfig();
			}
			configuration.debug(this,"Stopped database");
		}
	}
	
	public void destroy() {
		fileWriter.destroy();
		objectWriter.destroy();
		index.destroy();
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
	
	public List<IndexElement> listObjectsUseIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,String value,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjectsUseIndex(start,max,ascending,indexName,invert,operator,value,modAfter,modBefore,data);
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
	
	public List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,String value,long modAfter,long modBefore) {
		return index.getObjectsUseIndex(ascending,indexName,invert,operator,value,modAfter,modBefore);
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
	
	public static String removeControlCharacters(String name) {
		name = name.replace("\r","");
		name = name.replace("\n","");
		name = name.replace("\t","");
		return name;
	}
	
	protected StringBuilder getKey() {
		return configuration.getZODBKey();
	}
	
	protected void setKey(StringBuilder key) {
		configuration.setZODBKey(key);
		ZStringBuilder err = configuration.rewriteConfig();
		if (err.length()==0) {
			configuration.debug(this,"Changed database key");
		} else {
			configuration.error(this,"An error occured while changing the database key");
		}
	}
	
	protected void stateChanged(boolean open) {
		if (open) {
			if (indexConfig.isRebuild()) {
				configuration.debug(this,"Rebuilt indexes");
				indexConfig.setRebuild(false);
				writeConfig();
			}
			configuration.debug(this,"Database is open for business");
		} else {
			configuration.debug(this,"Database is closed for business");
		}
		for (StateListener listener: listeners) {
			listener.stateChanged(this,open);
		}
	}
	
	protected void readConfig() {
		File f = new File(getFullIndexDir() + "config.json");
		if (f.exists()) {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(f.getAbsolutePath());
			if (err.length()==0) {
				indexConfig.fromJson(json);
			} else {
				configuration.error(this,"Failed to read index configuration: " + err);
			}
		}
	}
	
	protected void writeConfig() {
		JsFile json = indexConfig.toJson();
		ZStringBuilder err = json.toFile(getFullIndexDir() + "config.json",true);
		if (err.length()==0) {
			indexConfig.fromJson(json);
		} else {
			configuration.error(this,"Failed to write index configuration: " + err);
		}
	}
}
