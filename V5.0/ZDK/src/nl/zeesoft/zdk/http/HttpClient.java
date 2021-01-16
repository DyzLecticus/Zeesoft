package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;
import nl.zeesoft.zdk.thread.RunCode;
import nl.zeesoft.zdk.thread.Waiter;

public class HttpClient {
	protected Lock				lock				= new Lock();
	
	protected String			method				= "";
	protected URL				url					= null;
	protected HttpHeaderList	headers				= new HttpHeaderList();
	protected int				readTimeoutMs		= 3000;
	
	private String				currentUrlBase		= "";
	private Socket				socket				= null;
	protected PrintWriter		writer				= null;
	protected BufferedReader	reader				= null;
	private CodeRunner			runner				= null;
	
	protected byte[]			responseBytes		= null;
	protected int				responseCode		= HttpURLConnection.HTTP_OK;
	protected String			responseMessage		= "";
	protected HttpHeaderList	responseHeaders		= new HttpHeaderList();
	
	private Str					error				= new Str();
	private Exception			exception			= null;
	
	public HttpClient(Logger logger) {
		runner = getNewResponseReader();
	}

	public HttpClient(String method,String url) {
		this.method = method;
		setUrl(url);
		runner = getNewResponseReader();
	}
	
	public HttpClient(Logger logger,String method,String url) {
		this.method = method;
		setUrl(url);
		runner = getNewResponseReader();
	}
	
	public void setMethod(String method) {
		lock.lock(this);
		this.method = method;
		lock.unlock(this);
	}
	
	public void setUrl(String url) {
		URL conUrl = null;
		try {
			conUrl = new URL(url);
			setUrl(conUrl);
		} catch (MalformedURLException ex) {
			logError("Malformed URL: " + url,ex);
		}
	}
	
	public void setUrl(URL url) {
		boolean disconnect = false;
		lock.lock(this);
		this.url = url;
		String urlBase = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
		if (this.currentUrlBase.equals(urlBase)) {
			disconnect = socket!=null;
		}
		lock.unlock(this);
		if (disconnect) {
			disconnect();
		}
	}
	
	public void setHeaders(HttpHeaderList headers) {
		lock.lock(this);
		this.headers = headers.copy();
		lock.unlock(this);
	}
	
	public void setReadTimeoutMs(int readTimeoutMs) {
		lock.lock(this);
		this.readTimeoutMs = readTimeoutMs;
		lock.unlock(this);
	}
	
	public boolean connect() {
		boolean error = false;
		boolean connected = false;
		lock.lock(this);
		if (socket==null) {
			int port = url.getPort();
			if (port==-1) {
				port = 80;
			}
			try {
				socket = new Socket(url.getHost(),port);
				connected = true;
			} catch (UnknownHostException ex) {
				logErrorNoLock("Unknown host: " + url.getHost(),ex);
				error = true;
			} catch (IOException ex) {
				logErrorNoLock("I/O exception",ex);
				error = true;
			}
			clearWriterAndReaderNoLock();
		}
		lock.unlock(this);
		if (connected) {
			HttpClientManager.connectedClient(this);
		}
		return error;
	}
	
