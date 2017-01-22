package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class Service extends Task {
	public static final String	SELF_INDEX					= "SelfIndex";
	public static final String	SELF_AUTHORIZER				= "SelfAuthorizer";
	public static final String	SELF_AUTHORIZATION_MANAGER	= "SelfAuthorizationManager";
	public static final String	SELF_SESSION_AUTHORIZED		= "SelfSessionAuthorized";

	private String 				method 						= "GET";
	private String 				path						= "";
	private StringBuilder		query						= new StringBuilder();
	private StringBuilder 		header						= new StringBuilder();
	private StringBuilder 		body						= new StringBuilder();
	private boolean				timeOut						= true;
	private int					timeOutSeconds				= 3;
	
	private String 				expectedCode				= "200";
	private StringBuilder 		expectedHeader				= new StringBuilder();
	private StringBuilder 		expectedBody				= new StringBuilder();
		
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("method")) {
			setMethod(obj.getPropertyValue("method").toString());
		}
		if (obj.hasPropertyValue("path")) {
			setPath(obj.getPropertyValue("path").toString());
		}
		if (obj.hasPropertyValue("query")) {
			setQuery(obj.getPropertyValue("query"));
		}
		if (obj.hasPropertyValue("header")) {
			setHeader(obj.getPropertyValue("header"));
		}
		if (obj.hasPropertyValue("body")) {
			setBody(obj.getPropertyValue("body"));
		}
		if (obj.hasPropertyValue("timeOut")) {
			setTimeOut(Boolean.parseBoolean(obj.getPropertyValue("timeOut").toString()));
		}
		if (obj.hasPropertyValue("timeOutSeconds")) {
			setTimeOutSeconds(Integer.parseInt(obj.getPropertyValue("timeOutSeconds").toString()));
		}
		if (obj.hasPropertyValue("expectedCode")) {
			setExpectedCode(obj.getPropertyValue("expectedCode").toString());
		}
		if (obj.hasPropertyValue("expectedHeader")) {
			setExpectedHeader(obj.getPropertyValue("expectedHeader"));
		}
		if (obj.hasPropertyValue("expectedBody")) {
			setExpectedBody(obj.getPropertyValue("expectedBody"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("method",new StringBuilder(getMethod()));
		r.setPropertyValue("path",new StringBuilder(getPath()));
		r.setPropertyValue("query",new StringBuilder(getQuery()));
		r.setPropertyValue("header",new StringBuilder(getHeader()));
		r.setPropertyValue("body",new StringBuilder(getBody()));
		r.setPropertyValue("timeOut",new StringBuilder("" + isTimeOut()));
		r.setPropertyValue("timeOutSeconds",new StringBuilder("" + getTimeOutSeconds()));
		r.setPropertyValue("expectedCode",new StringBuilder(getExpectedCode()));
		r.setPropertyValue("expectedHeader",new StringBuilder(getExpectedHeader()));
		r.setPropertyValue("expectedBody",new StringBuilder(getExpectedBody()));
		return r;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the query
	 */
	public StringBuilder getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(StringBuilder query) {
		this.query = query;
	}

	/**
	 * @return the expectedCode
	 */
	public String getExpectedCode() {
		return expectedCode;
	}

	/**
	 * @param expectedCode the expectedCode to set
	 */
	public void setExpectedCode(String expectedCode) {
		this.expectedCode = expectedCode;
	}

	/**
	 * @return the expectedHeader
	 */
	public StringBuilder getExpectedHeader() {
		return expectedHeader;
	}

	/**
	 * @param expectedHeader the expectedHeader to set
	 */
	public void setExpectedHeader(StringBuilder expectedHeader) {
		this.expectedHeader = expectedHeader;
	}

	/**
	 * @return the expectedBody
	 */
	public StringBuilder getExpectedBody() {
		return expectedBody;
	}

	/**
	 * @param expectedBody the expectedBody to set
	 */
	public void setExpectedBody(StringBuilder expectedBody) {
		this.expectedBody = expectedBody;
	}

	/**
	 * @return the header
	 */
	public StringBuilder getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(StringBuilder header) {
		this.header = header;
	}

	/**
	 * @return the body
	 */
	public StringBuilder getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(StringBuilder body) {
		this.body = body;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the timeOutSeconds
	 */
	public int getTimeOutSeconds() {
		return timeOutSeconds;
	}

	/**
	 * @param timeOutSeconds the timeOutSeconds to set
	 */
	public void setTimeOutSeconds(int timeOutSeconds) {
		this.timeOutSeconds = timeOutSeconds;
	}

	/**
	 * @return the timeOut
	 */
	public boolean isTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(boolean timeOut) {
		this.timeOut = timeOut;
	}
}
