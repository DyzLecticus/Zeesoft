package nl.zeesoft.zdk.json;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.StrUtil;

public class JsonParser {
	private int			level		= 0;
	private boolean		inQuote		= false;
	private boolean		inArray		= false;

	public JElem parse(StringBuilder str) {
		JElem r = new JElem();
        parse(r, str);
        return r;
	}

	protected void parse(JElem parent, StringBuilder str) {
		parent.isArray = clean(str);
		List<StringBuilder> elems = parseElements(str);
		for (StringBuilder elem: elems) {
			JElem child = new JElem();
			StringBuilder key = parseKey(elem);
			if (parent.isArray) {
				child.value = Json.getObjectValue(key);
			} else {
				setKeyValue(child, key, parseValue(elem, key));
			}
			parent.children.add(child);
		}
	}
	
	protected void setKeyValue(JElem child, StringBuilder key, StringBuilder val) {
		StrUtil.trim(key,":");
		StrUtil.trim(key,"\"");
		child.key = key.toString();
		if (StrUtil.startsWith(val, "{") || StrUtil.startsWith(val, "[")) {
			JsonParser parser = new JsonParser();
			parser.parse(child, val);
		} else {
			child.value = Json.getObjectValue(val);
		}
	}

	protected List<StringBuilder> parseElements(StringBuilder str) {
		List<StringBuilder> r = new ArrayList<StringBuilder>();
		StringBuilder elem = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			String c = str.substring(i,i+1);
			handleSpecialCharacter(c);
			if (!handleNextElement(c, elem, r)) {
				elem.append(c);
			} else {
				elem = new StringBuilder();
			}
		}
		if (elem.length()>0) {
			StrUtil.trim(elem);
			r.add(elem);
		}
		return r;
	}
	
	protected boolean clean(StringBuilder str) {
		boolean r = false;
		StrUtil.trim(str);
		if (StrUtil.startsWith(str, "[")) {
			r = true;
			StrUtil.trim(str,"[");
			StrUtil.trim(str,"]");
		} else if (StrUtil.startsWith(str, "{")) {
			StrUtil.trim(str,"{");
			StrUtil.trim(str,"}");
		}
		return r;
	}
	
	protected void handleSpecialCharacter(String c) {
		if (c.equals("\"")) {
			inQuote = !inQuote;
		}
		if (!inQuote) {
			if (c.equals("{")) {
				level++;
			} else if (c.equals("}")) {
				level--;
			}
			if (c.equals("[")) {
				inArray = true;
			} else if (c.equals("]")) {
				inArray = false;
			}
		}
	}
	
	protected boolean handleNextElement(String c, StringBuilder elem, List<StringBuilder> elems) {
		boolean r = false;
		if (!inQuote && !inArray && level==0) {
			if (c.equals(",")) {
				StrUtil.trim(elem);
				elems.add(elem);
				r = true;
			}				
		}
		return r;
	}
	
	protected StringBuilder parseKey(StringBuilder elem) {
		StringBuilder r = new StringBuilder();
		boolean inQuote = false;
		for (int i = 0; i < elem.length(); i++) {
			String c = elem.substring(i,i+1);
			if (c.equals("\"")) {
				inQuote = !inQuote;
			}
			r.append(c);
			if (c.equals(":") && !inQuote) {
				break;
			}
		}
		return r;
	}
	
	protected StringBuilder parseValue(StringBuilder elem, StringBuilder key) {
		StringBuilder r = StrUtil.substring(elem, key.length(), elem.length());
		StrUtil.trim(r);
		return r;
	}
}