	public boolean disconnect() {
		boolean error = false;
		
		lock.lock(this);
		Socket sock = socket;
		PrintWriter wrtr = writer;
		BufferedReader rdr = reader;
		lock.unlock(this);
		
		if (wrtr!=null) {
			wrtr.close();
		}
		if (rdr!=null) {
			try {
				rdr.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (sock!=null) {
			try {
				sock.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		
		lock.lock(this);
		writer = null;
		reader = null;
		socket = null;
		currentUrlBase = "";
		lock.unlock(this);
		
		HttpClientManager.disconnectedClient(this);
		
		return error;
	}
	
	public boolean sendRequest() {
		return sendRequest(null,false,true);
	}

	public boolean sendRequest(boolean keepAlive) {
		return sendRequest(null,keepAlive,!keepAlive);
	}
	
	public boolean sendRequest(Str body) {
		return sendRequest(body,false,true);
	}

	public boolean sendRequest(Str body,boolean keepAlive, boolean addDefaultHeaders) {
		boolean error = false;
		
		error = connect();
		if (error) {
			return error;
		}
		
		lock.lock(this);
		clearResponseNoLock();
		String mthd = method;
		URL ul = url;
		int readMs = readTimeoutMs;
		if (addDefaultHeaders) {
			addDefaultHeadersNoLock(body);
		}
		HttpHeaderList hdrs = headers.copy();
		lock.unlock(this);
			
		Str request = new Str();
		request.sb().append(mthd);
		request.sb().append(" ");
		request.sb().append(ul.getPath());
		request.sb().append(" ");
		request.sb().append("HTTP/1.1");
		request.sb().append("\r\n");
		request.sb().append(hdrs.toStr());
		request.sb().append("\r\n");
		request.sb().append("\r\n");
		if ((mthd.equals("POST") || mthd.equals("PUT")) && body!=null) {
			request.sb().append(body);
			request.sb().append("\r\n");
		}
		
		// Send request
		lock.lock(this);
		PrintWriter wrtr = getWriterNoLock();
		lock.unlock(this);
		if (wrtr!=null) {
			wrtr.print(request);
			wrtr.flush();
			
			runner.start();
			Waiter.waitFor(runner, readMs);
			runner.stop();
		}
		
		if (!keepAlive) {
			disconnect();
		}
		
		return error;
	}

	public CodeRunner getRunner() {
		return runner;
	}

	public Str getResponseBody() {
		lock.lock(this);
		Str r = new Str();
		if (responseBytes!=null) {
			for (int i = 0; i < responseBytes.length; i++) {
				r.sb().append((char)responseBytes[i]);
			}
		}
		lock.unlock(this);
		return r;
	}

	public byte[] getResponseBytes() {
		lock.lock(this);
		byte[] r = responseBytes;
		lock.unlock(this);
		return r;
	}

	public int getResponseCode() {
		lock.lock(this);
		int r = responseCode;
		lock.unlock(this);
		return r;
	}

	public String getResponseMessage() {
		lock.lock(this);
		String r = responseMessage;
		lock.unlock(this);
		return r;
	}

	public HttpHeaderList getResponseHeaders() {
		lock.lock(this);
		HttpHeaderList r = responseHeaders;
		lock.unlock(this);
		return r;
	}

	public Str getError() {
		lock.lock(this);
		Str r = error;
		lock.unlock(this);
		return r;
	}

	public Exception getException() {
		lock.lock(this);
		Exception r = exception;
		lock.unlock(this);
		return r;
	}
	
	protected void logError(String msg,Exception e) {
		lock.lock(this);
		logErrorNoLock(msg,e);
		lock.unlock(this);
	}

	protected void logErrorNoLock(String msg,Exception e) {
		if (error.length()==0) {
			error.sb().append(msg);
			exception = e;
		}
		Logger.err(this,new Str(msg),e);
	}
	
	protected void readResponse() {
		lock.lock(this);
		BufferedReader rdr = getReaderNoLock();
		Socket sock = socket;
		lock.unlock(this);
		
		Str header = new Str();
		if (rdr!=null) {
			boolean done = false;
			while(!done) {
				try {
					String line = rdr.readLine();
					if (line==null || line.length()==0) {
						done = true;
					} else {
						header.sb().append(line);
						header.sb().append("\n");
					}
				} catch (IOException ex) {
					if (sock==null || !sock.isClosed()) {
						logError("I/O exception",ex);
					}
					done = true;
				}
			}
		}

		if (header.length()>0) {
			List<Str> headers = header.split("\n");
			List<Str> params = headers.get(0).split(" ");
			
			lock.lock(this);
			responseCode = Integer.parseInt(params.get(1).toString());
			responseMessage = params.get(2).toString();
			headers.remove(0);
			for (Str head: headers) {
				if (head.length()>0) {
					List<Str> h = head.split(": ");
					responseHeaders.add(h.get(0).toString(),h.get(1).toString());
				}
			}
			int contentLength = responseHeaders.getIntegerValue(HttpHeader.CONTENT_LENGTH);
			byte[] bytes = new byte[contentLength];
			lock.unlock(this);
			
			if (contentLength>0) {
				int c = 0;
				for (int i = 0; i < contentLength; i++) {
					try {
						c = rdr.read();
						bytes[i] = (byte) c;
					} catch (IOException ex) {
						logError("I/O exception",ex);
					}
				}
			}
			
			lock.lock(this);
			responseBytes = bytes;
			lock.unlock(this);
		}
	}

	protected PrintWriter getWriterNoLock() {
		if (socket!=null && writer==null) {
			try {
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				logErrorNoLock("Failed to obtain output stream",e);
			}
		}
		return writer;
	}

	protected BufferedReader getReaderNoLock() {
		if (socket!=null && reader==null) {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				logErrorNoLock("Failed to obtain input stream",e);
			}
		}
		return reader;
	}
	
	protected void clearWriterAndReaderNoLock() {
		writer = null;
		reader = null;
	}
	
	protected void clearResponseNoLock() {
		responseBytes = null;
		responseCode = HttpURLConnection.HTTP_OK;
		responseMessage = "";
		responseHeaders = new HttpHeaderList();
	}
	
	protected void addDefaultHeadersNoLock(Str body) {
		headers.addContentTypeHeader("text/html");
		headers.addHostHeader(url.getHost());
		headers.addConnectionHeader("keep-alive");
		if ((method.equals("POST") || method.equals("PUT")) && body!=null) {
			headers.addContentLengthHeader(body.length());
		}
	}
	
	private RunCode getResponseReaderCode() {
		return new RunCode() {
			@Override
			protected boolean run() {
				readResponse();
				return true;
			}
			
		};
	}
	
	private CodeRunner getNewResponseReader() {
		CodeRunner r = new CodeRunner(getResponseReaderCode()) {
			@Override
			protected void caughtException(Exception exception) {
				String msg = "HTTP client response reader caught exception";
				if (lock.isLocked()) {
					logErrorNoLock(msg,exception);
				} else {
					logError(msg,exception);
				}
			}
		};
		r.setSleepMs(0);
		return r;
	}
}
