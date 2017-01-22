package nl.zeesoft.zodb.file.xml;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;

/**
 * This class is used to manage the configuration XML file parsing
 */
public final class XMLConfig extends Locker {
	private static XMLConfig					config				= null;
	private DocumentBuilderFactory 				factory				= null; 
	private SortedMap<String,DocumentBuilder>	sourceBuilderMap	= new TreeMap<String,DocumentBuilder>();
	
	private XMLConfig() {
		factory = DocumentBuilderFactory.newInstance();
	}

	public static XMLConfig getInstance() {
		if (config==null) {
			config = new XMLConfig();
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	/**
	 * @return the factory
	 */
	public DocumentBuilderFactory getFactory() {
		return factory;
	}

	/**
	 * @return a new builder
	 */
	public DocumentBuilder getNewBuilder() {
		DocumentBuilder builder	= null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Messenger.getInstance().error(this, "XML document builder configuration error: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
		}
		return builder;
	}

	/**
	 * @return a new builder for a certain source
	 */
	public DocumentBuilder getBuilderForSource(Object source) {
		lockMe(source);
		DocumentBuilder builder	= sourceBuilderMap.get(source.toString());
		if (builder==null) {
			builder = getNewBuilder();
			sourceBuilderMap.put(source.toString(),builder);
		}
		unlockMe(source);
		return builder;
	}

	public void removeBuilderForSource(Object source) {
		lockMe(source);
		String s = source.toString();
		if (sourceBuilderMap.containsKey(s)) {
			sourceBuilderMap.remove(s);
		}
		unlockMe(source);
	}	
}
