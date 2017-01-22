package nl.zeesoft.zodb.model.datatypes;

public class DtFloat extends DtObject {
	public DtFloat() {
		super(new Float(0));
	}

	public DtFloat(float f) {
		super(f);
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Float) {
			super.setValue(value);
		}
	}
	
	@Override
	public Float getValue() {
		if (super.getValue()!=null) {
			return (Float) super.getValue();
		} else {
			return null;
		}
	}
}
