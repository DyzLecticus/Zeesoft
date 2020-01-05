package nl.zeesoft.zsda.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;
import nl.zeesoft.zsda.mod.ModZSDA;

public class JsonZSDAGridHistoryHandler extends JsonHandlerObject {
	public final static String	PATH	= "/gridHistory.json"; 
	
	public JsonZSDAGridHistoryHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		setAllowGet(false);
		setAllowPost(true);
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		ZStringBuilder r = new ZStringBuilder();
		JsFile json = getPostBodyJson(request, response);
		if (json.rootElement==null) {
			r = setResponse(response,400,"Failed to parse JSON");
		} else {
			ModZSDA zsda = (ModZSDA) getConfiguration().getModule(ModZSDA.NAME);
			if (zsda!=null) {
				if (zsda.getGridHistory()==null || zsda.getGridHistory().getJson()==null) {
					r = setResponse(response,503,"Grid history is not available right now. Please try again at another time.");
				} else {
					JsFile res = zsda.getGridHistory().getJson();
					r = stringifyJson(res);
				}
			}
		}
		return r;
	}
}
