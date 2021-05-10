package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.StrUtil;
import nl.zeesoft.zdk.Util;

public class Json {
	public JElem	root	= null;
	
	public StringBuilder toStringBuilder() {
		StringBuilder r = new StringBuilder();
		if (root!=null) {
			r = root.toStringBuilder();
		}
		return r;
	}
	
	public StringBuilder toStringBuilderReadFormat() {
		StringBuilder r = new StringBuilder();
		if (root!=null) {
			r = root.toStringBuilderReadFormat();
		}
		return r;
	}

	public void fromStringBuilder(StringBuilder str) {
		JsonParser parser = new JsonParser();
		root = parser.parse(str);
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
