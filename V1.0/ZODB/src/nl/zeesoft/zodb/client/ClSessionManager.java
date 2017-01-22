package nl.zeesoft.zodb.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventPublisher;
import nl.zeesoft.zodb.protocol.PtcClient;
import nl.zeesoft.zodb.protocol.PtcClientControl;

public final class ClSessionManager extends EvtEventPublisher {
	public static final String SERVER_STOPPED_SESSION 			= "SERVER_STOPPED_SESSION";
	public static final String LOST_CONNECTION_TO_SERVER		= "LOST_CONNECTION_TO_SERVER";
	public static final String UNABLE_TO_DECODE_SERVER_RESPONSE	= "UNABLE_TO_DECODE_SERVER_RESPONSE";
	public static final String STARTED_SESSION 					= "STARTED_SESSION";
	public static final String CRC_MISMATCH 					= "CRC_MISMATCH";
	public static final String FAILED_TO_CONNECT_TO_SERVER 		= "FAILED_TO_CONNECT_TO_SERVER";
	public static final String FAILED_TO_OBTAIN_SESSION 		= "FAILED_TO_OBTAIN_SESSION";
	public static final String FAILED_TO_AUTHORIZE_SESSION 		= "FAILED_TO_AUTHORIZE_SESSION";
	public static final String OBTAINED_SESSION 				= "OBTAINED_SESSION";
	public static final String AUTHORIZED_SESSION 				= "AUTHORIZED_SESSION";
	public static final String SERVER_IS_WORKING 				= "SERVER_IS_WORKING";
	public static final String BATCH_IS_WORKING 				= "BATCH_IS_WORKING";
	public static final String SERVER_CACHE						= "SERVER_CACHE";
	public static final String SERVER_PROPERTIES				= "SERVER_PROPERTIES";
	public static final String REQUEST_RESPONSE_ERROR			= "REQUEST_RESPONSE_ERROR";
	public static final String REQUEST_RESPONSE_PROCESS_ERROR	= "REQUEST_RESPONSE_PROCESS_ERROR";
	public static final String ADDED_REQUEST					= "ADDED_REQUEST";
	public static final String SENT_REQUEST						= "SENT_REQUEST";
	public static final String RECEIVED_RESPONSE				= "RECEIVED_RESPONSE";

	private static ClSessionManager 			sessionManager 	= null;
	private SortedMap<Long,ClSessionWorker>		sessionWorkers	= new TreeMap<Long,ClSessionWorker>();
	private List<ClSessionInitializationWorker>	initWorkers		= new ArrayList<ClSessionInitializationWorker>();

	private ClSessionManager() {
		// Singleton
	}

	public static ClSessionManager getInstance() {
		if (sessionManager==null) {
			sessionManager = new ClSessionManager();
		}
		return sessionManager;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	public ClSessionInitializationWorker initializeNewSession() {
		return initializeNewSession(ClConfig.getInstance().getHostNameOrIp(),DbConfig.getInstance().getPort());
	}

	public ClSessionInitializationWorker initializeNewControlSession() {
		return initializeNewControlSession(ClConfig.getInstance().getHostNameOrIp(),(DbConfig.getInstance().getPort() + 1));
	}

	public ClSessionInitializationWorker initializeNewSession(String hostname, int port) {
		return getNewSessionInitializationWorker(hostname,port,new PtcClient());
	}

	public ClSessionInitializationWorker initializeNewControlSession(String hostname, int port) {
		return getNewSessionInitializationWorker(hostname,port,new PtcClientControl());
	}

	private ClSessionInitializationWorker getNewSessionInitializationWorker(String hostNameOrIp, int port, PtcClient protocol) {
		ClSessionInitializationWorker worker = new ClSessionInitializationWorker(hostNameOrIp,port,protocol);
		initWorkers.add(worker);
		worker.start();
		return worker;
	}

	protected void obtainedSession(ClSessionInitializationWorker siw) {
		sessionWorkers.put(siw.getSession().getSessionId(),siw.getSessionWorker());
		publishEvent(new EvtEvent(OBTAINED_SESSION,this,siw.getSession()));
	}

	protected void failedToObtainSession(ClSessionInitializationWorker siw) {
		publishEvent(new EvtEvent(FAILED_TO_OBTAIN_SESSION,this,FAILED_TO_OBTAIN_SESSION));
	}
	
	protected void failedToConnectToServer(ClSessionInitializationWorker siw,String msg) {
		publishEvent(new EvtEvent(FAILED_TO_CONNECT_TO_SERVER,this,msg));
	}

	public void stopSession(long sessionId) {
    	ClSessionWorker s = sessionWorkers.remove(sessionId);
    	if (s!=null) {
    		s.stop();
    	}
    }

    public void stopSessions() {
    	List<ClSessionInitializationWorker>	iWorkers = new ArrayList<ClSessionInitializationWorker>(initWorkers);
    	for (ClSessionInitializationWorker worker: iWorkers) {
    		worker.stop();
    		initWorkers.remove(worker);
    	}
    	SortedMap<Long,ClSessionWorker>	sessWorkers = new TreeMap<Long,ClSessionWorker>(sessionWorkers);
    	for (Entry<Long,ClSessionWorker> e: sessWorkers.entrySet()) {
    		stopSession((Long) e.getKey());
    	}
    }
	
	@Override
	public void publishEvent(EvtEvent e) {
		if (e.getType().equals(CRC_MISMATCH)) {
			Messenger.getInstance().error(this, (String) e.getValue());
		}
		super.publishEvent(e);
	}
}
