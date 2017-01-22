package nl.zeesoft.zodb.database.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

/**
 * This is the ZODB default cache configuration.
 * To customize, extend this class and refer to it in the database configuration.
 */
public class CcConfig {
	private static final int			INIT_CACHE_MAX_SIZE		= 5000;
	
	private boolean						active					= true;
	private boolean						debug					= false;
	private int							halfLifeSeconds			= 10;
	private SortedMap<String,Integer>	classMaxSizeMap			= new TreeMap<String,Integer>();
	
	/**
	 * Override or extend this method for custom default cache settings.
	 */
	protected void initializeConfig() {
		getClassMaxSizeMap().put(MdlModel.PACKAGE_CLASS_FULL_NAME,10);
		getClassMaxSizeMap().put(MdlModel.CLASS_CLASS_FULL_NAME,100);
		getClassMaxSizeMap().put(MdlModel.STRING_CLASS_FULL_NAME,100);
		getClassMaxSizeMap().put(MdlModel.NUMBER_CLASS_FULL_NAME,100);
		getClassMaxSizeMap().put(MdlModel.LINK_CLASS_FULL_NAME,100);
		getClassMaxSizeMap().put(MdlModel.UNIQUE_CONSTRAINT_CLASS_FULL_NAME,100);
	}

	public final void initialize() {
		if (!fileExists()) {
			initializeConfig();
			serialize();
		} else {
			unserialize();
		}
		checkConfig();
	}

	/**
	 * @return the active
	 */
	public final boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public final void setActive(boolean active) {
		this.active = active;
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
	 * @return the classMaxSizeMap
	 */
	public final SortedMap<String, Integer> getClassMaxSizeMap() {
		return classMaxSizeMap;
	}

	/**
	 * @return the halfLifeSeconds
	 */
	public int getHalfLifeSeconds() {
		return halfLifeSeconds;
	}

	/**
	 * @param halfLifeSeconds the halfLifeSeconds to set
	 */
	public void setHalfLifeSeconds(int halfLifeSeconds) {
		if (halfLifeSeconds<1) {
			halfLifeSeconds=1;
		} else if (halfLifeSeconds>600) {
			halfLifeSeconds=600;
		}
		this.halfLifeSeconds = halfLifeSeconds;
	}
	
	public final String getFullFileName() {
		return DbConfig.getInstance().getConfDir() + "CcConfig.xml";
	}
	
	private final void checkConfig() {
		boolean serialize = false;
		List<String> classNames = new ArrayList<String>(classMaxSizeMap.keySet());
		for (String className: classNames) {
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className);
			if (cls==null) {
				Messenger.getInstance().error(this,"Class not found: " + className);
				classMaxSizeMap.remove(className);
				serialize = true;
			} else if (cls.isAbstr()) {
				Messenger.getInstance().error(this,"Class is abstract: " + className);
				classMaxSizeMap.remove(className);
				serialize = true;
			}
		}
		for (MdlClass cls: DbConfig.getInstance().getModel().getClasses()) {
			if (!cls.isAbstr() && !classMaxSizeMap.containsKey(cls.getFullName())) {
				classMaxSizeMap.put(cls.getFullName(),INIT_CACHE_MAX_SIZE);
				serialize = true;
			}
		}
		if (serialize) {
			serialize();
		}
	}

	private void serialize() {
		XMLFile f = toXml();
		File file = new File(getFullFileName());
		if (file.getParentFile().exists()) {
			f.writeFile(getFullFileName(), f.toStringReadFormat());
		}
		f.cleanUp();
	}
	
	private final void unserialize() {
		XMLFile f = new XMLFile();
		f.parseFile(getFullFileName());
		fromXml(f);
		f.cleanUp();
	}

	private final boolean fileExists() {
		File configFile = new File(getFullFileName());
		return configFile.exists();
	}

	private XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("configuration",null,null));
		new XMLElem("active",new StringBuilder("" + active),f.getRootElement());
		new XMLElem("debug",new StringBuilder("" + debug),f.getRootElement());
		new XMLElem("halfLifeSeconds",new StringBuilder("" + halfLifeSeconds),f.getRootElement());
		XMLElem classesElem = new XMLElem("classes",null,f.getRootElement());
		for (String className: classMaxSizeMap.keySet()) {
			XMLElem classElem = new XMLElem("class",null,classesElem);
			new XMLElem("name",new StringBuilder(className),classElem);
			new XMLElem("maxSize",new StringBuilder("" + classMaxSizeMap.get(className)),classElem);
		}
		return f;
	}

	private void fromXml(XMLFile f) {
		if (f.getRootElement()!=null && f.getRootElement().getName().equals("configuration")) {
			for (XMLElem v: f.getRootElement().getChildren()) {
				if (v.getName().equals("active")) {
					active = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("debug")) {
					debug = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("halfLifeSeconds")) {
					setHalfLifeSeconds(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals("classes")) {
					for (XMLElem classElem: v.getChildren()) {
						String className = classElem.getChildByName("name").getValue().toString();
						int maxSize = 100;
						XMLElem maxSizeElem = classElem.getChildByName("maxSize");
						if (maxSizeElem!=null) {
							maxSize = Integer.parseInt(classElem.getChildByName("maxSize").getValue().toString());
						}
						if (maxSize>99999) {
							maxSize=99999;
						} else if (maxSize<10) {
							maxSize=10;
						}
						if (className.length()>0) {
							classMaxSizeMap.put(className,maxSize);
						}
					}
				}
			}
		}
	}
}
