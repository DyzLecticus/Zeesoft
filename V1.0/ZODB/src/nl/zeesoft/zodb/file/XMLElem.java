package nl.zeesoft.zodb.file;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zodb.Generic;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class is used to represent XML elements of XML files
 */
public class XMLElem {
	private String					path			= "";
    private String					name			= "";
    private StringBuffer			value			= null;
    private XMLElem					parent			= null;
    private NamedNodeMap			attributes		= null;
    private String					attributeStr	= "";
	private List<XMLElem>			children		= new ArrayList<XMLElem>();
	private boolean					cData			= false;

	/**
	 * Helps garbage collection 
	 */
	public void cleanUp() {
		if ((children!=null) && (children.size()>0)) {
			for (XMLElem child: children) {
				child.cleanUp();
			}
		    children.clear();
			children = null;
		}
		path        	= "";
	    name        	= "";
	    value       	= null;
	    parent      	= null;
	    attributes  	= null;
	    attributeStr	= "";
		cData			= false;
	}
	
	/**
	 * @param name The tag name of the element
	 * @param value The optional value for the element
	 * @param parent The parent element
	 */
    public XMLElem(String name, StringBuffer value, XMLElem parent) {
    	this.name = name;
    	this.value = value;
    	setParent(parent);
    }

    /**
	 * @param name The tag name of the element
	 * @param value The optional value for the element
	 * @param parent The parent element
	 */
    public static XMLElem getNewXMLElemForCData(String name, StringBuffer value, XMLElem parent) {
    	XMLElem r = new XMLElem(name,value,parent);
    	r.setCData(true);
    	return r;
    }
    
    /**
     * Returns the path of the element
     * 
     * @return The path of the element
     */
    public String getPath() {
		return path;
	}
    
    /**
     * Sets the path of the element
     * 
     * @param path The path of the element
     */
	public void setPath(String path) {
		this.path = path;
		for (XMLElem child: children) {
			// Reset children path
			child.setPath(path + child.getName() + "/");
		}
	}
	
    /**
     * Returns the name of the element
     * 
     * @return The name of the element
     */
	public String getName() {
		return name;
	}

    /**
     * Sets the name of the element
     * 
     * @param name The name of the element
     */
	public void setName(String name) {
		this.name = name;
	}
	
    /**
     * Returns the value of the element
     * 
     * @return The value of the element
     */
	public StringBuffer getValue() {
		return value;
	}

    /**
     * Sets the value of the element
     * 
     * @param value The value of the element
     */
	public void setValue(StringBuffer value) {
		this.value = value;
	}
	
    /**
     * Returns the parent of the element
     * 
     * @return The parent of the element
     */
	public XMLElem getParent() {
		return parent;
	}

    /**
     * Sets the parent of the element
     * 
     * @param parent The parent of the element
     */
	public void setParent(XMLElem parent) {
		this.parent = parent;
		if (parent!=null) {
			this.path = parent.getPath() + name + "/";
			parent.getChildren().add(this);
    	} else {
    		this.path = "/" + name + "/";
		}
		// Trigger path reset
		setPath(path);
	}
	
    /**
     * Returns the attributes of the element
     * 
     * @return The attributes of the element
     */
    public NamedNodeMap getAttributes() {
		return attributes;
	}

    /**
     * Sets the attributes of the element
     * 
     * @param attributes The attributes of the element
     */
    public void setAttributes(NamedNodeMap attributes) {
		this.attributes = attributes;
	}
    
    /**
     * Returns the attribute string of the element
     * 
     * @return The attribute string of the element
     */
    public String getAttributeStr() {
		return attributeStr;
	}

    /**
     * Sets the attribute string of the element
     * 
     * @param attr The attribute string of the element
     */
    public void setAttributeStr(String attr) {
		this.attributeStr = attr;
	}
	
    /**
     * Returns the list of child elements of the element
     * 
     * @return The list of children XML elements
     */
	public List<XMLElem> getChildren() {
		return children;
	}

	/**
     * Sets the list of child elements of the element
     * 
     * @param children The list of child elements of the element
     */
	public void setChildren(List<XMLElem> children) {
		this.children = children;
	}

	/**
	 * @param cData the cData to set
	 */
	public void setCData(boolean cData) {
		this.cData = cData;
	}
	
	/**
	 * Returns the value of a specified attribute
	 * 
	 * @param name The name of the attribute
	 * @return The value of the attribute
	 */
	public String getAttributeValue(String name) {
		String 	value 		= "";
		Node 	nod	 		= null;
		
		if (attributes!=null) {
			for (int n = 0; n < attributes.getLength(); n++) {
	        	nod = attributes.item(n);
	            if (nod.getNodeType() == Node.ATTRIBUTE_NODE) {
	            	if (nod.getNodeName().equals(name)) {
	            		value = nod.getNodeValue();
	            	}
	            }
	        }
		}
		
		return (value);
	}

