package nl.zeesoft.zodb.database.query;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;

public class QryError {
	private String				code					= "";
	private List<String>		properties				= new ArrayList<String>();
	private String				message					= "";
	private List<String>		values					= new ArrayList<String>();
	
	public QryError(String code, String property, String message) {
		this.code = code;
		if (property.length()>0) {
			this.properties.add(property);
		}
		this.message = message;
	}
	
	public static XMLFile toXml(QryError error) {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("error",null,null));
		new XMLElem("code",new StringBuffer(error.getCode()),f.getRootElement());
		new XMLElem("message",new StringBuffer(error.getMessage()),f.getRootElement());
		if (error.getProperties().size()>0) {
			XMLElem propertiesElem = new XMLElem("properties",null,f.getRootElement());
			for (String prop: error.getProperties()) {
				new XMLElem("property",new StringBuffer(prop),propertiesElem);
			}
		}
		if (error.getValues().size()>0) {
			XMLElem valuesElem = new XMLElem("values",null,f.getRootElement());
			for (String val: error.getValues()) {
				new XMLElem("value",new StringBuffer(val),valuesElem);
			}
		}
		return f;
	}

	public static QryError fromXml(XMLFile f) {
		QryError error = null;
		String code = "";
		String property = "";
		String message = "";
		List<String> properties = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		for (XMLElem cElem: f.getRootElement().getChildren()) {
			if (cElem.getName().equals("code")) {
				code = cElem.getValue().toString();
			}
			if (cElem.getName().equals("message")) {
				message = cElem.getValue().toString();
			}
			if (cElem.getName().equals("properties")) {
				for (XMLElem pElem: cElem.getChildren()) {
					if (property.length()==0) {
						property = pElem.getValue().toString();
					} else {
						properties.add(pElem.getValue().toString());
					}
				}
			}
			if (cElem.getName().equals("values")) {
				for (XMLElem vElem: cElem.getChildren()) {
					values.add(vElem.getValue().toString());
				}
			}
		}
		error = new QryError(code,property,message);
		for (String prop: properties) {
			error.getProperties().add(prop);
		}
		for (String val: values) {
			error.getValues().add(val);
		}
		return error;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the properties
	 */
	public List<String> getProperties() {
		return properties;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		String msg = message;
		for (int i = (values.size() - 1); i >= 0; i--) {
			String replace = "@" + (i + 1);
			msg = msg.replace(replace, values.get(i));
		}
		return msg;
	}

	/**
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}	
}
