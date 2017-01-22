package nl.zeesoft.zodb.database;

import java.awt.GraphicsEnvironment;
import java.io.File;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.cache.CcCache;
import nl.zeesoft.zodb.database.cache.CcConfig;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

/**
 * This is the ZODB default database configuration.
 * To customize, initialize the class name references to custom extensions before database installation.
 */
public final class DbConfig extends Locker {
	private static final String		WARNING					=
		"Do not modify the encryptionKey. It is used to encrypt and decrypt passwords. Do not modify the dataDir without also moving the contents.";

	private static DbConfig			config					= null;

	// Not persisted, functions as optional root directory
	private String 					installDir 				= "";
	
	private String 					dataDir 				= "data/";
	private String 					modelClassName 			= MdlModel.class.getName();
	private	MdlModel				model					= null;
	private String 					encryptionKey 			= "";
	private int						maxRequestWorkers		= 10;

	private boolean 				debug					= false;
	private String 					debugClassPrefix		= "nl.zeesoft";
	private boolean 				debugRequestLogging		= false;
	private boolean 				debugPerformance		= false;

	private boolean 				showGUI					= true;
	private boolean 				openServer				= true;

	private String 					cacheConfigClassName 	= CcConfig.class.getName();
	private CcConfig 				cacheConfig			 	= null;
	private CcCache 				cache				 	= null;
	
	private DbConfig() {
		// Singleton
	}