	/**
	 * Returns a specified child element
	 * 
	 * @param n The name of the child element
	 * @return The element
	 */
	public XMLElem getChildByName(String n) {
		XMLElem child = null;
		for (XMLElem c: children) {
			if (c.getName().equals(n)) {
				child = c;
				break;
			}
		}
		return (child);
	}
	
	/**
	 * Returns a specified child element using a path instruction.
	 * Traverses down the child branch until the end of the path is reached.
	 * 
	 * @param path The full path instruction
	 * @return The element
	 */
	public XMLElem getChildByPath(String path) {
		XMLElem child = null;

		for (XMLElem c: getChildren()) {
			if ((path.length() > c.getPath().length()) &&
				(path.startsWith(c.getPath()))
			) {
				child = c.getChildByPath(path);
			} else if (c.getPath().equals(path)) {
				child = c;
				break;
			}
			if (child!=null) {
				break;
			}
		}

		return (child);
	}

	/**
	 * Returns a string representation of the XML element
	 * 
	 * @return a string representation of the XML element 
	 */
	public String toString() {
		return toString(false,null,null).toString();
	}
	
	public StringBuffer toString(boolean readFormat, SortedMap<String,String> tagNames, SortedMap<String,Boolean> numerics) {
		StringBuffer	element		= new StringBuffer();
		String[]		path		= getPath().split("/");
		Node 			nod			= null;
		
		String			crlf		= "\r\n";
		String			space		= " ";
		
		String 			name 		= getName();
		StringBuffer	val 		= getValue();
		
		if (!readFormat) {
			crlf = "";
			space = "";
		}

		if (tagNames!=null) {
			name = tagNames.get(name);
			if (name==null) {
				name = getName();
			} else if ((numerics!=null) && (numerics.containsKey(name))) {
				if (numerics.get(name)) {
					val = Generic.compress(val);
				}
			}
		}
		
		for (int i = 2; i < path.length; i++) {
			element.append(space + space);
		}
		
		element.append("<");
		element.append(name);
		
		if (attributes!=null) {
			for (int n = 0; n < attributes.getLength(); n++) {
	        	nod = attributes.item(n);
	            if (nod.getNodeType() == Node.ATTRIBUTE_NODE) {
	    			element.append(" ");
	    			element.append(nod.getNodeName());
	    			element.append("=\"");
	    			element.append(nod.getNodeValue().replaceAll("&","&amp;"));
	    			element.append("\"");
	            }
	        }
		} 
		if (attributeStr.length()>0) {
			element.append(" ");
			element.append(attributeStr);
		}
		
		if (getChildren().size() > 0) {
			element.append(">");
			element.append(crlf);
			for (XMLElem child: getChildren()) {
				element.append(child.toString(readFormat,tagNames,numerics));
			}
			for (int i = 2; i < path.length; i++) {
				element.append(space);
				element.append(space);
			}
			element.append("</");
			element.append(name);
			element.append(">");
			element.append(crlf);
		} else if ((getValue()!=null) && (getValue().length()>0)) {
			if (((cData) || (getValue().indexOf("\n") > 0)) && (!Generic.stringBufferStartsWith(getValue(),XMLFile.CDATA_START))){
				element.append(">");
				element.append(XMLFile.CDATA_START);
				element.append(val);
				element.append(XMLFile.CDATA_END);
				element.append("</");
				element.append(name);
				element.append(">");
				element.append(crlf);
			} else {
				element.append(">");
				element.append(val);
				element.append("</");
				element.append(name);
				element.append(">");
				element.append(crlf);
			}
		} else {
			element.append(space);
			element.append("/>");
			element.append(crlf);
		}
		
		return element;
	}

	public void getCompressedTagNames(SortedMap<String,String> tagNames, SortedMap<String,Boolean> numerics) {
		String r = tagNames.get(getName());
		if ((r==null) && (!getName().contains("@")) && (!getName().contains(","))) {
			String c = Generic.ALPHABETIC.toLowerCase() + Generic.ALPHABETIC;
			int l = c.length();
			int s = tagNames.size();
			int i = s % l;
			int m = (s - i) / l;
			r = c.substring(i,(i + 1));
			if (m > 0) {
				if (m < l) {
					r = r + c.substring(m,(m + 1));
				} else {
					r = r + (m - l);
				}
			}
			tagNames.put(getName(),r);
		}
		if ((numerics!=null) && (r!=null) && (getValue()!=null) && (getValue().length()>0)) {
			boolean numeric = false; 
			if (!numerics.containsKey(r)) {
				numerics.put(r,((getValue().length()>=6) && (Generic.isNumeric(getValue()))));
			} else {
				numeric = numerics.get(r);
				if ((numeric) && 
					(
						(getValue().length()<6)) ||
						(!Generic.isNumeric(getValue()))
					) {
					numerics.remove(r);
					numerics.put(r, false);
				}
			}
		}
		for (XMLElem child: getChildren()) {
			child.getCompressedTagNames(tagNames,numerics);
		}
	}

}
