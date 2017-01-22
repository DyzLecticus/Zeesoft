package nl.zeesoft.zodb.database.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.gui.GuiController;
import nl.zeesoft.zodb.database.server.protocol.PtcServer;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.HTMLFile;

/**
 * This is the ZODB default server.
 * To customize, extend this class and refer to it in the database configuration.
 */
public class SvrServer implements SvrInterface, EvtEventSubscriber {
	private static final int				MAX_SESSIONS				= 9999;
	
	private ServerSocket 					serverSocket 				= null;
	private SvrServerAcceptWorker			serverAcceptWorker			= null;
	private SvrServerSessionWorker			serverSessionWorker			= null;

	private List<SvrSocketWorker>			socketWorkers				= new ArrayList<SvrSocketWorker>();
	private Object							socketWorkersLockedBy		= null;

	private long							uid							= 1;
	private List<SvrSession>				sessions					= new ArrayList<SvrSession>();
	private Object							sessionsLockedBy			= null;
	
	private String							encryptionKey				= "";	

	private SortedMap<String,StringBuilder> cachedHTTPResources			= new TreeMap<String,StringBuilder>();
	private SortedMap<String,byte[]> 		cachedHTTPImageResources	= new TreeMap<String,byte[]>();
	
	private String							indexHTML					= SvrHTTPResourceFactory.INDEX_HTML;
	private boolean							firstOpen					= true;

	private boolean							openAfterUpdate				= false;
	
	private SvrHTTPResourceFactory			httpResourceFactory			= null;
	
	@Override
	public void initialize() {
		DbController.getInstance().addSubscriber(this);
	}

	@Override
	public void handleEvent(EvtEvent e) {
		if (e.getType().equals(DbController.DB_UPDATING_MODEL)) {
			openAfterUpdate = true;
			close();
		} else if (e.getType().equals(DbController.DB_UPDATED_MODEL)) {
			if (openAfterUpdate) {
				openAfterUpdate = false;
				if (!SvrConfig.getInstance().isGenerateHTTPResources()) {
					generateHTTPResources();
				}
				open();
			} else {
				generateHTTPResources();
			}
		}
	}
		
	@Override
	public boolean open() {
		GuiController.getInstance().setProgressFrameTitle("Starting server ...");

		boolean startBrowser = false;
		boolean generate = SvrConfig.getInstance().isGenerateHTTPResources();
		if (firstOpen) {
			firstOpen = false;
			if (SvrController.getInstance().isJustInstalled()) {
				indexHTML = SvrHTTPResourceFactory.INSTALLED_HTML;
				startBrowser = true;
				generate = true;
			}
		}
		
		if (generate) {
			generateHTTPResources();
		}

		int todo = 3;
		List<String> resources = getPreloadHTTPResources();
		todo += resources.size();

		GuiController.getInstance().setProgressFrameTodo(todo);

		Messenger.getInstance().debug(this, "Opening server on port: " + getPort());
		boolean opened = false;
		try {
			serverSocket = new ServerSocket(getPort());
			encryptionKey = Generic.generateNewKey(128);
			GuiController.getInstance().incrementProgressFrameDone();
			preloadHTTPResources(resources);
			serverAcceptWorker = new SvrServerAcceptWorker(this);
			serverAcceptWorker.start();
			GuiController.getInstance().incrementProgressFrameDone();
			serverSessionWorker = new SvrServerSessionWorker(this);
			serverSessionWorker.start();
			GuiController.getInstance().incrementProgressFrameDone();
			opened = true;
		} catch (IOException e) {
			Messenger.getInstance().error(this,"Unable to listen on port: " + getPort() + ", error: " + e);
		}
		
		if (opened) {
			Messenger.getInstance().debug(this, "Opened server on port: " + getPort());
			if (startBrowser) {
				SvrController.getInstance().startBrowser();
			}
		}
		
		return opened;
	}
	
	@Override
	public boolean close() {
		Messenger.getInstance().debug(this, "Closing ZODB server on port: " + getPort());
		boolean closed = false;
		try {
			serverSocket.close();
			serverAcceptWorker.stop();
			serverSessionWorker.stop();
			lockWorkers(this);
			List<SvrSocketWorker> stopWorkers = new ArrayList<SvrSocketWorker>(socketWorkers);
			unlockWorkers(this);
			for (SvrSocketWorker socketWorker: stopWorkers) {
				socketWorker.stop();
			}
			lockWorkers(this);
			socketWorkers.clear();
			unlockWorkers(this);
			lockSessions(this);
			sessions.clear();
			unlockSessions(this);
			closed = true;
		} catch(IOException e) {
			Messenger.getInstance().error(this,"Unable to stop listening on port: " + getPort() + ", error: " + e);
		}
		if (closed) {
			Messenger.getInstance().debug(this, "Closed ZODB server on port: " + getPort());
		}
		return closed;
	}

