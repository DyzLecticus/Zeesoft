package nl.zeesoft.zdk.htm.grid;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A ZGridRequest is used to specify values and/or labels for ZGrid processing.
 * The ZGrid will notify listeners of request results via ZGridResult objects.
 * Supported input value data types are Float, Integer, Long, String and SDR.
 */
public class ZGridRequest implements JsAble {
	public long			id				= 0;
	public long			dateTime		= 0;
	public Object[]		inputValues		= null;
	public String[]		inputLabels		= null;
	
	protected boolean	learn			= true;
	
	public ZGridRequest(int columns) {
		initialize(columns);
	}
	
	/**
	 * Returns a copy of this request.
	 * 
	 * @return A copy of this request
	 */
	public ZGridRequest copy() {
		ZGridRequest r = new ZGridRequest(inputValues.length);
		r.id = id;
		r.dateTime = dateTime;
		for (int i = 0; i < inputValues.length; i++) {
			r.inputValues[i] = inputValues[i];
			r.inputLabels[i] = inputLabels[i];
		}
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("id","" + id));
		json.rootElement.children.add(new JsElem("dateTime","" + dateTime));
		JsElem valsElem = new JsElem("values",true);
		json.rootElement.children.add(valsElem);
		for (int i = 0; i < inputValues.length; i++) {
			JsElem valElem = new JsElem();
			boolean add = false;
			if (inputValues[i]!=null) {
				valElem.children.add(new JsElem("className","" + inputValues[i].getClass().getSimpleName(),true));
				addValueChildElement(valElem,"value",inputValues[i]);
				add = true;
			}
			if (inputLabels[i]!=null && inputLabels[i].length()>0) {
				valElem.children.add(new JsElem("label","" + inputLabels[i]));
				add = true;
			}
			if (add) {
				valElem.children.add(0,new JsElem("index","" + i));
				valsElem.children.add(valElem);
			}
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			id = json.rootElement.getChildLong("id",id);
			dateTime = json.rootElement.getChildLong("dateTime",dateTime);
			JsElem valsElem = json.rootElement.getChildByName("values");
			if (valsElem!=null && valsElem.children.size()>0) {
				for (JsElem valElem: valsElem.children) {
					int index = valElem.getChildInt("index");
					if (index<inputValues.length) {
						if (valElem.getChildByName("value")!=null) {
							String className = valElem.getChildString("className");
							Object value = getValueFromChildElement(valElem,"value",className);
							if (value!=null) {
								inputValues[index] = value;
							}
						}
						if (valElem.getChildByName("label")!=null) {
							inputLabels[index] = valElem.getChildString("label");
						}
					}
				}
			}
		}
	}

	public static Object getValueFromElement(JsElem elem, String childName, String className) {
		elem.name = childName;
		JsElem wrapper = new JsElem();
		wrapper.children.add(elem);
		return getValueFromChildElement(wrapper,childName,className);
	}

	public static Object getValueFromChildElement(JsElem parent, String childName, String className) {
		Object value = null;
		if (className.equals("String")) {
			value = parent.getChildString(childName);
		} else if (className.equals("Float")) {
			value = parent.getChildFloat(childName);
		} else if (className.equals("Integer")) {
			value = parent.getChildInt(childName);
		} else if (className.equals("Long")) {
			value = parent.getChildLong(childName);
		} else if (className.equals("SDR")) {
			ZStringBuilder val = parent.getChildZStringBuilder(childName);
			if (val.length()>0) {
				SDR sdr = new SDR(100);
				sdr.fromStringBuilder(val);
				value = sdr;
			}
		}
		return value;
	}
	
	public static void addValueChildElement(JsElem parent, String childName, Object value) {
		if (value instanceof String) {
			parent.children.add(new JsElem("value",(String)value,true));
		} else if (value instanceof SDR) {
			parent.children.add(new JsElem("value",((SDR)value).toStringBuilder(),true));
		} else {
			parent.children.add(new JsElem("value","" + value));
		}
	}
	
	protected void initialize(int columns) {
		dateTime = System.currentTimeMillis();
		inputValues = new Object[columns];
		inputLabels = new String[columns];
	}
}
