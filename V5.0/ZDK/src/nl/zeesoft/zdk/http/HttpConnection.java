package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;

public class HttpConnection {
	protected Lock				lock				= new Lock();
	protected HttpServerConfig	config				= new HttpServerConfig();
	
	protected Socket			socket				= null;

	private BufferedReader 		reader				= null;
	private PrintWriter			writer				= null;
	private CodeRunner			runner				= null;
	private HttpRequestHandler	requestHandler		= null;
	
	protected HttpConnection(HttpServerConfig config, Socket socket) {
		this.config = config;
		this.socket = socket;
		lock.setLogger(this,config.getLogger());
		runner = new CodeRunner(getHandler()) {
			@Override
			protected void stopped() {
				stoppedRunner(this);
			}
		};
		runner.setSleepMs(0);
		requestHandler = config.getNewHttpRequestHandler();
	}
	
	protected Str open() {
		Str error = new Str();
		lock.lock(this);
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException ex) {
			config.error(this,new Str("Failed to create connection reader"),ex);
		}
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException ex) {
			config.error(this,new Str("Failed to create connection writer"),ex);
		}
		lock.unlock(this);
		runner.start();
		return error;
	}
	
	protected Str close() {
		Str error = new Str();
		runner.stop();
		lock.lock(this);
		try {
			reader.close();
		} catch (IOException ex) {
			config.error(this,new Str("Exception occurred while closing connection socket"),ex);
		}
		writer.flush();
		writer.close();
		try {
			socket.close();
		} catch (IOException ex) {
			config.error(this,new Str("Exception occurred while closing connection socket"),ex);
		}
		lock.unlock(this);
		return error;
	}

	protected CodeRunner getRunner() {
		return runner;
	}
	
	protected void stoppedRunner(CodeRunner runner) {
		// Override to implement
	}
	
	private void handleRequest() {
		Str header = new Str();
		boolean done = false;
		while(!done) {
			lock.lock(this);
			try {
				String line = reader.readLine();
				if (line==null || line.length()==0) {
					done = true;
				} else {
					header.sb().append(line);
					header.sb().append("\n");
				}
			} catch (IOException ex) {
				config.error(this,new Str("Exception occurred while reading connection input"),ex);
				done = true;
			}
			lock.unlock(this);
		}
		if (header.length()>0) {
			HttpRequest request = new HttpRequest(config);
			List<Str> headers = header.split("\n");
			List<Str> params = headers.get(0).split(" ");
			request.method = params.get(0).toString();
			request.path = params.get(1).toString();
			request.protocol = params.get(2).toString();
			headers.remove(0);
			for (Str head: headers) {
				List<Str> h = head.split(": ");
				request.headers.add(h.get(0).toString(),h.get(1).toString());
			}
			int contentLength = request.getContentLength();
			
			if ((request.method.equals("POST") || request.method.equals("PUT")) && contentLength>0) {
				int c = 0;
				for (int i = 0; i < contentLength; i++) {
					lock.lock(this);
					try {
						c = reader.read();
						request.body.sb().append((char) c);
					} catch (IOException ex) {
						config.error(this,new Str("Exception occurred while reading POST request body"),ex);
					}
					lock.unlock(this);
				}
			}
			if (request.keepConnectionAlive()) {
				try {
					socket.setKeepAlive(true);
				} catch (SocketException ex) {
					config.error(this,new Str("Failed to enable keep alive on connection"),ex);
				}
			}
			HttpResponse response = new HttpResponse();
			try {
				requestHandler.handleRequest(request,response);
			} catch(Exception ex) {
				response.code = HttpURLConnection.HTTP_INTERNAL_ERROR;
				response.message = ex.toString();
			}
			handleResponse(request,response);
		} else {
			close();
		}
	}
	
	private void handleResponse(HttpRequest request, HttpResponse response) {
		lock.lock(this);
		response.addContentLengthHeader();
		writer.print(response.toStr().sb());
		if (response.bytes!=null) {
			writeOutput(response.bytes);
		}
		writer.flush();
		lock.unlock(this);
		if (!request.keepConnectionAlive()) {
			close();
		}
	}

	private void writeOutput(byte[] image) {
		try {
			socket.getOutputStream().write(image);
		} catch (IOException ex) {
			if (!socket.isClosed()) {
				config.error(this,new Str("Exception occurred while writing response"),ex);
			}
		}
	}
	
	private RunCode getHandler() {
		RunCode r = new RunCode() {
			@Override
			protected boolean run() {
				handleRequest();
				return false;
			}
		};
		return r;
	}
}
