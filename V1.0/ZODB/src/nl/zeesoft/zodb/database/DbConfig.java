package nl.zeesoft.zodb.database;

import java.io.File;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.ZODBModel;

public final class DbConfig {
	public final static	int			MAX_DB_LOAD					= 9999;
	
	public final static	String		PROPERTY_PORT				= "port";
	public final static	String		PROPERTY_MAX_SESSIONS		= "maxSessions";
	public final static	String		PROPERTY_DEBUG				= "debug";
	public final static	String		PROPERTY_DEBUG_CLASS_PREFIX	= "debugClassPrefix";
	public final static	String		PROPERTY_START_BATCH		= "startBatch";
	public final static	String		PROPERTY_REMOTE_BACKUP_DIR	= "remoteBackupDir";
	public final static	String		PROPERTY_ENCRYPT			= "encrypt";
	public final static	String		PROPERTY_XML_COMPRESSION	= "xmlCompression";
	public final static	String		PROPERTY_CACHE				= "cache";
	public final static	String		PROPERTY_CACHE_SIZE			= "cacheSize";
	public final static	String		PROPERTY_CACHE_PERSIST_SIZE	= "cachePersistSize";
	public final static	String		PROPERTY_CACHE_THRESHOLD	= "cacheThreshold";
	public final static	String		PROPERTY_MAX_FETCH_LOAD		= "maxFetchLoad";
	public final static	String		PROPERTY_MAX_FETCH_RESULTS	= "maxFetchResults";
	public final static	String		PROPERTY_PRELOAD_SPEED		= "preloadSpeed";

	public final static String[]	PROPERTIES					= {
		PROPERTY_PORT,
		PROPERTY_MAX_SESSIONS,
		PROPERTY_DEBUG,
		PROPERTY_DEBUG_CLASS_PREFIX,
		PROPERTY_START_BATCH,
		PROPERTY_REMOTE_BACKUP_DIR,
		PROPERTY_ENCRYPT,
		PROPERTY_XML_COMPRESSION,
		PROPERTY_CACHE,
		PROPERTY_CACHE_SIZE,
		PROPERTY_CACHE_PERSIST_SIZE,
		PROPERTY_CACHE_THRESHOLD,
		PROPERTY_MAX_FETCH_LOAD,
		PROPERTY_MAX_FETCH_RESULTS,
		PROPERTY_PRELOAD_SPEED
		};
	
	public final static	String		LABEL_PORT					= "Port";
	public final static	String		LABEL_MAX_SESSIONS			= "Maximum # sessions";
	public final static	String		LABEL_DEBUG					= "Debug";
	public final static	String		LABEL_DEBUG_CLASS_PREFIX	= "Debug class prefix";
	public final static	String		LABEL_START_BATCH			= "Start batch";
	public final static	String		LABEL_REMOTE_BACKUP_DIR		= "Remote backup directory";
	public final static	String		LABEL_ENCRYPT				= "Encrypt";
	public final static	String		LABEL_XML_COMPRESSION		= "XML Compression";
	public final static	String		LABEL_CACHE					= "Cache";
	public final static	String		LABEL_CACHE_SIZE			= "Cache size";
	public final static	String		LABEL_CACHE_PERSIST_SIZE	= "Cache persist size";
	public final static	String		LABEL_CACHE_THRESHOLD		= "Cache threshold";
	public final static	String		LABEL_MAX_FETCH_LOAD		= "Maximum fetch load";
	public final static	String		LABEL_MAX_FETCH_RESULTS		= "Maximum fetch results";
	public final static	String		LABEL_PRELOAD_SPEED			= "Preload speed";

