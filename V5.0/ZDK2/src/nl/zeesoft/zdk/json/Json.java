package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.StrUtil;

public class Json {
	public JElem	root	= new JElem();
	
	public Json() {
		
	}
	
	public Json(StringBuilder str) {
		fromStringBuilder(str);
	}
	
	public StringBuilder toStringBuilder() {
		return root.toStringBuilder();
	}
	
	public StringBuilder toStringBuilderReadFormat() {
		return root.toStringBuilderReadFormat();
	}

	public void fromStringBuilder(StringBuilder str) {
		JsonParser parser = new JsonParser();
		root = parser.parse(new StringBuilder(str));
	}
	
	protected static Object getObjectValue(StringBuilder val) {
		Object r = null;
		if (StrUtil.startsWith(val, "\"")) {
			StrUtil.trim(val, "\"");
			r = val;
		} else if (StrUtil.equalsIgnoreCase(val, "true")) {
			r = true;
		} else if (StrUtil.equalsIgnoreCase(val, "false")) {
			r = false;
		} else if (val.length()<20) {
			String str = val.toString();
			r = Util.parseInt(str);
			if (r==null) {
				r = Util.parseDouble(str);
			}
		} else {
			r = val;
		}
		return r;
	}
	
	protected static void addStart(StringBuilder str, boolean isArray) {
		if (isArray) {
			str.append("[");
		} else {
			str.append("{");
		}
	}
	
	protected static void addLineFeedIndent(StringBuilder str, int level) {
		str.append("\n");
		for (int i = 0; i < level; i++) {
			str.append("  ");
		}
	}

	protected static void addEnd(StringBuilder str, boolean isArray) {
		if (isArray) {
			str.append("]");
		} else {
			str.append("}");
		}
	}
}
