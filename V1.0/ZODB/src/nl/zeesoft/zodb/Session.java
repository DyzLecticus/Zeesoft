package nl.zeesoft.zodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import nl.zeesoft.zodb.client.ClSession;
import nl.zeesoft.zodb.client.ClSessionManager;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControlServer;
import nl.zeesoft.zodb.database.DbServerSession;
import nl.zeesoft.zodb.database.DbSessionServer;
import nl.zeesoft.zodb.event.EvtEvent;

/**
 * Abstract client/server communication class  
 */
public abstract class Session {
	private Socket 			socket			= null;
	private long	 		sessionId		= 0;

	private PrintWriter		out				= null;
	private BufferedReader 	in				= null;
	
	private boolean			lostConnection	= false;
	
	public Session(Socket sock, long sessId) {
		socket = sock;
		setSessionId(sessId);
	}
	
	protected boolean open() {
		boolean opened = false;
		if (socket!=null) {
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				opened = true;
			} catch (IOException e) {
				Messenger.getInstance().error(this,"Unable open server session: " + sessionId + ", error: " + e);
			}
		}
		return opened;
	}

	protected void close() {
		try {
			if (out!=null) {
				out.close();
				out = null;
			}
			if (in!=null) {
				in.close();
				in = null;
			}
			if (socket!=null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			Messenger.getInstance().error(this,"Unable to close server session: " + sessionId + ", error: " + e);
		}
	}

	protected StringBuffer readInput(long seed) {
		StringBuffer input = new StringBuffer();
		if (!lostConnection) {
			String line  = "input";
			StringBuffer read = new StringBuffer();
			try {
				while ((socket!=null) && (socket.isConnected()) && (!socket.isClosed()) && (!socket.isInputShutdown()) && ((line = in.readLine()) != null) && (line.length()>0)) {
					if (line.equals("EMPTY" + "LINE")) {
						read.append("\n");
					} else {
						read.append(line);
						if (!DbConfig.getInstance().isEncrypt()) {
							read.append("\n");
						}
					}
				}
				if ((socket.isConnected()) && (!socket.isClosed()) && (!socket.isInputShutdown())) {
					if (DbConfig.getInstance().isEncrypt()) {
						// Assumes input is encrypted
						input = Generic.decodeKey(read,DbConfig.getInstance().getEncryptionKey(),seed);
					} else {
						input = read;
					}
					if ((input.length()==0) || ((input.length()==1) && (input.equals("\n")))) {
						unableToDecodeInput();
					}
				} else {
					Messenger.getInstance().debug(this,"Lost connection for server session: " + sessionId);
					lostConnection();
				}
			} catch (IOException e) {
				Messenger.getInstance().debug(this,"Lost connection for server session: " + sessionId + ", error: " + e);
				lostConnection();
			}
		}
		return input;
	}

	protected void lostConnection() {
		lostConnection = true;
		if (this instanceof DbServerSession) {
			DbControlServer.getInstance().stopSession(sessionId);
			DbSessionServer.getInstance().stopSession(sessionId);
		} else if (this instanceof ClSession) {
			ClSessionManager.getInstance().stopSession(sessionId);
			ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.LOST_CONNECTION_TO_SERVER,this, "Lost connection to server"));
		}
	}

	protected void unableToDecodeInput() {
		Messenger.getInstance().debug(this,"Unable to decode input, closing session: " + sessionId);
		if (this instanceof DbServerSession) {
			DbControlServer.getInstance().stopSession(sessionId);
			DbSessionServer.getInstance().stopSession(sessionId);
		} else if (this instanceof ClSession) {
			ClSessionManager.getInstance().stopSession(sessionId);
			ClSessionManager.getInstance().publishEvent(new EvtEvent(ClSessionManager.UNABLE_TO_DECODE_SERVER_RESPONSE,this,"Unable to decode server response"));
		}
	}

	protected void writeOutput(StringBuffer output, long seed) {
		if (!lostConnection) {
			if ((out!=null) && (output!=null) && (output.length()>0) && (!output.equals("\n"))) {
				if (DbConfig.getInstance().isEncrypt()) {
					if (output.length()>0) {
						// Assumes output is not encrypted
						StringBuffer tmp = output;
						output = new StringBuffer();
						output.append(Generic.encodeKey(tmp,DbConfig.getInstance().getEncryptionKey(),seed));
						output.append("\n");
					}
				} else {
					output = Generic.stringBufferReplace(output,"\n\n","\n" + "EMPTY" + "LINE" + "\n");
					if ((output.length()>0) && (!output.substring(output.length() - 1, output.length()).equals("\n"))) {
						output.append("\n");
					}
				}
				out.println(output);
			}
		}
	}

	/**
	 * Override to implement 
	 */
	protected void readAndProcessInput() {
	
	}

	/**
	 * @return the socket
	 */
	protected Socket getSocket() {
		return socket;
	}

	/**
	 * @return the sessionId
	 */
	protected long getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	protected void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
}