	public static DbConfig getInstance() {
		if (config==null) {
			config = new DbConfig();
			if (GraphicsEnvironment.isHeadless()) {
				config.setShowGUI(false);
			}
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public MdlModel getModel() {
		if (model==null) {
			Messenger.getInstance().debug(this, "Model class: " + modelClassName);
			model = (MdlModel) Generic.instanceForName(modelClassName);
			model.initialize();
			model.initializeExtensionsAndIndexProperties();
			model.check();
		}
		return model;
	}

	public MdlModel getNewModel() {
		MdlModel r = null;
		r = (MdlModel) Generic.instanceForName(modelClassName);
		r.initialize();
		r.initializeExtensionsAndIndexProperties();
		r.check();
		return r;
	}

	public CcConfig getCacheConfig() {
		if (cacheConfig==null) {
			Messenger.getInstance().debug(this, "Cache class: " + cacheConfigClassName);
			cacheConfig = (CcConfig) Generic.instanceForName(cacheConfigClassName);
			cacheConfig.initialize();
		}
		return cacheConfig;
	}

	public CcCache getCache() {
		if (cache==null) {
			cache = new CcCache();
			cache.initialize(getCacheConfig());
		}
		return cache;
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

	public String getFullFileName() {
		return getConfDir() + "DbConfig.xml";
	}

	public boolean fileExists() {
		File configFile = new File(getFullFileName());
		return configFile.exists();
	}

	public StringBuilder encodePassword(StringBuilder passWord) {
    	return Generic.encodeKey(passWord,encryptionKey,0);
    }

	public StringBuilder decodePassword(StringBuilder passWord) {
    	return Generic.decodeKey(passWord,encryptionKey,0);
    }

	private XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("configuration",null,null));
		new XMLElem("WARNING",new StringBuilder(WARNING),f.getRootElement());
		new XMLElem("encryptionKey",Generic.compress(new StringBuilder(getEncryptionKey())),f.getRootElement());
		new XMLElem("dataDir",new StringBuilder(dataDir),f.getRootElement());
		new XMLElem("modelClassName",new StringBuilder(modelClassName),f.getRootElement());

		StringBuilder sb = null;
		
		sb = new StringBuilder();
		sb.append(maxRequestWorkers);
		new XMLElem("maxRequestWorkers",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(isDebug());
		new XMLElem("debug",sb,f.getRootElement());
		sb = new StringBuilder();
		sb.append(debugRequestLogging);
		new XMLElem("debugRequestLogging",sb,f.getRootElement());
		sb = new StringBuilder();
		sb.append(debugPerformance);
		new XMLElem("debugPerformance",sb,f.getRootElement());
		new XMLElem("debugClassPrefix",new StringBuilder(debugClassPrefix),f.getRootElement());
		
		sb = new StringBuilder();
		sb.append(showGUI);
		new XMLElem("showGUI",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(openServer);
		new XMLElem("openServer",sb,f.getRootElement());
		
		new XMLElem("cacheConfigClassName",new StringBuilder(cacheConfigClassName),f.getRootElement());
		return f;
	}

	private void fromXml(XMLFile f) {
		if (f.getRootElement()!=null && f.getRootElement().getName().equals("configuration")) {
			for (XMLElem v: f.getRootElement().getChildren()) {
				if (v.getName().equals("dataDir")) {
					setDataDir(v.getValue().toString());
				}
				if (v.getName().equals("modelClassName")) {
					setModelClassName(v.getValue().toString());
				}
				if (v.getName().equals("encryptionKey")) {
					encryptionKey = Generic.decompress(new StringBuilder(v.getValue())).toString();
				}
				if (v.getName().equals("maxRequestWorkers")) {
					setMaxRequestWorkers(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals("debug")) {
					setDebug(Boolean.parseBoolean(v.getValue().toString()));
				}
				if (v.getName().equals("debugRequestLogging")) {
					debugRequestLogging = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("debugPerformance")) {
					debugPerformance = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("debugClassPrefix")) {
					debugClassPrefix = v.getValue().toString();
				}
				if (v.getName().equals("showGUI")) {
					showGUI = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("openServer")) {
					openServer = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("cacheConfigClassName")) {
					setCacheConfigClassName(v.getValue().toString());
				}
			}
		}
	}

	/**
	 * @return the maxRequestWorkers
	 */
	public int getMaxRequestWorkers() {
		return maxRequestWorkers;
	}

	/**
	 * @param maxRequestWorkers the maxRequestWorkers to set
	 */
	public void setMaxRequestWorkers(int maxRequestWorkers) {
		if (maxRequestWorkers<1) {
			maxRequestWorkers = 1;
		} else if (maxRequestWorkers>100) {
			maxRequestWorkers = 100;
		}
		this.maxRequestWorkers = maxRequestWorkers;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		boolean r = false;
		lockMe(this);
		r = debug;
		unlockMe(this);
		return r;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		lockMe(this);
		this.debug = debug;
		unlockMe(this);
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
		return installDir + dataDir;
	}

	public String getConfDir() {
		return installDir + "conf/";
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
		if (modelClassName.length()==0) {
			Messenger.getInstance().error(this,"modelClassName is mandatory");
			return;
		} 
		Object testObject = Generic.testInstanceForName(modelClassName);
		if (testObject==null) {
			Messenger.getInstance().error(this,"Unable to intsantiate class for modelClassName: " + modelClassName);
			return;
		} else if (!Generic.instanceOf(testObject.getClass(),MdlModel.class.getName())) {
			Messenger.getInstance().error(this,"modelClassName class must extend " + MdlModel.class.getName());
			return;
		}
		this.modelClassName = modelClassName;
	}

	/**
	 * @return the encryptionKey
	 */
	public String getEncryptionKey() {
		if (encryptionKey.length()<64) {
			encryptionKey = Generic.generateNewKey(64);
		}
		return encryptionKey;
	}
	
	/**
	 * @param encryptionKey the encryptionKey to set
	 */
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	/**
	 * @return the debugRequestLogging
	 */
	public boolean isDebugRequestLogging() {
		return (debugRequestLogging && isDebug());
	}

	/**
	 * @param debugRequestLogging the debugRequestLogging to set
	 */
	public void setDebugRequestLogging(boolean debugRequestLogging) {
		this.debugRequestLogging = debugRequestLogging;
	}

	/**
	 * @return the debugPerformance
	 */
	public boolean isDebugPerformance() {
		return (debugPerformance && isDebug());
	}

	/**
	 * @param debugPerformance the debugPerformance to set
	 */
	public void setDebugPerformance(boolean debugPerformance) {
		this.debugPerformance = debugPerformance;
	}

	/**
	 * @return the openServer
	 */
	public boolean isOpenServer() {
		return openServer;
	}

	/**
	 * @param openServer the openServer to set
	 */
	public void setOpenServer(boolean openServer) {
		this.openServer = openServer;
	}

	/**
	 * @return the cacheConfigClassName
	 */
	public String getCacheConfigClassName() {
		return cacheConfigClassName;
	}

	/**
	 * @param cacheConfigClassName the cacheConfigClassName to set
	 */
	public void setCacheConfigClassName(String cacheConfigClassName) {
		if (cacheConfigClassName.length()==0) {
			Messenger.getInstance().error(this,"cacheConfigClassName is mandatory");
			return;
		} 
		Object testObject = Generic.testInstanceForName(cacheConfigClassName);
		if (testObject==null) {
			Messenger.getInstance().error(this,"Unable to intsantiate class for cacheConfigClassName: " + cacheConfigClassName);
			return;
		} else if (!Generic.instanceOf(testObject.getClass(),CcConfig.class.getName())) {
			Messenger.getInstance().error(this,"cacheConfigClassName class must extend " + CcConfig.class.getName());
			return;
		}
		this.cacheConfigClassName = cacheConfigClassName;
	}

	/**
	 * @return the installDir
	 */
	public String getInstallDir() {
		return installDir;
	}

	/**
	 * @param installDir the installDir to set
	 */
	public void setInstallDir(String installDir) {
		this.installDir = Generic.dirName(installDir);
	}

	/**
	 * @return the showGUI
	 */
	public boolean isShowGUI() {
		return showGUI;
	}

	/**
	 * @param showGUI the showGUI to set
	 */
	public void setShowGUI(boolean showGUI) {
		this.showGUI = showGUI;
	}
}
