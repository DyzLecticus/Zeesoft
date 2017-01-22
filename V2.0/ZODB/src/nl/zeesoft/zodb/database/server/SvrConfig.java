package nl.zeesoft.zodb.database.server;

import java.io.File;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public final class SvrConfig {
	private static final String		INFO						=
		"If you want to reset the authorizePassword to the default value 'admin', empty the value it before starting the database.";

	private static SvrConfig		config						= null;

	private	SvrInterface			server						= null;
	
	private String 					serverClassName 			= SvrServer.class.getName();
	private int 					port 						= 4321;
	private boolean 				debugSockets				= false;
	private boolean 				debugHTTPHeaders			= false;
	private boolean 				generateHTTPResources		= false;
	private int 					maxSocketWorkers			= 1000;
	private int 					getRequestLimit				= 1000;
	
	// Database authorization
	private int 					sessionTimeOutSeconds		= 600;
	private boolean					authorizeGetRequests		= true;
	private boolean					authorizePostRequests		= true;
	private StringBuilder			authorizePassword			= new StringBuilder();

	public static SvrConfig getInstance() {
		if (config==null) {
			config = new SvrConfig();
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public void serialize() {
		if (authorizePassword.length()==0) {
			authorizePassword = DbConfig.getInstance().encodePassword(new StringBuilder("admin"));
		}
		XMLFile f = toXml();
		f.writeFile(getFullFileName(), f.toStringReadFormat());
		f.cleanUp();
	}
	
	protected void unserialize() {
		XMLFile f = new XMLFile();
		f.parseFile(getFullFileName());
		fromXml(f);
		f.cleanUp();
		// Trigger password reset to admin
		if (authorizePassword.length()==0) {
			serialize();
		}
	}

	protected String getFullFileName() {
		return DbConfig.getInstance().getConfDir() + "SvrConfig.xml";
	}

	public boolean fileExists() {
		File configFile = new File(getFullFileName());
		return configFile.exists();
	}

	public StringBuilder toStringBuilder() {
		StringBuilder r = null;
		XMLFile f = toXml();
		r = f.toStringBuilder();
		f.cleanUp();
		return r;
	}

	private XMLFile toXml() {
		XMLFile f = new XMLFile();
		f.setRootElement(new XMLElem("configuration",null,null));
		new XMLElem("INFO",new StringBuilder(INFO),f.getRootElement());
		new XMLElem("serverClassName",new StringBuilder(serverClassName),f.getRootElement());

		StringBuilder sb = null;
		
		sb = new StringBuilder();
		sb.append(port);
		new XMLElem("port",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(debugSockets);
		new XMLElem("debugSockets",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(debugHTTPHeaders);
		new XMLElem("debugHTTPHeaders",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(generateHTTPResources);
		new XMLElem("generateHTTPResources",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(maxSocketWorkers);
		new XMLElem("maxSocketWorkers",sb,f.getRootElement());

		sb = new StringBuilder();
		sb.append(getRequestLimit);
		new XMLElem("getRequestLimit",sb,f.getRootElement());
		
		sb = new StringBuilder();
		sb.append(sessionTimeOutSeconds);
		new XMLElem("sessionTimeOutSeconds",sb,f.getRootElement());

		new XMLElem("authorizePassword",authorizePassword,f.getRootElement());
		
		sb = new StringBuilder();
		sb.append(authorizeGetRequests);
		new XMLElem("authorizeGetRequests",sb,f.getRootElement());
		
		sb = new StringBuilder();
		sb.append(authorizePostRequests);
		new XMLElem("authorizePostRequests",sb,f.getRootElement());
		
		return f;
	}

	private void fromXml(XMLFile f) {
		if (f.getRootElement()!=null && f.getRootElement().getName().equals("configuration")) {
			for (XMLElem v: f.getRootElement().getChildren()) {
				if (v.getName().equals("serverClassName")) {
					setServerClassName(v.getValue().toString());
				}
				if (v.getName().equals("port")) {
					setPort(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals("debugSockets")) {
					debugSockets = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("debugHTTPHeaders")) {
					debugHTTPHeaders = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("generateHTTPResources")) {
					generateHTTPResources = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("maxSocketWorkers")) {
					setMaxSocketWorkers(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals("getRequestLimit")) {
					setGetRequestLimit(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals("sessionTimeOutSeconds")) {
					setSessionTimeOutSeconds(Integer.parseInt(v.getValue().toString()));
				}
				if (v.getName().equals("authorizePassword")) {
					authorizePassword = v.getValue();
				}
				if (v.getName().equals("authorizeGetRequests")) {
					authorizeGetRequests = Boolean.parseBoolean(v.getValue().toString());
				}
				if (v.getName().equals("authorizePostRequests")) {
					authorizePostRequests = Boolean.parseBoolean(v.getValue().toString());
				}
			}
		}
	}
	
	public String getDataDir() {
		return DbConfig.getInstance().getDataDir() + "httpd/";
	}

	public SvrInterface getServer() {
		if (server==null) {
			Messenger.getInstance().debug(this, "Server class: " + serverClassName);
			server = (SvrInterface) Generic.instanceForName(serverClassName);
			server.initialize();
		}
		return server;
	}

	/**
	 * @return the serverClassName
	 */
	public String getServerClassName() {
		return serverClassName;
	}

	/**
	 * @param serverClassName the serverClassName to set
	 */
	public void setServerClassName(String serverClassName) {
		if (serverClassName.length()==0) {
			Messenger.getInstance().error(this,"serverClassName is mandatory");
			return;
		} 
		Object testObject = Generic.testInstanceForName(serverClassName);
		if (testObject==null) {
			Messenger.getInstance().error(this,"Unable to intsantiate class for serverClassName: " + serverClassName);
			return;
		} else if (!Generic.instanceOfImplements(testObject.getClass(),SvrInterface.class.getName())) {
			Messenger.getInstance().error(this,"serverClassName class must implement " + SvrInterface.class.getName());
			return;
		}
		this.serverClassName = serverClassName;
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		if (port<256) {
			port = 256;
		} else if (port>99999) {
			port = 99999;
		}
		this.port = port;
	}

	/**
	 * @return the debugSockets
	 */
	public boolean isDebugSockets() {
		return debugSockets && SvrController.getInstance().isDebug();
	}

	/**
	 * @param debugSockets the debugSockets to set
	 */
	public void setDebugSockets(boolean debugSockets) {
		this.debugSockets = debugSockets;
	}

	/**
	 * @return the debugHTTPHeaders
	 */
	public boolean isDebugHTTPHeaders() {
		return debugHTTPHeaders && SvrController.getInstance().isDebug();
	}

	/**
	 * @param debugHTTPHeaders the debugHTTPHeaders to set
	 */
	public void setDebugHTTPHeaders(boolean debugHTTPHeaders) {
		this.debugHTTPHeaders = debugHTTPHeaders;
	}

	/**
	 * @return the generateHTTPResources
	 */
	public boolean isGenerateHTTPResources() {
		return generateHTTPResources;
	}

	/**
	 * @param generateHTTPResources the generateHTTPResources to set
	 */
	public void setGenerateHTTPResources(boolean generateHTTPResources) {
		this.generateHTTPResources = generateHTTPResources;
	}

	/**
	 * @return the maxSocketWorkers
	 */
	public int getMaxSocketWorkers() {
		return maxSocketWorkers;
	}
	
	/**
	 * @param maxSocketWorkers the maxSocketWorkers to set
	 */
	public void setMaxSocketWorkers(int maxSocketWorkers) {
		if (maxSocketWorkers<DbConfig.getInstance().getMaxRequestWorkers()) {
			maxSocketWorkers = DbConfig.getInstance().getMaxRequestWorkers();
		} else if (maxSocketWorkers>99999) {
			maxSocketWorkers = 99999;
		}
		this.maxSocketWorkers = maxSocketWorkers;
	}

	/**
	 * @return the sessionTimeOutSeconds
	 */
	public int getSessionTimeOutSeconds() {
		return sessionTimeOutSeconds;
	}

	/**
	 * @param sessionTimeOutSeconds the sessionTimeOutSeconds to set
	 */
	public void setSessionTimeOutSeconds(int sessionTimeOutSeconds) {
		if (sessionTimeOutSeconds<1) {
			sessionTimeOutSeconds = 1;
		} else if (sessionTimeOutSeconds>86400) {
			sessionTimeOutSeconds = 86400;
		}
		this.sessionTimeOutSeconds = sessionTimeOutSeconds;
	}

	/**
	 * @return the getRequestLimit
	 */
	public int getGetRequestLimit() {
		return getRequestLimit;
	}

	/**
	 * @param getRequestLimit the getRequestLimit to set
	 */
	public void setGetRequestLimit(int getRequestLimit) {
		if (getRequestLimit<100) {
			getRequestLimit = 100;
		} else if (getRequestLimit>9999) {
			getRequestLimit = 9999;
		}
		this.getRequestLimit = getRequestLimit;
	}

	/**
	 * @return true if (authorizeGetRequests || authorizePostRequests)
	 */
	public boolean isAuthorizeGetOrPostRequests() {
		return (authorizeGetRequests || authorizePostRequests);
	}
	
	/**
	 * @return authorizeGetRequests
	 */
	public boolean isAuthorizeGetRequests() {
		return authorizeGetRequests;
	}

	/**
	 * @param authorizeGetRequests the authorizeGetRequests to set
	 */
	public void setAuthorizeGetRequests(boolean authorizeGetRequests) {
		this.authorizeGetRequests = authorizeGetRequests;
	}

	/**
	 * @return authorizePostRequests
	 */
	public boolean isAuthorizePostRequests() {
		return authorizePostRequests;
	}

	/**
	 * @param authorizePostRequests the authorizePostRequests to set
	 */
	public void setAuthorizePostRequests(boolean authorizePostRequests) {
		this.authorizePostRequests = authorizePostRequests;
	}

	/**
	 * @return the authorizePassword
	 */
	public StringBuilder getAuthorizePassword() {
		return authorizePassword;
	}

	/**
	 * @param authorizePassword the authorizePassword to set
	 */
	public void setAuthorizePassword(StringBuilder authorizePassword) {
		this.authorizePassword = authorizePassword;
	}
}
