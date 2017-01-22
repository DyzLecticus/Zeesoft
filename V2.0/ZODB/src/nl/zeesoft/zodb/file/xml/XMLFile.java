package nl.zeesoft.zodb.file.xml;

import java.io.CharArrayReader;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.file.FileIO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class is used to represent XML files
 */
public class XMLFile extends FileIO {
	public static final String			CDATA_START			= "<!" + "[" + "CDATA" + "[";
	public static final String			CDATA_END			= "]" + "]" + ">";

	public static final String			CDATA_START_REVERSE	= "]" + "ATADC" + "]" + "!>";
	public static final String			CDATA_END_REVERSE	= ">" + "[" + "[";

	private DocumentBuilder 			builder				= null;
	private Document 					doc					= null;
	
	private XMLElem		   				rootElement			= null;
	
	private static final String			COMPRESSED			= "compressed"; 
	private static final String			TAGS				= "tags"; 
	private static final String			DATA				= "data"; 
	private SortedMap<String,String>	compressedTags		= null;
	private List<String>				numericTags			= null;
	
	private boolean						compressNumerics	= true;

	public XMLFile() {
	}

	public XMLFile(DocumentBuilder builder) {
		this.builder = builder;
	}
	
	/**
	 * Helps garbage collection 
	 */
	public void cleanUp() {
		if (compressedTags!=null) {
			compressedTags.clear();
			compressedTags = null;
		}
		if (numericTags!=null) {
			numericTags.clear();
			numericTags = null;
		}
		if (rootElement!=null) {
			rootElement.cleanUp();
			rootElement=null;
		}
		doc=null;
	}
	
	/**
	 * Returns the root element of the XML file
	 * 
	 * @return the root element
	 */
	public XMLElem getRootElement() {
		return rootElement;
	}

	/**
	 * Sets the root element of the XML file
	 * 
	 * @param elem the root element
	 */
	public void setRootElement(XMLElem elem) {
		this.rootElement = elem;
	}

	/**
	 * Returns the XML document
	 * 
	 * @return The XML document
	 */
	public Document getDocument() {
		return doc;
	}
	
	/**
	 * Parses the XML
	 * 
	 * @param xml The XML
	 */
	public String parseXML(String xml) {
		return parseXML(xml,false);
	}
	
	/**
	 * Parses the XML
	 * 
	 * @param xml The XML
	 * @param silent If true, suppress error messages
	 */
	public String parseXML(String xml,boolean silent) { 
		if (builder==null) {
			builder = XMLConfig.getInstance().getNewBuilder();
		}
		String errMsg = "";
		xml = xml.trim();
		if (!xml.startsWith("<?xml")) {
			xml = createHeader() + "\r\n" + xml;
		}
		StringReader sr = null;
		try {
			sr = new StringReader(xml);
			InputSource is = new InputSource(sr);
			doc = builder.parse(is);
			parseXMLElem(null,null,"");
			sr.close();
		} catch (Exception e) {
			errMsg = "" + e.getMessage() + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n" + xml;
			if (!silent) {
				Messenger.getInstance().error(this, errMsg);
			}
		} finally {
			if (sr!=null) {
				sr.close();
			}
			doc = null;
		}
		return errMsg; 
	} 

	/**
	 * Parses the XML
	 * 
	 * @param xml The XML
	 */
	public String parseXML(StringBuilder xml) {
		return parseXML(xml,false);
	}
	
	/**
	 * Parses the XML
	 * 
	 * @param xml The XML
	 * @param silent If true, suppress error messages
	 */
	public String parseXML(StringBuilder xml,boolean silent) { 
		if (builder==null) {
			builder = XMLConfig.getInstance().getNewBuilder();
		}
		String errMsg = "";
		if (!xml.substring(0,5).equals("<?xml")) {
			xml.insert(0, createHeader() + "\r\n");
		}
		char[] chars = new char[xml.length()];
		xml.getChars(0, xml.length(), chars, 0);
		CharArrayReader car = new CharArrayReader(chars);
		try {
			car = new CharArrayReader(chars);
			InputSource is = new InputSource(car);
			doc = builder.parse(is);
			parseXMLElem(null,null,"");
		} catch (Exception e) {
			errMsg = "" + e.getMessage() + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n" + xml;
			if (!silent) {
				Messenger.getInstance().error(this, errMsg);
			}
		} finally {
			if (car!=null) {
				car.close();
			}
			doc = null;
		}
		return errMsg; 
	} 
	
	/**
	 * Reads and parses the XML file.
	 * 
	 * @param fileName The name of file
	 */
	public String parseFile(String fileName) {
		return parseXML(new File(fileName));
	}

	/**
	 * Reads and parses the XML file.
	 * 
	 * @param fileName The name of file
	 * @param silent If true, suppress error messages
	 */
	public String parseFile(String fileName,boolean silent) {
		return parseXML(new File(fileName),silent);
	}

	/**
	 * Parses the XML
	 * 
	 * @param xml The XML
	 */
	public String parseXML(File f) {
		return parseXML(f,false);
	}
	
	/**
	 * Parses the XML
	 * 
	 * @param xml The XML
	 * @param silent If true, suppress error messages
	 */
	public String parseXML(File f,boolean silent) { 
		if (builder==null) {
			builder = XMLConfig.getInstance().getNewBuilder();
		}
		String errMsg = "";
		try {
			doc = builder.parse(f);
			parseXMLElem(null,null,"");
		} catch (Exception e) {
			errMsg = "" + e.getMessage() + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n";
			if (!silent) {
				Messenger.getInstance().error(this, errMsg);
			}
		} finally {
			doc = null;
		}
		return errMsg; 
	} 

