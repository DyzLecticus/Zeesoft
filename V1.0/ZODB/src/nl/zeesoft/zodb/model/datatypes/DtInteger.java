package nl.zeesoft.zodb.model.datatypes;

public class DtInteger extends DtObject {
	public DtInteger() {
		super(new Integer(0));
	}

	public DtInteger(int i) {
		super(i);
	}
	
	@Override
	public void setValue(Object value) {
		if (value instanceof Integer) {
			super.setValue(value);
		}
	}

	@Override
	public Integer getValue() {
		if (super.getValue()!=null) {
			return (Integer) super.getValue();
		} else {
			return null;
		}
	}
}