	public final static String[]	PROPERTY_LABELS				= {
		LABEL_PORT,
		LABEL_MAX_SESSIONS,
		LABEL_DEBUG,
		LABEL_DEBUG_CLASS_PREFIX,
		LABEL_START_BATCH,
		LABEL_REMOTE_BACKUP_DIR,
		LABEL_ENCRYPT,
		LABEL_XML_COMPRESSION,
		LABEL_CACHE,
		LABEL_CACHE_SIZE,
		LABEL_CACHE_PERSIST_SIZE,
		LABEL_CACHE_THRESHOLD,
		LABEL_MAX_FETCH_LOAD,
		LABEL_MAX_FETCH_RESULTS,
		LABEL_PRELOAD_SPEED
		};

	public final static	String		HELP_PORT					= "The port the database server will listen on (256-99999)";
	public final static	String		HELP_MAX_SESSIONS			= "The maximum number of concurrent sessions (1-999)";
	public final static	String		HELP_DEBUG					= "Indicates debugging output";
	public final static	String		HELP_DEBUG_CLASS_PREFIX		= "Class name prefix used to filter the debugging output";
	public final static	String		HELP_START_BATCH			= "Indicates the batch will start when the server starts";
	public final static	String		HELP_REMOTE_BACKUP_DIR		= "Optional full remote backup directory";
	public final static	String		HELP_ENCRYPT				= "Indicates communication is encrypted";
	public final static	String		HELP_XML_COMPRESSION		= "Indicates the type of XML compression used";
	public final static	String		HELP_CACHE					= "Indicates the cache is active";
	public final static	String		HELP_CACHE_SIZE				= "The maximum number of cached fetch requests (100-9999)";
	public final static	String		HELP_CACHE_PERSIST_SIZE		= "The maximum number of persisted cache fetch requests (10-999)";
	public final static	String		HELP_CACHE_THRESHOLD		= "The minimum number of millseconds a fetch request must take to be stored in the cache (1/999)";
	public final static	String		HELP_MAX_FETCH_LOAD			= "The maximum fetch load (1000-" + MAX_DB_LOAD + ")";
	public final static	String		HELP_MAX_FETCH_RESULTS		= "The maximum fetch results (1000-" + MAX_DB_LOAD + ")";
	public final static	String		HELP_PRELOAD_SPEED			= "The preload speed (1-100%)";
	
	public final static String[]	PROPERTY_HELP				= {
		HELP_PORT,
		HELP_MAX_SESSIONS,
		HELP_DEBUG,
		HELP_DEBUG_CLASS_PREFIX,
		HELP_START_BATCH,
		HELP_REMOTE_BACKUP_DIR,
		HELP_ENCRYPT,
		HELP_XML_COMPRESSION,
		HELP_CACHE,
		HELP_CACHE_SIZE,
		HELP_CACHE_PERSIST_SIZE,
		HELP_CACHE_THRESHOLD,
		HELP_MAX_FETCH_LOAD,
		HELP_MAX_FETCH_RESULTS,
		HELP_PRELOAD_SPEED
		};

	public final static	String		XML_COMPRESSION_NONE		= "None";
	public final static	String		XML_COMPRESSION_TAGS		= "Tags";
	public final static	String		XML_COMPRESSION_FULL		= "Full";
	
	private static DbConfig			config						= null;

	private	ZODBModel				model						= null;
	
	private String 					installDir 					= "";
	private String 					dataDir 					= "data/";
	private String 					modelClassName 				= ZODBModel.class.getName();
	private String 					encryptionKey 				= "";
	private int 					port 						= 4321;
	private int 					maxSessions 				= 10;
	private boolean 				debug						= false;
	private String 					debugClassPrefix			= "nl.zeesoft";
	private boolean 				startBatches				= true;
	private String 					remoteBackupDir				= "";
	private boolean 				encrypt						= true;
	private String					xmlCompression				= XML_COMPRESSION_FULL;
	private boolean 				cache						= true;
	private int 					cacheSize					= 1000;
	private int 					cachePersistSize			= 100;
	private int 					cacheThreshold				= 3;
	private int 					maxFetchLoad				= 2000;
	private int 					maxFetchResults				= 2000;
	private int 					preloadSpeed				= 80;
	private String					runningDir					= Generic.dirName(new File("").getAbsolutePath());
	
