package nl.zeesoft.zodb.database.server.protocol;

import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.DbModelConverter;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlLink;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.model.MdlString;
import nl.zeesoft.zodb.database.request.ReqDataObject;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.database.request.ReqObject;
import nl.zeesoft.zodb.database.server.SvrConfig;
import nl.zeesoft.zodb.database.server.SvrController;
import nl.zeesoft.zodb.database.server.SvrHTTPResourceFactory;
import nl.zeesoft.zodb.database.server.SvrProtocolObject;
import nl.zeesoft.zodb.database.server.SvrServer;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;
import nl.zeesoft.zodb.file.xml.XMLElem;
import nl.zeesoft.zodb.file.xml.XMLFile;

public class PtcServer extends SvrProtocolObject implements EvtEventSubscriber {
	private static final int	MAX_AUTHORIZE_ATTEMPTS		= 3;
	private static final String	UNAUTHORIZED_GET_MESSAGE	= "You are not authorized to GET this data";
	private static final String	UNAUTHORIZED_POST_MESSAGE	= "You are not authorized to POST this data";
	
	private boolean 			waitingForRequest 			= false;
	private ReqObject 			request 					= null;
	private Object 				isLockedBy 					= null;
	
	public PtcServer(SvrServer server,Socket socket) {
		super(server,socket);
	}
	
