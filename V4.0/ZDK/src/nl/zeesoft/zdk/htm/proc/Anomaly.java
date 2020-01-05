package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Anomaly implements JsAble {
	public String	valueKey					= "";
	public float	detectedAccuracy			= 0;
	public float	averageLongTermAccuracy		= 0;
	public float	averageShortTermAccuracy	= 0;
	public float	difference					= 0;

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		json.rootElement.children.add(new JsElem("detectedAccuracy","" + detectedAccuracy));
		json.rootElement.children.add(new JsElem("averageLongTermAccuracy","" + averageLongTermAccuracy));
		json.rootElement.children.add(new JsElem("averageShortTermAccuracy","" + averageShortTermAccuracy));
		json.rootElement.children.add(new JsElem("difference","" + difference));
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			valueKey = json.rootElement.getChildString("valueKey");
			detectedAccuracy = json.rootElement.getChildFloat("detectedAccuracy");
			averageLongTermAccuracy = json.rootElement.getChildFloat("averageLongTermAccuracy");
			averageShortTermAccuracy = json.rootElement.getChildFloat("averageShortTermAccuracy");
			difference = json.rootElement.getChildFloat("difference");
		}
	}
}
