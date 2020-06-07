package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Str;

public class HttpClient {
	private Logger			logger				= null;
	private String			method				= "";
	private String			url					= "";
	
	private int				connectTimeoutMs	= 1000;
	private int				readTimeoutMs		= 3000;
	private String			encoding			= "UTF-8";
	
	private int				responseCode		= HttpURLConnection.HTTP_OK;
	private String			responseMessage		= "";
	private String			error				= "";
	private Exception		exception			= null;
	
	public HttpClient(String method,String url) {
		this.method = method;
		this.url = url;
	}
	
	public HttpClient(Logger logger,String method,String url) {
		this.logger = logger;
		this.method = method;
		this.url = url;
	}
	
	public void setConnectTimeoutMs(int timeoutMs) {
		this.connectTimeoutMs = timeoutMs;
	}
	
	public void setReadTimeoutMs(int timeoutMs) {
		this.readTimeoutMs = timeoutMs;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Str sendRequest() {
		return sendRequest(null,null);
	}

	public Str sendRequest(Str body) {
		return sendRequest(body,null);
	}

	public Str sendRequest(List<HttpHeader> headers) {
		return sendRequest(null,headers);
	}
	
	public Str sendRequest(Str body,List<HttpHeader> headers) {
		Str r = null;
		boolean error = false;
		
		if (headers==null) {
			headers = new ArrayList<HttpHeader>();
		}
		if (body!=null) {
			HttpHeader.addContentLengthHeader(headers,body.length());
		}
		
		// Open stuff
		URL conUrl = null;
		try {
			conUrl = new URL(url);
		} catch (MalformedURLException e) {
			error = true;
		}
		HttpURLConnection con = null;
		if (!error) {
			try {
				con = (HttpURLConnection) conUrl.openConnection();
				con.setRequestMethod(method);
				if (body!=null) {
					con.setDoOutput(true);
				}
				con.setConnectTimeout(connectTimeoutMs);
				con.setReadTimeout(readTimeoutMs);
				if (headers!=null) {
					for (HttpHeader header: headers) {
						con.addRequestProperty(header.name,header.value);
					}
				}
			} catch (java.net.SocketTimeoutException e) {
				logError("Socket timed out",e);
				error = true;
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		if (!error && (method.equals("POST") || method.equals("PUT")) && body!=null) {
			if (!error) {
				try {
					outputStream = con.getOutputStream();
				} catch (IOException e) {
					logError("I/O exception",e);
					error = true;
				}
			}
			if (!error) {
				try {
					outputStreamWriter = new OutputStreamWriter(outputStream,encoding);
				} catch (UnsupportedEncodingException e) {
					logError("Unsupported encoding",e);
					error = true;
				}
			}
			if (!error) {
				try {
					outputStreamWriter.write(body.toString());
					outputStreamWriter.flush();
				} catch (IOException e) {
					logError("I/O exception",e);
					error = true;
				}
			}
		}
		
		InputStream inputStream = null;
		if (!error) {
			try {
				inputStream = con.getInputStream();
			} catch (FileNotFoundException e) {
				inputStream = con.getErrorStream();
			} catch (IOException e) {
				inputStream = con.getErrorStream();
				if (inputStream==null) {
					logError("I/O exception",e);
					error = true;
				}
			}
		}
		InputStreamReader inputStreamReader = null;
		if (!error) {
			try {
				inputStreamReader = new InputStreamReader(inputStream,encoding);
			} catch (UnsupportedEncodingException e) {
				logError("Unsupported encoding",e);
				error = true;
			}
		}
		BufferedReader reader = null;
	
		// Read response
		if (!error) {
			reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			r = new Str();
			try {
				while ((line = reader.readLine()) != null) {
					if (line.length()>0) {
						if (r.length()>0) {
							r.sb().append("\n");
						}
						r.sb().append(line);
					}
				}
			} catch (java.net.SocketTimeoutException e) {
				logError("Read timed out",e);
				error = true;
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
			r.trim();
		}

		if (con!=null) {
			try {
				responseCode = con.getResponseCode();
				responseMessage = con.getResponseMessage();
			} catch (IOException e) {
				logError("Failed to obtain response code/message",e);
				error = true;
			}
		}
		
		// Close stuff
		if (outputStreamWriter!=null) {
			try {
				outputStreamWriter.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (outputStream!=null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (reader!=null) {
			try {
				reader.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (inputStreamReader!=null) {
			try {
				inputStreamReader.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (inputStream!=null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		if (con!=null) {
			con.disconnect();
		}
		
		return r;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}
	
	protected void logError(String msg,Exception e) {
		if (error.length()==0) {
			error = msg;
			exception = e;
		}
		if (logger!=null) {
			logger.error(this,new Str(msg),e);
		}
	}

	public String getError() {
		return error;
	}

	public Exception getException() {
		return exception;
	}
}
