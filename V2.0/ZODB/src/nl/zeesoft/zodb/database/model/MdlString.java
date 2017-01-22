package nl.zeesoft.zodb.database.model;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class MdlString extends MdlProperty {
	private int		maxLength 	= 24;
	private boolean	encode		= false;

	protected MdlString(MdlModel mdl) {
		super(mdl);
	}

	@Override
	protected XMLFile toXML() {
		XMLFile file = super.toXML();
		new XMLElem("maxLength",new StringBuilder("" + maxLength),file.getRootElement());
		new XMLElem("encode",new StringBuilder("" + encode),file.getRootElement());
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		super.fromXML(rootElem);
		for (XMLElem propElem: rootElem.getChildren()) {
			if (propElem.getName().equals("maxLength")) {
				setMaxLength(Integer.parseInt(propElem.getValue().toString()));
			}
			if (propElem.getName().equals("encode")) {
				setEncode(Boolean.parseBoolean(propElem.getValue().toString()));
			}
		}
	}

	@Override
	protected MdlProperty copy() {
		MdlString r = new MdlString(getModel());
		r.setCls(getCls());
		r.setName(new String(getName()));
		r.setIndex(new Boolean(isIndex()));
		r.setMaxLength(new Integer(maxLength));
		r.setEncode(new Boolean(encode));
		return r;
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		if (maxLength<1) {
			maxLength = 1;
		}
		this.maxLength = maxLength;
	}

	/**
	 * @return the encode
	 */
	public boolean isEncode() {
		return encode;
	}

	/**
	 * @param encode the encode to set
	 */
	public void setEncode(boolean encode) {
		this.encode = encode;
	}
}
