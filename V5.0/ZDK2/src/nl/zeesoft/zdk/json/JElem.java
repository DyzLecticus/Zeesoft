package nl.zeesoft.zdk.json;

import java.util.ArrayList;
import java.util.List;

public class JElem {
	public List<JElem>	children		= new ArrayList<JElem>();
	public String		key				= null;
	public Object		value			= null;
	public boolean		isArray			= false;
	
	public JElem() {
		
	}
	
	public JElem(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public StringBuilder toStringBuilder() {
		return toStringBuilder(false, 0);
	}
	
	public StringBuilder toStringBuilderReadFormat() {
		return toStringBuilder(true, 0);
	}
	
	public JElem put(String key, Object value) {
    	JElem r = null;
    	if (key==null || key.length()>0) {
    		r = new JElem(key, value);
        	children.add(r);
    	}
    	return r;
	}
	
	public JElem putArray(String key, List<Object> values) {
    	JElem r = put(key, null);
    	r.isArray = true;
    	if (values!=null) {
	    	for (Object obj: values) {
	    		r.put(null, obj);
	    	}
    	}
    	return r;
	}
	
	public JElem get(int index) {
		return children.get(index);
	}
	
	public JElem get(String key) {
		JElem r = null;
		for (JElem child: children) {
			if (child.key.equals(key)) {
				r = child;
				break;
			}
		}
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
		if (value instanceof String || value instanceof StringBuilder) {
			str.append("\"");
		}
		str.append(value);
		if (value instanceof String || value instanceof StringBuilder) {
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
}
