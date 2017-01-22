package nl.zeesoft.zodb.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.protocol.PtcClient;
import nl.zeesoft.zodb.protocol.PtcClientControl;

public final class ClSessionInitializationWorker extends Worker {
	private static final int		WAIT_SECONDS		= 3;

	private String 					hostNameOrIp		= "";
	private int 					port				= 0;
	private PtcClient				protocol			= null;
	private	ClSession				session				= null;
	private	ClSessionWorker			sessionWorker		= null;
	
	private boolean					stopping			= false;
	private String					errMsg				= "";
	
	protected ClSessionInitializationWorker(String hostNameOrIp, int port, PtcClient p) {
		this.hostNameOrIp = hostNameOrIp;
		this.port = port;
		protocol = p;
		setSleep(10);
	}
	
	@Override
	public void start() {
		super.start();
    }

	@Override
    public void stop() {
		stopping = true;
		super.stop();
    }
	
	@Override
	public void whileWorking() {
		Socket socket = getNewClientSocket(hostNameOrIp,port);
		if (socket==null) {
			stop();
			ClSessionManager.getInstance().failedToConnectToServer(this,errMsg);
		} else {
			if (initializeSession(socket)) {
				stop();
				ClSessionManager.getInstance().obtainedSession(this);
			} else {
				stop();
				ClSessionManager.getInstance().failedToObtainSession(this);
			}
		}
	}

	private Socket getNewClientSocket(String hostNameOrIp, int port) {
	    Socket s = null;
		try {
		    s = new Socket(hostNameOrIp,port);
	    } catch (UnknownHostException e) {
	    	errMsg = "No host found for connection to: " + hostNameOrIp;
	    	s = null;
	    } catch (IOException e) {
	    	errMsg = "No I/O for the connection to: " + hostNameOrIp + " on port: " + port;
	    	s = null;
	    }
		if (s==null) {
	        Messenger.getInstance().debug(this,errMsg);
		}
	    return s;
	}
	
	private boolean initializeSession(Socket socket) {
		boolean initialized = false;
		if (protocol instanceof PtcClientControl) {
			session = new ClControlSession(socket, protocol); 
		} else {
			session = new ClSession(socket, protocol); 
		}
		protocol.setSession(session);
		sessionWorker = new ClSessionWorker(session);
		sessionWorker.start();
		int test = 0;
		while(socket.isConnected() && session.getSessionId()==0) {
			if (stopping) {
				break;
			}
			test++;
			if (test>=(100 * WAIT_SECONDS)) {
				break;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
		if ((!stopping) && (session.getSessionId()>0)) {
			initialized = true;
		} else {
			sessionWorker.stop();
		}
		return initialized;
	}

	/**
	 * @return the session
	 */
	public ClSession getSession() {
		return session;
	}

	/**
	 * @return the sessionWorker
	 */
	protected ClSessionWorker getSessionWorker() {
		return sessionWorker;
	}

}
