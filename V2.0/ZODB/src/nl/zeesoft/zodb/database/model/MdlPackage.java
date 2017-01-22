package nl.zeesoft.zodb.database.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class MdlPackage extends MdlObject {
	private String 			name 		= "";
	private List<MdlClass>	classes		= new ArrayList<MdlClass>();

	protected MdlPackage(MdlModel mdl) {
		super(mdl);
	}

	@Override
	protected XMLFile toXML() {
		return toXML(false,false);
	}

	protected XMLFile toXML(boolean extended,boolean real) {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("root",null,null));
		new XMLElem("name",new StringBuilder(name),file.getRootElement());

		XMLElem clssElem = new XMLElem("classes",null,file.getRootElement());
		for (MdlClass cls: classes) {
			if (!real || !cls.isAbstr()) {
				XMLElem clsElem = new XMLElem("class",null,clssElem);
				for (XMLElem elem: cls.toXML(extended,real).getRootElement().getChildren()) {
					elem.setParent(clsElem);
				}
			}
		}
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		cleanUp();
		for (XMLElem propElem: rootElem.getChildren()) {
			if (propElem.getName().equals("name")) {
				name = propElem.getValue().toString();
			}
			if (propElem.getName().equals("classes")) {
				for (XMLElem clsElem: propElem.getChildren()) {
					MdlClass cls = new MdlClass(getModel());
					cls.setPkg(this);
					cls.fromXML(clsElem);
					if (cls.getName().length()>0 && this.getClassByName(cls.getName())==null) {
						classes.add(cls);
					} else {
						cls.cleanUp();
					}
				}
			}
		}
	}

	@Override
	protected void cleanUp() {
		for (MdlClass cls: classes) {
			cls.cleanUp();
		}
		classes.clear();
	}
	
	@Override
	protected String getFullName() {
		return name;
	}

	public MdlClass getClassByName(String name) {
		MdlClass r = null;
		for (MdlClass cls: classes) {
			if (cls.getName().equals(name)) {
				r = cls;
				break;
			}
		}
		return r;
	}
	
	public MdlClass getNewClass() {
		MdlClass cls = new MdlClass(getModel());
		cls.setPkg(this);
		classes.add(cls);
		return cls;
	}

	public void removeClass(MdlClass cls) {
		if (classes.remove(cls)) {
			cls.cleanUp();
		}
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
	 * @return the classes
	 */
	public List<MdlClass> getClasses() {
		return new ArrayList<MdlClass>(classes);
	}
}
