package nl.zeesoft.zodb.database.model;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public abstract class MdlProperty extends MdlObject {
	public static final String 	ID			= "id";

	private MdlClass			cls			= null;
	private String 				name 		= "";
	private boolean 			index		= false;

	protected MdlProperty(MdlModel mdl) {
		super(mdl);
	}

	@Override
	protected XMLFile toXML() {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("root",null,null));
		new XMLElem("name",new StringBuilder(name),file.getRootElement());
		new XMLElem("index",new StringBuilder("" + index),file.getRootElement());
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		for (XMLElem propElem: rootElem.getChildren()) {
			if (propElem.getName().equals("name")) {
				name = propElem.getValue().toString();
			}
			if (propElem.getName().equals("index")) {
				index = Boolean.parseBoolean(propElem.getValue().toString());
			}
		}
	}

	public String getIndexDirName() {
		return DbConfig.getInstance().getDataDir() + getFullName().replace(":","/") + "/"; 
	}

	protected abstract MdlProperty copy();
	
	@Override
	protected void cleanUp() {
		cls = null;
	}

	@Override
	public String getFullName() {
		return cls.getFullName() + ":" + name;
	}

	/**
	 * @return the class
	 */
	public MdlClass getCls() {
		return cls;
	}

	/**
	 * @param cls the class to set
	 */
	protected void setCls(MdlClass cls) {
		this.cls = cls;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the index
	 */
	public boolean isIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(boolean index) {
		this.index = index;
	}
}
