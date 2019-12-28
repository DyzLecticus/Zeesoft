package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import nl.zeesoft.zdk.htm.grid.ZGridRequest;
import nl.zeesoft.zdk.htm.util.DateTimeSDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A Classification is produced by the classifier.
 * It contains all vote counts for all relevant values and/or labels as well as a short list of most counted values and/or labels.
 */
public class Classification implements JsAble {
	public String					valueKey			= DateTimeSDR.VALUE_KEY;
	public String					labelKey			= DateTimeSDR.LABEL_KEY;
	public int						steps				= 0;
	public HashMap<Object,Integer>	valueCounts			= null;
	public HashMap<String,Integer>	labelCounts			= null;
	public List<Object>				mostCountedValues	= null;
	public List<String> 			mostCountedLabels	= null;
	
	public Classification() {
		
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		json.rootElement.children.add(new JsElem("labelKey",labelKey,true));
		json.rootElement.children.add(new JsElem("steps","" + steps));
		if (valueCounts!=null && valueCounts.size()>0) {
			Object value = valueCounts.keySet().iterator().next();
			json.rootElement.children.add(new JsElem("className",value.getClass().getSimpleName(),true));
			JsElem valCountsElem = new JsElem("valueCounts",true);
			json.rootElement.children.add(valCountsElem);
			for (Entry<Object,Integer> entry: valueCounts.entrySet()) {
				JsElem valCountElem = new JsElem();
				valCountsElem.children.add(valCountElem);
				ZGridRequest.addValueChildElement(valCountElem,"value",entry.getKey());
				valCountElem.children.add(new JsElem("count","" + entry.getValue()));
			}
			JsElem mostCountValsElem = new JsElem("mostCountedValues",true);
			json.rootElement.children.add(mostCountValsElem);
			for (Object val: mostCountedValues) {
				ZGridRequest.addValueChildElement(mostCountValsElem,null,val);
			}
		}
		if (labelCounts!=null && labelCounts.size()>0) {
			JsElem lblCountsElem = new JsElem("labelCounts",true);
			json.rootElement.children.add(lblCountsElem);
			for (Entry<String,Integer> entry: labelCounts.entrySet()) {
				JsElem lblCountElem = new JsElem();
				lblCountsElem.children.add(lblCountElem);
				lblCountElem.children.add(new JsElem("label",entry.getKey(),true));
				lblCountElem.children.add(new JsElem("count","" + entry.getValue()));
			}
			JsElem mostCountLblsElem = new JsElem("mostCountedLabels",true);
			json.rootElement.children.add(mostCountLblsElem);
			for (String lbl: mostCountedLabels) {
				mostCountLblsElem.children.add(new JsElem(null,(String)lbl,true));
			}
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			valueKey = json.rootElement.getChildString("valueKey",valueKey);
			labelKey = json.rootElement.getChildString("labelKey",labelKey);
			steps = json.rootElement.getChildInt("steps",steps);
			
			String className = json.rootElement.getChildString("className");
			JsElem valCountsElem = json.rootElement.getChildByName("valueCounts");
			if (className.length()>0 && valCountsElem!=null && valCountsElem.children.size()>0) {
				valueCounts = new HashMap<Object,Integer>();
				mostCountedValues = new ArrayList<Object>();
				for (JsElem valCountElem: valCountsElem.children) {
					Object value = ZGridRequest.getValueFromChildElement(valCountElem,"value",className);
					int count = valCountElem.getChildInt("count");
					if (value!=null && count>0) {
						valueCounts.put(value,count);
					}
				}
				JsElem mostCountValsElem = json.rootElement.getChildByName("mostCountedValues");
				for (JsElem valElem: mostCountValsElem.children) {
					Object value = ZGridRequest.getValueFromElement(valElem,"value",className);
					if (value!=null) {
						mostCountedValues.add(value);
					}
				}
			}

			JsElem lblCountsElem = json.rootElement.getChildByName("labelCounts");
			if (lblCountsElem!=null && lblCountsElem.children.size()>0) {
				labelCounts = new HashMap<String,Integer>();
				mostCountedLabels = new ArrayList<String>();
				for (JsElem lblCountElem: lblCountsElem.children) {
					String label = lblCountElem.getChildString("label");
					int count = lblCountElem.getChildInt("count");
					if (label!=null && label.length()>0 && count>0) {
						labelCounts.put(label,count);
					}
				}
				JsElem mostCountLblsElem = json.rootElement.getChildByName("mostCountedLabels");
				for (JsElem lblElem: mostCountLblsElem.children) {
					mostCountedLabels.add(lblElem.value.toString());
				}
			}
		}
	}
}
