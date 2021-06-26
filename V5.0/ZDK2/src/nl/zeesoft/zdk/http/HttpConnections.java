package nl.zeesoft.zdk.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Lock;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.function.Function;

public class HttpConnections implements Runnable {
	protected Lock						lock			= new Lock(this);
	protected HttpServerConfig			config			= null;
	
	public boolean						open			= false;
	public Function						acceptFunction	= null;
	public Thread						thread			= null;
	public List<HttpServerConnection>	connections		= new ArrayList<HttpServerConnection>();

	public HttpConnections(HttpServerConfig config) {
		this.config = config;
	}
	
	public void open(ServerSocket serverSocket) {
		lock.lock();
		open = true;
		acceptFunction = getAcceptFunction(serverSocket);
		thread = new Thread(this);
		thread.start();
		lock.unlock();
	}

	public void close() {
		lock.lock();
		open = false;
		lock.unlock();
		closeConnections();
	}
	
	public boolean isOpen() {
		lock.lock();
		boolean r = open;
		lock.unlock();
		return r;
	}
	
	public int size() {
		lock.lock();
		int r = connections.size();
		lock.unlock();
		return r;
	}
	
	public void accept() {
		while(isOpen()) {
			acceptFunction.execute(this);
		}
		lock.lock();
		acceptFunction = null;
		thread = null;
		lock.unlock();
	}
	
	@Override
	public void run() {
		accept();
	}
		
	protected Function getAcceptFunction(ServerSocket serverSocket) {
		return new Function() {
			@Override
			protected Object exec() {
				HttpServerConnection connection = getNewConnection();
				acceptConnection(serverSocket, connection, false);
				return connection;
			}
		};
	}
	
	protected HttpServerConnection getNewConnection() {
		return new HttpServerConnection(config) {
			@Override
			protected void stopped() {
				super.stopped();
				removeConnection(this);
			}
		};
	}
	
	protected void acceptConnection(ServerSocket serverSocket, HttpServerConnection connection, boolean mockException) {
		try {
			if (mockException) {
				throw new IOException();
			}
			if (connection.open(serverSocket.accept())) {
				addConnection(connection);
			}
		} catch (IOException e) {
			if (isOpen()) {
				Logger.error(this, "IO exception", e);
			}
		}
	}

	protected void addConnection(HttpServerConnection connection) {
		lock.lock();
		connections.add(connection);
		lock.unlock();
	}
	
	protected void removeConnection(HttpServerConnection connection) {
		lock.lock();
		connections.remove(connection);
		lock.unlock();
	}
	
	protected void closeConnections() {
		lock.lock();
		List<HttpServerConnection> list = new ArrayList<HttpServerConnection>(connections);
		lock.unlock();
		for (HttpServerConnection connection: list) {
			connection.close();
			removeConnection(connection);
		}
	}
}
