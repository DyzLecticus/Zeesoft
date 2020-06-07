package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	private Lock						lock				= new Lock();
	private Logger						logger				= null;
	
	private String						method				= "";
	private String						url					= "";
	private HttpHeaderList				headers				= new HttpHeaderList();
	private int							readTimeoutMs		= 3000;
	
	private String						currentUrlBase		= "";
	private Socket						socket				= null;
	private BufferedReader				reader				= null;
	private PrintWriter					writer				= null;
	private CodeRunner					runner				= null;
	
	private Str							responseBody		= new Str();
	private int							responseCode		= HttpURLConnection.HTTP_OK;
	private String						responseMessage		= "";
	private HttpHeaderList				responseHeaders		= new HttpHeaderList();
	
	private Str							error				= new Str();
	private Exception					exception			= null;
	
	public HttpClient(Logger logger) {
		this.logger = logger;
		lock.setLogger(this, logger);
		runner = getNewResponseReader();
	}

	public HttpClient(String method,String url) {
		this.method = method;
		this.url = url;
		lock.setLogger(this, logger);
		runner = getNewResponseReader();
	}
	
	public HttpClient(Logger logger,String method,String url) {
		this.logger = logger;
		this.method = method;
		this.url = url;
		lock.setLogger(this, logger);
		runner = getNewResponseReader();
	}
	
	public void setMethod(String method) {
		lock.lock(this);
		this.method = method;
		lock.unlock(this);
	}
	
	public void setUrl(String url) {
		boolean disconnect = false;
		URL conUrl = null;
		try {
			conUrl = new URL(url);
			lock.lock(this);
			this.url = url;
			String urlBase = conUrl.getProtocol() + "://" + conUrl.getHost() + ":" + conUrl.getPort();
			if (this.currentUrlBase.equals(urlBase)) {
				disconnect = socket!=null;
			}
			lock.unlock(this);
		} catch (MalformedURLException ex) {
			logError("Malformed URL: " + url,ex);
		}
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
		
		lock.lock(this);
		if (socket==null) {
			URL conUrl = null;
			try {
				conUrl = new URL(url);
			} catch (MalformedURLException ex) {
				logErrorNoLock("Malformed URL: " + url,ex);
				error = true;
			}
			OutputStream oStream = null;
			InputStream iStream = null;
			if (!error) {
				int port = conUrl.getPort();
				if (port==-1) {
					port = 80;
				}
				try {
					socket = new Socket(conUrl.getHost(),port);
					oStream = socket.getOutputStream();
					iStream = socket.getInputStream();
				} catch (UnknownHostException ex) {
					logErrorNoLock("Unknown host: " + conUrl.getHost(),ex);
					error = true;
				} catch (IOException ex) {
					logErrorNoLock("I/O exception",ex);
					error = true;
				}
			}
			writer = null;
			reader = null;
			if (!error) {
				if (oStream==null) {
					logErrorNoLock("Failed to obtain output stream: " + conUrl.getHost(),null);
					error = true;
				} else {
					writer = new PrintWriter(oStream, true);
				}
				if (iStream==null) {
					logErrorNoLock("Failed to obtain input stream: " + conUrl.getHost(),null);
					error = true;
				} else {
					reader = new BufferedReader(new InputStreamReader(iStream));
				}
			}
		}
		lock.unlock(this);
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
		
		if (!error) {
			runner.start();
		}
		
		lock.lock(this);
		String mthd = method;
		String ul = url;
		HttpHeaderList hdrs = headers.copy();
		int readMs = readTimeoutMs;
		PrintWriter wrtr = writer;
		responseBody = new Str();
		responseCode = HttpURLConnection.HTTP_OK;
		responseMessage = "";
		responseHeaders = new HttpHeaderList();
		lock.unlock(this);
			
		// Send request
		if (wrtr!=null) {
			URL conUrl = null;
			try {
				conUrl = new URL(ul);
			} catch (MalformedURLException e) {
				// Ignore
			}

			if (conUrl!=null) {
				if (addDefaultHeaders) {
					hdrs.addContentTypeHeader("text/html");
					hdrs.addHostHeader(conUrl.getHost());
					hdrs.addConnectionHeader("keep-alive");
					if ((mthd.equals("POST") || mthd.equals("PUT")) && body!=null) {
						hdrs.addContentLengthHeader(body.length());
					}
				}
	
				Str request = new Str();
				request.sb().append(mthd);
				request.sb().append(" ");
				request.sb().append(conUrl.getPath());
				request.sb().append(" ");
				request.sb().append("HTTP/1.1");
				request.sb().append("\r\n");
				request.sb().append(hdrs.toStr());
				request.sb().append("\r\n");
				request.sb().append("\r\n");
				if (!error && (mthd.equals("POST") || mthd.equals("PUT")) && body!=null) {
					request.sb().append(body);
					request.sb().append("\r\n");
				}
				
				wrtr.print(request);
				wrtr.flush();
			}
		}
		
		if (runner.isBusy()) {
			Waiter.waitTillDone(runner, readMs);
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
		Str r = responseBody;
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
	
	protected void logError(String msg,Exception e) {
		lock.lock(this);
		logErrorNoLock(msg,e);
		lock.unlock(this);
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
	
	protected void logErrorNoLock(String msg,Exception e) {
		if (error.length()==0) {
			error.sb().append(msg);
			exception = e;
		}
		if (logger!=null) {
			logger.error(this,new Str(msg),e);
		}
	}
	
	private void readResponse() {
		Str r = new Str();
		lock.lock(this);
		BufferedReader rdr = reader;
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
					if (!sock.isClosed()) {
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
				List<Str> h = head.split(": ");
				responseHeaders.add(h.get(0).toString(),h.get(1).toString());
			}
			int contentLength = responseHeaders.getIntegerValue(HttpHeader.CONTENT_LENGTH);
			lock.unlock(this);
			
			if (contentLength>0) {
				int c = 0;
				for (int i = 0; i < contentLength; i++) {
					try {
						c = rdr.read();
						r.sb().append((char) c);
					} catch (IOException ex) {
						logError("I/O exception",ex);
					}
				}
			}
			
			lock.lock(this);
			responseBody = r;
			lock.unlock(this);
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
		CodeRunner r = new CodeRunner(getResponseReaderCode());
		r.setSleepMs(0);
		return r;
	}
}
