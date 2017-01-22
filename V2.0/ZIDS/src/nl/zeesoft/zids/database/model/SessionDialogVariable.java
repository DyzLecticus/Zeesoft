package nl.zeesoft.zids.database.model;

import nl.zeesoft.zids.json.JsElem;
import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class SessionDialogVariable extends HlpObject {
	private String							code					= "";
	private StringBuilder					value					= new StringBuilder();
	
	private long							sessionId				= 0; 
	private Session							session					= null; 

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("code")) {
			setCode(obj.getPropertyValue("code").toString());
		}
		if (obj.hasPropertyValue("value")) {
			setValue(obj.getPropertyValue("value"));
		}
		if (obj.hasPropertyValue("session") && obj.getLinkValue("session")!=null && obj.getLinkValue("session").size()>0) {
			setSessionId(obj.getLinkValue("session").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("code",new StringBuilder(getCode()));
		r.setPropertyValue("value",getValue());
		r.setLinkValue("session",getSessionId());
		return r;
	}

	public JsFile toJSON() {
		JsFile f = new JsFile();
		f.rootElement = new JsElem();
		JsElem c = null;
		
		c = new JsElem();
		c.name = "id";
		f.rootElement.children.add(c);
		c.value = new StringBuilder("" + getId());
		
		c = new JsElem();
		c.name = "code";
		f.rootElement.children.add(c);
		c.value = new StringBuilder(getCode());
		c.cData = true;
		
		c = new JsElem();
		c.name = "value";
		f.rootElement.children.add(c);
		c.value = getValue();
		c.cData = true;

		return f;
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
	 * @return the sessionId
	 */
	public long getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		if (session==null) {
			sessionId = 0;
		} else {
			sessionId = session.getId();
		}
		this.session = session;
	}

	/**
	 * @return the value
	 */
	public StringBuilder getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(StringBuilder value) {
		this.value = value;
	}
}
