package nl.zeesoft.zdk.json;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * JSON file element support class.
 */
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
	
	public ZStringBuilder getChildZStringBuilder(String name,ZStringBuilder def) {
		ZStringBuilder r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = new ZStringBuilder(v);
		}
		return r;
	}
	
	public ZStringSymbolParser getChildZStringSymbolParser(String name,ZStringSymbolParser def) {
		ZStringSymbolParser r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = new ZStringSymbolParser(v);
		}
		return r;
	}
	
	public String getChildString(String name,String def) {
		String r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = v.toString();
		}
		return r;
	}
	
	public boolean getChildBoolean(String name,boolean def) {
		boolean r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = Boolean.parseBoolean(v.toString());
		}
		return r;
	}

	public float getChildFloat(String name,float def) {
		float r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = Float.parseFloat(v.toString());
		}
		return r;
	}

	public double getChildDouble(String name,double def) {
		double r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = Double.parseDouble(v.toString());
		}
		return r;
	}

	public long getChildLong(String name,long def) {
		long r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = Long.parseLong(v.toString());
		}
		return r;
	}

	public int getChildInt(String name,int def) {
		int r = def;
		ZStringBuilder v = getChildValueByName(name);
		if (v!=null) {
			r = Integer.parseInt(v.toString());
		}
		return r;
	}

	public ZStringBuilder getChildZStringBuilder(String name) {
		return getChildZStringBuilder(name,new ZStringBuilder());
	}
	
	public ZStringSymbolParser getChildZStringSymbolParser(String name) {
		return getChildZStringSymbolParser(name,new ZStringSymbolParser());
	}
	
	public String getChildString(String name) {
		return getChildString(name,"");
	}
	
	public boolean getChildBoolean(String name) {
		return getChildBoolean(name,false);
	}

	public float getChildFloat(String name) {
		return getChildFloat(name,0F);
	}

	public double getChildDouble(String name) {
		return getChildDouble(name,0D);
	}

	public long getChildLong(String name) {
		return getChildLong(name,0L);
	}

	public int getChildInt(String name) {
		return getChildInt(name,0);
	}
}
