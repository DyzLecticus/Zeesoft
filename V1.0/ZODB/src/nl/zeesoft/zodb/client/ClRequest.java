package nl.zeesoft.zodb.client;

import java.util.Date;

import nl.zeesoft.zodb.database.query.QryObjectList;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;

public class ClRequest extends EvtEventPublisher {
	public static String 	RECEIVED_REQUEST_RESPONSE 	= "RECEIVED_REQUEST_RESPONSE";
	
	private long			id							= 0;
	private String			actionRequest 				= "";
	private String			actionResponse 				= "";
	private QryObjectList	queryRequest				= null;
	private Boolean			error 						= false;
	private Date			sendTime					= null;
	
	protected ClRequest() {
		
	}
	
	public static XMLFile toXml(ClRequest request) {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("request",null,null));
		StringBuffer sb = new StringBuffer();
		sb.append(request.getId());
		new XMLElem("id",sb,f.getRootElement());
		if (request.getQueryRequest()!=null) {
			XMLFile qryList = QryObjectList.toXml(request.getQueryRequest());
			qryList.getRootElement().setParent(f.getRootElement());
		} else {
			new XMLElem("request",new StringBuffer(request.getActionRequest()),f.getRootElement());
		}
		if (!request.getActionResponse().equals("")) {
			new XMLElem("response",new StringBuffer(request.getActionResponse()),f.getRootElement());
		}
		if (request.getError()) {
			sb = new StringBuffer();
			sb.append(request.getError());
			new XMLElem("error",sb,f.getRootElement());
		}
		return f;
	}
	
	public static ClRequest fromXml(XMLFile f) {
		ClRequest r = null;
		if (f.getRootElement().getName().equals("request")) {
			r = new ClRequest();
			for (XMLElem rElem: f.getRootElement().getChildren()) {
				if (rElem.getName().equals("id")) {
					r.setId(Long.parseLong(rElem.getValue().toString()));
				} else if (rElem.getName().equals("request")) {
					r.setActionRequest(rElem.getValue().toString());
				} else if (rElem.getName().equals("response")) {
					r.setActionResponse(rElem.getValue().toString());
				} else if (rElem.getName().equals("error")) {
					r.setError(Boolean.parseBoolean(rElem.getValue().toString()));
				} else {
					XMLFile fl = new XMLFile();
					fl.setRootElement(rElem);
					QryObjectList l = QryObjectList.fromXml(fl);
					r.setQueryRequest(l);
					fl.cleanUp();
				}
			}
		}
		return r;
	}

	@Override
	protected void publishEvent(EvtEvent e) {
		super.publishEvent(e);
	}

	/**
	 * @return the actionRequest
	 */
	public String getActionRequest() {
		return actionRequest;
	}
	
	/**
	 * @param actionRequest the actionRequest to set
	 */
	public void setActionRequest(String actionRequest) {
		this.actionRequest = actionRequest;
	}
	
	/**
	 * @return the queryRequest
	 */
	public QryObjectList getQueryRequest() {
		return queryRequest;
	}
	
	/**
	 * @param queryRequest the queryRequest to set
	 */
	public void setQueryRequest(QryObjectList queryRequest) {
		this.queryRequest = queryRequest;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the actionResponse
	 */
	public String getActionResponse() {
		return actionResponse;
	}

	/**
	 * @param actionResponse the actionResponse to set
	 */
	public void setActionResponse(String actionResponse) {
		this.actionResponse = actionResponse;
	}

	/**
	 * @return the error
	 */
	public Boolean getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Boolean error) {
		this.error = error;
	}

	/**
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
}