	private DbConfig() {
		// Singleton
	}

	public static DbConfig getInstance() {
		if (config==null) {
			config = new DbConfig();
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public ZODBModel getModel() {
		if (model==null) {
			model = (ZODBModel) Generic.instanceForName(modelClassName);
			model.buildModel();
			Messenger.getInstance().debug(this, "Model class: " + modelClassName + ", CRC: " + model.getCrc());
		}
		return model;
	}

	public void serialize() {
		XMLFile f = toXml();
		f.writeFile(getFullFileName(), f.toStringReadFormat());
		f.cleanUp();
	}
	
	public void unserialize() {
		XMLFile f = new XMLFile();
		f.parseFile(getFullFileName());
		fromXml(f);
		f.cleanUp();
	}

	public String getFullDataDir() {
		return getInstallDir() + dataDir;
	}

	public String getFullIndexDir() {
		return getFullDataDir() + "index/";
	}

	public String getFullCacheDir() {
		return getFullDataDir() + "cache/";
	}

	public String getFullBackupDir() {
		return getFullDataDir() + "backup/";
	}

	public String getFullFileName() {
		return getFullDataDir() + "DbConfig.xml";
	}

	public StringBuffer encodePassword(StringBuffer passWord) {
    	return Generic.encodeKey(passWord,encryptionKey,0);
    }

	public StringBuffer decodePassword(StringBuffer passWord) {
    	return Generic.decodeKey(passWord,encryptionKey,0);
    }

	public XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("config",null,null));
		if (!runningDir.equals(getInstallDir())) {
			new XMLElem("installDir",new StringBuffer(installDir),f.getRootElement());
		}
		new XMLElem("dataDir",new StringBuffer(dataDir),f.getRootElement());
		new XMLElem("modelClassName",new StringBuffer(modelClassName),f.getRootElement());
		new XMLElem("encryptionKey",Generic.compress(new StringBuffer(encryptionKey)),f.getRootElement());

		StringBuffer sb = new StringBuffer();
		sb.append(port);
		new XMLElem(PROPERTY_PORT,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(maxSessions);
		new XMLElem(PROPERTY_MAX_SESSIONS,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(debug);
		new XMLElem(PROPERTY_DEBUG,sb,f.getRootElement());
		new XMLElem(PROPERTY_DEBUG_CLASS_PREFIX,new StringBuffer(debugClassPrefix),f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(startBatches);
		new XMLElem(PROPERTY_START_BATCH,sb,f.getRootElement());

		sb = new StringBuffer();
		sb.append(remoteBackupDir);
		new XMLElem(PROPERTY_REMOTE_BACKUP_DIR,sb,f.getRootElement());

		sb = new StringBuffer();
		sb.append(encrypt);
		new XMLElem(PROPERTY_ENCRYPT,sb,f.getRootElement());
		
		new XMLElem(PROPERTY_XML_COMPRESSION,new StringBuffer(xmlCompression),f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(cache);
		new XMLElem(PROPERTY_CACHE,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(cacheSize);
		new XMLElem(PROPERTY_CACHE_SIZE,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(cachePersistSize);
		new XMLElem(PROPERTY_CACHE_PERSIST_SIZE,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(cacheThreshold);
		new XMLElem(PROPERTY_CACHE_THRESHOLD,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(maxFetchLoad);
		new XMLElem(PROPERTY_MAX_FETCH_LOAD,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(maxFetchResults);
		new XMLElem(PROPERTY_MAX_FETCH_RESULTS,sb,f.getRootElement());
		
		sb = new StringBuffer();
		sb.append(preloadSpeed);
		new XMLElem(PROPERTY_PRELOAD_SPEED,sb,f.getRootElement());
		
		return f;
	}

	private void fromXml(XMLFile f) {
		if ((f.getRootElement()!=null) && (f.getRootElement().getName().equals("config"))) {
			for (XMLElem v: f.getRootElement().getChildren()) {
				if (v.getName().equals("installDir")) {
					setInstallDir(v.getValue().toString());
				}
				if (v.getName().equals("dataDir")) {
					setDataDir(v.getValue().toString());
				}
				if (v.getName().equals("modelClassName")) {
					modelClassName = v.getValue().toString();
				}
				if (v.getName().equals("encryptionKey")) {
					encryptionKey = Generic.decompress(new StringBuffer(v.getValue())).toString();
				}
				if (v.getName().equals(PROPERTY_PORT)) {
					setPort(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_MAX_SESSIONS)) {
					setMaxSessions(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_DEBUG)) {
					debug = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_DEBUG_CLASS_PREFIX)) {
					debugClassPrefix = v.getValue().toString();
				}
				if (v.getName().equals(PROPERTY_START_BATCH)) {
					startBatches = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_REMOTE_BACKUP_DIR)) {
					setRemoteBackupDir(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_ENCRYPT)) {
					encrypt = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_XML_COMPRESSION)) {
					setXmlCompression(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_CACHE)) {
					cache = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals(PROPERTY_CACHE_SIZE)) {
					setCacheSize(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_CACHE_PERSIST_SIZE)) {
					setCachePersistSize(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_CACHE_THRESHOLD)) {
					setCacheThreshold(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_MAX_FETCH_LOAD)) {
					setMaxFetchLoad(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_MAX_FETCH_RESULTS)) {
					setMaxFetchResults(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals(PROPERTY_PRELOAD_SPEED)) {
					setPreloadSpeed(Integer.parseInt(v.getValue().toString()));
				}
			}
		}
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		if (port<256) {
			port = 256;
		} else if (port>99999) {
			port = 99999;
		}
		this.port = port;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * @return the startBatches
	 */
	public boolean isStartBatches() {
		return startBatches;
	}
	
	/**
	 * @param startBatches the startBatches to set
	 */
	public void setStartBatches(boolean startBatches) {
		this.startBatches = startBatches;
	}
	
	/**
	 * @return the remoteBackupDir
	 */
	public String getRemoteBackupDir() {
		return remoteBackupDir;
	}

	/**
	 * @param remoteBackupDir the remoteBackupDir to set
	 */
	public void setRemoteBackupDir(String remoteBackupDir) {
		this.remoteBackupDir = Generic.dirName(remoteBackupDir);
	}
	
	/**
	 * @return the debugClassPrefix
	 */
	public String getDebugClassPrefix() {
		return debugClassPrefix;
	}
	
	/**
	 * @param debugClassPrefix the debugClassPrefix to set
	 */
	public void setDebugClassPrefix(String debugClassPrefix) {
		this.debugClassPrefix = debugClassPrefix;
	}
	
	/**
	 * @return the dataDir
	 */
	public String getDataDir() {
		return dataDir;
	}
	/**
	 * @param dataDir the dataDir to set
	 */
	public void setDataDir(String dataDir) {
		this.dataDir = Generic.dirName(dataDir);
	}
	
	/**
	 * @return the modelClassName
	 */
	public String getModelClassName() {
		return modelClassName;
	}
	
	/**
	 * @param modelClassName the modelClassName to set
	 */
	public void setModelClassName(String modelClassName) {
		this.modelClassName = modelClassName;
	}
	
	/**
	 * @return the installDir
	 */
	public String getInstallDir() {
		if (installDir.length()==0) {
			installDir = Generic.dirName(new File("").getAbsolutePath());
		}
		return installDir;
	}
	
	/**
	 * @param installDir the installDir to set
	 */
	public void setInstallDir(String installDir) {
		this.installDir = Generic.dirName(installDir);
	}
	
	/**
	 * @return the encryptionKey
	 */
	public String getEncryptionKey() {
		return encryptionKey;
	}
	
	/**
	 * @param encryptionKey the encryptionKey to set
	 */
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	/**
	 * @return the encrypt
	 */
	public boolean isEncrypt() {
		return encrypt;
	}

	/**
	 * @param encrypt the encrypt to set
	 */
	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}

	/**
	 * @return the xmlCompression
	 */
	public String getXmlCompression() {
		return xmlCompression;
	}

	/**
	 * @param xmlCompression the xmlCompression to set
	 */
	public void setXmlCompression(String xmlCompression) {
		if (
			(!xmlCompression.equals(XML_COMPRESSION_FULL)) &&
			(!xmlCompression.equals(XML_COMPRESSION_NONE)) &&
			(!xmlCompression.equals(XML_COMPRESSION_TAGS))
			) {
			xmlCompression = XML_COMPRESSION_NONE;
		}
		this.xmlCompression = xmlCompression;
	}

	/**
	 * @return the cache
	 */
	public boolean isCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(boolean cache) {
		this.cache = cache;
	}

	/**
	 * @return the cacheSize
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * @param cacheSize the cacheSize to set
	 */
	public void setCacheSize(int cacheSize) {
		if (cacheSize<100) {
			cacheSize = 100;
		} else if (cacheSize>9999) {
			cacheSize = 9999;
		}
		this.cacheSize = cacheSize;
	}

	/**
	 * @return the cachePersistSize
	 */
	public int getCachePersistSize() {
		return cachePersistSize;
	}

	/**
	 * @param cachePersistSize the cachePersistSize to set
	 */
	public void setCachePersistSize(int cachePersistSize) {
		if (cachePersistSize<10) {
			cachePersistSize = 10;
		} else if (cachePersistSize>999) {
			cachePersistSize = 999;
		}
		this.cachePersistSize = cachePersistSize;
	}

	/**
	 * @return the cacheThreshold
	 */
	public int getCacheThreshold() {
		return cacheThreshold;
	}

	/**
	 * @param cacheThreshold the cacheThreshold to set
	 */
	public void setCacheThreshold(int cacheThreshold) {
		if (cacheThreshold<1) {
			cacheThreshold = 1;
		} else if (cacheThreshold>999) {
			cacheThreshold = 999;
		}
		this.cacheThreshold = cacheThreshold;
	}

	/**
	 * @return the maxSessions
	 */
	public int getMaxSessions() {
		return maxSessions;
	}

	/**
	 * @param maxSessions the maxSessions to set
	 */
	public void setMaxSessions(int maxSessions) {
		if (maxSessions<1) {
			maxSessions = 1;
		} else if (maxSessions>999) {
			maxSessions = 999;
		}
		this.maxSessions = maxSessions;
	}

	/**
	 * @return the maxFetchLoad
	 */
	public int getMaxFetchLoad() {
		return maxFetchLoad;
	}

	/**
	 * @param maxFetchLoad the maxFetchLoad to set
	 */
	public void setMaxFetchLoad(int maxFetchLoad) {
		if (maxFetchLoad<1000) {
			maxFetchLoad = 1000;
		} else if (maxFetchLoad>MAX_DB_LOAD) {
			maxFetchLoad = MAX_DB_LOAD;
		}
		this.maxFetchLoad = maxFetchLoad;
	}

	/**
	 * @return the maxFetchResults
	 */
	public int getMaxFetchResults() {
		return maxFetchResults;
	}

	/**
	 * @param maxFetchResults the maxFetchResults to set
	 */
	public void setMaxFetchResults(int maxFetchResults) {
		if (maxFetchResults<1000) {
			maxFetchResults = 1000;
		} else if (maxFetchResults>MAX_DB_LOAD) {
			maxFetchResults = MAX_DB_LOAD;
		}
		this.maxFetchResults = maxFetchResults;
	}

	/**
	 * @return the preloadSpeed
	 */
	public int getPreloadSpeed() {
		return preloadSpeed;
	}

	/**
	 * @param preloadSpeed the preloadSpeed to set
	 */
	public void setPreloadSpeed(int preloadSpeed) {
		if (preloadSpeed<1) {
			preloadSpeed = 1;
		} else if (preloadSpeed>100) {
			preloadSpeed = 100;
		}
		this.preloadSpeed = preloadSpeed;
	}
}
