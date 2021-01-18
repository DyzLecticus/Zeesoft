package nl.zeesoft.zdk.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class HttpServer {
	protected Lock					lock				= new Lock();
	protected HttpServerConfig		config				= new HttpServerConfig();
	
	private ServerSocket			socket				= null;
	
	private CodeRunner				runner				= null;
	private List<HttpConnection>	connections			= new ArrayList<HttpConnection>();
	
	private List<CodeRunner>		activeRunners		= new ArrayList<CodeRunner>();
	
	public HttpServer(HttpServerConfig config) {
		this.config = config.copy();
		if (config.getCloseServerRunner()==null) {
			config.setCloseServerRunner(new CodeRunner(new RunCode() {
				@Override
				protected boolean run() {
					close();
					return true;
				}
			}) {
				@Override
				protected void doneCallback() {
					System.exit(0);
				}
			});
		}
		intializeServer();
	}
	
	public Str open() {
		Str error = new Str();
		lock.lock(this);
		if (socket==null) {
			activeRunners.clear();
			Str msg = new Str("Opening server socket on port: ");
			msg.sb().append(config.getPort());
			msg.sb().append(" ...");
			config.debug(this,msg);
			try {
				socket = new ServerSocket(config.getPort());
				runner.start();
				activeRunners.add(runner);
				msg = new Str("Opened server socket on port: ");
				msg.sb().append(config.getPort());
				config.debug(this,msg);
			} catch (IOException ex) {
				error.sb().append("Failed to open server socket on port: ");
				error.sb().append(config.getPort());
				config.error(this,error,ex);
			}
		} else {
			error.sb().append("Server socket is already open");
			config.error(this,error);
		}
		lock.unlock(this);
		return error;
	}
	
	public boolean isOpen() {
		boolean r = false;
		lock.lock(this);
		if (socket!=null && !socket.isClosed()) {
			r = true;
		}
		lock.unlock(this);
		return r;
	}
	
	public Str close() {
		Str error = new Str();
		List<HttpConnection> openConnections = null;
		
		lock.lock(this);
		ServerSocket sock = socket;
		if (sock!=null) {
			runner.stop();
			openConnections = new ArrayList<HttpConnection>(connections);
		}
		lock.unlock(this);
		
		if (openConnections!=null) {
			config.debug(this,new Str("Closing connections ..."));
			for (HttpConnection connection: openConnections) {
				connection.close();
			}
			config.debug(this,new Str("Closed connections"));
		}
		
		if (sock!=null) {
			Str msg = new Str("Closing server socket on port: ");
			msg.sb().append(config.getPort());
			msg.sb().append(" ...");
			config.debug(this,msg);
			try {
				lock.lock(this);
				runner.stop();
				lock.unlock(this);
				
				sock.close();
				
				lock.lock(this);
				socket = null;
				lock.unlock(this);
				msg = new Str("Closed server socket on port: ");
				msg.sb().append(config.getPort());
				config.debug(this,msg);
			} catch (IOException ex) {
				error.sb().append("Failed to close server socket");
				config.error(this,error,ex);
			}
		} else {
			error.sb().append("Server socket is not open");
			config.error(this,error);
		}
		return error;
	}
	
	public List<CodeRunner> getActiveRunners() {
		lock.lock(this);
		List<CodeRunner> r = new ArrayList<CodeRunner>(activeRunners);
		lock.unlock(this);
		return r;
	}
	
	private HttpConnection acceptConnection() {
		HttpConnection r = null;
		lock.lock(this);
		int cons = connections.size();
		ServerSocket sock = socket;
		lock.unlock(this);
		if (cons < config.getMaxConnections()) {
			try {
				Socket conn = sock.accept();
				if (conn!=null) {
					r = new HttpConnection(config,conn) {
						@Override
						protected Str close() {
							Str error = super.close();
							closedConnection(this);
							return error;
						}
						@Override
						protected void stoppedRunner(CodeRunner runner) {
							stoppedConnectionRunner(runner);
						}
					};
					lock.lock(this);
					connections.add(r);
					activeRunners.addAll(r.getRunners());
					lock.unlock(this);
					r.open();
				}
			} catch (IOException ex) {
				boolean closed = false;
				if (sock.isClosed()) {
					closed = true;
				}
				if (!closed) {
					config.error(this,new Str("Failed to accept connection"),ex);
				}
			}
		} else {
			config.error(this,new Str("Failed accept to connection: number of open connections has reached the configured maximum"));
		}
		return r;
	}
	
	private void closedConnection(HttpConnection connection) {
		lock.lock(this);
		connections.remove(connection);
		lock.unlock(this);
	}
	
	private void stoppedConnectionRunner(CodeRunner runner) {
		lock.lock(this);
		activeRunners.remove(runner);
		lock.unlock(this);		
	}
	
	private RunCode getConnector() {
		RunCode r = new RunCode() {
			@Override
			protected boolean run() {
				acceptConnection();
				return false;
			}
		};
		return r;
	}
	
	private void intializeServer() {
		runner = new CodeRunner(getConnector());
		runner.setSleepMs(0);
	}
}
