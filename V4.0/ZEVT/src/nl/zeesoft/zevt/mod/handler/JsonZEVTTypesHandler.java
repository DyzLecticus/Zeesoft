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

public class JsonZEVTTypesHandler extends JsonHandlerObject {
	public final static String	PATH	= "/types.json";
	
	public ZStringBuilder		res		= null;
	
	public JsonZEVTTypesHandler(Config config, ModObject mod) {
		super(config,mod,PATH);
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem typesElem = new JsElem("types",true);
		json.rootElement.children.add(typesElem);
		
		ModObject zm = getConfiguration().getModule(ModZEVT.NAME);
		if (zm!=null) {
			ModZEVT zevt = (ModZEVT) zm;
			int i = 0;
			for (String type: zevt.getTranslator().getTypes()) {
				JsElem typeElem = new JsElem();
				typesElem.children.add(typeElem);
				typeElem.children.add(new JsElem("code",type,true));
				typeElem.children.add(new JsElem("name",zevt.getTranslator().getTypeNames().get(i),true));
				i++;
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