	@Override
	protected Object processInputAndReturnOutput(StringBuilder input) {
		Object output = null;

		if (getMethod().length()>0 && getUrl()!=null) {
			StringBuilder response = null;
			
			String status = "200 OK";
			List<String> parameters = new ArrayList<String>();
			SortedMap<String,String> cookies = new TreeMap<String,String>();
			
			if (getMethod().equals("GET")) {
				String getURL = getUrl().getPath();
				if (getURL.equals("/")) {
					getURL = getServer().getIndexHTML();
				} else if (getURL.equals("/favicon.ico") || getURL.equals("/favicon.png")) {
					getURL = "favicon.png";
				} else {
					getURL = getURL.substring(1);
				}
				if (response==null) {
					if (getUrl().getPath().equals("/sessionAuthorized.xml")) {
						response = generateRESTGetSessionAuthorizedResponse();
						checkSession();
					} else {
						if (getUrl().getPath().startsWith("/data/") && getUrl().getPath().endsWith(".xml")) {
							if (SvrConfig.getInstance().isAuthorizeGetRequests() && (getSession()==null || !getSession().isAuthorized())) {
								response = get401Response().toStringBuilder();
								status = "401 Unauthorized";
							} else {
								response = generateRESTGetURLResponse();
							}
						} else if (getUrl().getPath().equals("/databaseModel.xml")) {
							if (SvrConfig.getInstance().isAuthorizeGetRequests() && (getSession()==null || !getSession().isAuthorized())) {
								response = get401Response().toStringBuilder();
								status = "401 Unauthorized";
							} else {
								response = generateRESTGetDatabaseModelResponse();
							}
						} else if (getUrl().getPath().equals("/modelChanged.xml")) {
							if (SvrConfig.getInstance().isAuthorizeGetRequests() && (getSession()==null || !getSession().isAuthorized())) {
								response = get401Response().toStringBuilder();
								status = "401 Unauthorized";
							} else {
								response = generateRESTGetModelChangedResponse();
							}
						} else if (getUrl().getPath().equals("/modelChanges.xml")) {
							if (SvrConfig.getInstance().isAuthorizeGetRequests() && (getSession()==null || !getSession().isAuthorized())) {
								response = get401Response().toStringBuilder();
								status = "401 Unauthorized";
							} else {
								response = generateRESTGetModelChangesResponse();
							}
						}
					}
				}
				if (response==null) {
					response = getServer().getCachedHTTPResourceByName(getURL);
					if (response!=null && 
						(
							getURL.equals(SvrHTTPResourceFactory.AUTHORIZATION_MANAGER_HTML) || 
							getURL.equals(SvrHTTPResourceFactory.AUTHORIZER_HTML)
						)
						) {
						checkSession();
					}
				}
				if (response==null) {
					if (getURL.endsWith(".ico") || 
						getURL.endsWith(".png")
						) {
						byte[] img = getServer().getCachedHTTPImageResourceByName(getURL); 
						if (img==null) {
							img = SvrController.getInstance().readImage(getURL);
						}
						if (img!=null) {
							setImage(img);
							response = new StringBuilder();
						}
					}
				}
				if (response==null) {
					status = "404 Not Found";
					if (getUrl().getPath().endsWith(".xml")) {
						response = get404Response().toStringBuilder();
					} else if (getUrl().getPath().endsWith(".html")) {
						response = getServer().getCachedHTTPResourceByName(SvrHTTPResourceFactory.RESOURCE_NOT_FOUND_HTML);
					} 
					if (response==null) {
						response = new StringBuilder();
					}
				}
			} else if (getMethod().equals("POST")) {
				if (getUrl().getPath().equals("/updateModel.xml")) {
					response = generateRESTPostUpdateModelResponse();
				} else if (getUrl().getPath().equals("/revertModel.xml")) {
					response = generateRESTPostUpdateDbModelResponse();
				} else if (getBody().length()==0) {
					Messenger.getInstance().error(this,"Body is empty");
					status = "400 Bad Request";
					response = get400Response().toStringBuilder();
				} else if (getBody().length()>0 && getBody().substring(0,1).equals("<")) {
					response = generateRESTPostBodyResponse();
					if (response==null) {
						status = "400 Bad Request";
					} else if (Generic.stringBuilderStartsWith(response,get401Response().toString())) {
						status = "401 Unauthorized";
					} else if (getSession()!=null && getSession().getAuthorizeAttempts()>=MAX_AUTHORIZE_ATTEMPTS) {
						removeSession();
					}
				} else {
					Messenger.getInstance().error(this,"Body is not XML: " + getBody());
					status = "400 Bad Request";
				}
			}

			if (getUrl().getPath().endsWith(".html")) {
				parameters.add("Content-Type: text/html; charset=UTF-8");
			} else if (getUrl().getPath().endsWith(".xml")) {
				parameters.add("Content-Type: text/xml; charset=UTF-8");
			} else if (getUrl().getPath().endsWith(".js")) {
				parameters.add("Content-Type: application/javascript; charset=UTF-8");
			} else if (getUrl().getPath().endsWith(".css")) {
				parameters.add("Content-Type: text/css; charset=UTF-8");
			} else if (getUrl().getPath().endsWith(".txt")) {
				parameters.add("Content-Type: text/plain; charset=UTF-8");
			} else if (getUrl().getPath().endsWith(".ico")) {
				parameters.add("Content-Type: image/ico");
			} else if (getUrl().getPath().endsWith(".png")) {
				parameters.add("Content-Type: image/png");
			} else {
				parameters.add("Content-Type: text/html; charset=UTF-8");
			}
			
			if (getImage()!=null) {
				parameters.add("Accept-Ranges: bytes");
				parameters.add("Content-Length: " + getImage().length);
				parameters.add("Connection: close");
			} else if (response!=null) {
				parameters.add("Allow: GET, POST");
				parameters.add("Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
				parameters.add("Pragma: no-cache");
				parameters.add("Connection: " + getParameters().get("Connection"));

				byte[] outputBytes = response.toString().getBytes();
				parameters.add("Content-Length: " + outputBytes.length);
			}
			addHttpHeaderToOutput(response,status,parameters,cookies);
			output = response;
		}
		
		return output;
	}
	
	@Override
	public void handleEvent(EvtEvent e) {
		lock(this);
		if (request!=null && request == e.getValue()) {
			request = (ReqObject) e.getValue();
			if (request instanceof ReqGet && !request.hasError()) {
				MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(request.getClassName());
				List<String> encodeProps = new ArrayList<String>();
				for (MdlProperty prop: cls.getPropertiesExtended()) {
					if (prop instanceof MdlString && ((MdlString)prop).isEncode()) {
						encodeProps.add(prop.getName());
					}
				}
				if (encodeProps.size()>0) {
					for (ReqDataObject object: request.getObjects()) {
						DbDataObject obj = object.getDataObject();
						for (String propName: encodeProps) {
							StringBuilder val = obj.getPropertyValue(propName);
							if (val!=null && val.length()>0) {
								obj.setPropertyValue(propName,Generic.encodeAscii(val));
							}
						}
					}
				}
			}
			waitingForRequest = false;
		}
		unlock(this);
	}

	/**************************** PRIVATE METHODS **************************/
	private synchronized void lock(Object source) {
		int attempt = 0;
		while (isLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	// Ignore
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,Locker.getLockFailedMessage(attempt,source));
				attempt = 0;
			}
		}
		isLockedBy = source;
	}

	private synchronized void unlock(Object source) {
		if (isLockedBy==source) {
			isLockedBy = null;
			notifyAll();
		} else {
			Messenger.getInstance().error(this,"Invalid attempt to unlock by source: " + source);
		}
	}
	
	private synchronized boolean isLocked() {
		return isLockedBy!=null;
	}
	
	private StringBuilder generateRESTPostBodyResponse() {
		StringBuilder response = null;
		XMLFile reqXML = new XMLFile(); 
		String err = reqXML.parseXML(getBody());
		if (err.length()==0) {
			PtcServerResponse resp = null;
			if (reqXML.getRootElement().getName().equals("authorize")) {
				if (getSession()==null) {
					resp = new PtcServerResponse("No session found to authorize");
				} else {
					StringBuilder pwd = null;
					getSession().setAuthorized(false);
					XMLElem pwdElem = reqXML.getRootElement().getChildByName("password");
					if (pwdElem!=null && pwdElem.getValue()!=null && pwdElem.getValue().length()>0) {
						pwd = DbConfig.getInstance().encodePassword(pwdElem.getValue());
						if (Generic.stringBuilderEquals(pwd,SvrConfig.getInstance().getAuthorizePassword())) {
							getSession().setAuthorized(true);
							String url = "/" + SvrHTTPResourceFactory.INDEX_HTML;
							if (getSession().getAuthorizeRedirectUrl().length()>0) {
								url = getSession().getAuthorizeRedirectUrl();
							}
							resp = new PtcServerResponse("",url);
						} else {
							resp = new PtcServerResponse("Failed to authorize session");
						}
					} else {
						resp = new PtcServerResponse("No password value found in authorize request");
					}
					XMLElem upwdElem = reqXML.getRootElement().getChildByName("updatePassword");
					if (upwdElem!=null && upwdElem.getValue()!=null && upwdElem.getValue().length()>=8) {
						if (pwd!=null && pwd.length()>0 && getSession().isAuthorized()) {
							StringBuilder upwd = DbConfig.getInstance().encodePassword(upwdElem.getValue());
							if (!Generic.stringBuilderEquals(pwd,upwd)) {
								SvrConfig.getInstance().setAuthorizePassword(upwd);
								SvrConfig.getInstance().serialize();
							} else {
								resp = new PtcServerResponse("Update password equals current password");
							}
						}
					} else if (upwdElem!=null && upwdElem.getValue()!=null && upwdElem.getValue().length()<8) {
						resp = new PtcServerResponse("Password value must be at least 8 characters long");
					}
					if (!getSession().isAuthorized()) {
						getSession().setAuthorizeAttempts(getSession().getAuthorizeAttempts() + 1);
						int remaining = (MAX_AUTHORIZE_ATTEMPTS - getSession().getAuthorizeAttempts());
						resp.setValue("" + remaining);
						if (getSession().getAuthorizeAttempts()>=MAX_AUTHORIZE_ATTEMPTS) {
							//Let client decide using remaining attempts
							//resp.setRedirect(SvrHTTPResourceFactory.AUTHORIZER_HTML);
						}
					}
				}
			} else {
				ReqObject req = ReqObject.abstractfromXML(reqXML.getRootElement());
				if (req!=null) {
					if (SvrConfig.getInstance().isAuthorizePostRequests() && (getSession()==null || !getSession().isAuthorized())) {
						resp = get401Response();
					} else {
						response = executeRequestAndWaitForResponse(req);
					}
				} else {
					resp = new PtcServerResponse("Unable to understand request");
				}
			}
			if (resp!=null) {
				response = resp.toStringBuilder();
			}
		} else {
			Messenger.getInstance().error(this,"Body XML parse error: " + err);
		}
		reqXML.cleanUp();
		return response;
	}

	private StringBuilder generateRESTPostUpdateModelResponse() {
		StringBuilder response = null;
		PtcServerResponse resp = new PtcServerResponse("Abstract data model update initiated. The server may be unavailable for several minutes.");
		response = resp.toStringBuilder();
		DbController.getInstance().update();
		return response;
	}

	private StringBuilder generateRESTPostUpdateDbModelResponse() {
		StringBuilder response = null;
		boolean reverted = DbController.getInstance().updateDbModel();
		PtcServerResponse resp = new PtcServerResponse("Failed to revert database data model changes");
		if (reverted) {
			resp.setMessage("Database data model changes been reverted");
		}
		response = resp.toStringBuilder();
		return response;
	}
	
	private StringBuilder generateRESTGetURLResponse() {
		StringBuilder response = null;
		String[] pathParts = getUrl().getPath().split("/");
		String fileName = pathParts[(pathParts.length - 1)];
		String className = fileName.replace(".xml","");
		MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className);
		if (cls!=null) {
			ReqGet get = new ReqGet(className);
			if (getUrl().getQuery()!=null && getUrl().getQuery().length()>0) {
				String query = "";
				try {
					query = getUrl().toURI().getQuery();
				} catch (URISyntaxException e) {
					Messenger.getInstance().error(this,"Failed to parse query: " + getUrl().getQuery());
				}
				String[] keyVals = new String[0];
				if (query!=null && query.length()>0) {
					keyVals = query.split("&");
				}
				for (int i = 0; i < keyVals.length; i++) {
					String[] keyVal = keyVals[i].split("=");
					MdlProperty prop = cls.getPropertyByName(keyVal[0]);
					if (prop!=null) {
						if (prop instanceof MdlLink) {
							get.addFilter(prop.getName(),ReqGetFilter.CONTAINS,keyVal[1]);
						} else {
							get.addFilter(prop.getName(),ReqGetFilter.EQUALS,keyVal[1]);
						}
					} else {
						if (keyVal[0].equals("_orderBy") && keyVal.length>1 && keyVal[1].length()>0) {
							get.setOrderBy(keyVal[1]);
						}
						if (keyVal[0].equals("_orderAscending") && keyVal.length>1 && keyVal[1].length()>0) {
							try {
								get.setOrderAscending(Boolean.parseBoolean(keyVal[1]));
							} catch(Exception e) {
								Messenger.getInstance().error(this,"Unable to parse boolean input: " + keyVal[1]);
							}
						}
						if (keyVal[0].equals("_start") && keyVal.length>1 && keyVal[1].length()>0) {
							try {
								get.setStart(Integer.parseInt(keyVal[1]));
							} catch(NumberFormatException e) {
								Messenger.getInstance().error(this,"Unable to integer boolean input: " + keyVal[1]);
							} 
						}
						if (keyVal[0].equals("_limit") && keyVal.length>1 && keyVal[1].length()>0) {
							try {
								get.setLimit(Integer.parseInt(keyVal[1]));
							} catch(NumberFormatException e) {
								Messenger.getInstance().error(this,"Unable to integer boolean input: " + keyVal[1]);
							} 
						}
						if (keyVal[0].equals("_properties") && keyVal.length>1 && keyVal[1].length()>0) {
							String props[] = keyVal[1].split(",");
							for (int p = 0; p < props.length; p++) {
								get.getProperties().add(props[p]);
							}
						}
						if (keyVal[0].equals("_childIndexes") && keyVal.length>1 && keyVal[1].length()>0) {
							String cidxs[] = keyVal[1].split(",");
							for (int p = 0; p < cidxs.length; p++) {
								get.getChildIndexes().add(cidxs[p]);
							}
						}
						if (keyVal[0].equals("_filterStrict") && keyVal.length>1 && keyVal[1].length()>0) {
							get.setFilterStrict(Boolean.parseBoolean(keyVal[1]));
						}
					}
				}
			}
			response = executeRequestAndWaitForResponse(get);
		}
		return response;
	}

	private StringBuilder generateRESTGetDatabaseModelResponse() {
		StringBuilder response = null;
		MdlModel updateModel = DbConfig.getInstance().getNewModel();
		List<String> originalFullNames = updateModel.getFullNames();
		updateModel.unserialize(MdlModel.getFullFileName());
		DbModelConverter converter = new DbModelConverter();
		boolean converted = converter.updateMdlModel(updateModel,originalFullNames,true);
		if (!converted && converter.getUpdateLog().length()==0) {
			PtcServerResponse resp = new PtcServerResponse("Error loading differences","","");
			response = resp.toStringBuilder();
		} else {
			XMLFile updateXML = updateModel.toXML();
			response = updateXML.toStringReadFormat();
			updateXML.cleanUp();
		}
		updateModel.cleanUp();
		return response;
	}

	private StringBuilder generateRESTGetSessionAuthorizedResponse() {
		PtcServerResponse resp = null;
		if (getSession()!=null) {
			resp = new PtcServerResponse("","","" + getSession().isAuthorized());
		} else {
			if (!SvrConfig.getInstance().isAuthorizeGetOrPostRequests()) {
				resp = new PtcServerResponse("Authorization is not required for GET or POST requests","","false");
			} else {
				resp = new PtcServerResponse("","","false");
			}
		}
		resp.toStringBuilder();
		return resp.toStringBuilder();
	}

	private StringBuilder getCompareDbToMdlModelLog() {
		MdlModel updateModel = DbConfig.getInstance().getNewModel();
		List<String> originalFullNames = updateModel.getFullNames();
		updateModel.unserialize(MdlModel.getFullFileName());
		DbModelConverter converter = new DbModelConverter();
		boolean converted = converter.updateMdlModel(updateModel,originalFullNames,true);
		StringBuilder log = converter.getUpdateLog();
		if (!converted && log.length()==0) {
			log = new StringBuilder("<b>ERROR:</b> Error loading differences");
		}
		updateModel.cleanUp();
		return log;
	}

	private StringBuilder generateRESTGetModelChangedResponse() {
		boolean changed = getCompareDbToMdlModelLog().length()>0;
		PtcServerResponse resp = new PtcServerResponse("","","" + changed);
		return resp.toStringBuilder();
	}

	private StringBuilder generateRESTGetModelChangesResponse() {
		StringBuilder diff = getCompareDbToMdlModelLog();
		PtcServerResponse resp = null;
		if (diff.length()>0) {
			diff = Generic.stringBuilderReplace(diff,"\n","<br/>");
			resp = new PtcServerResponse("","",diff.toString());
		} else {
			resp = new PtcServerResponse("The abstract data model matches the database data model","","false");
		}
		return resp.toStringBuilder();
	}
	
	private StringBuilder executeRequestAndWaitForResponse(ReqObject req) {
		StringBuilder response = null;
		lock(this);
		if (req instanceof ReqGet) {
			ReqGet r = (ReqGet) req;
			if (r.getProperties().size()==0) {
				r.getProperties().add(ReqGet.ALL_PROPERTIES);
			}
			if (r.getLimit()==0 || r.getLimit()>SvrConfig.getInstance().getGetRequestLimit()) {
				r.setLimit(SvrConfig.getInstance().getGetRequestLimit());
			}
		}
		waitingForRequest = true;
		request = req;
		request.addSubscriber(this);
		unlock(this);
		DbRequestQueue.getInstance().addRequest(request, this);
		while(waitingForRequest()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Messenger.getInstance().error(this,"Waiting for database request response was interrupted");
			}
		}
		lock(this);
		XMLFile file = request.toXML();
		response = file.toStringBuilder();
		file.cleanUp();
		request = null;
		unlock(this);
		return response;
	}
	
	private boolean waitingForRequest() {
		boolean r = false;
		lock(this);
		r = waitingForRequest;
		unlock(this);
		return r;
	}
	
	private PtcServerResponse get404Response() {
		return new PtcServerResponse("Resource not found","/" + SvrHTTPResourceFactory.INDEX_HTML,"404");
	}
 
	private PtcServerResponse get400Response() {
		return new PtcServerResponse("Bad request","","400");
	}

	private PtcServerResponse get401Response() {
		String message = "";
		if (getMethod().equals("GET")) {
			message = UNAUTHORIZED_GET_MESSAGE;
		} else if (getMethod().equals("POST")) {
			message = UNAUTHORIZED_POST_MESSAGE;
		}
		return new PtcServerResponse(message,"/" + SvrHTTPResourceFactory.AUTHORIZER_HTML,"401");
	}
}
