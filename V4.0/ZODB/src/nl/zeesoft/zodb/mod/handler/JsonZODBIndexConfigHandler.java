package nl.zeesoft.zodb.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.idx.IndexRequest;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.ModZODB;

public class JsonZODBIndexConfigHandler extends JsonZODBHandlerObject {
	public final static String	PATH	= "/indexConfig.json"; 
	
	public JsonZODBIndexConfigHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		setAllowGet(true);
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		if (method.equals(METH_GET)) {
			ModZODB zodb = getZODB(request,response);
			if (zodb!=null) {
				r = checkRequest(zodb,request,response);
				if (r.length()==0) {
					r = stringifyJson(zodb.getDatabase().getIndexConfig().toUpdateJson());
				}
			}
		} else if (method.equals(METH_POST)) {
			JsFile json = getPostBodyJson(request, response);
			if (json.rootElement==null) {
				r = setResponse(response,400,"Failed to parse JSON");
			} else {
				ModZODB zodb = getZODB(request,response);
				if (zodb!=null) {
					r = checkRequest(zodb,request,response);
					if (r.length()==0) {
						IndexRequest req = new IndexRequest();
						req.fromJson(json);
						if (req.type.length()==0) {
							r = setResponse(response,400,"Request type is mandatory");
						} else if (
							!req.type.equals(IndexRequest.TYPE_LIST) && 
							!req.type.equals(IndexRequest.TYPE_GET) && 
							!req.type.equals(IndexRequest.TYPE_ADD) && 
							!req.type.equals(IndexRequest.TYPE_REMOVE)
							) {
							r = setResponse(response,400,"Request type must be " + IndexRequest.TYPE_LIST + ", " + IndexRequest.TYPE_GET + ", " + IndexRequest.TYPE_ADD + " or " + IndexRequest.TYPE_REMOVE);
						} else if (req.type.equals(IndexRequest.TYPE_LIST)) {
							r = stringifyJson(zodb.getDatabase().getIndexConfig().toJson());
						} else if (req.type.equals(IndexRequest.TYPE_GET)) {
							r = stringifyJson(zodb.getDatabase().getIndexConfig().toUpdateJson());
						} else if (req.type.equals(IndexRequest.TYPE_ADD)) {
							req.objectNamePrefix = Database.removeSpecialCharacters(req.objectNamePrefix);
							req.propertyName = Database.removeSpecialCharacters(req.propertyName);
							if (req.objectNamePrefix.length()==0) {
								r = setResponse(response,400,"Request objectNamePrefix is mandatory");
							} else if (req.propertyName.length()==0) {
								r = setResponse(response,400,"Request propertyName is mandatory");
							} else {
								ZStringBuilder err = zodb.getDatabase().getIndexConfig().addIndex(req.objectNamePrefix,req.propertyName,req.numeric,req.unique);
								if (err.length()>0) {
									r = setResponse(response,400,err.toString());
								} else {
									r = setResponse(response,200,"Added index " + req.getName());
								}
							}
						} else if (req.type.equals(IndexRequest.TYPE_REMOVE)) {
							req.name = Database.removeSpecialCharacters(req.name);
							if (req.name.length()==0) {
								r = setResponse(response,400,"Request name is mandatory");
							} else {
								ZStringBuilder err = zodb.getDatabase().getIndexConfig().removeIndex(req.name);
								if (err.length()>0) {
									r = setResponse(response,400,err.toString());
								} else {
									r = setResponse(response,200,"Removed index " + req.name);
								}
							}
						}
					}
				}
			}
		}
		return r;
	}
	
	private ZStringBuilder checkRequest(ModZODB zodb,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		if (zodb==null) {
			r = setResponse(response,405,"ZODB module not found");
		} else if (!zodb.getWhiteList().isAllowed(request.getRemoteAddr())) {
			r = setResponse(response,403,"ZODB module does not allow requests from " + request.getRemoteAddr());
		} else if (!zodb.getDatabase().isOpen()) {
			r = setResponse(response,503,DatabaseRequestHandler.RESPONSE_CLOSED);
		}
		return r;
	}
}
