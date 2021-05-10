package nl.zeesoft.zdk.json;

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
		parent.isArray = unwrapReturnIsArray(str);
		parseElements(parent, str);
	}

	protected void parseElements(JElem parent, StringBuilder str) {
		StringBuilder elem = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			String c = str.substring(i,i+1);
			handleSpecialCharacter(c);
			if (!inQuote && !inArray && level==0 && c.equals(",")) {
				addChild(parent, elem);
				elem = new StringBuilder();
			} else {
				elem.append(c);
			}
		}
		if (elem.length()>0) {
			addChild(parent, elem);
		}
	}
	
	protected void addChild(JElem parent, StringBuilder elem) {
		StrUtil.trim(elem);
		JElem child = new JElem();
		StringBuilder key = parseKey(elem);
		if (parent.isArray) {
			if (StrUtil.startsWith(key, "{")) {
				JsonParser parser = new JsonParser();
				parser.parse(child, elem);
			} else {
				child.value = Json.getObjectValue(key);
			}
		} else {
			setKeyValue(child, key, parseValue(elem, key));
		}
		parent.children.add(child);
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
	
	protected boolean unwrapReturnIsArray(StringBuilder str) {
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
