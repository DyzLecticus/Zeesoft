package nl.zeesoft.zdk.http;

import java.io.IOException;
import java.net.ServerSocket;

import nl.zeesoft.zdk.Logger;

public class HttpServer {
	protected HttpServerConfig		config				= null;
	protected HttpConnections		connections			= null;
	
	protected ServerSocket			socket				= null;

	public HttpServer(HttpServerConfig config) {
		this.config = config.copy();
		connections = new HttpConnections(this.config);
	}
	
	public synchronized boolean open() {
		boolean r = openSocket();
		if (r) {
			connections.open(socket);
		}
		return r;
	}
	
	public synchronized boolean close() {
		boolean r = isOpened();
		if (r) {
			connections.close();
			r = closeSocket(false);
		}
		return r;
	}
	
	public synchronized boolean isOpen() {
		return isOpened();
	}
	
	public synchronized int getNumberOfConnections() {
		return connections.size();
	}

	protected boolean isOpened() {
		return socket!=null;
	}
	
	protected boolean openSocket() {
		boolean r = false;
		if (socket==null) {
			try {
				socket = new ServerSocket(config.getPort());
				r = true;
			} catch (IOException e) {
				Logger.error(this, "IO exception", e);
			}
		}
		return r;
	}

	protected boolean closeSocket(boolean mockException) {
		boolean r = false;
		try {
			if (mockException) {
				throw new IOException();
			}
			socket.close();
			socket = null;
			r = true;
		} catch (IOException e) {
			Logger.error(this, "IO exception", e);
		}
		return r;
	}
}
