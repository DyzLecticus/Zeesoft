package nl.zeesoft.zodb.file.json;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.file.FileIO;

public class JsFile extends FileIO {
	public JsElem rootElement = null;
	
	public void fromStringBuilder(StringBuilder str) {
		if (rootElement!=null) {
			rootElement.children.clear();
			rootElement = null;
		}
		str = Generic.stringBuilderTrim(str);
		if (Generic.stringBuilderStartsWith(str,"{") &&
			Generic.stringBuilderEndsWith(str,"}")
			) {
			rootElement = new JsElem();
			rootElement.name = "rootElement";
			elementsFromStringBuilder(rootElement,str);
		}
	}

	public StringBuilder toStringBuilder() {
		return toStringBuilder(false);
	}	

	public StringBuilder toStringBuilderReadFormat() {
		return toStringBuilder(true);
	}	

	private void elementsFromStringBuilder(JsElem parent, StringBuilder cElemStr) {
		boolean parseObject = Generic.stringBuilderStartsWith(cElemStr,"{");
		if (!parseObject) {
			parent.array = true;
		}
		cElemStr = cElemStr.delete(0,1);
		cElemStr = cElemStr.delete(cElemStr.length()-1,cElemStr.length());
		int objectLevel = 0;
		int arrayLevel = 0;
		boolean inQuote = false;
		boolean inName = parseObject;
		StringBuilder name = new StringBuilder();
		StringBuilder value = new StringBuilder();
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
				value = new StringBuilder();
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
		if (value.length()>0) {
			parsedChildValue(child,value);
		} else if (!parseObject) {
			parent.children.remove(child);
		}
	}

	private void parsedChildValue(JsElem child,StringBuilder value) {
		if (((Generic.stringBuilderStartsWith(value,"{") && Generic.stringBuilderEndsWith(value,"}"))) ||
			((Generic.stringBuilderStartsWith(value,"[") && Generic.stringBuilderEndsWith(value,"]")))
			) {
			elementsFromStringBuilder(child,value);
		} else if (!value.toString().equals("null") || child.cData) {
			if (child.cData) {
				value = Generic.stringBuilderReplace(value,"<NEWLINE>","\n");
				value = Generic.stringBuilderReplace(value,"<QUOTE>","\"");
			}
			child.value = value;
		}
	}
	
	private StringBuilder toStringBuilder(boolean readFormat) {
		StringBuilder s = new StringBuilder();
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

	private StringBuilder elementToStringBuilder(JsElem elem, int level, boolean readFormat) {
		StringBuilder s = new StringBuilder();
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
				StringBuilder value = elem.value;
				if (value!=null && elem.cData) {
					value = Generic.stringBuilderReplace(value,"\n","<NEWLINE>");
					value = Generic.stringBuilderReplace(value,"\"","<QUOTE>");
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