	@Override
	public boolean install() {
		return createDataDir();
	}

	public StringBuilder getCachedHTTPResourceByName(String name) {
		StringBuilder r = cachedHTTPResources.get(name);
		if (r!=null) {
			r = new StringBuilder(r);
		}
		return r;
	}

	public byte[] getCachedHTTPImageResourceByName(String name) {
		return cachedHTTPImageResources.get(name);
	}

	public String getIndexHTML() {
		return indexHTML;
	}

	protected SvrHTTPResourceFactory getNewHTTPResourceFactory() {
		return new SvrHTTPResourceFactory();
	}

	protected int getPort() {
		return SvrConfig.getInstance().getPort();
	}
	
	protected SvrProtocolObject getNewProtocol(Socket socket) {
		return new PtcServer(this,socket);
	}
	
	// Called by server accept worker
	protected boolean acceptAndProcessSockets() {
		boolean isAccepting = false;
		if (socketWorkers.size()<SvrConfig.getInstance().getMaxSocketWorkers()) {
			isAccepting = true;
			Socket socket = acceptSocket();
			if (socket!=null && !socket.isOutputShutdown() && !socket.isInputShutdown() && !socket.isClosed()) {
				lockWorkers(this);
				SvrSocketWorker socketWorker = new SvrSocketWorker(socket,getNewProtocol(socket));
				socketWorkers.add(socketWorker);
				unlockWorkers(this);
				SvrSocketLogger.getInstance().openedSocket(this,socket);
				socketWorker.start();
			}
		} else {
			Messenger.getInstance().warn(this,"Server cannot accept anymore requests: " + SvrConfig.getInstance().getMaxSocketWorkers());
		}
		return isAccepting;
	}

	// Called by socket worker
	protected void removeSocketWorker(SvrSocketWorker socketWorker) {
		lockWorkers(this);
		SvrSocketLogger.getInstance().closedSocket(this,socketWorker.getSocket());
		socketWorkers.remove(socketWorker);
		if (indexHTML.equals(SvrHTTPResourceFactory.INSTALLED_HTML)) {
			indexHTML = SvrHTTPResourceFactory.INDEX_HTML;
		}
		unlockWorkers(this);
	}

	// Called by server session worker
	protected void checkSessionTimeOut(Object source) {
		lockSessions(source);
		List<SvrSession> testSessions = new ArrayList<SvrSession>(sessions);
		for (SvrSession session: testSessions) {
			if ((new Date()).getTime() > session.getLatestActivity().getTime() + (SvrConfig.getInstance().getSessionTimeOutSeconds() * 1000)) {
				sessions.remove(session);
				Messenger.getInstance().debug(this,"Session " + session.getId() + " timed out");
			}
		}
		unlockSessions(source);
	}

	// Called by protocol
	protected void removeSession(long id,Object source) {
		lockSessions(source);
		List<SvrSession> testSessions = new ArrayList<SvrSession>(sessions);
		for (SvrSession session: testSessions) {
			if (session.getId()==id) {
				sessions.remove(session);
				Messenger.getInstance().debug(this,"Removed session " + session.getId());
			}
		}
		unlockSessions(source);
	}
	
	// Called by protocol
	protected SvrSession getSessionById(long id,Object source) {
		SvrSession r = null;
		lockSessions(source);
		for (SvrSession session: sessions) {
			if (session.getId()==id) {
				r = session;
				break;
			}
		}
		unlockSessions(source);
		return r;
	}

	// Called by protocol
	protected SvrSession getNewSessionForSocket(Socket socket,Object source) {
		SvrSession session = null;
		long uidInc = Generic.generateRandom(1,99);
		lockSessions(this);
		if (sessions.size()<MAX_SESSIONS) {
			uid = (uid + uidInc);
			session = new SvrSession();
			sessions.add(session);
			session.setRemoteSocketAddress(socket.getRemoteSocketAddress().toString());
			session.setId(uid);
		}
		unlockSessions(this);
		return session;
	}

	/**
	 * @return the encryptionKey (Called by protocol)
	 */
	protected String getEncryptionKey() {
		return encryptionKey;
	}
	
