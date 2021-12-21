package nl.zeesoft.zdk.im;

import java.util.List;

public class ObjectArray {
	public Object[]		objects		= new Object[0];
	
	public ObjectArray() {
		
	}
	
	public ObjectArray(int length) {
		objects = new Object[length];
	}
	
	public ObjectArray(Object... values) {
		objects = values;
	}
	
	public ObjectArray(List<Object> values) {
		objects = new Object[values.size()];
		int i = 0;
		for (Object value: values) {
			objects[i] = value;
			i++;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < objects.length; i++) {
			if (str.length()>0) {
				str.append(", ");
			}
			str.append("" + objects[i]);
		}
		str.insert(0, "[");
		str.append("]");
		return str.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean r = false;
		if (other!=null && other instanceof ObjectArray && ((ObjectArray)other).objects.length == objects.length) {
			r = true;
			for (int i = 0; i < objects.length; i++) {
				Object otherVal = ((ObjectArray)other).objects[i];
				if ((objects[i]!=null && otherVal==null) || (objects[i]==null && otherVal!=null) || !objects[i].equals(otherVal)) {
					r = false;
					break;
				}
			}
		}
		return r;
	}
}
