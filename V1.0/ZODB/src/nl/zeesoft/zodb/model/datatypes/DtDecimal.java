package nl.zeesoft.zodb.model.datatypes;

import java.math.BigDecimal;

public class DtDecimal extends DtObject {
	public DtDecimal() {
		super(new BigDecimal("0.00"));
	}

	public DtDecimal(BigDecimal d) {
		super(d);
		if (d.scale()<2) {
			d.setScale(2);
		}
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof BigDecimal) {
			if (((BigDecimal)value).scale()<2) {
				((BigDecimal)value).setScale(2);
			}
			super.setValue(value);
		}
	}
	
	@Override
	public BigDecimal getValue() {
		if (super.getValue()!=null) {
			return (BigDecimal) super.getValue();
		} else {
			return null;
		}
	}
}