	protected HTMLFile generateConfigHTML() {
		HTMLFile file = new HTMLFile("ZODB - Configuration");
		file.setBodyBgColor(SvrHTTPResourceFactory.BACKGROUND_COLOR);

		StringBuilder body = new StringBuilder();
		body.append(getHTTPResourceFactory().getMenuHtmlForPage("config"));
		body.append("<div>");
		body.append("The <a href=\"/about.html\">Zeesoft Object Database</a> uses the following configuration files; ");
		body.append("<ul>");
		body.append("<li>The database configuration: <b>");
		body.append(DbConfig.getInstance().getFullFileName());
		body.append("</b></li>");
		body.append("<li>The database cache configuration: <b>");
		body.append(DbConfig.getInstance().getCacheConfig().getFullFileName());
		body.append("</b></li>");
		body.append("<li>The server configuration: <b>");
		body.append(SvrConfig.getInstance().getFullFileName());
		body.append("</b></li>");
		body.append("</ul>");
		
		// Database
		body.append("The database is currently configured to; ");
		body.append("<ul>");
		body.append("<li>Use the model class <b>" + DbConfig.getInstance().getModelClassName() +  "</b></li>");
		body.append("<li>Use the cache configuration class <b>" + DbConfig.getInstance().getCacheConfigClassName() +  "</b></li>");
		body.append("<li>Handle <b>" + DbConfig.getInstance().getMaxRequestWorkers() +  "</b> requests simultaniously</li>");
		if (DbConfig.getInstance().isDebug()) {
			body.append("<li>Log debug statements for classes starting with <b>" + DbConfig.getInstance().getDebugClassPrefix() + "</b></li>");
		}
		if (DbConfig.getInstance().isDebugPerformance()) {
			body.append("<li>Log <b>performance</b> debug statements</li>");
		}
		if (DbConfig.getInstance().isDebugRequestLogging()) {
			body.append("<li>Log <b>request execution</b> debug statements</li>");
		}
		body.append("</ul>");

		// Cache
		if (!DbConfig.getInstance().getCacheConfig().isActive()) {
			body.append("The database cache is currently inactive. ");
		} else {
			body.append("The database cache is currently configured to; ");
			body.append("<ul>");
			body.append("<li>Apply a half life decay to cache hits every <b>" + DbConfig.getInstance().getCacheConfig().getHalfLifeSeconds() + "</b> seconds");
			body.append("<li>Limit class cache sizes to;");
			body.append("<ul>");
			for (String className: DbConfig.getInstance().getCacheConfig().getClassMaxSizeMap().keySet()) {
				body.append("<li>");
				body.append(className);
				body.append(": <b>");
				body.append(DbConfig.getInstance().getCacheConfig().getClassMaxSizeMap().get(className));
				body.append("</b></li>");
			}
			body.append("</ul></li>");
			if (DbConfig.getInstance().isDebugPerformance() && DbConfig.getInstance().getCacheConfig().isDebug()) {
				body.append("<li>Log <b>performance</b> debug statements</li>");
			}
			body.append("</ul>");
		}
		
		// Server
		body.append("The server is currently configured to;");
		body.append("<ul>");
		body.append("<li>Use the server class <b>" + SvrConfig.getInstance().getServerClassName() +  "</b></li>");
		body.append("<li>Handle <b>" + SvrConfig.getInstance().getMaxSocketWorkers() + "</b> requests simultaniously</li>");
		body.append("<li>Limit external database get request results to <b>" + SvrConfig.getInstance().getGetRequestLimit() +  "</b> objects</li>");
		if (SvrConfig.getInstance().isGenerateHTTPResources()) {
			body.append("<li>Generate <b>HTTP resources</b> every time the server starts</li>");
		}
		if (SvrConfig.getInstance().isAuthorizeGetOrPostRequests()) {
			body.append("<li>Time out sessions after <b>" + SvrConfig.getInstance().getSessionTimeOutSeconds() + "</b> seconds of inactivity</li>");
		}
		if (SvrConfig.getInstance().isAuthorizeGetRequests()) {
			body.append("<li>Require session authorization for <b>GET</b> requests that access database data</li>");
		}
		if (SvrConfig.getInstance().isAuthorizePostRequests()) {
			body.append("<li>Require session authorization for <b>POST</b> requests that access database data</li>");
		}
		if (SvrConfig.getInstance().isDebugSockets()) {
			body.append("<li>Log <b>socket</b> debug statements</li>");
		}
		if (SvrConfig.getInstance().isDebugHTTPHeaders()) {
			body.append("<li>Log <b>HTTP header</b> debug statements</li>");
		}
		body.append("</ul>");
		body.append("</div>");
		file.getBodyElements().add(body.toString());
		return file;
	}
	
