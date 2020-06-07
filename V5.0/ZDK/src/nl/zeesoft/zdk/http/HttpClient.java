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
	private Lock						lock				= new Lock();
	private Logger						logger				= null;
	
	private String						method				= "";
	private String						url					= "";
	private HttpHeaderList				headers				= new HttpHeaderList();
	private int							readTimeoutMs		= 3000;
	
	private BufferedReader				reader				= null;
	private Str							responseBody		= new Str();
	private int							responseCode		= HttpURLConnection.HTTP_OK;
	private String						responseMessage		= "";
	private HttpHeaderList				responseHeaders		= new HttpHeaderList();
	
	private Str							error				= new Str();
	private Exception					exception			= null;
	
	public HttpClient(String method,String url) {
		this.method = method;
		this.url = url;
	}
	
	public HttpClient(Logger logger,String method,String url) {
		lock.setLogger(this, logger);
		this.logger = logger;
		this.method = method;
		this.url = url;
	}

	public HttpHeaderList getHeaders() {
		return headers;
	}
	
	public void setReadTimeoutMs(int readTimeoutMs) {
		this.readTimeoutMs = readTimeoutMs;
	}
	
	public CodeRunner sendRequest() {
		return sendRequest(null);
	}

	public CodeRunner sendRequest(Str body) {
		lock.lock(this);
		responseBody = new Str();
		lock.unlock(this);
		boolean error = false;
		
		// Open stuff
		URL conUrl = null;
		try {
			conUrl = new URL(url);
		} catch (MalformedURLException e) {
			error = true;
		}
		Socket socket = null;
		if (!error) {
			int port = conUrl.getPort();
			if (port==-1) {
				port = 80;
			}
			try {
				socket = new Socket(conUrl.getHost(),port);
			} catch (UnknownHostException ex) {
				logError("Unknown host:" + conUrl.getHost(),ex);
				error = true;
			} catch (IOException ex) {
				logError("I/O exception",ex);
				error = true;
			}
		}
		lock.lock(this);
		responseBody = new Str();
		responseCode = HttpURLConnection.HTTP_OK;
		responseMessage = "";
		responseHeaders = new HttpHeaderList();
		reader = null;
		if (!error) {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException ex) {
				logError("I/O exception",ex);
				error = true;
			}
		}
		lock.unlock(this);
		CodeRunner runner = getResponseReaderRunner();
		runner.start();
		
		PrintWriter writer = null;
		if (!error) {
			try {
				writer = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException ex) {
				logError("I/O exception",ex);
				error = true;
			}
		}
		
		// Send request
		if (!error) {
			headers.addContentTypeHeader("text/html");
			headers.addHostHeader(conUrl.getHost());
			headers.addConnectionHeader("keep-alive");
			if (body!=null) {
				headers.addContentLengthHeader(body.length());
			}

			Str request = new Str();
			request.sb().append(method);
			request.sb().append(" ");
			request.sb().append(conUrl.getPath());
			request.sb().append(" ");
			request.sb().append("HTTP/1.1");
			request.sb().append("\r\n");
			request.sb().append(headers.toStr());
			request.sb().append("\r\n");
			if (!error && (method.equals("POST") || method.equals("PUT")) && body!=null) {
				request.sb().append(body);
				request.sb().append("\r\n");
			}
			
			//System.out.println(">>>");
			//System.out.println(request);
			
			writer.print(request);
			writer.flush();
		}
		
		Waiter.waitTillDone(runner, readTimeoutMs);
		runner.stop();
		
		// Close stuff
		if (writer!=null) {
			writer.close();
			writer = null;
		}
		if (reader!=null) {
			try {
				reader.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		
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
		if (error.length()==0) {
			error.sb().append(msg);
			exception = e;
		}
		if (logger!=null) {
			logger.error(this,new Str(msg),e);
		}
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
	
	private void readResponse() {
		Str r = new Str();
		lock.lock(this);
		BufferedReader rdr = reader;
		lock.unlock(this);
		if (rdr!=null) {
			Str header = new Str();
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
					logError("I/O exception",ex);
					done = true;
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
			}
			
			//System.out.println("<<<");
			//System.out.println(r);
			
			lock.lock(this);
			responseBody = r;
			lock.unlock(this);
		}
	}
	
	private RunCode getResponseReader() {
		return new RunCode() {
			@Override
			protected boolean run() {
				readResponse();
				return true;
			}
			
		};
	}
	
	private CodeRunner getResponseReaderRunner() {
		CodeRunner r = new CodeRunner(getResponseReader());
		r.setSleepMs(0);
		return r;
	}
}
