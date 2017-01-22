package nl.zeesoft.zodb.model.datatypes;

public class DtBoolean extends DtObject {
	public DtBoolean() {
		super(new Boolean(false));
	}

	public DtBoolean(boolean b) {
		super(b);
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Boolean) {
			super.setValue(value);
		}
	}

	@Override
	public Boolean getValue() {
		if (super.getValue()!=null) {
			return (Boolean) super.getValue();
		} else {
			return null;
		}
	}
}