	/**************************** PRIVATE METHODS **************************/
	private synchronized void lockWorkers(Object source) {
		int attempt = 0;
		while (workersAreLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		socketWorkersLockedBy = source;
	}

	private synchronized void unlockWorkers(Object source) {
		if (socketWorkersLockedBy==source) {
			socketWorkersLockedBy = null;
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempt to unlock workers by source: " + source);
		}
	}
	
	private synchronized boolean workersAreLocked() {
		return socketWorkersLockedBy!=null;
	}

	private synchronized void lockSessions(Object source) {
		int attempt = 0;
		while (sessionsAreLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		sessionsLockedBy = source;
	}

	private synchronized void unlockSessions(Object source) {
		if (sessionsLockedBy==source) {
			sessionsLockedBy = null;
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempt to unlock sessions by source: " + source);
		}
	}
	
	private synchronized boolean sessionsAreLocked() {
		return sessionsLockedBy!=null;
	}
	
	private Socket acceptSocket() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			socket.setSoTimeout(100);
		} catch (SocketTimeoutException e) {
			Messenger.getInstance().debug(this,"Accept timed out on port: " + getPort() + ", error: " + e);
			socket = null;
		} catch (SocketException e) {
			if (!serverSocket.isClosed()) {
				Messenger.getInstance().debug(this,"Unable to set time out on port: " + getPort() + ", error: " + e);
			}
			socket = null;
		} catch (IOException e) {
			if (!serverSocket.isClosed()) {
				Messenger.getInstance().debug(this,"Accept failed on port: " + getPort() + ", error: " + e);
			}
			socket = null;
		}
		return socket;
	}

	private List<String> getPreloadHTTPResources() {
		List<String> resources = new ArrayList<String>();
		File httpDir = new File(SvrConfig.getInstance().getDataDir());
		for (File file: httpDir.listFiles()) {
			if (file.isFile()) { 
				if (file.getName().endsWith(".html") || 
					file.getName().endsWith(".js") || 
					file.getName().endsWith(".css") || 
					file.getName().endsWith(".xml") ||
					file.getName().endsWith(".ico") || 
					file.getName().endsWith(".png") 
					) {
					resources.add(file.getName());
				}
			}
		}
		return resources;
	}

	private void preloadHTTPResources(List<String> resources) {
		Date started = null;
		if (DbConfig.getInstance().isDebugPerformance()) {
			started = new Date();
		}

		SvrHTTPResourceReadWorker readWorker = new SvrHTTPResourceReadWorker();
		readWorker.setResources(resources);
		readWorker.start();
		while (!readWorker.isDone()) {
    		try {
				GuiController.getInstance().refreshProgressFrame();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this, "HTTP resource read worker was interrupted");
			}
		}
		cachedHTTPResources = readWorker.getCachedHTTPResources();
		cachedHTTPImageResources = readWorker.getCachedHTTPImageResources();
		
		// Add configuration
		cachedHTTPResources.put("config.html",generateConfigHTML().toStringBuilder());
		
		int total = cachedHTTPResources.size() + cachedHTTPImageResources.size(); 
		if (DbConfig.getInstance().isDebugPerformance()) {
			Messenger.getInstance().debug(this,"Preloading " + total + " HTTP resources from: " + SvrConfig.getInstance().getDataDir() + " took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			started = new Date();
		} else {
			Messenger.getInstance().debug(this,"Preloaded " + total + " HTTP resources from: " + SvrConfig.getInstance().getDataDir());
		}
	}
	
	private boolean createDataDir() {
		boolean error = false;
		File dataDir = new File(SvrConfig.getInstance().getDataDir());
		if (!dataDir.exists()) {
			dataDir.mkdir();
		}
		if (!dataDir.exists()) {
			Messenger.getInstance().error(this, "Unable to create data directory: " + SvrConfig.getInstance().getDataDir());
			error = true;
		}
		if (!error) {
			SvrConfig.getInstance().serialize();
			if (!SvrConfig.getInstance().fileExists()) {
				Messenger.getInstance().error(this, "Unable to create configuration file: " + SvrConfig.getInstance().getFullFileName());
				error = true;
			}
		}
		return !error;
	}

	private SvrHTTPResourceFactory getHTTPResourceFactory() {
		if (httpResourceFactory==null) {
			httpResourceFactory = getNewHTTPResourceFactory();
		}
		return httpResourceFactory;
	}

	private boolean generateHTTPResources() {
		getHTTPResourceFactory().reinitializeHTTPResources();
		return getHTTPResourceFactory().generateHTTPResources();
	}
}
