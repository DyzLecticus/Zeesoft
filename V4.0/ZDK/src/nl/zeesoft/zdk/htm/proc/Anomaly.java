package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Anomaly implements JsAble {
	public float averageAccuracy	= 0;
	public float detectedAccuracy	= 0;
	public float difference			= 0;

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("averageAccuracy","" + averageAccuracy));
		json.rootElement.children.add(new JsElem("detectedAccuracy","" + detectedAccuracy));
		json.rootElement.children.add(new JsElem("difference","" + difference));
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			averageAccuracy = json.rootElement.getChildFloat("averageAccuracy");
			detectedAccuracy = json.rootElement.getChildFloat("detectedAccuracy");
			difference = json.rootElement.getChildFloat("difference");
		}
	}
}
