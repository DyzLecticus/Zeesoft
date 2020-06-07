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
	
	private List<CodeRunner>		runners				= new ArrayList<CodeRunner>();
	
	public HttpServer(HttpServerConfig config) {
		this.config = config.copy();
		lock.setLogger(this,config.getLogger());
		intializeServer();
	}
	
	public Str open() {
		Str error = new Str();
		lock.lock(this);
		if (socket==null) {
			Str msg = new Str("Opening server socket on port: ");
			msg.sb().append(config.getPort());
			msg.sb().append(" ...");
			config.debug(this,msg);
			try {
				socket = new ServerSocket(config.getPort());
				runner.start();
				msg = new Str("Opened server socket on port: ");
				msg.sb().append(config.getPort());
				config.debug(this,msg);
			} catch (IOException ex) {
				error.sb().append("Failed to open server socket on port: ");
				error.sb().append(config.getPort());
				config.error(this,error,ex);
			}
			runners.clear();
		} else {
			error.sb().append("Server socket is already open");
			config.error(this,error);
		}
		lock.unlock(this);
		return error;
	}
	
	public Str close() {
		Str error = new Str();
		List<HttpConnection> openConnections = null;
		
		lock.lock(this);
		if (socket!=null) {
			runner.stop();
			openConnections = new ArrayList<HttpConnection>(connections);
		}
		runners.clear();
		lock.unlock(this);
		
		if (openConnections!=null) {
			config.debug(this,new Str("Closing connections ..."));
			for (HttpConnection connection: openConnections) {
				connection.close();
				lock.lock(this);
				runners.add(connection.getRunner());
				lock.unlock(this);
			}
			config.debug(this,new Str("Closed connections"));
		}
		
		lock.lock(this);
		if (socket!=null) {
			Str msg = new Str("Closing server socket on port: ");
			msg.sb().append(config.getPort());
			msg.sb().append(" ...");
			config.debug(this,msg);
			try {
				runner.stop();
				socket.close();
				socket = null;
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
		runners.add(runner);
		lock.unlock(this);
		return error;
	}
	
	public List<CodeRunner> getRunners() {
		lock.lock(this);
		List<CodeRunner> r = new ArrayList<CodeRunner>(runners);
		lock.unlock(this);
		return r;
	}
	
	private HttpConnection acceptConnection() {
		HttpConnection r = null;
		lock.lock(this);
		int cons = connections.size();
		lock.unlock(this);
		if (cons < config.getMaxConnections()) {
			try {
				Socket conn = socket.accept();
				if (conn!=null) {
					r = new HttpConnection(config,conn) {
						@Override
						protected Str close() {
							Str error = super.close();
							closedConnection(this);
							return error;
						}
					};
					lock.lock(this);
					connections.add(r);
					lock.unlock(this);
					r.open();
				}
			} catch (IOException ex) {
				boolean closed = false;
				lock.lock(this);
				if (socket.isClosed()) {
					closed = true;
				}
				lock.unlock(this);
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
