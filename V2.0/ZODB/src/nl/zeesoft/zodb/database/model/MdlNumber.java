package nl.zeesoft.zodb.database.model;

import java.math.BigDecimal;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class MdlNumber extends MdlProperty {
	private BigDecimal minValue = new BigDecimal("" + Long.MIN_VALUE);
	private BigDecimal maxValue = new BigDecimal("" + Long.MAX_VALUE);

	protected MdlNumber(MdlModel mdl) {
		super(mdl);
	}

	@Override
	protected XMLFile toXML() {
		XMLFile file = super.toXML();
		new XMLElem("minValue",new StringBuilder("" + minValue),file.getRootElement());
		new XMLElem("maxValue",new StringBuilder("" + maxValue),file.getRootElement());
		return file;
	}

	@Override
	protected void fromXML(XMLElem rootElem) {
		super.fromXML(rootElem);
		for (XMLElem propElem: rootElem.getChildren()) {
			if (propElem.getName().equals("minValue")) {
				minValue = new BigDecimal(propElem.getValue().toString());
			}
			if (propElem.getName().equals("maxValue")) {
				maxValue = new BigDecimal(propElem.getValue().toString());
			}
		}
	}

	@Override
	protected MdlProperty copy() {
		MdlNumber r = new MdlNumber(getModel());
		r.setCls(getCls());
		r.setName(new String(getName()));
		r.setIndex(new Boolean(isIndex()));
		r.setMinValue(new BigDecimal(minValue.toString()));
		r.setMaxValue(new BigDecimal(maxValue.toString()));
		return r;
	}

	/**
	 * @return the minValue
	 */
	public BigDecimal getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public BigDecimal getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}
}
