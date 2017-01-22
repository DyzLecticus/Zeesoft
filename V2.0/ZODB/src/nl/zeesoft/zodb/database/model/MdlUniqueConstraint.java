package nl.zeesoft.zodb.database.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class MdlUniqueConstraint extends MdlObject {
	private boolean								caseSensitive	= false;
	private List<String>						classes			= new ArrayList<String>();
	private List<String>						properties		= new ArrayList<String>();
	private List<MdlClass>						classesList		= new ArrayList<MdlClass>();
	private SortedMap<String,List<MdlProperty>>	propertiesList	= new TreeMap<String,List<MdlProperty>>();

	protected MdlUniqueConstraint(MdlModel mdl) {
		super(mdl);
	}

	@Override
	protected XMLFile toXML() {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("root",null,null));
		if (caseSensitive) {
			new XMLElem("caseSensitive",new StringBuilder("" + caseSensitive),file.getRootElement());
		}
		XMLElem classElem = new XMLElem("classes",null,file.getRootElement());
		for (String className: classes) {
			new XMLElem("name",new StringBuilder(className),classElem);
		}
		XMLElem propsElem = new XMLElem("properties",null,file.getRootElement());
		for (String propName: properties) {
			new XMLElem("name",new StringBuilder(propName),propsElem);
		}
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		for (XMLElem propElem: rootElem.getChildren()) {
			if (propElem.getName().equals("caseSensitive")) {
				caseSensitive = Boolean.parseBoolean(propElem.getValue().toString());
			}
			if (propElem.getName().equals("classes")) {
				for (XMLElem cElem: propElem.getChildren()) {
					classes.add(cElem.getValue().toString());
				}
			}
			if (propElem.getName().equals("properties")) {
				for (XMLElem cElem: propElem.getChildren()) {
					properties.add(cElem.getValue().toString());
				}
			}
		}
	}

	@Override
	protected void cleanUp() {
		classes.clear();
		properties.clear();
		classesList.clear();
		propertiesList.clear();
	}

	/**
	 * @return the classes
	 */
	public List<MdlClass> getClassesList() {
		if (classesList.size()==0) {
			if (classes.size()>0) {
				for (String name: classes) {
					MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(name);
					if (cls!=null) {
						classesList.add(cls);
					}
				}
			}
		}
		return classesList;
	}

	/**
	 * @return the properties
	 */
	public List<MdlProperty> getPropertiesListForClass(String className) {
		List<MdlProperty> r = null;
		if (propertiesList.size()==0 && properties.size()>0) {
			for (MdlClass cls: getClassesList()) {
				for (String name: properties) {
					List<MdlProperty> props = propertiesList.get(cls.getFullName());
					MdlProperty prop = cls.getPropertyByName(name);
					if (prop!=null) {
						if (props==null) {
							props = new ArrayList<MdlProperty>();
							propertiesList.put(cls.getFullName(),props);
						}
						props.add(prop);
					}
				}
			}
		}
		r = propertiesList.get(className);
		if (r==null) {
			r = new ArrayList<MdlProperty>();
		}
		return r;
	}
	
	@Override
	public String getFullName() {
		StringBuilder clss = new StringBuilder();
		for (String cls: classes) {
			if (clss.length()>0) {
				clss.append(",");
			}
			clss.append(cls);
		}
		StringBuilder props = new StringBuilder();
		for (String prop: properties) {
			if (props.length()>0) {
				props.append(",");
			}
			props.append(prop);
		}
		return clss + "::" + props;
	}

	public String getIndexDirName() {
		return DbConfig.getInstance().getDataDir() + getFullName().replace(".","_").replace(",","_").replace(":","_") + "/"; 
	}

	
	/**
	 * @return the classes
	 */
	public List<String> getClasses() {
		return classes;
	}

	/**
	 * @return the properties
	 */
	public List<String> getProperties() {
		return properties;
	}

	/**
	 * @return the caseSensitive
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
}
