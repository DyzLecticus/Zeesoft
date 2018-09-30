package nl.zeesoft.zdk.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;

public class ZHttpRequest {
	private Messenger		messenger			= null;
	private String			method				= "";
	private String			url					= "";
	
	private int				connectTimeoutMs	= 1000;
	private int				readTimeoutMs		= 3000;
	private String			encoding			= "UTF-8";
	
	private int				responseCode		= 200;
	private String			error				= "";
	private Exception		exception			= null;
	
	public ZHttpRequest(String method,String url) {
		this.method = method;
		this.url = url;
	}
	
	public ZHttpRequest(Messenger msgr,String method,String url) {
		this.messenger = msgr;
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

	public JsFile sendJsonRequest() {
		return sendJsonRequest(null,null);
	}

	public JsFile sendJsonRequest(ZStringBuilder body) {
		return sendJsonRequest(body,null);
	}

	public JsFile sendJsonRequest(SortedMap<String,String> headers) {
		return sendJsonRequest(null,headers);
	}

	public JsFile sendJsonRequest(ZStringBuilder body,SortedMap<String,String> headers) {
		JsFile r = new JsFile();
		if (headers==null) {
			headers = new TreeMap<String,String>();
		}
		if (!headers.containsKey("Content-type")) {
			headers.put("Content-Type","application/json");
		}
		ZStringBuilder json = sendRequest(body,headers);
		if (json!=null) {
			r.fromStringBuilder(json);
		}
		return r;
	}

	public ZStringBuilder sendRequest() {
		return sendRequest(null,null);
	}

	public ZStringBuilder sendRequest(SortedMap<String,String> headers) {
		return sendRequest(null,headers);
	}
	
	public ZStringBuilder sendRequest(ZStringBuilder body,SortedMap<String,String> headers) {
		ZStringBuilder r = null;
		boolean error = false;
		String enc = encoding;
		if (body!=null) {
			enc = body.getEncoding();
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
					for (Entry<String,String> entry: headers.entrySet()) {
						con.addRequestProperty(entry.getKey(),entry.getValue());
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
		if (!error && method.equals("POST") && body!=null) {
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
					outputStreamWriter = new OutputStreamWriter(outputStream,enc);
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
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
		}
		InputStreamReader inputStreamReader = null;
		if (!error) {
			try {
				inputStreamReader = new InputStreamReader(inputStream,enc);
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
			r = new ZStringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					if (line.length()>0) {
						if (r.length()>0) {
							r.append("\n");
						}
						r.append(line);
					}
				}
			} catch (IOException e) {
				logError("I/O exception",e);
				error = true;
			}
			r.trim();
		}

		if (con!=null) {
			try {
				responseCode = con.getResponseCode();
			} catch (IOException e) {
				logError("Failed to obtain response code",e);
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
	
	protected void logError(String msg,Exception e) {
		if (error.length()==0) {
			error = msg;
			exception = e;
		}
		if (messenger!=null) {
			messenger.error(this,msg,e);
		}
	}

	public String getError() {
		return error;
	}

	public Exception getException() {
		return exception;
	}
}
