package nl.zeesoft.zdk.blackbox;

public class BoxElementValue {
	private Object value	= null;

	public synchronized Object getValue() {
		return value;
	}

	public synchronized void setValue(Object value) {
		this.value = value;
	}
	
	public static BoxElementValue[] initializeArray(int length) {
		BoxElementValue[] r = new BoxElementValue[length];
		for (int i = 0; i < length; i++) {
			r[i] = new BoxElementValue();
		}
		return r;
	}
	
	public static String toString(BoxElementValue[] array) {
		StringBuilder r = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (r.length()>0) {
				r.append(", ");
			}
			r.append(array[i].getValue());
		}
		return r.toString();
	}
}
