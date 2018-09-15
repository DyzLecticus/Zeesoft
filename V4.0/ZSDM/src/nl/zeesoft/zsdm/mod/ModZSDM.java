package nl.zeesoft.zsdm.mod;

import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsdm.ZSDMConfig;
import nl.zeesoft.zsdm.mod.handler.HtmlZSDMIndexHandler;

public class ModZSDM extends ModObject {
	public static final String		NAME			= "ZSDM";
	public static final String		DESC			= 
		"The Zeesoft Smart Dialog Manager provides a simple JSON API to manage smart dialogs.";
	
	public ModZSDM(ZSDMConfig config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
		// TODO Implement
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSDMIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		// TODO Implement
		super.initialize();
	}
	
	@Override
	public void destroy() {
		// TODO Implement
	}
}
