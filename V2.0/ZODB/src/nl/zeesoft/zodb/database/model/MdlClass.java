package nl.zeesoft.zodb.database.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class MdlClass extends MdlObject {
	private MdlPackage					pkg						= null;
	private String 						name 					= "";
	private boolean 					abstr 					= false;
	private List<String>				extendsClasses			= new ArrayList<String>();
	
	private List<MdlProperty>			properties				= new ArrayList<MdlProperty>();
	private List<MdlProperty>			propertiesExt			= null;
	
	private List<MdlLink>				childLinks				= null;

	private List<MdlUniqueConstraint>	uniqueConstraintList	= null;

	protected MdlClass(MdlModel mdl) {
		super(mdl);
	}
	
	@Override
	protected XMLFile toXML() {
		return toXML(false,false);
	}

	protected XMLFile toXML(boolean extended,boolean real) {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("root",null,null));
		new XMLElem("name",new StringBuilder("" + name),file.getRootElement());
		if (!real) {
			new XMLElem("abstract",new StringBuilder("" + abstr),file.getRootElement());
		}
		if (!extended) {
			if (extendsClasses.size()>0) {
				XMLElem classElem = new XMLElem("extendsClasses",null,file.getRootElement());
				for (String className: extendsClasses) {
					new XMLElem("name",new StringBuilder(className),classElem);
				}
			}
		}

		List<MdlProperty> props = properties;
		if (extended) {
			props = propertiesExt;
		}
		if (props.size()>0) {
			XMLElem propsElem = new XMLElem("properties",null,file.getRootElement());
			for (MdlProperty prop: props) {
				String type = "";
				if (prop instanceof MdlString) {
					type = "string";
				} else if (prop instanceof MdlNumber) {
					type = "number";
				} else if (prop instanceof MdlLink) {
					type = "link";
				}
				XMLElem propElem = new XMLElem(type,null,propsElem);
				for (XMLElem elem: prop.toXML().getRootElement().getChildren()) {
					elem.setParent(propElem);
				}
			}
		}
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		cleanUp();
		for (XMLElem clsElem: rootElem.getChildren()) {
			if (clsElem.getName().equals("name")) {
				name = clsElem.getValue().toString();
			}
			if (clsElem.getName().equals("abstract")) {
				abstr = Boolean.parseBoolean(clsElem.getValue().toString());
			}
			if (clsElem.getName().equals("extendsClasses")) {
				for (XMLElem cElem: clsElem.getChildren()) {
					extendsClasses.add(cElem.getValue().toString());
				}
			}
			if (clsElem.getName().equals("properties")) {
				for (XMLElem propElem: clsElem.getChildren()) {
					MdlProperty prop = null;
					if (propElem.getName().equals("string")) {
						prop = new MdlString(getModel());
					} else if (propElem.getName().equals("number")) {
						prop = new MdlNumber(getModel());
					} else if (propElem.getName().equals("link")) {
						prop = new MdlLink(getModel());
					} else {
						Messenger.getInstance().error(this,"Unable to determine property type: " + propElem.getName());
					}
					if (prop!=null) {
						prop.fromXML(propElem);
						prop.setCls(this);
						if (prop.getName().length()>0 && 
							!prop.getName().equals(MdlProperty.ID) &&
							getClassPropertyByName(prop.getName())==null
							) {
							properties.add(prop);
						} else {
							Messenger.getInstance().error(this,"Property not added to class: " + getFullName() + ":" + prop.getName());
							prop.cleanUp();
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void cleanUp() {
		for (MdlProperty prop: properties) {
			prop.cleanUp();
		}
		properties.clear();
		
		if (propertiesExt!=null) {
			propertiesExt.clear();
			propertiesExt = null;
		}
		if (childLinks!=null) {
			childLinks.clear();
			childLinks = null;
		}
		if (uniqueConstraintList!=null) {
			uniqueConstraintList.clear();
			uniqueConstraintList = null;
		}
	}

	private MdlProperty getClassPropertyByName(String name) {
		MdlProperty r = null;
		for (MdlProperty prop: properties) {
			if (prop.getName().equals(name)) {
				r = prop;
				break;
			}
		}
		return r;
	}

	public String getClassDirName() {
		return DbConfig.getInstance().getDataDir() + getFullName() + "/"; 
	}

	public String getIndexDirName() {
		return getClassDirName() + MdlProperty.ID + "/"; 
	}
	
	/**
	 * @return the properties including extended class properties
	 */
	public List<MdlProperty> getPropertiesExtended() {
		if (propertiesExt==null) {
			propertiesExt = new ArrayList<MdlProperty>();
			MdlNumber idProp = new MdlNumber(getModel());
			idProp.setName(MdlProperty.ID);
			idProp.setCls(this);
			idProp.setMinValue(new BigDecimal("1"));
			propertiesExt.add(idProp);
			for (MdlProperty prop: properties) {
				propertiesExt.add(prop);
			}
			for (MdlClass cls: getExtendsClassesList()) {
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					boolean add = true;
					for (MdlProperty testProp: propertiesExt) {
						if (testProp.getName().equals(prop.getName())) {
							add = false;
							break;
						}
					}
					if (add) {
						MdlProperty addProp = prop.copy();
						addProp.setCls(this);
						propertiesExt.add(addProp);
					}
				}
			}
		}
		return propertiesExt;
	}

	public MdlProperty getPropertyByName(String name) {
		MdlProperty r = null;
		for (MdlProperty prop: this.getPropertiesExtended()) {
			if (prop.getName().equals(name)) {
				r = prop;
				break;
			}
		}
		return r;
	}

	@Override
	public String getFullName() {
		return pkg.getName() + "." + name;
	}

	/**
	 * @param pkg the pkg to set
	 */
	protected void setPkg(MdlPackage pkg) {
		this.pkg = pkg;
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
	 * @return the abstr
	 */
	public boolean isAbstr() {
		return abstr;
	}

	/**
	 * @param abstr the abstr to set
	 */
	public void setAbstr(boolean abstr) {
		this.abstr = abstr;
	}

	/**
	 * @return the properties
	 */
	public List<MdlProperty> getProperties() {
		return new ArrayList<MdlProperty>(properties);
	}

	public MdlString getNewString() {
		if (propertiesExt!=null) {
			propertiesExt.clear();
			propertiesExt = null;
		}
		return (MdlString) getNewProperty(MdlString.class.getName());
	}

	public MdlNumber getNewNumber() {
		if (propertiesExt!=null) {
			propertiesExt.clear();
			propertiesExt = null;
		}
		return (MdlNumber) getNewProperty(MdlNumber.class.getName());
	}

	public MdlLink getNewLink() {
		if (propertiesExt!=null) {
			propertiesExt.clear();
			propertiesExt = null;
		}
		return (MdlLink) getNewProperty(MdlLink.class.getName());
	}

	private MdlProperty getNewProperty(String typeClassName) {
		MdlProperty prop = null;
		if (typeClassName.equals(MdlString.class.getName())) {
			prop = new MdlString(getModel());
		} else if (typeClassName.equals(MdlNumber.class.getName())) {
			prop = new MdlNumber(getModel());
		} else if (typeClassName.equals(MdlLink.class.getName())) {
			prop = new MdlLink(getModel());
		} else {
			Messenger.getInstance().error(this,"Unknown property type class name: " + typeClassName);
		}
		if (prop!=null) {
			prop.setCls(this);
			properties.add(prop);
		}
		return prop;
	}

	public void removeProperty(MdlProperty prop) {
		if (properties.remove(prop)) {
			prop.cleanUp();
		}
	}
	
	/**
	 * @return the extendsClasses
	 */
	public List<MdlClass> getExtendsClassesList() {
		List<MdlClass> r = new ArrayList<MdlClass>();
		if (extendsClasses.size()>0) {
			for (String fullName: extendsClasses) {
				if (fullName.length()>0) {
					MdlClass cls = getModel().getClassByFullName(fullName);
					if (cls!=null) {
						r.add(cls);
					}
				}
			}
		}
		return r;
	}

	/**
	 * @return the extendsClasses
	 */
	public List<String> getExtendsClasses() {
		return extendsClasses;
	}
	
	public List<MdlLink> getChildLinks() {
		if (childLinks==null) {
			childLinks = new ArrayList<MdlLink>();
			for (MdlClass cls: getModel().getClasses()) { 
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlLink) {
						MdlLink lnk = (MdlLink) prop;
						if (lnk.getClassTo().equals(getFullName())) {
							childLinks.add(lnk);
						}
					}
				}
			}
		}
		return childLinks;
	}

	/**
	 * @return the uniqueConstraintList
	 */
	public List<MdlUniqueConstraint> getUniqueConstraintList() {
		if (uniqueConstraintList==null) {
			uniqueConstraintList = new ArrayList<MdlUniqueConstraint>();
			for (MdlUniqueConstraint uc: getModel().getUniqueConstraints()) {
				if (uc.getClasses().contains(getFullName())) {
					uniqueConstraintList.add(uc);
				}
			}
		}
		return uniqueConstraintList;
	}	
}
