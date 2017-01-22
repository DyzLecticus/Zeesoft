package nl.zeesoft.zodb.model.datatypes;

public class DtString extends DtObject {
	public DtString() {
		super(new String(""));
	}

	public DtString(String s) {
		super(s);
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String) {
			super.setValue(value);
		}
	}

	@Override
	public String getValue() {
		if (super.getValue()!=null) {
			return (String) super.getValue();
		} else {
			return null;
		}
	}
}
