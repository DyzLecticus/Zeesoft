package nl.zeesoft.zodb.database.server.protocol;

import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class PtcServerResponse {
	private String		message				= "";
	private String		redirect			= "";
	private String		value				= "";

	protected PtcServerResponse(String message) {
		this.message = message;
	}

	protected PtcServerResponse(String message, String redirect) {
		this.message = message;
		this.redirect = redirect;
	}

	protected PtcServerResponse(String message, String redirect, String value) {
		this.message = message;
		this.redirect = redirect;
		this.value = value;
	}

	protected StringBuilder toStringBuilder() {
		StringBuilder r = null;
		XMLFile file = new XMLFile();
		file.setRootElement(new XMLElem("response",null,null));
		if (message!=null && message.length()>0) {
			XMLElem elem = new XMLElem("message",new StringBuilder(message),file.getRootElement());
			elem.setCData(true);
		}
		if (redirect!=null && redirect.length()>0) {
			XMLElem elem = new XMLElem("redirect",new StringBuilder(redirect),file.getRootElement());
			elem.setCData(true);
		}
		if (value!=null && value.length()>0) {
			XMLElem elem = new XMLElem("value",new StringBuilder(value),file.getRootElement());
			elem.setCData(true);
		}
		r = file.toStringBuilder();
		file.cleanUp();
		return r;
	}

	/**
	 * @param message the message to set
	 */
	protected void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * @param redirect the redirect to set
	 */
	protected void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	/**
	 * @param value the value to set
	 */
	protected void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.toStringBuilder().toString();
	}
}
