package nl.zeesoft.zodb.database.model;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;


public class MdlLink extends MdlProperty {
	private int 			maxSize		= 1;
	private String 			classTo		= "";

	protected MdlLink(MdlModel mdl) {
		super(mdl);
		super.setIndex(true);
	}

	@Override
	protected XMLFile toXML() {
		XMLFile file = super.toXML();
		new XMLElem("maxSize",new StringBuilder("" + maxSize),file.getRootElement());
		new XMLElem("classTo",new StringBuilder(classTo),file.getRootElement());
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		super.fromXML(rootElem);
		for (XMLElem propElem: rootElem.getChildren()) {
			if (propElem.getName().equals("maxSize")) {
				setMaxSize(Integer.parseInt(propElem.getValue().toString()));
			}
			if (propElem.getName().equals("classTo")) {
				classTo = propElem.getValue().toString();
			}
		}
	}

	@Override
	public void setIndex(boolean index) {
		super.setIndex(true);
	}

	@Override
	protected MdlProperty copy() {
		MdlLink r = new MdlLink(getModel());
		r.setCls(getCls());
		r.setName(new String(getName()));
		r.setIndex(new Boolean(isIndex()));
		r.setMaxSize(new Integer(maxSize));
		r.setClassTo(new String(classTo));
		return r;
	}

	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		if (maxSize<1) {
			maxSize = 1;
		}
		this.maxSize = maxSize;
	}

	/**
	 * @return the classTo
	 */
	public String getClassTo() {
		return classTo;
	}

	/**
	 * @param classTo the classTo to set
	 */
	public void setClassTo(String classTo) {
		this.classTo = classTo;
	}
}
