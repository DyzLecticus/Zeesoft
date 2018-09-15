package nl.zeesoft.zodb.mod;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.handler.HandlerObject;
import nl.zeesoft.zodb.mod.handler.HtmlNotFoundHandler;
import nl.zeesoft.zodb.mod.handler.JsonNotFoundHandler;

public abstract class ModObject implements JsAble {
	public Config				configuration			= null;
	
	public String				name					= "";
	public ZStringBuilder		desc					= new ZStringBuilder();
	public String				url						= "";
	public boolean				selfTest				= true;
	
	public List<HandlerObject>	handlers				= new ArrayList<HandlerObject>();
	public HandlerObject		notFoundHtmlHandler		= null;
	public HandlerObject		notFoundJsonHandler		= null;
	
	public TesterObject			tester					= null;
	
	public ModObject(Config config) {
		configuration = config;
		addDefaultNotFoundHandlers();
	}
	
	public void install() {
		// Override to implement
	}
	
	public void initialize() {
		for (HandlerObject handler: handlers) {
			if (handler instanceof HtmlNotFoundHandler) {
				notFoundHtmlHandler = handler;
			}
			if (handler instanceof JsonNotFoundHandler) {
				notFoundJsonHandler = handler;
			}
		}
	}
	
	public void destroy() {
		// Override to implement
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		json.rootElement.children.add(new JsElem("url",url,true));
		json.rootElement.children.add(new JsElem("selfTest","" + selfTest));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			name = json.rootElement.getChildString("name",name);
			url = json.rootElement.getChildString("url",url);
			selfTest = json.rootElement.getChildBoolean("selfTest",selfTest);
		}
	}
	
	public HandlerObject getHandlerForRequest(HttpServletRequest request) {
		HandlerObject r = null;
		String[] elems = request.getServletPath().split("/");
		String path = "/" + elems[(elems.length - 1)];
		if (path.equals("/")) {
			path = "/index.html";
		}
		path = path.toLowerCase();
		for (HandlerObject handler: handlers) {
			if (path.equalsIgnoreCase(handler.getPath())) {
				r = handler; 
				break;
			}
		}
		if (r==null) {
			if (!path.endsWith(".json")) {
				r = notFoundHtmlHandler;
			} else {
				r = notFoundJsonHandler;
			}
		}
		return r;
	}
	
	private void addDefaultNotFoundHandlers() {
		handlers.add(new HtmlNotFoundHandler(configuration,this));
		handlers.add(new JsonNotFoundHandler(configuration,this));
	}
}
