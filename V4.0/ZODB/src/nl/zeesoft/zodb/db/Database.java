package nl.zeesoft.zodb.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class Database extends Locker {
	public static final String				STAT_OPEN		= "OPEN";
	
	private static final String				STAT_STOPPING	= "STOPPING";
	private static final String				STAT_CLOSED		= "CLOSED";
	private static final String				STAT_STARTING	= "STARTING";
	
	private static final String				INDEX_DIR		= "ZODB/Index/";
	private static final String				OBJECT_DIR		= "ZODB/Objects/";
	
	private DatabaseConfig					configuration	= null;
	
	private Index							index			= null;
	private IndexFileWriterWorker			fileWriter		= null;
	private IndexObjectWriterWorker			objectWriter	= null;
	
	private List<DatabaseStateListener>		listeners		= new ArrayList<DatabaseStateListener>();
	
	private String							state			= STAT_CLOSED;
	
	public Database(Messenger msgr, WorkerUnion uni) {
		super(msgr);
		configuration = new DatabaseConfig(msgr,uni);
	}
	
	public void addListener(DatabaseStateListener listener) {
		listeners.add(listener);
	}
	
	public DatabaseConfig getConfiguration() {
		return configuration;
	}
	
	public void install() {
		File dir = new File(getFullIndexDir());
		dir.mkdirs();
		dir = new File(getFullObjectDir());
		dir.mkdirs();
		configuration.indexConfig.setRebuild(false);
		writeConfig();
	}

	public void initialize() {
		index = new Index(configuration.getMessenger(),configuration.getUnion(),this,configuration.indexConfig,configuration.indexBlockSize,configuration.dataBlockSize);
		fileWriter = new IndexFileWriterWorker(configuration.getMessenger(),configuration.getUnion(),index);
		objectWriter = new IndexObjectWriterWorker(configuration.getMessenger(),configuration.getUnion(),index);
	}

	public boolean start() {
		lockMe(this);
		boolean start = state.equals(STAT_CLOSED);
		if (start) {
			state = STAT_STARTING;
		}
		unlockMe(this);
		if (start) {
			configuration.debug(this,"Starting database ...");
			readConfig();
			configuration.indexConfig.initialize();
			StringBuilder newKey = configuration.newKey;
			if (newKey!=null && newKey.length()>0) {
				configuration.debug(this,"Changing database key ...");
			}
			if (configuration.indexConfig.isRebuild()) {
				configuration.debug(this,"Rebuilding indexes ...");
			}
			IndexFileReader reader = new IndexFileReader(configuration.getMessenger(),configuration.getUnion(),index,newKey);
			reader.start();
			index.getObjectReader().start();
			configuration.debug(this,"Started database");
		}
		return start;
	}

	public boolean stop() {
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
			if (configuration.indexConfig.isRebuild()) {
				writeConfig();
			}
			index.clear();
			configuration.debug(this,"Stopped database");
			lockMe(this);
			state = STAT_CLOSED;
			unlockMe(this);
		}
		return stop;
	}
	
	public void destroy() {
		fileWriter.destroy();
		objectWriter.destroy();
		index.destroy();
		configuration.indexConfig.destroy();
	}
	
	public boolean isOpen() {
		boolean r = false;
		lockMe(this);
		r = state.equals(STAT_OPEN);
		unlockMe(this);
		return r;
	}
	
	public String getState() {
		String r = "";
		lockMe(this);
		r = state;
		unlockMe(this);
		return r;
	}
	
	public IndexElement addObject(ZStringBuilder name,JsFile obj,List<ZStringBuilder> errors) {
		return index.addObject(name, obj, errors);
	}
	
	public IndexElement getObjectById(long id) {
		return index.getObjectById(id,0,null);
	}
	
	public IndexElement getObjectById(long id,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		return index.getObjectById(id,readTimeOutSeconds,errors);
	}
	
	public IndexElement getObjectByName(ZStringBuilder name) {
		return index.getObjectByName(name,0,null);
	}
	
	public IndexElement getObjectByName(ZStringBuilder name,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		return index.getObjectByName(name,readTimeOutSeconds,errors);
	}
	
	public List<IndexElement> listObjectsUseIndex(int start, int max,boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjectsUseIndex(start,max,ascending,indexName,invert,operator,value,modAfter,modBefore,data);
	}
	
	public List<IndexElement> listObjects(int start,int max,long modAfter,long modBefore,List<Integer> data) {
		return index.listObjects(start,max,modAfter,modBefore,data);
	}
	
	public List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore) {
		return index.getObjectsUseIndex(ascending,indexName,invert,operator,value,modAfter,modBefore,0,null);
	}
	
	public List<IndexElement> getObjectsUseIndex(boolean ascending,String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		return index.getObjectsUseIndex(ascending,indexName,invert,operator,value,modAfter,modBefore,readTimeOutSeconds,errors);
	}
	
	public void setObject(long id, JsFile obj,List<ZStringBuilder> errors) {
		index.setObject(id,obj,errors);
	}

	public void setObjectName(long id, ZStringBuilder name) {
		index.setObjectName(id,name,0,null);
	}

	public void setObjectName(long id, ZStringBuilder name,int readTimeOutSeconds,List<ZStringBuilder> errors) {
		index.setObjectName(id,name,readTimeOutSeconds,errors);
	}

	public IndexElement removeObject(long id,List<ZStringBuilder> errors) {
		return index.removeObject(id,errors);
	}
	
	public List<IndexElement> removeObjectsUseIndex(String indexName,boolean invert,String operator,ZStringBuilder value,long modAfter,long modBefore,List<ZStringBuilder> errors) {
		return index.removeObjectsUseIndex(indexName,invert,operator,value,modAfter,modBefore,errors);
	}
	
	public static String removeSpecialCharacters(String str) {
		str = str.replace("\"","");
		str = str.replace("{","");
		str = str.replace("}","");
		str = str.replace("'","");
		str = removeControlCharacters(str);
		return str;
	}
	
	public static String removeControlCharacters(String str) {
		str = str.replace("\r","");
		str = str.replace("\n","");
		str = str.replace("\t","");
		return str;
	}

	public static void removeSpecialCharacters(ZStringBuilder sb) {
		sb.replace("\"","");
		sb.replace("{","");
		sb.replace("}","");
		sb.replace("'","");
		removeControlCharacters(sb);
	}

	public static void removeControlCharacters(ZStringBuilder sb) {
		sb.replace("\r","");
		sb.replace("\n","");
		sb.replace("\t","");
	}
	
	protected String getFullIndexDir() {
		return configuration.dataDir + INDEX_DIR;
	}
	
	protected String getFullObjectDir() {
		return configuration.dataDir + OBJECT_DIR;
	}
	
	protected StringBuilder getKey() {
		return configuration.key;
	}
	
	protected void setKey(StringBuilder key) {
		for (DatabaseStateListener listener: listeners) {
			listener.keyChanged(key);
		}
	}
	
	protected void stateChanged(boolean open) {
		if (open) {
			fileWriter.start();
			objectWriter.start();
			if (configuration.indexConfig.isRebuild()) {
				configuration.debug(this,"Rebuilt indexes");
				configuration.indexConfig.setRebuild(false);
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
		for (DatabaseStateListener listener: listeners) {
			listener.stateChanged(this,open);
		}
	}
	
	protected void readConfig() {
		File f = new File(getFullIndexDir() + "config.json");
		if (f.exists()) {
			JsFile json = new JsFile();
			ZStringBuilder err = json.fromFile(f.getAbsolutePath());
			if (err.length()==0) {
				configuration.indexConfig.fromJson(json);
			} else {
				configuration.error(this,"Failed to read index configuration: " + err);
			}
		}
	}
	
	protected void writeConfig() {
		JsFile json = configuration.indexConfig.toUpdateJson();
		ZStringBuilder err = json.toFile(getFullIndexDir() + "config.json",true);
		if (err.length()>0) {
			configuration.error(this,"Failed to write index configuration: " + err);
		}
	}
}
