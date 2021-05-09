package nl.zeesoft.zdk.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JElem {
	public List<JElem>	children		= new ArrayList<JElem>();
	public String		key				= null;
	public Object		value			= null;
	public boolean		isArray			= false;
	
	public StringBuilder toStr() {
		StringBuilder r = new StringBuilder();
		if (key!=null) {
			r.append("\"");
			r.append(key);
			r.append("\":");
		}
		if (value!=null) {
			appendValue(r);
		} else {
			appendChildren(r);
		}
		return r;
	}
	
	public JElem put(String key, Object value) {
    	JElem r = null;
    	if (key!=null && key.length()>0) {
    		r = new JElem();
    		r.key = key;
        	if (value!=null) {
        		r.value = value;
        	}
        	children.add(r);
    	}
    	return r;
	}
	
	public JElem putArray(String key, List<Object> values) {
    	JElem r = put(key, values);
    	r.isArray = true;
    	return r;
	}
	
	protected void appendValue(StringBuilder str) {
		if (value instanceof String) {
			str.append("\"");
		}
		str.append(value);
		if (value instanceof String) {
			str.append("\"");
		}
	}
	
	protected void appendChildren(StringBuilder str) {
		if (isArray) {
			str.append("[");
		} else {
			str.append("{");
		}
		boolean first = true;
		for (JElem child: children) {
			if (!first) {
				str.append(",");
			}
			str.append(child.toStr());
			first = false;
		}
		if (isArray) {
			str.append("]");
		} else {
			str.append("}");
		}
	}
	
	protected void fromElements(Map<String,Object> elems) {
		children.clear();
		if (elems!=null) {
	        for (String key: elems.keySet()) {
	        	JElem child = new JElem();
	        	child.key = key;
	        	child.setValue(elems.get(key));
	        	children.add(child);
	        }
		}
	}
	
	protected void setValue(Object value) {
    	if (value instanceof Map) {
    		@SuppressWarnings("unchecked")
    		Map<String,Object> subElems = (Map<String,Object>)value;
    		fromElements(subElems);
    	} else if (value instanceof List) {
    		isArray = true;
    		@SuppressWarnings("unchecked")
    		List<Object> values = (List<Object>)value;
    		fromList(values);
    	} else {
    		this.value = value;
    	}
	}
	
	protected void fromList(List<Object> values) {
		children.clear();
        for (Object value: values) {
        	JElem child = new JElem();
    		child.value = value;
        	children.add(child);
        }
	}
}
