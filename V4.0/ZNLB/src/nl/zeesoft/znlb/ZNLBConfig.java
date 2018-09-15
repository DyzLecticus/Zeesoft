package nl.zeesoft.znlb;

import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;

public class ZNLBConfig extends Config {
	private Languages	languages	= null;
	
	public ZNLBConfig() {
		languages = getNewLanguages();
		languages.initialize();
	}

	public Languages getLanguages() {
		return languages;
	}
	
	@Override
	protected void addModules() {
		super.addModules();
		getZODB().selfTest = false;
		addModule(new ModZNLB(this));
	}
	
	protected Languages getNewLanguages() {
		return new Languages();
	}
}
