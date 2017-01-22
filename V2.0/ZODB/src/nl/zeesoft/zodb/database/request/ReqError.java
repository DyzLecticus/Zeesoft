package nl.zeesoft.zodb.database.request;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class ReqError {
	private String			code		= "";
	private String			message		= "";
	private List<String>	properties	= new ArrayList<String>();

	protected ReqError() {
		// Used by fromXML
	}
	
	protected ReqError(String code,String message) {
		this.code = code;
		this.message = message;
	}

	public ReqError copy() {
		ReqError copy = new ReqError();
		copy.setCode(code);
		copy.setMessage(message);
		copy.getProperties().clear();
		for (String prop: properties) {
			copy.getProperties().add(prop);
		}
		return copy;
	}
	
	protected XMLFile toXML() {
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("error",null,null));
		new XMLElem("code",new StringBuilder(code),file.getRootElement());
		new XMLElem("message",new StringBuilder(message),file.getRootElement());
		if (properties.size()>0) {
			XMLElem propsElem = new XMLElem("properties",null,file.getRootElement());
			for (String prop: properties) {
				new XMLElem("name",new StringBuilder(prop),propsElem);
			}
		}
		return file;
	}

	protected void fromXML(XMLElem rootElem) {
		if (rootElem.getName().equals("error")) {
			for (XMLElem cElem: rootElem.getChildren()) {
				if (cElem.getName().equals("code") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					code = cElem.getValue().toString();
				}
				if (cElem.getName().equals("message") && cElem.getValue()!=null && cElem.getValue().length()>0) {
					message = cElem.getValue().toString();
				}
				if (cElem.getName().equals("properties")) {
					for (XMLElem propElem: cElem.getChildren()) {
						if (propElem.getValue()!=null && propElem.getValue().length()>0) {
							properties.add(propElem.getValue().toString());
						}
					}
				}
			}
		}
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the properties
	 */
	public List<String> getProperties() {
		return properties;
	}
}
