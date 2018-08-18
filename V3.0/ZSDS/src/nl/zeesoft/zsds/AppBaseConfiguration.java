package nl.zeesoft.zsds;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;

public class AppBaseConfiguration extends BaseConfiguration {
	private boolean		debug		= false;
	private boolean		selfTest	= false;
	
	@Override
	public JsFile toJson() {
		JsFile json = super.toJson();
		json.rootElement.children.add(new JsElem("debug","" + debug));
		json.rootElement.children.add(new JsElem("selfTest","" + selfTest));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		super.fromJson(json);
		debug = json.rootElement.getChildBoolean("debug",debug);
		selfTest = json.rootElement.getChildBoolean("selfTest",selfTest);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isSelfTest() {
		return selfTest;
	}

	public void setSelfTest(boolean selfTest) {
		this.selfTest = selfTest;
	}
}
