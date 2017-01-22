package nl.zeesoft.zodb.client;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.XMLElem;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.ZODBModel;

public final class ClConfig {
	public final static	String	PROPERTY_HOST_NAME_OR_IP	= "hostNameOrIp";
	public final static	String	LABEL_HOST_NAME_OR_IP		= "Server name or IP";
	public final static	String	HELP_HOST_NAME_OR_IP		= "The name or IP address of the server";
	
	private static ClConfig		config						= null;
	
	private String				hostNameOrIp				= "localhost";
	private String				userName					= ZODBModel.INITIAL_ADMIN_USERNAME;
	private StringBuffer		userPassword				= DbConfig.getInstance().encodePassword(new StringBuffer(ZODBModel.INITIAL_ADMIN_PASSWORD));
	private int					maxRequestQueueSize			= 100;

	private ClConfig() {
		// Singleton
	}

	public static ClConfig getInstance() {
		if (config==null) {
			config = new ClConfig();
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	public void serialize() {
		XMLFile f = toXml();
		f.writeFile(DbConfig.getInstance().getFullDataDir() + "ClConfig.xml", f.toStringReadFormat());
		f.cleanUp();
	}
	
	public void unserialize() {
		XMLFile f = new XMLFile();
		f.parseFile(DbConfig.getInstance().getFullDataDir() + "ClConfig.xml");
		fromXml(f);
		f.cleanUp();
	}

	public XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("config",null,null));
		StringBuffer sb = new StringBuffer();
		sb.append(hostNameOrIp);
		new XMLElem(PROPERTY_HOST_NAME_OR_IP,sb,f.getRootElement());
		new XMLElem("userName",new StringBuffer(userName),f.getRootElement());
		new XMLElem("userPassword",userPassword,f.getRootElement());
		sb = new StringBuffer();
		sb.append(maxRequestQueueSize);
		new XMLElem("maxRequestQueueSize",sb,f.getRootElement());
		return f;
	}

	private void fromXml(XMLFile f) {
		if ((f.getRootElement()!=null) && (f.getRootElement().getName().equals("config"))) {
			for (XMLElem v: f.getRootElement().getChildren()) {
				if (v.getName().equals(PROPERTY_HOST_NAME_OR_IP)) {
					hostNameOrIp = v.getValue().toString();
				}
				if (v.getName().equals("userName")) {
					userName = v.getValue().toString();
				}
				if (v.getName().equals("userPassword")) {
					userPassword = v.getValue();
				}
				if (v.getName().equals("maxRequestQueueSize")) {
					maxRequestQueueSize = Integer.parseInt(v.getValue().toString());
				}
			}
		}
	}

	/**
	 * @return the hostNameOrIp
	 */
	public String getHostNameOrIp() {
		return hostNameOrIp;
	}

	/**
	 * @param hostNameOrIp the hostNameOrIp to set
	 */
	public void setHostNameOrIp(String hostNameOrIp) {
		if (!hostNameOrIp.equals("")) {
			this.hostNameOrIp = hostNameOrIp;
		}
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
	 * @return the userPassword
	 */
	public StringBuffer getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(StringBuffer userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the maxRequestQueueSize
	 */
	public int getMaxRequestQueueSize() {
		return maxRequestQueueSize;
	}

	/**
	 * @param maxRequestQueueSize the maxRequestQueueSize to set
	 */
	public void setMaxRequestQueueSize(int maxRequestQueueSize) {
		if (maxRequestQueueSize<2) {
			maxRequestQueueSize=2;
		}
		this.maxRequestQueueSize = maxRequestQueueSize;
	}
	

}
