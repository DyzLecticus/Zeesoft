package nl.zeesoft.zids.json;

import java.util.ArrayList;
import java.util.List;

public class JsElem {
	public String		 	name		= null;
	public StringBuilder 	value		= null;
	public boolean			cData		= false;
	public boolean			array		= false;
	public List<JsElem>		children 	= new ArrayList<JsElem>();

	public JsElem getChildByName(String name) {
		JsElem r = null;
		for (JsElem child: children) {
			if (child.name.equals(name)) {
				r = child;
				break;
			}
		}
		return r;
	}

	public StringBuilder getChildValueByName(String name) {
		StringBuilder r = null;
		JsElem child = getChildByName(name);
		if (child!=null) {
			r = child.value;
		}
		return r;
	}
}
