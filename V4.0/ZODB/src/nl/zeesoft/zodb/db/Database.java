package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.idx.IndexConfig;

public class Database extends Locker {
	private static final String				STAT_CLOSED		= "CLOSED";
	private static final String				STAT_STARTING	= "STARTING";
	private static final String				STAT_OPEN		= "OPEN";
	private static final String				STAT_STOPPING	= "STOPPING";
	
	private static final String				INDEX_DIR		= "ZODB/Index/";
	private static final String				OBJECT_DIR		= "ZODB/Objects/";
	
	private Config							configuration	= null;
	private IndexConfig						indexConfig		= null;
	private Index							index			= null;
	private IndexFileWriterWorker			fileWriter		= null;
	private IndexObjectWriterWorker			objectWriter	= null;
	
	private List<StateListener>				listeners		= new ArrayList<StateListener>();
	
	private String							state			= STAT_CLOSED;
	
	public Database(Config config,int idxBlkSize,int datBlkSize) {
		super(config.getMessenger());
		configuration = config;
		indexConfig = new IndexConfig(config.getMessenger());
		index = new Index(config,this,idxBlkSize,datBlkSize,indexConfig);
		fileWriter = new IndexFileWriterWorker(config.getMessenger(),config.getUnion(),index);
		objectWriter = new IndexObjectWriterWorker(config.getMessenger(),config.getUnion(),index);
	}
	
	public void addListener(StateListener listener) {
		listeners.add(listener);
	}
	
	public IndexConfig getIndexConfig() {
		return indexConfig;
	}
	
	public void install() {
		File dir = new File(getFullIndexDir());
		dir.mkdirs();
		dir = new File(getFullObjectDir());
		dir.mkdirs();
		indexConfig.setRebuild(false);
		writeConfig();
	}

	public void start() {
		lockMe(this);
		boolean start = state.equals(STAT_CLOSED);
		if (start) {
			state = STAT_STARTING;
		}
		unlockMe(this);
		if (start) {
			configuration.debug(this,"Starting database ...");
			readConfig();
			indexConfig.initialize();
			StringBuilder newKey = configuration.getZODB().getNewKey();
			if (newKey!=null && newKey.length()>0) {
				configuration.debug(this,"Changing database key ...");
			}
			if (indexConfig.isRebuild()) {
				configuration.debug(this,"Rebuilding indexes ...");
			}
			IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index,newKey);
			reader.start();
			index.getObjectReader().start();
			configuration.debug(this,"Started database");
		}
	}

	public void stop() {
		lockMe(this);
		boolean stop = state.equals(STAT_OPEN);
		if (stop) {
			state = STAT_STOPPING;
		}
		unlockMe(this);
		if (stop) {
			index.setOpen(false);
			configuration.debug(this,"Stopping database ...");
			index.getObjectReader().stop();
			fileWriter.stop();
			objectWriter.stop();
			if (indexConfig.isRebuild()) {
				writeConfig();
			}
			index.clear();
			configuration.debug(this,"Stopped database");
			lockMe(this);
			state = STAT_CLOSED;
			unlockMe(this);
		}
	}
	
	public void destroy() {
		fileWriter.destroy();
		objectWriter.destroy();
		index.destroy();
		indexConfig.destroy();
	}
	
	public boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STAT_OPEN);
		unlockMe(this);
		return r;
	}
	
	public IndexElement addObject(ZStringBuilder name,JsFile obj,List<ZStringBuilder> errors) {
		return index.addObject(name, obj, errors);
	}
	
	public IndexElement getObjectById(long id) {
		return index.getObjectById(id);
	}
	
	public IndexElement getObjectByName(ZStringBuilder name) {
		return index.getObjectByName(name);
	}
	
	public List<IndexElement> listObjectsUseIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjectsUseIndex(start,max,ascending,indexName,invert,operator,value,modAfter,modBefore,data);
	}
	
	public List<IndexElement> listObjects(int start,int max,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjects(start,max,modAfter,modBefore,data);
	}
	
	public List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore) {
		return index.getObjectsUseIndex(ascending,indexName,invert,operator,value,modAfter,modBefore);
	}
	
	public void setObject(long id, JsFile obj,List<ZStringBuilder> errors) {
		index.setObject(id,obj,errors);
	}

	public void setObjectName(long id, ZStringBuilder name,List<ZStringBuilder> errors) {
		index.setObjectName(id,name,errors);
	}

	public IndexElement removeObject(long id,List<ZStringBuilder> errors) {
		return index.removeObject(id,errors);
	}
	
	public List<IndexElement> removeObjectsUseIndex(String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		return index.removeObjectsUseIndex(indexName,invert,operator,value,modAfter,modBefore,errors);
	}
	
	public static String removeControlCharacters(String str) {
		str = str.replace("\r","");
		str = str.replace("\n","");
		str = str.replace("\t","");
		return str;
	}
	
	public static void removeControlCharacters(ZStringBuilder sb) {
		sb.replace("\r","");
		sb.replace("\n","");
		sb.replace("\t","");
	}
	
	protected String getFullIndexDir() {
		return configuration.getFullDataDir() + INDEX_DIR;
	}
	
	protected String getFullObjectDir() {
		return configuration.getFullDataDir() + OBJECT_DIR;
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
			fileWriter.start();
			objectWriter.start();
			if (indexConfig.isRebuild()) {
				configuration.debug(this,"Rebuilt indexes");
				indexConfig.setRebuild(false);
				writeConfig();
			}
			configuration.debug(this,"Database is open for business");
		} else {
			configuration.debug(this,"Database is closed for business");
		}
		lockMe(this);
		if (open) {
			state = STAT_OPEN;
		}
		unlockMe(this);
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
		JsFile json = indexConfig.toUpdateJson();
		ZStringBuilder err = json.toFile(getFullIndexDir() + "config.json",true);
		if (err.length()>0) {
			configuration.error(this,"Failed to write index configuration: " + err);
		}
	}
}
