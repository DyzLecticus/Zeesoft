package nl.zeesoft.zenn.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zenn.mod.ModZENN;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZENNEnvironmentHandler extends JsonHandlerObject {
	public final static String	PATH	= "/state.json"; 
	
	public JsonZENNEnvironmentHandler(Config config, ModObject mod) {
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
			ModZENN zenn = (ModZENN) getConfiguration().getModule(ModZENN.NAME);
			if (zenn!=null) {
				JsFile res = zenn.getSimulator().getEnvironmentState();
				if (res==null) {
					r = setResponse(response,503,"Environment simulator is not working right now. Please try again at another time.");
				} else {
					JsElem orgsElem = res.rootElement.getChildByName("organisms");
					if (orgsElem!=null) {
						res.rootElement.children.remove(orgsElem);
					}
					r = stringifyJson(res);
				}
			}
		}
		return r;
	}
}
