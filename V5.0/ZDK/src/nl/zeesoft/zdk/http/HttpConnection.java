package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
		runner = new CodeRunner(getHandler()) {
			@Override
			protected void stopped() {
				stoppedRunner(this);
			}
			@Override
			protected void caughtException(Exception exception) {
				config.error(this,new Str("HTTP connection request handler caught an exception"),exception);
				close();
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
		if (reader!=null && writer!=null) {
			runner.start();
		} else {
			close();
		}
		return error;
	}
	
	protected Str close() {
		Str error = new Str();
		runner.stop();
		lock.lock(this);
		BufferedReader rdr = reader;
		PrintWriter wrtr = writer;
		Socket sock = socket;
		lock.unlock(this);
		if (wrtr!=null) {
			wrtr.flush();
			wrtr.close();
		}
		if (rdr!=null) {
			try {
				rdr.close();
			} catch (IOException ex) {
				config.error(this,new Str("Exception occurred while closing connection socket"),ex);
			}
		}
		try {
			sock.close();
		} catch (IOException ex) {
			config.error(this,new Str("Exception occurred while closing connection socket"),ex);
		}
		return error;
	}

	protected List<CodeRunner> getRunners() {
		List<CodeRunner> r = new ArrayList<CodeRunner>(requestHandler.runners);
		r.add(runner);
		return r;
	}
	
	protected void stoppedRunner(CodeRunner runner) {
		// Override to implement
	}
	
	private void handleRequest() {
		Str header = new Str();
		boolean done = false;
		lock.lock(this);
		BufferedReader rdr = reader;
		Socket sock = socket;
		lock.unlock(this);
		while(!done) {
			try {
				String line = rdr.readLine();
				if (line==null || line.length()==0) {
					done = true;
				} else {
					header.sb().append(line);
					header.sb().append("\n");
				}
			} catch (java.net.SocketException ex) {
				if (!sock.isClosed()) {
					config.error(this,new Str("Socket exception occurred while reading connection input"),ex);
				}
				done = true;
			} catch (IOException ex) {
				if (!sock.isClosed()) {
					config.error(this,new Str("I/O exception occurred while reading connection input"),ex);
				}
				done = true;
			}
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
				if (head.length()>0) {
					List<Str> h = head.split(": ");
					request.headers.add(h.get(0).toString(),h.get(1).toString());
				}
			}
			int contentLength = request.getContentLength();
			
			if ((request.method.equals("POST") || request.method.equals("PUT")) && contentLength>0) {
				int c = 0;
				for (int i = 0; i < contentLength; i++) {
					try {
						c = rdr.read();
						request.body.sb().append((char) c);
					} catch (IOException ex) {
						config.error(this,new Str("Exception occurred while reading POST request body"),ex);
					}
				}
			}
			if (request.keepConnectionAlive()) {
				try {
					sock.setKeepAlive(true);
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
			if (config.getLogger().isDebug() && config.isDebugLogHeaders()) {
				Str msg = new Str("Request/response headers (Port: ");
				msg.sb().append(config.getPort());
				msg.sb().append(");");
				msg.sb().append("\n");
				msg.sb().append("<<<");
				msg.sb().append("\n");
				msg.sb().append(request.toHeaderStr());
				msg.sb().append("\n");
				msg.sb().append(">>>");
				msg.sb().append("\n");
				msg.sb().append(response.toHeaderStr());
				config.debug(this, msg);
			}
			handleResponse(request,response);
		} else {
			close();
		}
	}
	
	private void handleResponse(HttpRequest request, HttpResponse response) {
		lock.lock(this);
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
