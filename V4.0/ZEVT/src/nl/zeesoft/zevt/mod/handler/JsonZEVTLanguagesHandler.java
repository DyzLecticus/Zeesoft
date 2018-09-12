package nl.zeesoft.zevt.mod.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonHandlerObject;

public class JsonZEVTLanguagesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/languages.json";
	
	public ZStringBuilder		res		= null;
	
	public JsonZEVTLanguagesHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem langsElem = new JsElem("languages",true);
		json.rootElement.children.add(langsElem);
		
		ModObject zm = getConfiguration().getModule(ModZEVT.NAME);
		if (zm!=null) {
			ModZEVT zevt = (ModZEVT) zm;
			for (String language: zevt.getTranslator().getLanguages()) {
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
