package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * JSON file support class.
 */
public class JsFile {
	public JsElem rootElement = null;
	
	public String fromFile(String fileName) {
		String error = "";
		ZStringBuilder json = new ZStringBuilder();
		error = json.fromFile(fileName);
		if (error.length()==0) {
			json.trim();
			if (json.startsWith("{") && json.endsWith("}")) {
				fromStringBuilder(json);
			} else {
				error = "JSON must start with '{' and end with '}'";
			}
		}
		return error;
	}

	public String toFile(String fileName,boolean readFormat) {
		ZStringBuilder json = null;
		if (rootElement==null) {
			json = new ZStringBuilder("{}");
		} else {
			json = toStringBuilder(readFormat);
		}
		return json.toFile(fileName);
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		ZStringBuilder work = new ZStringBuilder(str);
		work.replace("\r","");
		work.replace("\t","  ");
		if (rootElement!=null) {
			rootElement.children.clear();
			rootElement = null;
		}
		work.trim();
		if (work.startsWith("{") &&
			work.endsWith("}")
			) {
			rootElement = new JsElem();
			rootElement.name = "rootElement";
			elementsFromStringBuilder(rootElement,work);
		}
	}

	public ZStringBuilder toStringBuilder() {
		return toStringBuilder(false);
	}	

	public ZStringBuilder toStringBuilderReadFormat() {
		return toStringBuilder(true);
	}	

	private void elementsFromStringBuilder(JsElem parent, ZStringBuilder cElemStr) {
		boolean parseObject = cElemStr.startsWith("{");
		if (!parseObject) {
			parent.array = true;
		}
		cElemStr.replace(0,1,"");
		cElemStr.replace(cElemStr.length()-1,cElemStr.length(),"");
		int objectLevel = 0;
		int arrayLevel = 0;
		boolean inQuote = false;
		boolean inName = parseObject;
		StringBuilder name = new StringBuilder();
		ZStringBuilder value = new ZStringBuilder();
		JsElem child = null;
		if (!parseObject) {
			child = new JsElem();
			parent.children.add(child);
		}
		String pC = "";
		for (int i = 0; i < cElemStr.length(); i++) {
			String c = cElemStr.substring(i,(i+1));
			boolean skip = false;
			if (c.equals("\"") && !pC.equals("\\") && objectLevel==0 && arrayLevel==0) {
				if (inQuote) {
					skip = true;
					inQuote = false;
				} else {
					skip = true;
					inQuote = true;
					if (!inName) {
						child.cData = true;
					}
				}
			}
			if (c.equals(":") && !inQuote && inName) {
				inName = false;
				child = new JsElem();
				child.name = name.toString();
				parent.children.add(child);
				skip = true;
			}
			if (c.equals(",") && !inQuote && objectLevel==0 && arrayLevel==0) {
				parsedChildValue(child,value);
				if (parseObject) {
					inName = true;
				}
				name = new StringBuilder();
				value = new ZStringBuilder();
				skip = true;
				if (!parseObject) {
					child = new JsElem();
					parent.children.add(child);
				}
			}
			if (!skip) {
				if (!inQuote && !inName && c.equals("{")) {
					objectLevel++;
				}
				if (!inQuote && !inName && c.equals("}")) {
					objectLevel--;
				}
				if (!inQuote && !inName && c.equals("[")) {
					arrayLevel++;
				}
				if (!inQuote && !inName && c.equals("]")) {
					arrayLevel--;
				}
				if (!inQuote && inName && c.equals(":")) {
					inName = false;
					child = new JsElem();
					child.name = name.toString();
					parent.children.add(child);
				}
				if ((!c.equals(" ") && !c.equals("\n")) || inQuote || objectLevel>0 || arrayLevel>0) {
					if (inName) {
						name.append(c);
					} else {
						value.append(c);
					}
				}
			}
			pC=c;
		}
		if (child!=null) {
			if (value.length()>0) {
				parsedChildValue(child,value);
			} else if (!parseObject) {
				parent.children.remove(child);
			}
		}
	}

	private void parsedChildValue(JsElem child,ZStringBuilder value) {
		if (((value.startsWith("{") && value.endsWith("}"))) ||
			((value.startsWith("[") && value.endsWith("]")))
			) {
			elementsFromStringBuilder(child,value);
		} else if (!value.toString().equals("null") || child.cData) {
			if (child.cData) {
				value.replace("<NEWLINE>","\n");
				value.replace("<QUOTE>","\"");
			}
			child.value = value;
		}
	}
	
	private ZStringBuilder toStringBuilder(boolean readFormat) {
		ZStringBuilder s = new ZStringBuilder();
		s.append("{");
		if (rootElement!=null) {
			boolean added = false;
			for (JsElem child: rootElement.children) {
				if (added) {
					s.append(",");
				}
				s.append(elementToStringBuilder(child,1,readFormat));
				added = true;
			}
		}
		if (readFormat) {
			s.append("\n");
		}
		s.append("}");
		return s;
	}

	private ZStringBuilder elementToStringBuilder(JsElem elem, int level, boolean readFormat) {
		ZStringBuilder s = new ZStringBuilder();
		if (readFormat) {
			s.append("\n");
			for (int i = 0; i < level; i++) {
				s.append("  ");
			}
		}
		if (elem.name!=null) {
			s.append("\"");
			s.append(elem.name);
			s.append("\": ");
		}
		if (elem.children.size()>0) {
			if (!elem.array) {
				s.append("{");
			} else {
				s.append("[");
			}
			boolean added = false;
			for (JsElem child: elem.children) { 
				if (added) {
					s.append(",");
				}
				s.append(elementToStringBuilder(child,(level + 1),readFormat));
				added = true;
			}
			if (readFormat) {
				s.append("\n");
				for (int i = 0; i < level; i++) {
					s.append("  ");
				}
			}
			if (!elem.array) {
				s.append("}");
			} else {
				s.append("]");
			}
		} else {
			if (elem.cData) {
				s.append("\"");
			}
			if (elem.array) {
				s.append("[]");
			} else {
				ZStringBuilder value = elem.value;
				if (value!=null && elem.cData) {
					value.replace("\n","<NEWLINE>");
					value.replace("\"","<QUOTE>");
				}
				s.append(value);
			}
			if (elem.cData) {
				s.append("\"");
			}
		}
		return s;
	}
}
