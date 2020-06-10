package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.Waiter;

public class HttpsClient extends HttpClient {
	private HttpsURLConnection connection	= null;
	
	public HttpsClient(Logger logger) {
		super(logger);
	}

	public HttpsClient(String method, String url) {
		super(method, url);
	}

	public HttpsClient(Logger logger, String method, String url) {
		super(logger, method, url);
	}

	@Override
	public boolean connect() {
		boolean error = false;
		boolean connected = false;
		lock.lock(this);
		if (connection==null) {
			if (!error) {
				try {
					connection = (HttpsURLConnection)url.openConnection();
					connection.setConnectTimeout(1000);
					connection.setReadTimeout(readTimeoutMs);
					connected = true;
				} catch (IOException ex) {
					logErrorNoLock("I/O exception: " + url.getHost(),ex);
					error = true;
				}
			}
			clearWriterAndReaderNoLock();
		}
		lock.unlock(this);
		if (connected) {
			HttpClientManager.connectedClient(this);
		}
		return error;
	}

	@Override
	public boolean disconnect() {
		boolean error = super.disconnect();

		lock.lock(this);
		HttpsURLConnection con = connection;
		lock.unlock(this);
		
		if (con!=null) {
			con.disconnect();
		}
		
		lock.lock(this);
		connection = null;
		lock.unlock(this);

		HttpClientManager.disconnectedClient(this);
		
		return error;
	}

	@Override
	public boolean sendRequest(Str body,boolean keepAlive, boolean addDefaultHeaders) {
		boolean error = false;
		
		error = connect();
		if (error) {
			return error;
		}
		
		lock.lock(this);
		clearResponseNoLock();
		HttpsURLConnection con = connection;
		String mthd = method;
		int readMs = readTimeoutMs;
		if (addDefaultHeaders) {
			addDefaultHeadersNoLock(body);
		}
		HttpHeaderList hdrs = headers.copy();
		lock.unlock(this);
		
		// Send request
		if (con!=null) {
			if (!mthd.equals("CONNECT")) {
				try {
					con.setRequestMethod(mthd);
				} catch (ProtocolException ex) {
					logErrorNoLock("Protocol exception",ex);
					error = true;
				}
			}
			for (HttpHeader header: hdrs.list()) {
				con.addRequestProperty(header.name, header.value);
			}
	
			if ((mthd.equals("POST") || mthd.equals("GET")) && body!=null && body.length()>0) {
				// Send request body
				lock.lock(this);
				PrintWriter wrtr = getWriterNoLock();
				lock.unlock(this);
				if (wrtr!=null) {
					wrtr.print(body);
					wrtr.flush();
				}
			} else if (mthd.equals("CONNECT")) {
				try {
					con.connect();
				} catch (IOException ex) {
					logErrorNoLock("I/O exception",ex);
					error = true;
				}
			}
			
			getRunner().start();
			Waiter.waitTillDone(getRunner(), readMs);
			getRunner().stop();
		}
		
		if (!keepAlive) {
			disconnect();
		}
		
		return error;
	}

	@Override
	protected void readResponse() {
		Str r = new Str();
		lock.lock(this);
		BufferedReader rdr = null;
		if (!method.equals("CONNECT")) {
			rdr = getReaderNoLock();
		}
		HttpsURLConnection con = connection;
		lock.unlock(this);
		
		if (rdr!=null) {
			int c = 0;
			while(c!=-1) {
				try {
					c = rdr.read();
					if (c!=-1) {
						r.sb().append((char) c);
					}
				} catch (IOException ex) {
					logError("I/O exception",ex);
				}
			}
		}
		
		lock.lock(this);
		responseBody = r;
		try {
			if (method.equals("CONNECT")) {
				responseCode = HttpURLConnection.HTTP_OK;
				responseMessage = "Connection established";
				responseHeaders.add("Proxy-agent","Zeesoft-Proxy/1.0");
			} else {
				responseCode = con.getResponseCode();
				responseMessage = con.getResponseMessage();
			}
			for (Entry<String,List<String>> entry: con.getHeaderFields().entrySet()) {
				if (entry.getKey()!=null) {
					responseHeaders.add(entry.getKey(),Str.mergeStringList(entry.getValue(),",").toString());
				}
			}
		} catch (IOException ex) {
			logError("I/O exception",ex);
		}
		lock.unlock(this);
	}
	
	@Override
	protected PrintWriter getWriterNoLock() {
		if (connection!=null && writer==null) {
			try {
				writer = new PrintWriter(connection.getOutputStream());
			} catch (IOException e) {
				logErrorNoLock("Failed to obtain output stream",e);
			}
		}
		return writer;
	}

	@Override
	protected BufferedReader getReaderNoLock() {
		if (connection!=null && reader==null) {
			try {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (IOException e) {
				logErrorNoLock("Failed to obtain input stream",e);
			}
		}
		return reader;
	}
}
