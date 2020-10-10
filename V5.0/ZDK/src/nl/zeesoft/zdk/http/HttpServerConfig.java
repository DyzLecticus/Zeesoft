package nl.zeesoft.zdk.http;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

public class HttpServerConfig {
	private Logger		logger				= new Logger();
	
	private int			port				= 8080;
	private int			maxConnections		= 1000;
	private String		filePath			= "http/";
	private String		indexFileName		= "index.html";
	
	private boolean		allowGet			= true;
	private boolean		allowHead			= true;
	private boolean		allowPost			= false;
	private boolean		allowPut			= false;
	private boolean		allowDelete			= false;
	private boolean		allowConnect		= false;

	private boolean		debugLogHeaders		= false;

	public HttpServerConfig() {
		
	}

	public HttpServerConfig(Logger logger) {
		this.logger = logger;
	}
	
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getIndexFileName() {
		return indexFileName;
	}

	public void setIndexFileName(String indexFileName) {
		this.indexFileName = indexFileName;
	}
	
	public void setAllowAll() {
		this.allowGet = true;
		this.allowHead = true;
		this.allowPost = true;
		this.allowPut = true;
		this.allowDelete = true;
		this.allowConnect = true;
	}

	public boolean isAllowGet() {
		return allowGet;
	}

	public void setAllowGet(boolean allowGet) {
		this.allowGet = allowGet;
	}

	public boolean isAllowHead() {
		return allowHead;
	}

	public void setAllowHead(boolean allowHead) {
		this.allowHead = allowHead;
	}

	public boolean isAllowPost() {
		return allowPost;
	}

	public void setAllowPost(boolean allowPost) {
		this.allowPost = allowPost;
	}

	public boolean isAllowPut() {
		return allowPut;
	}

	public void setAllowPut(boolean allowPut) {
		this.allowPut = allowPut;
	}

	public boolean isAllowDelete() {
		return allowDelete;
	}

	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public boolean isAllowConnect() {
		return allowConnect;
	}

	public void setAllowConnect(boolean allowConnect) {
		this.allowConnect = allowConnect;
	}

	public boolean isDebugLogHeaders() {
		return debugLogHeaders;
	}

	public void setDebugLogHeaders(boolean debugLogHeaders) {
		this.debugLogHeaders = debugLogHeaders;
	}

	protected HttpServerConfig copy() {
		HttpServerConfig r = (HttpServerConfig) Instantiator.getNewClassInstance(this.getClass());
		r.setLogger(getLogger());
		r.setPort(getPort());
		r.setMaxConnections(getMaxConnections());
		r.setFilePath(getFilePath());
		r.setIndexFileName(getIndexFileName());
		r.setAllowGet(isAllowGet());
		r.setAllowHead(isAllowHead());
		r.setAllowPost(isAllowPost());
		r.setAllowPut(isAllowPut());
		r.setAllowDelete(isAllowDelete());
		r.setDebugLogHeaders(isDebugLogHeaders());
		return r;
	}
	
	protected HttpRequestHandler getNewHttpRequestHandler() {
		return new HttpRequestHandler(this);
	}
	
	protected void debug(Object source, Str message) {
		logger.debug(source, message);
	}
	
	protected void error(Object source, Str message) {
		logger.error(source, message);
	}
	
	protected void error(Object source, Str message, Exception ex) {
		logger.error(source, message, ex);
	}
}
