package nl.zeesoft.zjmo.json;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class JsElem {
	public String		 	name		= null;
	public ZStringBuilder 	value		= null;
	public boolean			cData		= false;
	public boolean			array		= false;
	public List<JsElem>		children 	= new ArrayList<JsElem>();

	public JsElem() {
		
	}

	public JsElem(String name) {
		this.name = name;
	}

	public JsElem(String name,String value) {
		this.name = name;
		this.value = new ZStringBuilder(value);
	}

	public JsElem(String name,ZStringBuilder value) {
		this.name = name;
		this.value = value;
	}

	public JsElem(String name,String value,boolean cData) {
		this.name = name;
		this.value = new ZStringBuilder(value);
		this.cData = cData;
	}

	public JsElem(String name,ZStringBuilder value,boolean cData) {
		this.name = name;
		this.value = value;
		this.cData = cData;
	}

	public JsElem(String name,boolean array) {
		this.name = name;
		this.array = true;
	}

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

	public ZStringBuilder getChildValueByName(String name) {
		ZStringBuilder r = null;
		JsElem child = getChildByName(name);
		if (child!=null) {
			r = child.value;
		}
		return r;
	}
}