	/**
	 * Parses the XML file elements recursively 
	 * 
	 * @param pElem The optional parent element
	 * @param parent The optional parent XML element
	 * @param parentPath The parent path
	 * @throws Exception 
	 */
	private void parseXMLElem(Element pElem, XMLElem parent, String parentPath) throws Exception {
	
		NodeList   						nodList	  = null;
		Node	   						nod		  = null;
		Element							nodElem  	 = null;
		StringBuilder					nodValue	 = null;
		String							elemName	 = "";
		XMLElem							newXMLElem   = null;
		
		if (pElem==null) {
			compressedTags = null;
			numericTags = null;

			doc.getDocumentElement().normalize();
			elemName = doc.getDocumentElement().getNodeName();
			NodeList nodListRoot = doc.getElementsByTagName(elemName);
			pElem = (Element) nodListRoot.item(0);

			rootElement = null;
			newXMLElem = new XMLElem(elemName,null,null);
			if (pElem.getAttributes()!=null) {
				newXMLElem.setProperties(pElem.getAttributes());
			}
			rootElement = newXMLElem;
			parent = newXMLElem;
			
			if (rootElement.getName().equals(COMPRESSED)) {
				compressedTags = new TreeMap<String,String>();
				numericTags = new ArrayList<String>();
			}
		}

		nodList = pElem.getChildNodes();
		for (int n = 0; n < nodList.getLength(); n++) {
			nod = nodList.item(n);
			if (nod.getNodeType() == Node.ELEMENT_NODE) {
				elemName = nod.getNodeName();
				nodElem  = (Element) nodList.item(n);
				nodValue = null;
				
				if (nodElem!=null) { 
					Node valNode = (Node) nodElem.getChildNodes().item(0);
					if ((valNode!=null) && (valNode.getNodeValue()!=null)) { 
						nodValue = new StringBuilder(valNode.getNodeValue());
					}
				}

				if (compressedTags!=null) {
					if ((nodValue!=null) && (elemName.equals(TAGS))) {
						String[] tagNameReps = nodValue.toString().split("@");
						for (int i = 0; i < tagNameReps.length; i++) {
							String[] tagNameRep = tagNameReps[i].split(",");
							compressedTags.put(tagNameRep[0],tagNameRep[1]);
							if (tagNameRep.length>2) {
								numericTags.add(tagNameRep[0]);
							}
						}
					} else if (!elemName.equals(DATA)) {
						String rn = compressedTags.get(elemName);
						if (rn!=null) {
							if (numericTags.contains(elemName)) {
								if ((nodValue!=null) && (nodValue.length()>0)) {
									nodValue = Generic.decompress(nodValue);
								}
							}
							elemName = rn;
						}
					}
				}

				if (nodValue==null) {
					nodValue = new StringBuilder();
				}
				newXMLElem = new XMLElem(elemName,nodValue,parent);

				if ((compressedTags!=null) && (parent.getName().equals(DATA))) {
					rootElement = newXMLElem;
				}
				
				if (nod.getAttributes()!=null) {
					newXMLElem.setProperties(nod.getAttributes());
				}
				
				parseXMLElem(nodElem,newXMLElem,parent.getPath());
				
			}
		}
	}
	
	/**
	 * Returns a string representation of the XML file
	 * 
	 * @return a string representation of the XML file 
	 */
	public String toString() {
		return toStringBuilder().toString();
	}

	public StringBuilder toStringBuilder() {
		StringBuilder s = rootElement.toString(false,null,null);
		s.insert(0,createHeader());
		s.append("\n");
		return s;
	}
	
	public StringBuilder toStringReadFormat() {
		StringBuilder s = rootElement.toString(true,null,null);
		s.insert(0,"\r\n");
		s.insert(0,createHeader());
		s.append("\r\n");
		return s;
	}

	public StringBuilder toStringCompressed() {
		SortedMap<String,String> tagNames = new TreeMap<String,String>();
		SortedMap<String,Boolean> numerics = null;
		if (compressNumerics) {
			numerics = new TreeMap<String,Boolean>();
		}
		rootElement.getCompressedTagNames(tagNames,numerics);
		XMLElem compressed = new XMLElem(COMPRESSED,null,null);
		StringBuilder tags = new StringBuilder();
		for (Entry<String,String> entry: tagNames.entrySet()) {
			boolean numeric = false;
			if ((numerics!=null) && (numerics.containsKey(entry.getValue()))) {
				numeric = numerics.get(entry.getValue());
			}
			if (tags.length()>0) {
				tags.append("@");
			}
			tags.append(entry.getValue());
			tags.append(",");
			tags.append(entry.getKey());
			if (numeric) {
				tags.append(",");
				tags.append(1);
			}
		}
		new XMLElem(TAGS,tags,compressed);
		rootElement.setParent(new XMLElem(DATA,null,compressed));
		StringBuilder c = compressed.toString(false,tagNames,numerics);
		c.insert(0, createHeader());
		return c;
	}

	/**
	 * Returns a default header for XML files
	 * 
	 * @return A default header for XML files
	 */
	public static String createHeader() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	/**
	 * Returns a header for a specific XML stylesheet (XSL)
	 * 
	 * @param url The URL of the styleshet
	 * @return A header for a specific XML stylesheet
	 */
	public static String createXSLHeader(String url) {
		return "<?xml-stylesheet type=\"text/xsl\" href=\"" + url + "\"?>";
	}

	/**
	 * @param compressNumerics the compressNumerics to set
	 */
	public void setCompressNumerics(boolean compressNumerics) {
		this.compressNumerics = compressNumerics;
	}

}
