package nl.zeesoft.zids.server.requesthandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zids.server.resource.RscDataIndexHtml;
import nl.zeesoft.zids.server.resource.RscDataPosterHtml;
import nl.zeesoft.zids.server.resource.RscErrorHtml;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.MdlClass;
import nl.zeesoft.zodb.database.model.MdlProperty;
import nl.zeesoft.zodb.database.request.ReqGet;
import nl.zeesoft.zodb.database.request.ReqGetFilter;
import nl.zeesoft.zodb.event.EvtEvent;

public class RhGetData extends RhRequestHandlerObject {
	private ReqGet getRequest = null;

	@Override
	public void handleEvent(EvtEvent e) {
		if (getRequest!=null && e.getValue()!=null && e.getValue().equals(getRequest)) {
			setDone(true);
		}
	}
	
	@Override
	public String getPath() {
		return "/data";
	}

	@Override
	public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		if (request.getServletPath().equals(getFileAlias()) ||
			request.getServletPath().equals(getPath()) || 
			request.getServletPath().equals(getPathAlias())
			) {
			RscDataIndexHtml index = new RscDataIndexHtml(getTitle(),getBackgroundColor());
			index.addToResponse(response);
		} else if (request.getServletPath().endsWith("poster.html")) {
			RscDataPosterHtml poster = new RscDataPosterHtml(getTitle(),getBackgroundColor());
			poster.addToResponse(response);
		} else {
			String className = request.getServletPath().substring(getPathAlias().length());
			className = className.replaceAll("/","");
			if (className.endsWith(".json")) {
				className = className.replace(".json","");
			}
			MdlClass cls = DbConfig.getInstance().getModel().getClassByFullName(className);
			if (cls!=null) {
				ReqGet get = new ReqGet(className);
				Map<String,String[]> params = request.getParameterMap();
				if (params.size()>0) {
					for (String key: params.keySet()) {
						if (key.startsWith("_")) {
							try {
								if (key.equals("_start") && params.get(key).length>0) {
									get.setStart(Integer.parseInt(params.get(key)[0]));
								} else if (key.equals("_limit") && params.get(key).length>0) {
									get.setLimit(Integer.parseInt(params.get(key)[0]));
								} else if (key.equals("_orderBy") && params.get(key).length>0) {
									get.setOrderBy(params.get(key)[0]);
								} else if (key.equals("_orderAscending") && params.get(key).length>0) {
									get.setOrderAscending(Boolean.parseBoolean(params.get(key)[0]));
								}
							} catch (NumberFormatException e) {
								RscErrorHtml error = new RscErrorHtml(getTitle(),getBackgroundColor(),"Number format exception for filter property: " + className + "." + key + ", value: " + params.get(key)[0]);
								error.addToResponse(response);
								get = null;
							}
						} else if (params.get(key).length>0) {
							MdlProperty prop = cls.getPropertyByName(key);
							if (prop!=null) {
								get.addFilter(prop.getName(),ReqGetFilter.EQUALS,params.get(key)[0]);
							} else {
								RscErrorHtml error = new RscErrorHtml(getTitle(),getBackgroundColor(),"Property not found: " + className + "." + key);
								error.addToResponse(response);
								get = null;
							}
						}
					}
				}
				if (get!=null) {
					executeGetRequest(get);
					generateResponse(request,response);
				}
			} else {
				RscErrorHtml error = new RscErrorHtml(getTitle(),getBackgroundColor(),"Class not found: " + className);
				error.addToResponse(response);
			}
		}
	}
	
	private void executeGetRequest(ReqGet get) {
		getRequest = get;
		getRequest.addSubscriber(this);
		if (get.getProperties().size()==0) {
			get.getProperties().add(ReqGet.ALL_PROPERTIES);
		}
		setDone(false);
		DbRequestQueue.getInstance().addRequest(getRequest,this);
		waitTillDone();
	}
	
	private void generateResponse(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if (getRequest!=null) {
			out.println(RhReqObjectToJSON.requestToJSON(getRequest).toStringBuilderReadFormat());
		}
	}

}
