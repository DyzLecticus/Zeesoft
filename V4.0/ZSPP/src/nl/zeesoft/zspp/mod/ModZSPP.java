package nl.zeesoft.zspp.mod;

import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zspp.ZSPPConfig;
import nl.zeesoft.zspp.mod.handler.HtmlZSPPIndexHandler;
import nl.zeesoft.zspp.mod.handler.JsonZSPPRequestHandler;
import nl.zeesoft.zspp.prepro.Preprocessor;

public class ModZSPP extends ModObject {
	public static final String		NAME			= "ZSPP";
	public static final String		DESC			= 
		"The Zeesoft Sequence Preprocessor provides a simple JSON API for language specific sentence preprocessing.";

	private Preprocessor			preprocessor	= null;
	
	public ModZSPP(ZSPPConfig config) {
		super(config);
		name = NAME;
		desc.append(DESC);
	}
	
	@Override
	public void install() {
		// TODO: Install sequence preprocessor
		Preprocessor prepro = getNewPreprocessor();
		prepro.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSPPIndexHandler(configuration,this));
		handlers.add(new JsonZSPPRequestHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		preprocessor = getNewPreprocessor();
		preprocessor.initialize();
		tester = getNewTester();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		if (selfTest) {
			tester.stop();
		}
		preprocessor.destroy();
	}
	
	protected Preprocessor getNewPreprocessor() {
		return new Preprocessor(configuration);
	}
	
	protected ZSPPTester getNewTester() {
		return new ZSPPTester(configuration,configuration.getModuleUrl(NAME) + JsonZSPPRequestHandler.PATH);
	}
}
