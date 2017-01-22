package nl.zeesoft.zodb.database.server;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;

public abstract class SvrProtocolObject extends Locker {
	protected static final String		URL_PREFIX	= "http://localhost";
	
	private String 						method 		= "";				
	private URL							url			= null;
	private SortedMap<String,String> 	parameters 	= new TreeMap<String,String>();
	private SortedMap<String,String> 	cookies 	= new TreeMap<String,String>();
	private StringBuilder				body 		= new StringBuilder();				

	private SvrServer					server		= null;
	private Socket 						socket 		= null;
	private SvrSession					session		= null;
	private boolean						initialized = false;
	private boolean						removed 	= false;
	
	private byte[]						image		= null;
	
	public SvrProtocolObject(SvrServer server,Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	protected void cleanUp() {
		lockMe(this);
		reset();
		server = null;
		socket = null;
		unlockMe(this);
	}

	private void reset() {
		method = "";
		url = null;
		parameters.clear();
		cookies.clear();
		body = new StringBuilder();
		session = null;
		initialized = false;
		removed = false;
		image = null;
	}

	// @Override this method to implement
	protected abstract Object processInputAndReturnOutput(StringBuilder input); 

	// Override to extend protocol
	protected Object processIONotRecognized(StringBuilder input) {
		List<String> parameters = new ArrayList<String>();
		parameters.add("Allow: GET, POST");
		parameters.add("Connection: close");
		parameters.add("Content-Length: 0");
		return addHttpHeaderToOutput(new StringBuilder(),"400 Bad Request",parameters,null);
	}

	// Called by socket worker
	protected Object processIO(StringBuilder input) {
		lockMe(this);
		reset();
		Object r = null;
		if (
			Generic.stringBuilderStartsWith(input,"GET ") ||
			Generic.stringBuilderStartsWith(input,"POST ")
			) {
			StringBuilder header = parseHttpHeaderFromInput(input);
			if (SvrConfig.getInstance().isDebugHTTPHeaders()) {
				Messenger.getInstance().debug(this,"Header:\n" + header);
			}
			body = new StringBuilder();
			if (Generic.stringBuilderStartsWith(input,"POST ")) {
				for (int i = header.length(); i < input.length(); i++) {
					body.append(input.substring(i,(i+1)));
				}
			}
			parseMethodAndUrlFromHttpHeader(header);
			parseParametersFromHttpHeader(header);
			r = processInputAndReturnOutput(input);
		} else {
			r = processIONotRecognized(input);
		}
		unlockMe(this);
		return r;
	}	
	
	/**
	 * @return the method
	 */
	protected String getMethod() {
		return method;
	}

	/**
	 * @return the url
	 */
	protected URL getUrl() {
		return url;
	}

	/**
	 * @return the parameters
	 */
	protected SortedMap<String,String> getParameters() {
		return parameters;
	}

	/**
	 * @return the cookies
	 */
	protected SortedMap<String,String> getCookies() {
		return cookies;
	}

	/**
	 * @return the session
	 */
	protected SvrSession getSession() {
		return session;
	}

	/**
	 * @return the server
	 */
	protected SvrServer getServer() {
		return server;
	}

	/**
	 * @return the body
	 */
	protected StringBuilder getBody() {
		return body;
	}

	/**
	 * @param image the image to set
	 */
	protected void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * @return the image
	 */
	protected byte[] getImage() {
		return image;
	}

	protected void checkSession() {
		if (SvrConfig.getInstance().isAuthorizeGetOrPostRequests()) {
			if (session==null) {
				session = server.getNewSessionForSocket(socket,this);
				if (session!=null) {
					initialized = true;
				}
			}
			if (session!=null) {
				String referer = getParameters().get("Referer");
				if (referer!=null && referer.length()>0 && 
					!referer.endsWith(SvrHTTPResourceFactory.AUTHORIZER_HTML) &&
					!referer.endsWith(SvrHTTPResourceFactory.AUTHORIZATION_MANAGER_HTML)
					) {
					session.setAuthorizeRedirectUrl(referer);
				}
			}
		}
	}

	protected void removeSession() {
		if (SvrConfig.getInstance().isAuthorizeGetOrPostRequests()) {
			if (session!=null) {
				server.removeSession(session.getId(),this);
				session = null;
				removed = true;
			}
		}
	}
	
	protected StringBuilder addHttpHeaderToOutput(StringBuilder output, String status, List<String> parameters, SortedMap<String,String> cookies) {
		if (cookies==null) {
			cookies = new TreeMap<String,String>();
		}
		cookies.put("debug",SvrController.getInstance().isDebug() + "; MaxAge=60");
		if (SvrConfig.getInstance().isAuthorizeGetOrPostRequests()) {
			if (initialized && session!=null) {
				cookies.put("sessionid","" + encodeSessionId(session.getId()));
				initialized = false;
			} else if (removed) {
				cookies.put("sessionid","");
			}
		} else if (session==null){
			cookies.put("sessionid","");
		}
		StringBuilder header = new StringBuilder();
		if (status!=null && status.length()>0) {
			header.append("HTTP/1.1 " + status);
		} else {
			header.append("HTTP/1.1 200 OK");
		}
		header.append("\r\n");
		if (parameters!=null && parameters.size()>0) {
			for (String parameter: parameters) {
				header.append(parameter);
				header.append("\r\n");
			}
		}
		if (cookies!=null && cookies.size()>0) {
			for (Entry<String,String> entry: cookies.entrySet()) {
				header.append("Set-Cookie: ");
				header.append(entry.getKey());
				header.append("=");
				header.append(entry.getValue());
				header.append("\r\n");
			}
		}
		if (SvrConfig.getInstance().isDebugHTTPHeaders()) {
			Messenger.getInstance().debug(this,"Header:\n" + header);
		}
		header.append("\r\n");
		output.insert(0,header);
		return output;
	}
	
	private StringBuilder parseHttpHeaderFromInput(StringBuilder input) {
		StringBuilder header = new StringBuilder();
		String c = "";
		int l = 0;
		for (int i=0; i<input.length(); i++) {
			c = input.substring(i,(i+1));
			if (c.equals("\n")) {
				if (l==0 || l==1) {
					break;
				}
				l = 0;
			} else {
				l++;
			}
			if (!c.equals("\r")) {
				header.append(c);
			}
		}
		header.append("\n");
		return header;
	}

	private void parseMethodAndUrlFromHttpHeader(StringBuilder header) {
		method = "";
		url = null;
		StringBuilder mtd = new StringBuilder();
		StringBuilder u = new StringBuilder();
		int space = 0;
		for (int i = 0; i < header.length(); i++) {
			String c = header.substring(i,(i+1));
			if (c.equals(" ")) {
				space++;
			} else {
				if (space==0) {
					mtd.append(c);
				} else if (space==1) {
					u.append(c);
				} else if (space>0) {
					break;
				}
			}
		}
		method = mtd.toString();
		try {
			url = new URL(URL_PREFIX + u.toString());
		} catch (MalformedURLException e) {
			Messenger.getInstance().debug(this,"Failed to parse url: " + URL_PREFIX + u.toString());
		}
	}

	private void parseParametersFromHttpHeader(StringBuilder header) {
		initialized = false;
		removed = false;
		parameters.clear();
		cookies.clear();
		session = null;
		
		StringBuilder key = new StringBuilder();
		StringBuilder value = new StringBuilder();
		
		int colon = 0;
		int line = 0;
		for (int i = 0; i < header.length(); i++) {
			String c = header.substring(i,(i+1));
			if (c.equals("\n")) {
				if (line>0) {
					if (key.length()>0 && value.length()>0) {
						String k = key.toString().trim();
						String v = value.toString().trim();
						if (k.equals("Cookie")) {
							String[] cooks = v.split("; "); 
							for (int cook = 0; cook < cooks.length; cook++) {
								String[] kv = cooks[cook].split("=");
								if (kv.length>1) {
									if (kv[0].equals("sessionid")) {
										if (SvrConfig.getInstance().isAuthorizeGetOrPostRequests()) {
											if (session==null) {
												session = getSessionUsingCookie(kv[1]);
												if (session!=null) {
													cookies.put(kv[0],kv[1]);
												}
											}
										}
									} else {
										cookies.put(kv[0],kv[1]);
									}
								} else {
									cookies.put(kv[0],"");
								}
							}
						}
						parameters.put(key.toString().trim(), value.toString().trim());
						key = new StringBuilder();
						value = new StringBuilder();
					}
				}
				line++;
				colon = 0;
			} else if (c.equals(":")) {
				colon++;
				if (colon>1) {
					value.append(c);
				}
			} else if (line>=1 && !c.equals("\r")){
				if (colon==0) {
					key.append(c);
				} else if (colon>=1) {
					value.append(c);
				}
			}
		}
	}

	private SvrSession getSessionUsingCookie(String id) {
		SvrSession s = null;
		if (id!=null && id.length()>0) {
			long sessionId = decodeSessionId(id);
			s = server.getSessionById(sessionId,this);
		}
		return s;
	}

	private String encodeSessionId(long id) {
		StringBuilder enc = new StringBuilder("" + id);
		while (enc.length()<10) {
			enc.insert(0,"0");
		}
		enc = Generic.encodeKey(enc, server.getEncryptionKey(),0);
		return enc.toString();
	}

	private long decodeSessionId(String encodedId) {
		long id = 0;
		if (encodedId.length()>=3) {
			StringBuilder dec = Generic.decodeKey(new StringBuilder(encodedId), server.getEncryptionKey(),0);
			if (dec.length()>=3) {
				try {
					id = Long.parseLong(dec.toString());
					//Messenger.getInstance().debug(this,"Parsed sessionId from: " + encodedId + " => " + dec);
				} catch (NumberFormatException e) {
					// Ignore; might be old cookie
					//Messenger.getInstance().debug(this,"Unable to parse sessionId from: " + encodedId + " => " + dec);
				}
			}
		}
		return id;
	}
}
