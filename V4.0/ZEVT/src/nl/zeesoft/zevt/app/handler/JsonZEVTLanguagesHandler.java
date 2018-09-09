package nl.zeesoft.zevt.app.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.AppObject;
import nl.zeesoft.zodb.app.handler.JsonHandlerObject;

public class JsonZEVTLanguagesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/languages.json";
	
	public ZStringBuilder		res		= null;
	
	public JsonZEVTLanguagesHandler(Config config, AppObject app) {
		super(config,app,PATH);
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem langsElem = new JsElem("languages",true);
		json.rootElement.children.add(langsElem);
		
		AppObject zapp = getConfiguration().getApplication(AppZEVT.NAME);
		if (zapp!=null) {
			AppZEVT zevt = (AppZEVT) zapp;
			for (String language: zevt.getEntityValueTranslator().getLanguages()) {
				langsElem.children.add(new JsElem(null,language,true));
			}
		}
		
		if (config.isDebug()) {
			res = json.toStringBuilderReadFormat();
		} else {
			res = json.toStringBuilder();
		}
	}

	@Override
	protected ZStringBuilder handleAllowedRequest(String method,HttpServletRequest request,HttpServletResponse response) {
		return res;
	}
}
