package nl.zeesoft.zdk.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JElem {
	public List<JElem>	children		= new ArrayList<JElem>();
	public String		key				= null;
	public Object		value			= null;
	public boolean		isArray			= false;
	
	public JElem() {
		
	}
	
	public JElem(String key, Object value) {
		this.key = key;
		setValue(value);
	}
	
	public StringBuilder toStringBuilder() {
		return toStringBuilder(false, 0);
	}
	
	public StringBuilder toStringBuilderReadFormat() {
		return toStringBuilder(true, 0);
	}
	
	public JElem put(String key, Object value) {
    	JElem r = null;
    	if (key!=null && key.length()>0) {
    		r = new JElem(key, value);
        	children.add(r);
    	}
    	return r;
	}
	
	public JElem putArray(String key, List<Object> values) {
    	JElem r = put(key, values);
    	r.isArray = true;
    	return r;
	}
	
	protected StringBuilder toStringBuilder(boolean readFormat, int level) {
		StringBuilder r = new StringBuilder();
		appendKey(r, readFormat);
		if (value!=null) {
			appendValue(r);
		} else {
			appendChildren(r, readFormat, level + 1);
		}
		return r;
	}
	
	protected void appendKey(StringBuilder str, boolean readFormat) {
		if (key!=null) {
			str.append("\"");
			str.append(key);
			str.append("\":");
			if (readFormat) {
				str.append(" ");
			}
		}
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
	
	protected void appendChildren(StringBuilder str, boolean readFormat, int level) {
		Json.addStart(str, isArray);
		boolean first = true;
		for (JElem child: children) {
			if (!first) {
				str.append(",");
			}
			if (readFormat) {
				Json.addLineFeedIndent(str, level);
			}
			str.append(child.toStringBuilder(readFormat, level));
			first = false;
		}
		if (readFormat && children.size()>0) {
			Json.addLineFeedIndent(str, level - 1);
		}
		Json.addEnd(str, isArray);
	}
	
	protected void fromElements(Map<String,Object> elems) {
		children.clear();
		if (elems!=null) {
	        for (String key: elems.keySet()) {
	        	children.add(new JElem(key, elems.get(key)));
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
    		child.setValue(value);
        	children.add(child);
        }
	}
}
