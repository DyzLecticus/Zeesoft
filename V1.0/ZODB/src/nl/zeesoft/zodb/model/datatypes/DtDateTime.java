package nl.zeesoft.zodb.model.datatypes;

import java.util.Date;

public class DtDateTime extends DtObject {
	public DtDateTime() {
		super(null);
	}

	public DtDateTime(Date d) {
		super(d);
	}

	@Override
	public void setValue(Object value) {
		//Allow null values
		//if (value instanceof Date) {
			super.setValue(value);
		//}
	}

	@Override
	public Date getValue() {
		if (super.getValue()!=null) {
			return (Date) super.getValue();
		} else {
			return null;
		}
	}
	
}
