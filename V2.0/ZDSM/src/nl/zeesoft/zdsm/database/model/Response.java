package nl.zeesoft.zdsm.database.model;

import nl.zeesoft.zodb.database.DbDataObject;

public class Response extends Log  {
	private String 			method			= "";
	private String 			fullUrl			= "";
	private StringBuilder	requestHeader	= new StringBuilder();
	private StringBuilder	requestBody		= new StringBuilder();
	private String 			code			= "";
	private StringBuilder 	header			= new StringBuilder();
	private StringBuilder 	body			= new StringBuilder();

	private long 			serviceId		= 0;
	private Service			service			= null;
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("method")) {
			setMethod(obj.getPropertyValue("method").toString());
		}
		if (obj.hasPropertyValue("fullUrl")) {
			setFullUrl(obj.getPropertyValue("fullUrl").toString());
		}
		if (obj.hasPropertyValue("requestHeader")) {
			setRequestHeader(obj.getPropertyValue("requestHeader"));
		}
		if (obj.hasPropertyValue("requestBody")) {
			setRequestBody(obj.getPropertyValue("requestBody"));
		}
		if (obj.hasPropertyValue("code")) {
			setCode(obj.getPropertyValue("code").toString());
		}
		if (obj.hasPropertyValue("header")) {
			setHeader(obj.getPropertyValue("header"));
		}
		if (obj.hasPropertyValue("body")) {
			setBody(obj.getPropertyValue("body"));
		}
		if (obj.hasPropertyValue("service") && obj.getLinkValue("service").size()>0) {
			setServiceId(obj.getLinkValue("service").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("method",new StringBuilder(getMethod()));
		r.setPropertyValue("fullUrl",new StringBuilder(getFullUrl()));
		r.setPropertyValue("requestHeader",new StringBuilder(getRequestHeader()));
		r.setPropertyValue("requestBody",new StringBuilder(getRequestBody()));
		r.setPropertyValue("code",new StringBuilder(getCode()));
		r.setPropertyValue("header",new StringBuilder(getHeader()));
		r.setPropertyValue("body",new StringBuilder(getBody()));
		r.setLinkValue("service",getServiceId());
		return r;
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
	 * @return the serviceId
	 */
	public long getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}
	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
		if (service!=null) {
			serviceId = service.getId();
		} else {
			serviceId = 0;
		}
	}

	/**
	 * @return the fullUrl
	 */
	public String getFullUrl() {
		return fullUrl;
	}

	/**
	 * @param fullUrl the fullUrl to set
	 */
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
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
	 * @return the requestHeader
	 */
	public StringBuilder getRequestHeader() {
		return requestHeader;
	}

	/**
	 * @param requestHeader the requestHeader to set
	 */
	public void setRequestHeader(StringBuilder requestHeader) {
		this.requestHeader = requestHeader;
	}

	/**
	 * @return the requestBody
	 */
	public StringBuilder getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(StringBuilder requestBody) {
		this.requestBody = requestBody;
	}
}
