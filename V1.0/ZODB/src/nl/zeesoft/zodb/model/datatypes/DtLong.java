package nl.zeesoft.zodb.model.datatypes;

public class DtLong extends DtObject {
	public DtLong() {
		super(new Long(0));
	}

	public DtLong(long l) {
		super(l);
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Long) {
			super.setValue(value);
		}
	}
	
	@Override
	public Long getValue() {
		if (super.getValue()!=null) {
			return (Long) super.getValue();
		} else {
			return null;
		}
	}
}
