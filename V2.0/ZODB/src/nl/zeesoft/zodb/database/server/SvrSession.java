package nl.zeesoft.zodb.database.server;

import java.util.Date;

public class SvrSession {
	private long 				id 						= 0;
	private String 				remoteSocketAddress 	= "";
	private Date				started					= new Date();
	private Date				latestActivity			= new Date();
	private boolean				authorized				= false;
	private String				authorizeRedirectUrl	= "";
	private int					authorizeAttempts		= 0;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	protected void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the remoteSocketAddress
	 */
	protected String getRemoteSocketAddress() {
		return remoteSocketAddress;
	}
	
	/**
	 * @param remoteSocketAddress the remoteSocketAddress to set
	 */
	protected void setRemoteSocketAddress(String remoteSocketAddress) {
		this.remoteSocketAddress = remoteSocketAddress;
	}
	
	/**
	 * @return the started
	 */
	protected Date getStarted() {
		return started;
	}
	
	/**
	 * @param started the started to set
	 */
	protected void setStarted(Date started) {
		this.started = started;
	}

	/**
	 * @return the latestActivity
	 */
	protected Date getLatestActivity() {
		return latestActivity;
	}

	/**
	 * @param latestActivity the latestActivity to set
	 */
	protected void setLatestActivity(Date latestActivity) {
		this.latestActivity = latestActivity;
	}

	/**
	 * @return the authorized
	 */
	public boolean isAuthorized() {
		return authorized;
	}

	/**
	 * @param authorized the authorized to set
	 */
	public void setAuthorized(boolean authorized) {
		if (authorized) {
			authorizeAttempts=0;
		}
		this.authorized = authorized;
	}

	/**
	 * @return the authorizeRedirectUrl
	 */
	public String getAuthorizeRedirectUrl() {
		return authorizeRedirectUrl;
	}

	/**
	 * @param authorizeRedirectUrl the authorizeRedirectUrl to set
	 */
	public void setAuthorizeRedirectUrl(String authorizeRedirectUrl) {
		if (authorizeRedirectUrl=="/") {
			authorizeRedirectUrl = "/" + SvrHTTPResourceFactory.INDEX_HTML;
		}
		this.authorizeRedirectUrl = authorizeRedirectUrl;
	}

	/**
	 * @return the authorizeAttempts
	 */
	public int getAuthorizeAttempts() {
		return authorizeAttempts;
	}

	/**
	 * @param authorizeAttempts the authorizeAttempts to set
	 */
	public void setAuthorizeAttempts(int authorizeAttempts) {
		this.authorizeAttempts = authorizeAttempts;
	}
}
