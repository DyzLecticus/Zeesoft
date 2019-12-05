package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridColumnContext implements JsAble {
	protected int sourceRow		= 0;
	protected int sourceColumn	= 0;
	protected int sourceIndex	= 0;

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("sourceRow","" + sourceRow));
		json.rootElement.children.add(new JsElem("sourceColumn","" + sourceColumn));
		json.rootElement.children.add(new JsElem("sourceIndex","" + sourceIndex));
		return json;
	}
	
	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			sourceRow = json.rootElement.getChildInt("sourceRow",sourceRow);
			sourceColumn = json.rootElement.getChildInt("sourceColumn",sourceColumn);
			sourceIndex = json.rootElement.getChildInt("sourceIndex",sourceIndex);
		}
	}
}
