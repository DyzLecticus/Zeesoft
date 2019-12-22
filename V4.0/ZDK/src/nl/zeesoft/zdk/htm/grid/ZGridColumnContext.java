package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ZGridColumnContext implements JsAble {
	public int sourceRow	= 0;
	public int sourceColumn	= 0;
	public int sourceIndex	= 0;
	
	protected ZGridColumnContext() {
		
	}
	
	protected ZGridColumnContext copy() {
		ZGridColumnContext r = new ZGridColumnContext();
		r.sourceRow = sourceRow;
		r.sourceColumn = sourceColumn;
		r.sourceIndex = sourceIndex;
		return r;
	}

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
