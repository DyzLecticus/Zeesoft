package nl.zeesoft.zodb.client;

import java.net.Socket;
import java.util.Date;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Session;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.impl.DbUser;
import nl.zeesoft.zodb.protocol.PtcClient;
import nl.zeesoft.zodb.protocol.PtcObject;

public class ClSession extends Session {	
	private PtcClient 		protocol 			= null;
	private ClRequestQueue	requestQueue 		= null;
	private long			userId				= 0;
	private String			userName			= "";
	private boolean 		userAdmin			= false;
	private int				userLevel			= (DbUser.USER_LEVEL_MAX + 1);

	private boolean			closing				= false;

	public ClSession(Socket sock, PtcClient prot) {
		super(sock, 0);
		protocol = prot;
		requestQueue = new ClRequestQueue(this);
	}
	
	public void sendRequest(ClRequest r) {
		StringBuffer output = new StringBuffer();
		if ((r.getQueryRequest()!=null) && (!DbConfig.getInstance().getXmlCompression().equals(DbConfig.XML_COMPRESSION_NONE))) {
			if (DbConfig.getInstance().getXmlCompression().equals(DbConfig.XML_COMPRESSION_FULL)) {
				XMLFile rx = ClRequest.toXml(r);
				rx.setCompressNumerics(true);
				output = rx.toStringCompressed();
				rx.cleanUp();
			} else if (DbConfig.getInstance().getXmlCompression().equals(DbConfig.XML_COMPRESSION_TAGS)) {
				XMLFile rx = ClRequest.toXml(r);
				rx.setCompressNumerics(false);
				output = rx.toStringCompressed();
				rx.cleanUp();
			}
		} else {
			XMLFile rx = ClRequest.toXml(r);
			output = rx.toStringBuffer();
			rx.cleanUp();
		}
		r.setSendTime(new Date());
		writeOutput(output,getSessionId());	
	}
	
	/**
	 * Returns a new client request to authorise the session.
	 * 
	 * @param userName The name of the user
	 * @param userPassword The encoded password
	 * @return A new client request to authorise the session.
	 */
	public ClRequest getNewAuthorizationRequest(String userName, StringBuffer userPassword) {
		ClRequest r = requestQueue.getNewRequest(this);
		StringBuffer userNamePassword = new StringBuffer();
		userNamePassword.append(userName);
		userNamePassword.append(Generic.SEP_STR);
		userNamePassword.append(userPassword);
		if (!DbConfig.getInstance().isEncrypt()) {
			userNamePassword = Generic.encodeKey(userNamePassword, DbConfig.getInstance().getEncryptionKey(), getSessionId());
		}
		r.setActionRequest(PtcObject.AUTHORIZE_SESSION + Generic.SEP_STR + userNamePassword);
		return r;
	}

	/**
	 * Returns a new client request to stop the session.
	 * @return A new client request to stop the session.
	 */
	public ClRequest getNewStopSessionRequest() {
		ClRequest r = requestQueue.getNewRequest(this);
		r.setActionRequest(PtcObject.STOP_SESSION);
		return r;
	}

	@Override
	protected boolean open() {
		return super.open();
    }

	@Override
	protected void close() {
		closing = true;
		writeOutput(new StringBuffer(PtcObject.STOP_SESSION),getSessionId());
		super.close();
	}
	
	@Override
    protected void readAndProcessInput() {
    	StringBuffer input = readInput(getSessionId());
    	if (!closing) {
	    	if (
	    		(getSocket()!=null) && (getSocket().isConnected()) && 
	    		(input!=null) && (input.length()>0)
	    		) {
				StringBuffer output = protocol.processInputAndReturnOutput(input);
				writeOutput(output,getSessionId());
	    	} else if ((getSocket()!=null) && (!getSocket().isConnected())) {
	    		ClSessionManager.getInstance().stopSession(getSessionId());
	    	}
    	}
    }

	@Override
	public long getSessionId() {
		return super.getSessionId();
	}
	
	@Override
	public void setSessionId(long sessionId) {
		super.setSessionId(sessionId);
	}

	/**
	 * @return the requestQueue
	 */
	public ClRequestQueue getRequestQueue() {
		return requestQueue;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userAdmin
	 */
	public boolean isUserAdmin() {
		return userAdmin;
	}

	/**
	 * @param userAdmin the userAdmin to set
	 */
	public void setUserAdmin(boolean userAdmin) {
		this.userAdmin = userAdmin;
	}

	/**
	 * @return the userLevel
	 */
	public int getUserLevel() {
		return userLevel;
	}

	/**
	 * @param userLevel the userLevel to set
	 */
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

}
