package nl.zeesoft.zodb.model.datatypes;

public class DtStringBuffer extends DtObject {
	public DtStringBuffer() {
		super(new StringBuffer());
	}

	public DtStringBuffer(StringBuffer s) {
		super(s);
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof StringBuffer) {
			super.setValue(value);
		} else if (value instanceof String) {
			if (getValue().length()>0) {
				super.setValue(new StringBuffer());
			}
			getValue().append((String) value);
		}
	}

	@Override
	public StringBuffer getValue() {
		if (super.getValue()!=null) {
			return (StringBuffer) super.getValue();
		} else {
			return null;
		}
	}
}
